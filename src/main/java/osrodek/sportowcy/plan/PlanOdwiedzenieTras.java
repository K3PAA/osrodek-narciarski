package osrodek.sportowcy.plan;

import osrodek.kolejkaZdarzen.zdarzenia.RozpoczecieZjazdu;
import osrodek.kolejkaZdarzen.zdarzenia.Zdarzenie;
import osrodek.osrodek.Wezel;
import osrodek.osrodek.krawedz.Krawedz;
import osrodek.osrodek.krawedz.Trasa;
import osrodek.sportowcy.Sportowiec;

import java.util.*;

public class PlanOdwiedzenieTras extends PlanZeSciezka {
  private final HashMap<Trasa, Integer> licznikZjazdow = new HashMap<>();
  private List<Trasa> najmniejszaIloscCache = new ArrayList<>();

  public PlanOdwiedzenieTras(Trasa[] trasy) {
    for (Trasa trasa : trasy) {
      licznikZjazdow.put(trasa, 0);
    }
  }

  @Override
  protected Deque<Krawedz> zbudujNowyPlan(Sportowiec sportowiec, Wezel wezelStartowy) {
    Trasa szukanaTrasa = znajdzMinimalnaTrase(sportowiec, wezelStartowy);
    return generatorDrog.wyznaczNajkrotszaDrogeWezelTrasa(wezelStartowy, szukanaTrasa);
  }

  // Wykorzystujemy metodę-haczyk odziedziczoną z PlanZeSciezka
  @Override
  protected void zarejestrujDecyzje(Zdarzenie zdarzenie) {
    if (zdarzenie instanceof RozpoczecieZjazdu zd) {
      Trasa trasa = zd.trasa();
      licznikZjazdow.put(trasa, licznikZjazdow.get(trasa) + 1);
      najmniejszaIloscCache.clear();
    }
  }

  private Trasa znajdzMinimalnaTrase(Sportowiec sportowiec, Wezel wezelStartowy) {
    if (najmniejszaIloscCache.isEmpty()) {
      najmniejszaIloscCache = generujNajmniejszaIlosc();
    }

    ArrayList<Trasa> trasyMinOdleglosc = new ArrayList<>();
    int minimalnyRozmiar = generatorDrog.wyznaczNajkrotszaDrogeWezelTrasa(wezelStartowy, najmniejszaIloscCache.getFirst()).size();

    for (Trasa trasa : najmniejszaIloscCache) {
      int odleglosc = generatorDrog.wyznaczNajkrotszaDrogeWezelTrasa(wezelStartowy, trasa).size();

      if (odleglosc < minimalnyRozmiar) {
        trasyMinOdleglosc.clear();
        trasyMinOdleglosc.add(trasa);
        minimalnyRozmiar = odleglosc;
      } else if (odleglosc == minimalnyRozmiar) {
        trasyMinOdleglosc.add(trasa);
      }
    }

    if (trasyMinOdleglosc.size() == 1) {
      return trasyMinOdleglosc.getFirst();
    }

    return wybierzNajatrakcyjniejsza(sportowiec, trasyMinOdleglosc);
  }

  private Trasa wybierzNajatrakcyjniejsza(Sportowiec sportowiec, ArrayList<Trasa> trasyMinOdleglosc) {
    Trasa najlepsza = null;
    double maxAtrakcyjnosc = -1.0;

    for (Trasa trasa : trasyMinOdleglosc) {
      double atrakcyjnosc = sportowiec.lacznaAtrakcyjnosc(trasa);
      if (atrakcyjnosc > maxAtrakcyjnosc) {
        maxAtrakcyjnosc = atrakcyjnosc;
        najlepsza = trasa;
      }
    }
    return najlepsza;
  }

  private List<Trasa> generujNajmniejszaIlosc() {
    List<Trasa> najlepszeTrasy = new ArrayList<>();
    int najmniejszaLiczba = Integer.MAX_VALUE;

    for (Map.Entry<Trasa, Integer> wpis : licznikZjazdow.entrySet()) {
      Trasa obecnaTrasa = wpis.getKey();
      int obecnaLiczba = wpis.getValue();

      if (obecnaLiczba < najmniejszaLiczba) {
        najmniejszaLiczba = obecnaLiczba;
        najlepszeTrasy.clear();
        najlepszeTrasy.add(obecnaTrasa);
      } else if (obecnaLiczba == najmniejszaLiczba) {
        najlepszeTrasy.add(obecnaTrasa);
      }
    }
    return najlepszeTrasy;
  }
}
