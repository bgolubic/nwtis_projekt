package org.foi.nwtis.bgolubic.aplikacija_1;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.Konfiguracija;
import org.foi.nwtis.KonfiguracijaApstraktna;
import org.foi.nwtis.NeispravnaKonfiguracija;

public class GlavniPosluzitelj {

  protected Konfiguracija konf;
  private int mreznaVrata;
  private int brojCekaca;
  private int brojRadnika;
  private ServerSocket posluzitelj;
  protected int brojAktivnihDretvi = 0;
  protected boolean kraj = false;
  protected boolean pauza = true;
  protected int brojZahtjeva = 0;
  protected boolean info = false;

  public GlavniPosluzitelj(String datotekaKonfiguracije) {
    try {
      this.konf = ucitajPostavke(datotekaKonfiguracije);
    } catch (NeispravnaKonfiguracija e) {
      Logger.getLogger(PokretacPosluzitelja.class.getName()).log(Level.SEVERE,
          "ERROR 05 Pogreška kod učitavanja postavki iz datoteke! " + e.getMessage());
    }
    this.mreznaVrata = Integer.parseInt(konf.dajPostavku("mreznaVrata"));
    this.brojCekaca = Integer.parseInt(konf.dajPostavku("brojCekaca"));
    this.brojRadnika = Integer.parseInt(konf.dajPostavku("brojRadnika"));
  }

  Konfiguracija ucitajPostavke(String nazivDatoteke) throws NeispravnaKonfiguracija {
    return KonfiguracijaApstraktna.preuzmiKonfiguraciju(nazivDatoteke);
  }

  public void pokreniPosluzitelj() {
    Logger.getGlobal().log(Level.INFO, "Poslužitelj upaljen");
    otvoriMreznaVrata();
  }

  public synchronized void otvoriMreznaVrata() {
    try {
      if (this.brojAktivnihDretvi < this.brojRadnika) {
        this.posluzitelj = new ServerSocket(this.mreznaVrata, this.brojCekaca);
        while (!this.kraj) {
          var uticnica = this.posluzitelj.accept();
          var dretva = new MrezniRadnik(uticnica, konf, this);
          dretva.start();
          this.brojAktivnihDretvi++;
        }
      }
    } catch (IOException e) {
      Logger.getGlobal().log(Level.SEVERE, e.getMessage());
    } finally {
      try {
        this.posluzitelj.close();
      } catch (IOException e) {
        Logger.getGlobal().log(Level.INFO, "Poslužitelj ugašen");
      }
    }
  }

  public void ugasiPosluzitelj() {
    if (this.brojAktivnihDretvi == 0)
      try {
        this.posluzitelj.close();
      } catch (IOException e) {
        Logger.getGlobal().log(Level.INFO, "Poslužitelj ugašen");
      }
  }
}
