package osrodek.kolejkaZdarzen;

import osrodek.kolejkaZdarzen.zdarzenia.Zdarzenie;

public class ParaZdarzenieNumer {
  private final Zdarzenie zdarzenie;
  private final int numer;

  public ParaZdarzenieNumer(Zdarzenie zdarzenie, int numer) {
    this.zdarzenie = zdarzenie;
    this.numer = numer;
  }

  public Zdarzenie zdarzenie() {
    return zdarzenie;
  }

  public int numer() {
    return numer;
  }
}
