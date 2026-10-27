package osrodek.generatorDrog;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import osrodek.czas.Interwal;
import osrodek.osrodek.Wezel;
import osrodek.osrodek.krawedz.Krawedz;
import osrodek.osrodek.krawedz.Trasa;
import osrodek.osrodek.krawedz.wyciag.Wyciag;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PrzeszukiwanieGrafuTest {
  private Wezel zero, jeden, dwa, trzy, cztery, piec;
  private Wyciag wZeroJeden, wDwaTrzy, wDwaCztery, wCzteryPiec;
  private Trasa tJedenZero, tJedenDwa, tDwaZero, tTrzyJeden, tTrzyCztery, tPiecTrzy1, tPiecTrzy2;
  private final GeneratorDrog generatorDrog = new GeneratorDrog();

  @BeforeEach
  void setUp() {
    zero = new Wezel(0, 0, 0, 0, true);
    jeden = new Wezel(1, 200, 0, 0, false);
    dwa = new Wezel(2, 150, 0, 0, false);
    trzy = new Wezel(3, 350, 0, 0, false);
    cztery = new Wezel(4, 300, 0, 0, false);
    piec = new Wezel(5, 500, 0, 0, false);

    wZeroJeden = new Wyciag(0, zero, jeden, new Interwal(5), new Interwal(90), 3);
    wDwaTrzy = new Wyciag(1, dwa, trzy, new Interwal(5), new Interwal(90), 3);
    wDwaCztery = new Wyciag(2, dwa, cztery, new Interwal(5), new Interwal(90), 3);
    wCzteryPiec = new Wyciag(3, cztery, piec, new Interwal(5), new Interwal(90), 3);

    tJedenZero = new Trasa(0, jeden, zero, new Interwal(5), 2, 0.5, 0.5);
    tJedenDwa = new Trasa(1, jeden, dwa, new Interwal(5), 2, 0.5, 0.5);
    tDwaZero = new Trasa(2, dwa, zero, new Interwal(5), 2, 0.5, 0.5);
    tTrzyJeden = new Trasa(3, trzy, jeden, new Interwal(5), 2, 0.5, 0.5);
    tTrzyCztery = new Trasa(4, trzy, cztery, new Interwal(5), 2, 0.5, 0.5);
    tPiecTrzy1 = new Trasa(5, piec, trzy, new Interwal(5), 2, 0.5, 0.5);
    tPiecTrzy2 = new Trasa(6, piec, trzy, new Interwal(5), 2, 0.5, 0.5);

    zero.wychodzaceWyciagi(new Wyciag[]{wZeroJeden});
    zero.wychodzaceTrasy(new Trasa[0]);

    jeden.wychodzaceWyciagi(new Wyciag[0]);
    jeden.wychodzaceTrasy(new Trasa[]{tJedenZero, tJedenDwa});

    dwa.wychodzaceWyciagi(new Wyciag[]{wDwaTrzy, wDwaCztery});
    dwa.wychodzaceTrasy(new Trasa[]{tDwaZero});

    trzy.wychodzaceWyciagi(new Wyciag[0]);
    trzy.wychodzaceTrasy(new Trasa[]{tTrzyJeden, tTrzyCztery});

    cztery.wychodzaceWyciagi(new Wyciag[]{wCzteryPiec});
    cztery.wychodzaceTrasy(new Trasa[0]);

    piec.wychodzaceWyciagi(new Wyciag[0]);
    piec.wychodzaceTrasy(new Trasa[]{tPiecTrzy1, tPiecTrzy2});
  }

  @Test
  public void przeszukiwanieGrafuPierwszyTest() {
    Deque<Krawedz> najkrotszaDroga = generatorDrog.wyznaczNajkrotszaDrogeWezelWezel(zero, cztery);

    assertEquals(3, najkrotszaDroga.size());

    List<Integer> odwiedzoneWezly = new ArrayList<>();
    odwiedzoneWezly.add(zero.id());

    for (Krawedz krawedz : najkrotszaDroga) {
      odwiedzoneWezly.add(krawedz.koniec().id());
    }

    assertEquals(List.of(0, 1, 2, 4), odwiedzoneWezly);
  }

  @Test
  public void przeszukiwanieGrafuDrugiTest() {
    Deque<Krawedz> najkrotszaDroga = generatorDrog.wyznaczNajkrotszaDrogeWezelWezel(trzy, jeden);

    assertEquals(1, najkrotszaDroga.size());

    List<Integer> odwiedzoneWezly = new ArrayList<>();
    odwiedzoneWezly.add(trzy.id());

    for (Krawedz krawedz : najkrotszaDroga) {
      odwiedzoneWezly.add(krawedz.koniec().id());
    }

    assertEquals(List.of(3, 1), odwiedzoneWezly);
  }

  @Test
  public void przeszukiwanieGrafuTrzeciTest() {
    Deque<Krawedz> najkrotszaDroga = generatorDrog.wyznaczNajkrotszaDrogeWezelWezel(dwa, dwa);
    assertEquals(0, najkrotszaDroga.size());

    List<Integer> odwiedzoneWezly = new ArrayList<>();
    odwiedzoneWezly.add(dwa.id());
    for (Krawedz krawedz : najkrotszaDroga) {
      odwiedzoneWezly.add(krawedz.koniec().id());
    }
    assertEquals(List.of(2), odwiedzoneWezly);
  }

  @Test
  public void przeszukiwanieGrafuCzwartyTest() {
    Deque<Krawedz> najkrotszaDroga = generatorDrog.wyznaczNajkrotszaDrogeWezelWezel(cztery, trzy);
    assertEquals(2, najkrotszaDroga.size());

    List<Integer> odwiedzoneWezly = new ArrayList<>();
    odwiedzoneWezly.add(cztery.id());
    for (Krawedz krawedz : najkrotszaDroga) {
      odwiedzoneWezly.add(krawedz.koniec().id());
    }
    assertEquals(List.of(4, 5, 3), odwiedzoneWezly);
  }
}
