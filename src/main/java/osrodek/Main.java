package osrodek;

import java.util.Locale;
import java.util.Scanner;

import kadra.mapki.GeneratorMapek;
import kadra.mapki.pliki.WyjatekSystemuPlikow;
import osrodek.dziennik.DziennikStandardoweWyjscie;
import osrodek.kolejkaZdarzen.WbudowanaKolejkaZdarzen;
import osrodek.losowosc.DeterministycznaMaszynaLosujaca;
import osrodek.symulacja.Symulacja;
import osrodek.wczytywacz.DaneWejsciowe;
import osrodek.wczytywacz.Wczytywacz;

public class Main {
  public static void main(String[] args) {
    if (args.length == 0) {
      System.err.println("Nie podano ściezki do katalogu jako argumentu!");
      return;
    }
    try {
      GeneratorMapek generatorMapek = new GeneratorMapek(args[0]);
      DaneWejsciowe daneWejsciowe = wczytajWejscie();

      new Symulacja(generatorMapek).przeprowadzSymulacje(new DziennikStandardoweWyjscie(),
        new WbudowanaKolejkaZdarzen(),
        daneWejsciowe.osrodek(),
        daneWejsciowe.sportowcy());

    } catch (WyjatekSystemuPlikow e) {
      System.err.println("Wystąpił błąd systemu plików. Upewnij się, że podana ścieżka do katalogu na mapki jest poprawna i posiadasz odpowiednie uprawnienia zapisu.");
      e.printStackTrace();
    } catch (Exception e) {
      System.err.println("Wystąpił krytyczny błąd logiki. Proszę zgłosić problem zespołowi deweloperskiemu.");
      e.printStackTrace();
    }
  }

  private static DaneWejsciowe wczytajWejscie() {
    Scanner scanner = new Scanner(System.in);
    scanner.useLocale(Locale.ENGLISH);

    Wczytywacz wczytywacz = new Wczytywacz(scanner, new DeterministycznaMaszynaLosujaca(0));
    return wczytywacz.wczytajWejscie();
  }
}
