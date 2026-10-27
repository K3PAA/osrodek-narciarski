package osrodek.sportowcy.pamiec;

import osrodek.osrodek.krawedz.Trasa;

import java.util.HashMap;
import java.util.Map;

public class Znudzenie {
  private final double wspolczynnikZnudzenia; // [0, 1]
  private int lacznaLiczbaZjazdow;
  private final Map<Trasa, WpisZnudzenia> historia = new HashMap<>();

  public Znudzenie(double wspolczynnikZnudzenia) {
    this.wspolczynnikZnudzenia = wspolczynnikZnudzenia;
    this.lacznaLiczbaZjazdow = 0;
  }

  public double podajObecneZnudzenie(Trasa trasa) {
    WpisZnudzenia wpis = historia.get(trasa);
    if (wpis == null) {
      return 0.0;
    }
    int i = this.lacznaLiczbaZjazdow - wpis.numerZjazdu;
    return wpis.poziomZnudzenia * Math.pow(1 - wspolczynnikZnudzenia, i);
  }

  public void zarejestrujZjazdTrasa(Trasa trasa) {
    double z_t_minus_1 = podajObecneZnudzenie(trasa);
    double z_t = wspolczynnikZnudzenia * 1.0 + (1 - wspolczynnikZnudzenia) * z_t_minus_1;
    lacznaLiczbaZjazdow++;
    historia.put(trasa, new WpisZnudzenia(z_t, lacznaLiczbaZjazdow));
  }
}
