package osrodek.sportowcy.plan;

import osrodek.osrodek.Wezel;
import osrodek.osrodek.krawedz.Krawedz;
import osrodek.osrodek.krawedz.Trasa;
import osrodek.osrodek.krawedz.wyciag.Wyciag;
import osrodek.sportowcy.Sportowiec;

import java.util.*;

public class PlanNajatrakcyjniejszaTrasa extends PlanZeSciezka {
  @Override
  protected Deque<Krawedz> zbudujNowyPlan(Sportowiec sportowiec, Wezel wezelStartowy) {
    Trasa najlepszaTrasa = znajdzNajlepszaTrase(sportowiec, wezelStartowy);
    return generatorDrog.wyznaczNajkrotszaDrogeWezelTrasa(wezelStartowy, najlepszaTrasa);
  }

  private Trasa znajdzNajlepszaTrase(Sportowiec sportowiec, Wezel startowy) {
    Trasa najlepsza = null;
    double maxAtrakcyjnosc = -1.0;

    Set<Wezel> odwiedzone = new HashSet<>();
    Queue<Wezel> kolejka = new LinkedList<>();

    kolejka.add(startowy);
    odwiedzone.add(startowy);

    while (!kolejka.isEmpty()) {
      Wezel aktualny = kolejka.poll();

      for (Trasa trasa : aktualny.wychodzaceTrasy()) {
        double atrakcyjnosc = sportowiec.lacznaAtrakcyjnosc(trasa);
        if (atrakcyjnosc > maxAtrakcyjnosc) {
          maxAtrakcyjnosc = atrakcyjnosc;
          najlepsza = trasa;
        }
        if (!odwiedzone.contains(trasa.koniec())) {
          odwiedzone.add(trasa.koniec());
          kolejka.add(trasa.koniec());
        }
      }

      for (Wyciag wyciag : aktualny.wychodzaceWyciagi()) {
        if (!odwiedzone.contains(wyciag.koniec())) {
          odwiedzone.add(wyciag.koniec());
          kolejka.add(wyciag.koniec());
        }
      }
    }
    return najlepsza;
  }
}
