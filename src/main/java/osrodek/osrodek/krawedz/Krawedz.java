package osrodek.osrodek.krawedz;

import osrodek.czas.Interwal;
import osrodek.czas.Moment;
import osrodek.kolejkaZdarzen.zdarzenia.Zdarzenie;
import osrodek.osrodek.Wezel;
import osrodek.sportowcy.Sportowiec;

public abstract class Krawedz {
  private final int id;
  private final Wezel poczatek;
  private final Wezel koniec;
  protected final Interwal dlugosc;

  public Krawedz(int id, Wezel poczatek, Wezel koniec, Interwal dlugosc) {
    this.id = id;
    this.poczatek = poczatek;
    this.koniec = koniec;
    this.dlugosc = dlugosc;
  }

  public int id() {
    return id;
  }

  public Wezel poczatek() {
    return poczatek;
  }

  public Wezel koniec() {
    return koniec;
  }

  public Interwal dlugosc() {
    return dlugosc;
  }

  public abstract String wypiszStatystyki();

  public abstract Zdarzenie wygenerujZdarzenie(Moment moment, Sportowiec sportowiec);

  @Override
  public String toString() {
    return "Krawedz [id=" + id + ", poczatek=" + poczatek + ", koniec=" + koniec + ", dlugosc=" + dlugosc + "]";
  }
}
