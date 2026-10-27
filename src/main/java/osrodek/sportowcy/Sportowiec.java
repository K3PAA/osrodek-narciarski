package osrodek.sportowcy;

import osrodek.czas.Interwal;
import osrodek.czas.Moment;
import osrodek.kolejkaZdarzen.zdarzenia.DolaczenieDoKolejki;
import osrodek.kolejkaZdarzen.zdarzenia.RozpoczecieZjazdu;
import osrodek.kolejkaZdarzen.zdarzenia.Zdarzenie;
import osrodek.losowosc.MaszynaLosujaca;
import osrodek.osrodek.Wezel;
import osrodek.osrodek.krawedz.Trasa;
import osrodek.osrodek.krawedz.wyciag.Wyciag;
import osrodek.sportowcy.pamiec.Znudzenie;
import osrodek.sportowcy.plan.Plan;

public class Sportowiec {
  private final int WSPOLCZYNNIK_ROZNICY_POZIOMOW_TRUDNOSCI = 5;
  private final int WSPOLCZYNNIK_ROZNICY_POZIOMOW_TRUDNOSCI_LATWEJ_TRASY = 7;
  private final double DOMYSLNA_ATRAKCYJNOSC_LATWEJ_TRASY = 0.2;
  private final int id;
  private final int poziomZaawansowania; // {0, 1, ..., 10}
  private final double wspolczynnikSpontanicznosci; // [0, 1]
  private final double wagaTrudnosci; // [0, 1]
  private final double wagaNawierzchni; // [0, 1]
  private final double wagaZnudzenia; // [0, 1]
  private final HistoriaPrzejazdow historiaPrzejazdow;

  private final Wezel wezelStartowy;
  private final Moment momentStartu;
  private final Plan planDzialania;
  private final Znudzenie znudzenie;
  private final MaszynaLosujaca maszynaLosujaca;

  public Sportowiec(int id,
                    int poziomZaawansowania,
                    double wspolczynnikSpontanicznosci,
                    double wagaTrudnosci,
                    double wagaNawierzchni,
                    double wagaZnudzenia,
                    HistoriaPrzejazdow historiaPrzejazdow,
                    Wezel wezelStartowy,
                    Moment momentStartu,
                    Plan planDzialania,
                    Znudzenie znudzenie,
                    MaszynaLosujaca maszynaLosujaca) {
    this.id = id;
    this.poziomZaawansowania = poziomZaawansowania;
    this.wspolczynnikSpontanicznosci = wspolczynnikSpontanicznosci;
    this.wagaTrudnosci = wagaTrudnosci;
    this.wagaNawierzchni = wagaNawierzchni;
    this.wagaZnudzenia = wagaZnudzenia;
    this.historiaPrzejazdow = historiaPrzejazdow;
    this.wezelStartowy = wezelStartowy;
    this.momentStartu = momentStartu;
    this.planDzialania = planDzialania;
    this.znudzenie = znudzenie;
    this.maszynaLosujaca = maszynaLosujaca;
  }

  public int id() {
    return id;
  }

  public int poziomZaawansowania() {
    return poziomZaawansowania;
  }

  public double wspolczynnikSpontanicznosci() {
    return wspolczynnikSpontanicznosci;
  }

  public double wagaTrudnosci() {
    return wagaTrudnosci;
  }

  public double wagaNawierzchni() {
    return wagaNawierzchni;
  }

  public boolean sledzony() {
    return historiaPrzejazdow != null;
  }

  public HistoriaPrzejazdow historiaPrzejazdow() {
    return historiaPrzejazdow;
  }

  public Wezel wezelStartowy() {
    return wezelStartowy;
  }

  public Moment momentStartu() {
    return momentStartu;
  }

  public MaszynaLosujaca maszynaLosujaca() {
    return maszynaLosujaca;
  }

  public Zdarzenie nastepnyKrok(Moment moment, Wezel obecnyWezel) {
    return planDzialania.podajNastepnyKrok(this, moment, obecnyWezel);
  }

  public void zarejestrujZjazd(Trasa trasa) {
    this.znudzenie.zarejestrujZjazdTrasa(trasa);
  }

  /**
   * Spontaniczna decyzja polega na wylosowaniu następnej krawędzi jednostajnie
   * spośród wszystkich zaczynających się w obecnym wierzchołku.
   */
  public Zdarzenie podejmijSpontanicznaDecyzje(Moment moment, Wezel obecnyWezel) {
    Trasa[] bezposrednieTrasy = obecnyWezel.wychodzaceTrasy();
    Wyciag[] wyciagi = obecnyWezel.wychodzaceWyciagi();

    int losowyWybor = maszynaLosujaca.losowyInt(0, bezposrednieTrasy.length + wyciagi.length);

    if (losowyWybor < bezposrednieTrasy.length) {
      return nastepnyKrokTrasa(moment, bezposrednieTrasy[losowyWybor]);
    } else {
      return nastepnyKrokWyciag(moment, wyciagi[losowyWybor - bezposrednieTrasy.length]);
    }
  }

  public boolean czySpontanicznaDecyzja() {
    return maszynaLosujaca.losowyDouble(0, 1) < this.wspolczynnikSpontanicznosci;
  }

  /**
   * Wylicza atrakcyjność trasy na podstawie wzoru z treści zadania.
   */
  public double lacznaAtrakcyjnosc(Trasa trasa) {
    return wagaTrudnosci * atrakcyjnoscPoziomuTrudnosci(trasa)
      + wagaNawierzchni * trasa.wyrownanieNawierzchni()
      + wagaZnudzenia * (1 - znudzenie.podajObecneZnudzenie(trasa));
  }

  private double atrakcyjnoscPoziomuTrudnosci(Trasa trasa) {
    int poziomTrudnosci = trasa.poziomTrudnosci();

    double roznicaPoziomow = poziomTrudnosci - poziomZaawansowania;

    if (poziomTrudnosci >= poziomZaawansowania + WSPOLCZYNNIK_ROZNICY_POZIOMOW_TRUDNOSCI) {
      return 0;
    } else if (poziomZaawansowania + WSPOLCZYNNIK_ROZNICY_POZIOMOW_TRUDNOSCI > poziomTrudnosci
      && poziomTrudnosci >= poziomZaawansowania) {
      return 1.0 - roznicaPoziomow / (double) WSPOLCZYNNIK_ROZNICY_POZIOMOW_TRUDNOSCI;
    } else {
      return Math.max(DOMYSLNA_ATRAKCYJNOSC_LATWEJ_TRASY,
        1 - (-roznicaPoziomow) / (double) WSPOLCZYNNIK_ROZNICY_POZIOMOW_TRUDNOSCI_LATWEJ_TRASY);
    }
  }

  /**
   * Tworzy zdarzenie dołączenia do kolejki do wyciągu w nastepnym kroku.
   */
  public DolaczenieDoKolejki nastepnyKrokWyciag(Moment moment, Wyciag wyciag) {
    return new DolaczenieDoKolejki(moment, wyciag, this);
  }

  /**
   * Tworzy zdarzenie zjazdu bezpośrednią trasą w następnym kroku.
   */
  public RozpoczecieZjazdu nastepnyKrokTrasa(Moment moment, Trasa trasa) {
    return new RozpoczecieZjazdu(moment, trasa, this);
  }
  
  /**
   * Tworzy kopie sportowca z tymi samymi parametrami ale zwiekszonym id oraz momentem startu.
   * Uzywane na potrzeby tworzenia wielu sportowcow z jednej grupy sportowcow z wejscia.
   */
  public Sportowiec kopia(int przesuniecieId, Interwal przesuniecieMomentuStartu) {
    return new Sportowiec(id + przesuniecieId,
      poziomZaawansowania,
      wspolczynnikSpontanicznosci,
      wagaTrudnosci,
      wagaNawierzchni,
      wagaZnudzenia,
      historiaPrzejazdow,
      wezelStartowy,
      momentStartu.dodajInterwal(przesuniecieMomentuStartu),
      planDzialania,
      znudzenie,
      maszynaLosujaca);
  }

  @Override
  public String toString() {
    return String.format("Sportowiec nr %d", id);
  }
}
