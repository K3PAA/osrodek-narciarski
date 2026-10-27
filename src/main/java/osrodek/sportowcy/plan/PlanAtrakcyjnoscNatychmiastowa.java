package osrodek.sportowcy.plan;

import osrodek.czas.Moment;
import osrodek.kolejkaZdarzen.zdarzenia.Zdarzenie;
import osrodek.osrodek.Wezel;
import osrodek.osrodek.krawedz.Trasa;
import osrodek.osrodek.krawedz.wyciag.Wyciag;
import osrodek.sportowcy.Sportowiec;


public class PlanAtrakcyjnoscNatychmiastowa implements Plan {
  @Override
  public Zdarzenie podajNastepnyKrok(Sportowiec sportowiec, Moment moment, Wezel obecnyWezel) {
    if (sportowiec.czySpontanicznaDecyzja()) {
      return sportowiec.podejmijSpontanicznaDecyzje(moment, obecnyWezel);
    } else {
      return podejmijPrzemyslanaDecyzje(sportowiec, moment, obecnyWezel);
    }
  }

  private Zdarzenie podejmijPrzemyslanaDecyzje(Sportowiec sportowiec, Moment moment, Wezel obecnyWezel) {
    Trasa[] bezposrednieTrasy = obecnyWezel.wychodzaceTrasy();
    Wyciag[] wyciagi = obecnyWezel.wychodzaceWyciagi();

    double najwiekszaAtrakcyjnosc = -1;
    Trasa najlepszaTrasa = null;
    Wyciag nastepnyWyciag = null;

    for (Trasa trasa : bezposrednieTrasy) {
      double atrakcyjnosc = sportowiec.lacznaAtrakcyjnosc(trasa);
      if (atrakcyjnosc > najwiekszaAtrakcyjnosc) {
        najwiekszaAtrakcyjnosc = atrakcyjnosc;
        najlepszaTrasa = trasa;
      }
    }

    for (Wyciag wyciag : wyciagi) {
      for (Trasa trasa : wyciag.koniec().wychodzaceTrasy()) {
        double atrakcyjnosc = sportowiec.lacznaAtrakcyjnosc(trasa);
        if (atrakcyjnosc > najwiekszaAtrakcyjnosc) {
          najwiekszaAtrakcyjnosc = atrakcyjnosc;
          najlepszaTrasa = trasa;
          nastepnyWyciag = wyciag;
        }
      }
    }

    if (najlepszaTrasa == null) {
      return sportowiec.nastepnyKrokWyciag(moment, wyciagi[0]);
    } else if (nastepnyWyciag == null) {
      return sportowiec.nastepnyKrokTrasa(moment, najlepszaTrasa);
    } else {
      return sportowiec.nastepnyKrokWyciag(moment, nastepnyWyciag);
    }
  }
}
