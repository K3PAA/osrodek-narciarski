package osrodek.kolejkaZdarzen.zdarzenia;

import osrodek.czas.Moment;
import osrodek.dziennik.Dziennik;
import osrodek.osrodek.Wezel;
import osrodek.osrodek.krawedz.Trasa;
import osrodek.sportowcy.Sportowiec;

public class DotarcieDoWezlaPoZjezdzie extends DotarcieDoWezla {
  public DotarcieDoWezlaPoZjezdzie(Moment moment, Trasa trasa, Wezel wezel, Sportowiec sportowiec) {
    super(moment, trasa, wezel, sportowiec);
  }

  @Override
  public Zdarzenie[] przetworz(Dziennik dziennik) {
    sportowiec.zarejestrujZjazd((Trasa) poprzedniaKrawedz);
    return super.przetworz(dziennik);
  }
}
