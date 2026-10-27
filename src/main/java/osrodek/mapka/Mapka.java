package osrodek.mapka;

import kadra.mapki.GeneratorMapek;
import kadra.mapki.pliki.WyjatekSystemuPlikow;
import kadra.mapki.styl.GruboscKonturu;
import kadra.mapki.styl.StylKrawedzi;
import kadra.mapki.styl.StylLinii;
import kadra.mapki.styl.StylWezla;
import osrodek.osrodek.Osrodek;
import osrodek.osrodek.Wezel;
import osrodek.osrodek.krawedz.Trasa;
import osrodek.osrodek.krawedz.wyciag.Wyciag;
import osrodek.sportowcy.Sportowiec;

public class Mapka {
  private final Osrodek osrodek;
  private final Sportowiec[] sportowcy;
  private final GeneratorMapek generatorMapek;

  public Mapka(Osrodek osrodek, Sportowiec[] sportowcy, GeneratorMapek generatorMapek)  {
    this.osrodek = osrodek;
    this.sportowcy = sportowcy;
    this.generatorMapek = generatorMapek;
  }

  private void generujWezly() {
    for(Wezel wezel: osrodek.wezly()) {
      generatorMapek.dodajWezel(
        wezel.id(),
        wezel.wspolrzednaX(),
        wezel.wspolrzednaY(),
        new StylWezla(wezel.czyStartowy() ? GruboscKonturu.POGRUBIONY : GruboscKonturu.ZWYKLY)
      );
    }
  }

  private void generujLinie(RodzajTekstu rodzaj) {
    generujLinieTras(rodzaj);
    generujLinieWyciagow(rodzaj);
  }

  private void generujLinie(Sportowiec sportowiec) {
    generujLinieTras(sportowiec);
    generujLinieWyciagow(sportowiec);
  }

  private void generujLinieTras(RodzajTekstu rodzaj) {
    for(Trasa trasa: osrodek.trasy()) {
      generatorMapek.dodajKrawedz(
        trasa.poczatek().id(),
        trasa.koniec().id(),
        new StylKrawedzi(StylLinii.CIAGLA),
        trasa.mapkaTekst(rodzaj)
      );
    }
  }

  private void generujLinieTras(Sportowiec sportowiec) {
    for(Trasa trasa: osrodek.trasy()) {
      generatorMapek.dodajKrawedz(
        trasa.poczatek().id(),
        trasa.koniec().id(),
        new StylKrawedzi(StylLinii.CIAGLA),
        String.format("t%d%s", trasa.id(),
          sportowiec.historiaPrzejazdow().wygenerujStatystykiDlaKrawedzi(trasa))
      );
    }
  }

  private void generujLinieWyciagow(RodzajTekstu rodzaj) {
    for(Wyciag wyciag: osrodek.wyciagi()) {
        generatorMapek.dodajKrawedz(
          wyciag.poczatek().id(),
          wyciag.koniec().id(),
          new StylKrawedzi(StylLinii.PRZERYWANA),
          wyciag.mapkaTekst(rodzaj)
        );
    }
  }

  private void generujLinieWyciagow(Sportowiec sportowiec) {
    for(Wyciag wyciag: osrodek.wyciagi()) {
        generatorMapek.dodajKrawedz(
          wyciag.poczatek().id(),
          wyciag.koniec().id(),
          new StylKrawedzi(StylLinii.PRZERYWANA),
          String.format("w%d%s", wyciag.id(),
            sportowiec.historiaPrzejazdow().wygenerujStatystykiDlaKrawedzi(wyciag))
      );
    }
  }

  public void generujMapkeParametry() throws WyjatekSystemuPlikow {
    generatorMapek.zeruj();
    generujWezly();
    generujLinie(RodzajTekstu.PARAMETRY);
    generatorMapek.tworzMapke("mapka-parametry.tex");
  }

  public void generujMapkeStatystyki() throws WyjatekSystemuPlikow  {
    generatorMapek.zeruj();
    generujWezly();
    generujLinie(RodzajTekstu.STATYSTYKI);
    generatorMapek.tworzMapke("mapka-statystyki.tex");
  }

  public void generujMapkeSportowcy() throws WyjatekSystemuPlikow {
    for(Sportowiec sportowiec: sportowcy) {
      if (sportowiec.sledzony()) {
        generatorMapek.zeruj();
        generujWezly();
        generujLinie(sportowiec);
        generatorMapek.tworzMapke(String.format("mapka-sportowiec-%d.tex", sportowiec.id()));
      }
    }
  }
}
