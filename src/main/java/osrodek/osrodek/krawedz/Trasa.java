package osrodek.osrodek.krawedz;

import osrodek.czas.Interwal;
import osrodek.czas.Moment;
import osrodek.kolejkaZdarzen.zdarzenia.Zdarzenie;
import osrodek.mapka.RodzajTekstu;
import osrodek.osrodek.Wezel;
import osrodek.sportowcy.Sportowiec;

import java.util.ArrayList;

public class Trasa extends Krawedz {
  private final int poziomTrudnosci;
  private final double bazowaAtrakcyjnosc;
  private final double odpornoscNaNierownosci;
  private int liczbaZjazdow;

  public Trasa(int id,
               Wezel poczatek,
               Wezel koniec,
               Interwal dlugoscZjazdu,
               int poziomTrudnosci,
               double bazowaAtrakcyjnosc,
               double odpornoscNaNierownosci) {
    super(id, poczatek, koniec, dlugoscZjazdu);

    assert poczatek.wysokosc() > koniec.wysokosc()
      : String.format("Trasa %d prowadzi w górę: %d -> %d", id, poczatek.wysokosc(), koniec.wysokosc());

    this.poziomTrudnosci = poziomTrudnosci;
    this.bazowaAtrakcyjnosc = bazowaAtrakcyjnosc;
    this.odpornoscNaNierownosci = odpornoscNaNierownosci;
    liczbaZjazdow = 0;
  }

  public int poziomTrudnosci() {
    return poziomTrudnosci;
  }

  public double bazowaAtrakcyjnosc() {
    return bazowaAtrakcyjnosc;
  }

  public double odpornoscNaNierownosci() {
    return odpornoscNaNierownosci;
  }

  public int liczbaZjazdow() {
    return liczbaZjazdow;
  }

  public Moment przemierz(Moment start) {
    liczbaZjazdow++;
    return start.dodajInterwal(dlugosc());
  }

  public double wyrownanieNawierzchni() {
    return (bazowaAtrakcyjnosc + (1 - bazowaAtrakcyjnosc) * Math.pow(odpornoscNaNierownosci, liczbaZjazdow));
  }

  @Override
  public Zdarzenie wygenerujZdarzenie(Moment moment, Sportowiec sportowiec) {
    return sportowiec.nastepnyKrokTrasa(moment, this);
  }

  private ArrayList<String> parametry() {
    ArrayList<String> linieTekstu = new ArrayList<>();

    linieTekstu.add(
      String.format("t%d: poziom: %d, czas: %ds", id(), poziomTrudnosci(), dlugosc().sekundy())
    );
    linieTekstu.add(
      String.format("odporność: %.2f, %.5f", bazowaAtrakcyjnosc, odpornoscNaNierownosci)
    );

    return linieTekstu;
  }

  private ArrayList<String> statystyki() {
    ArrayList<String> linieTekstu = new ArrayList<>();

    linieTekstu.add(
      String.format("t%d: śnieg: %.2f", id(), wyrownanieNawierzchni())
    );
    linieTekstu.add(
      String.format("zjazdy: %d", liczbaZjazdow)
    );

    return linieTekstu;
  }

  public ArrayList<String> mapkaTekst(RodzajTekstu rodzaj) {
    return rodzaj == RodzajTekstu.STATYSTYKI ? statystyki() : parametry();
  }

  @Override
  public String wypiszStatystyki() {
    return String.format("%d zjazdów", liczbaZjazdow);
  }

  @Override
  public String toString() {
    return String.format("Trasa nr %d", id());
  }
}
