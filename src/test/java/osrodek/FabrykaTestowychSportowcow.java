package osrodek;

import osrodek.czas.Moment;
import osrodek.losowosc.DeterministycznaMaszynaLosujaca;
import osrodek.sportowcy.Sportowiec;
import osrodek.osrodek.Wezel;
import osrodek.sportowcy.pamiec.Znudzenie;
import osrodek.sportowcy.plan.PlanAtrakcyjnoscNatychmiastowa;

public class FabrykaTestowychSportowcow {
  private static int obecneId = 1;

  public static Sportowiec generuj(Wezel startowy) {
    return new Sportowiec(
      obecneId++,
      5,
      0.5,
      0.1,
      0.3,
      0.3,
      null,
      startowy,
      new Moment(0, 0, 0),
      new PlanAtrakcyjnoscNatychmiastowa(),
      new Znudzenie(0.1),
      new DeterministycznaMaszynaLosujaca(0)
    );
  }
}
