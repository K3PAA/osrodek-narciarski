package osrodek.osrodek.krawedz.wyciag;

import osrodek.sportowcy.Sportowiec;

/**
 * Reprezentuje kolejkę prostą sportowców do wyciągu.
 */
public interface KolejkaSportowcow {
  void dodaj(Sportowiec sportowiec);

  Sportowiec[] zdejmij(int ile);

  int rozmiar();
}
