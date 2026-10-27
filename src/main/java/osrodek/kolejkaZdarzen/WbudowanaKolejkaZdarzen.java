package osrodek.kolejkaZdarzen;

import osrodek.kolejkaZdarzen.zdarzenia.Zdarzenie;

import java.util.Comparator;
import java.util.PriorityQueue;

public class WbudowanaKolejkaZdarzen implements KolejkaZdarzen {
  private int licznikDodanychZdarzen = 0;
  private final PriorityQueue<ParaZdarzenieNumer> kolejka;

  public WbudowanaKolejkaZdarzen() {
    Comparator<ParaZdarzenieNumer> komparator = (para1, para2) -> {
      int porownanieCzasu = para1.zdarzenie().moment().compareTo(para2.zdarzenie().moment());
      if (porownanieCzasu != 0) {
        return porownanieCzasu;
      }
      return Integer.compare(para1.numer(), para2.numer());
    };
    this.kolejka = new PriorityQueue<>(komparator);
  }

  @Override
  public void dodaj(Zdarzenie zdarzenie) {
    ParaZdarzenieNumer nowaPara = new ParaZdarzenieNumer(zdarzenie, licznikDodanychZdarzen++);
    kolejka.add(nowaPara);
  }

  @Override
  public Zdarzenie zdejmij() {
    assert !czyPusta() : "Nie można zdjąć elementu z pustej kolejki";
    return kolejka.remove().zdarzenie();
  }
  
  @Override
  public boolean czyPusta() {
    return kolejka.isEmpty();
  }
}
