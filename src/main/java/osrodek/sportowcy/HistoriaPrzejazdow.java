package osrodek.sportowcy;

import osrodek.osrodek.krawedz.Krawedz;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HistoriaPrzejazdow {
  private final List<Krawedz> chronologicznePrzejazdy = new ArrayList<>();

  public void dodajPrzejazd(Krawedz krawedz) {
    chronologicznePrzejazdy.add(krawedz);
  }
  public String wygenerujStatystykiDlaKrawedzi(Krawedz szukanaKrawedz) {
    List<Integer> numeryPrzejazdow = new ArrayList<>();

    for (int i = 0; i < chronologicznePrzejazdy.size(); i++) {
      if (chronologicznePrzejazdy.get(i).equals(szukanaKrawedz)) {
        numeryPrzejazdow.add(i + 1);
      }
    }

    if (numeryPrzejazdow.isEmpty()) {
      return "(0): ";
    }

    String numeryPoPrzecinku = numeryPrzejazdow.stream()
      .map(String::valueOf)
      .collect(Collectors.joining(","));

    return String.format("(%d): %s", numeryPrzejazdow.size(), numeryPoPrzecinku);
  }
}
