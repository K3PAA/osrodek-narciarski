package osrodek.osrodek.krawedz.wyciag;

import osrodek.czas.Interwal;
import osrodek.czas.Moment;
import osrodek.dziennik.Dziennik;
import osrodek.kolejkaZdarzen.zdarzenia.DotarcieDoWezlaPoWjezdzie;
import osrodek.kolejkaZdarzen.zdarzenia.OdjazdWyciagu;
import osrodek.kolejkaZdarzen.zdarzenia.Zdarzenie;
import osrodek.mapka.RodzajTekstu;
import osrodek.osrodek.Wezel;
import osrodek.osrodek.krawedz.Krawedz;
import osrodek.sportowcy.Sportowiec;

import java.util.ArrayList;

public class Wyciag extends Krawedz {
  private final Interwal odstepMiedzyOdjazdami;
  private final int ladownosc;
  private final KolejkaSportowcow obecnaKolejka;
  private int lacznaLiczbaPasazerow;
  private int maxDlugoscKolejki = 0;
  private int maxPrzepustownosc = 0;
  private long sumaOsobosekund = 0;
  private int czasOstatniejZmiany = 9 * 3600; // start symulacji

  public Wyciag(int id,
                Wezel poczatek,
                Wezel koniec,
                Interwal odstepMiedzyOdjazdami,
                Interwal dlugoscPrzejazdu,
                int ladownosc) {
    super(id, poczatek, koniec, dlugoscPrzejazdu);

    assert poczatek.wysokosc() < koniec.wysokosc()
      : String.format("Wyciąg %d prowadzi w dół: %d -> %d", id, poczatek.wysokosc(), koniec.wysokosc());

    this.odstepMiedzyOdjazdami = odstepMiedzyOdjazdami;
    this.ladownosc = ladownosc;
    obecnaKolejka = new BuforCyklicznySportowcow();
    lacznaLiczbaPasazerow = 0;
  }

  public int lacznaLiczbaPasazerow() {
    return lacznaLiczbaPasazerow;
  }

  public int maxDlugoscKolejki() {
    return maxDlugoscKolejki;
  }

  private void aktualizujStatystykiKolejki(Moment moment) {
    int obecnyCzas = moment.sekundy();
    if (obecnyCzas > czasOstatniejZmiany) {
      int limitCzasu = Math.min(obecnyCzas, 15 * 3600); // Tniemy o równej 15:00
      int czasTrwania = Math.max(0, limitCzasu - czasOstatniejZmiany);

      sumaOsobosekund += (long) czasTrwania * obecnaKolejka.rozmiar();
      czasOstatniejZmiany = Math.max(czasOstatniejZmiany, limitCzasu);
    }
  }

  public void dodajDoKolejki(Sportowiec sportowiec, Moment moment) {
    aktualizujStatystykiKolejki(moment);
    obecnaKolejka.dodaj(sportowiec);
    if (obecnaKolejka.rozmiar() > maxDlugoscKolejki) {
      maxDlugoscKolejki = obecnaKolejka.rozmiar();
    }
  }

  public Zdarzenie[] odjazd(Moment moment, Dziennik dziennik) {
    aktualizujStatystykiKolejki(moment);
    Sportowiec[] odjezdzajacySportowcy = obecnaKolejka.zdejmij(Math.min(obecnaKolejka.rozmiar(), ladownosc));

    for (Sportowiec sportowiec : odjezdzajacySportowcy) {
      dziennik.dodajWpisZeSportowcem(moment, sportowiec, String.format("rozpoczął wjazd %s", toString()));
    }

    Zdarzenie[] noweZdarzenia = new Zdarzenie[1 + odjezdzajacySportowcy.length];
    noweZdarzenia[0] = new OdjazdWyciagu(moment.dodajInterwal(odstepMiedzyOdjazdami), this);

    for (int i = 0; i < odjezdzajacySportowcy.length; i++) {
      noweZdarzenia[1 + i] = new DotarcieDoWezlaPoWjezdzie(moment.dodajInterwal(dlugosc()),
        this,
        koniec(),
        odjezdzajacySportowcy[i]);
    }

    lacznaLiczbaPasazerow += odjezdzajacySportowcy.length;
    maxPrzepustownosc += ladownosc;

    return noweZdarzenia;
  }

  @Override
  public Zdarzenie wygenerujZdarzenie(Moment moment, Sportowiec sportowiec) {
    return sportowiec.nastepnyKrokWyciag(moment, this);
  }

  private ArrayList<String> parametry() {
    ArrayList<String> linieTekstu = new ArrayList<>();

    linieTekstu.add(
      String.format("w%d: %d os. co %ds", id(), ladownosc, odstepMiedzyOdjazdami.sekundy())
    );
    linieTekstu.add(
      String.format("czas: %ds", dlugosc().sekundy())
    );
    return linieTekstu;
  }

  private ArrayList<String> statystyki() {
    ArrayList<String> linieTekstu = new ArrayList<>();
    linieTekstu.add(
      String.format("w%d: kol: %d(śr), %d(maks)", id(), sredniaLiczbaPasazerow(), maxDlugoscKolejki)
    );
    linieTekstu.add(
      String.format("wjazdy: %d / %d (%d%%)", lacznaLiczbaPasazerow,
        maxPrzepustownosc, obliczProcentPrzepustowosci())
    );
    return linieTekstu;
  }

  public ArrayList<String> mapkaTekst(RodzajTekstu rodzaj) {
    return rodzaj == RodzajTekstu.STATYSTYKI ? statystyki() : parametry();
  }

  private int sredniaLiczbaPasazerow() {
    int calkowityCzasDnia = 15 * 3600 - 9 * 3600;
    long koncowaSuma = sumaOsobosekund;

    if (15 * 3600 > czasOstatniejZmiany) {
      koncowaSuma += (long) (15 * 3600 - czasOstatniejZmiany) * obecnaKolejka.rozmiar();
    }

    return (int) Math.round((double) koncowaSuma / calkowityCzasDnia);
  }

  private int obliczProcentPrzepustowosci() {
    return (int) (((double) lacznaLiczbaPasazerow / (double) maxPrzepustownosc) * 100);
  }

  @Override
  public String wypiszStatystyki() {
    return String.format("%d laczna liczba pasazerow", lacznaLiczbaPasazerow);
  }

  @Override
  public String toString() {
    return String.format("Wyciąg nr %d", id());
  }
}
