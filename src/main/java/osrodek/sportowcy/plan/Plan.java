package osrodek.sportowcy.plan;
import osrodek.czas.Moment;
import osrodek.kolejkaZdarzen.zdarzenia.Zdarzenie;
import osrodek.osrodek.Wezel;
import osrodek.sportowcy.Sportowiec;

public interface Plan {
  Zdarzenie podajNastepnyKrok(Sportowiec sportowiec, Moment moment, Wezel obecnyWezel);
}
