package osrodek.wyciag;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import osrodek.FabrykaTestowychSportowcow;
import osrodek.czas.Interwal;
import osrodek.czas.Moment;
import osrodek.dziennik.DziennikStandardoweWyjscie;
import osrodek.osrodek.Wezel;
import osrodek.osrodek.krawedz.wyciag.Wyciag;
import osrodek.sportowcy.Sportowiec;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class WyciagiTest {
  private final Wezel a = new Wezel(1, 0, 0, 0, true);
  private final Wezel b = new Wezel(2, 1000, 0, 0, false);
  private Wyciag ab;

  @BeforeEach
  public void setUp() {
    ab = new Wyciag(1, a, b, new Interwal(20), new Interwal(500), 3);
  }

  @Test
  public void pojemnoscTestPierwszy() {
    Sportowiec
      sp1 = FabrykaTestowychSportowcow.generuj(a),
      sp2 = FabrykaTestowychSportowcow.generuj(a),
      sp3 = FabrykaTestowychSportowcow.generuj(a),
      sp4 = FabrykaTestowychSportowcow.generuj(a);

    ab.dodajDoKolejki(sp1, new Moment(9, 0, 0));
    ab.dodajDoKolejki(sp2, new Moment(9, 0, 0));
    ab.dodajDoKolejki(sp3, new Moment(9, 0, 0));
    ab.dodajDoKolejki(sp4, new Moment(9, 0, 0));

    int przedOdjazdem = ab.lacznaLiczbaPasazerow();
    assertEquals(0, przedOdjazdem);

    ab.odjazd(new Moment(9, 0, 20), new DziennikStandardoweWyjscie());

    int poOdjezdzie = ab.lacznaLiczbaPasazerow();
    assertEquals(3, poOdjezdzie);
  }

  @Test
  public void pojemnoscTestDrugi() {
    Sportowiec sp1 = FabrykaTestowychSportowcow.generuj(a),
      sp2 = FabrykaTestowychSportowcow.generuj(a);
    ab.dodajDoKolejki(sp1, new Moment(9, 0, 0));
    ab.dodajDoKolejki(sp2, new Moment(9, 0, 0));

    int przedOdjazdem = ab.lacznaLiczbaPasazerow();
    assertEquals(0, przedOdjazdem);

    ab.odjazd(new Moment(9, 0, 20), new DziennikStandardoweWyjscie());

    int poOdjezdzie = ab.lacznaLiczbaPasazerow();
    assertEquals(2, poOdjezdzie);
  }

  @Test
  public void maxymalnaDlugoscKolejkiTest() {
    Sportowiec
      sp1 = FabrykaTestowychSportowcow.generuj(a),
      sp2 = FabrykaTestowychSportowcow.generuj(a),
      sp3 = FabrykaTestowychSportowcow.generuj(a),
      sp4 = FabrykaTestowychSportowcow.generuj(a);

    ab.dodajDoKolejki(sp1, new Moment(9, 0, 0));
    ab.dodajDoKolejki(sp2, new Moment(9, 0, 0));
    ab.dodajDoKolejki(sp3, new Moment(9, 0, 0));
    ab.dodajDoKolejki(sp4, new Moment(9, 0, 0));

    ab.odjazd(new Moment(9, 0, 20), new DziennikStandardoweWyjscie());
    ab.odjazd(new Moment(9, 0, 40), new DziennikStandardoweWyjscie());

    assertEquals(4, ab.maxDlugoscKolejki());
  }
}
