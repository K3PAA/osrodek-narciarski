package osrodek.generatorDrog;

import osrodek.osrodek.Wezel;
import osrodek.osrodek.krawedz.Krawedz;
import osrodek.osrodek.krawedz.Trasa;
import osrodek.osrodek.krawedz.wyciag.Wyciag;

import java.util.*;

public class GeneratorDrog {
  private Map<Wezel, Krawedz> wyznaczKrawedzieWejsciowe(Wezel start, Wezel docelowa) {
    Map<Wezel, Krawedz> krawedzWejsciowa = new HashMap<>();
    Set<Wezel> odwiedzone = new HashSet<>();
    Queue<Wezel> kolejka = new LinkedList<>();

    kolejka.add(start);
    odwiedzone.add(start);

    boolean znaleziono = (start == docelowa);

    while (!kolejka.isEmpty() && !znaleziono) {
      Wezel aktualny = kolejka.poll();

      for (Trasa trasa : aktualny.wychodzaceTrasy()) {
        Wezel sasiad = trasa.koniec();
        if (!odwiedzone.contains(sasiad)) {
          odwiedzone.add(sasiad);
          krawedzWejsciowa.put(sasiad, trasa);
          kolejka.add(sasiad);

          if (sasiad == docelowa) {
            znaleziono = true;
            break;
          }
        }
      }

      if (znaleziono) break;

      for (Wyciag wyciag : aktualny.wychodzaceWyciagi()) {
        Wezel sasiad = wyciag.koniec();
        if (!odwiedzone.contains(sasiad)) {
          odwiedzone.add(sasiad);
          krawedzWejsciowa.put(sasiad, wyciag);
          kolejka.add(sasiad);

          if (sasiad == docelowa) {
            znaleziono = true;
            break;
          }
        }
      }
    }

    return krawedzWejsciowa;
  }

  public Deque<Krawedz> wyznaczNajkrotszaDrogeWezelWezel(Wezel start, Wezel docelowa) {
    Map<Wezel, Krawedz> krawedzWejsciowa = wyznaczKrawedzieWejsciowe(start, docelowa);
    Deque<Krawedz> droga = new LinkedList<>();
    Wezel pomocniczyDoOdtworzenia = docelowa;

    while (pomocniczyDoOdtworzenia != start) {
      Krawedz wejsciowa = krawedzWejsciowa.get(pomocniczyDoOdtworzenia);
      if (wejsciowa == null) {
        throw new IllegalStateException(
          String.format("Błąd w strukturze grafu ! Nie ma ściezki z wezla %d do wezla %d", start.id(), docelowa.id())
        );
      }
      droga.addFirst(wejsciowa);
      pomocniczyDoOdtworzenia = wejsciowa.poczatek();
    }

    return droga;
  }

  public Deque<Krawedz> wyznaczNajkrotszaDrogeWezelTrasa(Wezel start, Trasa docelowa) {
    Deque<Krawedz> droga = wyznaczNajkrotszaDrogeWezelWezel(start, docelowa.poczatek());
    droga.addLast(docelowa);

    return droga;
  }
}
