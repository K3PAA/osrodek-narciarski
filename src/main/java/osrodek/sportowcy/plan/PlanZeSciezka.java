package osrodek.sportowcy.plan;

import osrodek.czas.Moment;
import osrodek.generatorDrog.GeneratorDrog;
import osrodek.kolejkaZdarzen.zdarzenia.Zdarzenie;
import osrodek.osrodek.Wezel;
import osrodek.osrodek.krawedz.Krawedz;
import osrodek.sportowcy.Sportowiec;

import java.util.Deque;
import java.util.LinkedList;

public abstract class PlanZeSciezka implements Plan {
  protected Deque<Krawedz> aktualnyPlan = new LinkedList<>();
  protected final GeneratorDrog generatorDrog = new GeneratorDrog();

  @Override
  public Zdarzenie podajNastepnyKrok(Sportowiec sportowiec, Moment moment, Wezel obecnyWezel) {
    Zdarzenie decyzja;

    if (aktualnyPlan.isEmpty()) {
      if (sportowiec.czySpontanicznaDecyzja()) {
        decyzja = sportowiec.podejmijSpontanicznaDecyzje(moment, obecnyWezel);
        zarejestrujDecyzje(decyzja);
        return decyzja;
      } else {
        aktualnyPlan = zbudujNowyPlan(sportowiec, obecnyWezel);
      }
    }

    Krawedz nastepnaKrawedz = aktualnyPlan.removeFirst();
    decyzja = nastepnaKrawedz.wygenerujZdarzenie(moment, sportowiec);

    zarejestrujDecyzje(decyzja);
    return decyzja;
  }

  protected abstract Deque<Krawedz> zbudujNowyPlan(Sportowiec sportowiec, Wezel wezelStartowy);

  protected void zarejestrujDecyzje(Zdarzenie zdarzenie) {
  }
}
