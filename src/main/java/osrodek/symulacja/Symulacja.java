package osrodek.symulacja;

import kadra.mapki.GeneratorMapek;
import kadra.mapki.pliki.WyjatekSystemuPlikow;
import osrodek.czas.Moment;
import osrodek.dziennik.Dziennik;
import osrodek.kolejkaZdarzen.KolejkaZdarzen;
import osrodek.kolejkaZdarzen.zdarzenia.OdjazdWyciagu;
import osrodek.kolejkaZdarzen.zdarzenia.PoczatekDnia;
import osrodek.kolejkaZdarzen.zdarzenia.Zdarzenie;
import osrodek.mapka.Mapka;
import osrodek.osrodek.Osrodek;
import osrodek.osrodek.krawedz.wyciag.Wyciag;
import osrodek.sportowcy.Sportowiec;

public class Symulacja {
    private final GeneratorMapek generatorMapek;
    private static final Moment POCZATEK_DNIA = new Moment(9, 0, 0);
    private static final Moment KONIEC_SYMULACJI = new Moment(15, 0, 0);

    public Symulacja(GeneratorMapek generatorMapek) {
      this.generatorMapek = generatorMapek;
    }

    public void przeprowadzSymulacje(Dziennik dziennik,
        KolejkaZdarzen kolejkaZdarzen,
        Osrodek osrodek,
        Sportowiec[] sportowcy) throws WyjatekSystemuPlikow {
        przygotujPoczatkoweZdarzenia(kolejkaZdarzen, osrodek, sportowcy);
        glownaPetla(kolejkaZdarzen, dziennik);
        zbierzStatystyki(osrodek, dziennik);
        wygenerujMapki(osrodek, sportowcy);
    }

    /**
     * Inicjuje kolejkę zdarzeń poprzez wrzucenie na nią początkowych zdarzeń:
     * 1. Początek dnia na stoku dla każdego sportowca zgodnie z jego momentem startu.
     * 2. Pierwszy odjazd wagonika dla każdego wyciągu dokładnie o 9:00:00.
     */
    private void przygotujPoczatkoweZdarzenia(KolejkaZdarzen kolejkaZdarzen, Osrodek osrodek, Sportowiec[] sportowcy) {
        for (Sportowiec sportowiec : sportowcy) {
            kolejkaZdarzen.dodaj(new PoczatekDnia(sportowiec.momentStartu(), sportowiec.wezelStartowy(), sportowiec));
        }
        for (Wyciag wyciag : osrodek.wyciagi()) {
            kolejkaZdarzen.dodaj(new OdjazdWyciagu(POCZATEK_DNIA, wyciag));
        }
    }

    /**
     * Dopóki kolejka zdarzeń nie jest pusta, kolejno:
     * 1. Zdejmujemy następne zdarzenie z kolejki,
     * 2. Przetwarzamy je,
     * 3. Dorzucamy na kolejkę nowe zdarzenia, o ile chcemy je jeszcze przetwarzać.
     */
    private void glownaPetla(KolejkaZdarzen kolejkaZdarzen, Dziennik dziennik) {
        while (!kolejkaZdarzen.czyPusta()) {
            Zdarzenie nastepneZdarzenie = kolejkaZdarzen.zdejmij();
            Zdarzenie[] noweZdarzenia = nastepneZdarzenie.przetworz(dziennik);

            for (Zdarzenie noweZdarzenie : noweZdarzenia) {
                if (noweZdarzenie.moment().wczesniejNiz(KONIEC_SYMULACJI)
                    || noweZdarzenie.czyPrzetwarzacPoZakonczeniuSymulacji()) {
                    kolejkaZdarzen.dodaj(noweZdarzenie);
                }
            }
        }
    }

    /**
     * Wypisuje statystyki końcowe symulacji.
     */
    private void zbierzStatystyki(Osrodek osrodek, Dziennik dziennik) {
        String[][] statystyki = new String[osrodek.trasy().length + osrodek.wyciagi().length][2];
        for (int i = 0; i < osrodek.trasy().length; i++) {
            statystyki[i] = new String[]{osrodek.trasy()[i].toString(), osrodek.trasy()[i].wypiszStatystyki()};
        }
        for (int i = 0; i < osrodek.wyciagi().length; i++) {
            statystyki[osrodek.trasy().length + i] = new String[]{osrodek.wyciagi()[i].toString(),
                osrodek.wyciagi()[i].wypiszStatystyki()};
        }
        dziennik.dodajTabele(statystyki);
    }

    private void wygenerujMapki(Osrodek osrodek, Sportowiec[] sportowcy) throws WyjatekSystemuPlikow {
        Mapka mapka = new Mapka(osrodek, sportowcy, generatorMapek);
        mapka.generujMapkeParametry();
        mapka.generujMapkeStatystyki();
        mapka.generujMapkeSportowcy();
    }
}
