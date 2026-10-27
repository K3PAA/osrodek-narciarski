package osrodek.kolejkaZdarzen.zdarzenia;

import osrodek.czas.Moment;
import osrodek.osrodek.Wezel;
import osrodek.osrodek.krawedz.wyciag.Wyciag;
import osrodek.sportowcy.Sportowiec;

public class DotarcieDoWezlaPoWjezdzie extends DotarcieDoWezla {
  public DotarcieDoWezlaPoWjezdzie(Moment moment, Wyciag wyciag, Wezel wezel, Sportowiec sportowiec) {
    super(moment, wyciag, wezel, sportowiec);
  }
}
