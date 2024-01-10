package org.foi.nwtis.bgolubic.aplikacija_1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.Konfiguracija;

/**
 * Klasa MrezniRadnik koja je zadužena za obradu zahtjeva koji stižu na glavni poslužitelj
 * 
 * @author Bruno Golubić
 *
 */
public class MrezniRadnik extends Thread {

  protected Socket mreznaUticnica;
  protected Konfiguracija konfig;
  private GlavniPosluzitelj gp;
  private String odgovor;

  /**
   * Konstruktor klase u kojem se učitavaju u memoriju podaci iz konfiguracije
   */
  public MrezniRadnik(Socket mreznaUticnica, Konfiguracija konfig, GlavniPosluzitelj gp) {
    this.mreznaUticnica = mreznaUticnica;
    this.konfig = konfig;
    this.gp = gp;
  }

  /**
   * Metoda koja se vrti kada se stvori dretva, ispisuje naziv trenutne dretve
   */
  @Override
  public void start() {
    super.start();
  }

  /**
   * Metoda koja se vrti nakon pokretanja dretve U ovoj metodi se otvaraju čitač i pisač koji
   * čitaju/pišu podatke preko mrežne utičnice Nakon pročitanih podataka ide se na obradu poslanog
   * zahtjeva
   * 
   * @throws IOException - baca iznimku ako dođe do problema
   */
  @Override
  public void run() {
    try {
      var citac = new BufferedReader(
          new InputStreamReader(this.mreznaUticnica.getInputStream(), Charset.forName("UTF-8")));
      var pisac = new BufferedWriter(
          new OutputStreamWriter(this.mreznaUticnica.getOutputStream(), Charset.forName("UTF-8")));
      var poruka = new StringBuilder();
      while (true) {
        var red = citac.readLine();
        if (red == null)
          break;

        poruka.append(red);
      }
      this.mreznaUticnica.shutdownInput();
      if (gp.info)
        System.out.println(poruka.toString());
      this.obradiZahtjev(poruka.toString());
      pisac.write(odgovor);
      pisac.flush();
      this.mreznaUticnica.shutdownOutput();
      this.mreznaUticnica.close();
      this.interrupt();
    } catch (IOException e) {
      Logger.getGlobal().log(Level.SEVERE, e.getMessage());
    }
  }

  /**
   * Metoda u kojoj se provjerava korisnik i ako je u redu kreće se s obradom predmetnog dijela
   * komande
   */
  public void obradiZahtjev(String komanda) {
    if (komanda.equals("PAUZA")) {
      odgovor = obradiPauzu();
      return;
    }

    if (komanda.equals("STATUS")) {
      odgovor = obradiStatus();
      return;
    }

    if (komanda.equals("KRAJ")) {
      odgovor = "OK";
      gp.kraj = true;
      gp.ugasiPosluzitelj();
      return;
    }

    if (komanda.equals("INIT")) {
      odgovor = obradiInicijalizaciju();
      return;
    }


    if (gp.pauza) {
      odgovor = "ERROR 01 Poslužitelj je pauziran";
      return;
    } else {
      if (komanda.startsWith("INFO")) {
        odgovor = obradiInfo(komanda);
        return;
      }

      if (komanda.startsWith("UDALJENOST")) {
        odgovor = obradiUdaljenost(komanda);
        return;
      }
    }
  }

  private String obradiPauzu() {
    gp.pauza = true;
    return "OK " + gp.brojZahtjeva;
  }

  private String obradiStatus() {
    int status;
    if (gp.pauza)
      status = 0;
    else
      status = 1;
    return "OK " + status;
  }

  private String obradiInicijalizaciju() {
    if (gp.pauza) {
      gp.pauza = false;
      gp.brojZahtjeva = 0;
      return "OK";
    } else
      return "ERROR 02 Poslužitelj je već aktivan";

  }

  private String obradiInfo(String komanda) {
    String[] podijeljenaKomanda = komanda.split("\\s+");
    if (podijeljenaKomanda[1].equals("DA")) {
      if (gp.info)
        return "ERROR 03 Poslužitelj već ispisuje podatke";
      else
        gp.info = true;
    }

    else if (podijeljenaKomanda[1].equals("NE")) {
      if (!gp.info)
        return "ERROR 04 Poslužitelj ne ispisuje podatke";
      else
        gp.info = false;
    }

    return "OK";
  }

  /**
   * Metoda u kojoj se obrađuje komanda UDALJENOST
   */
  public String obradiUdaljenost(String komanda) {
    String[] podijeljenaKomanda = komanda.split("\\s+");
    var gpsSirina1 = podijeljenaKomanda[1];
    var gpsDuzina1 = podijeljenaKomanda[2];
    var gpsSirina2 = podijeljenaKomanda[3];
    var gpsDuzina2 = podijeljenaKomanda[4];

    return "OK " + izracunajUdaljenost(gpsSirina1, gpsDuzina1, gpsSirina2, gpsDuzina2);
  }

  private String izracunajUdaljenost(String gpsSirina1, String gpsDuzina1, String gpsSirina2,
      String gpsDuzina2) {

    float sirina1 = Float.parseFloat(gpsSirina1);
    float sirina2 = Float.parseFloat(gpsSirina2);
    float duzina1 = Float.parseFloat(gpsDuzina1);
    float duzina2 = Float.parseFloat(gpsDuzina2);
    if (((sirina1 < -90 || sirina1 > 90) || (sirina2 < -90 || sirina2 > 90))
        || ((duzina1 < -180 || duzina1 > 180) || (duzina2 < -180 || duzina2 > 180)))
      return "ERROR 05 Širina/dužina je neispravna.";

    double earthRadius = 6371000;
    double dLat = Math.toRadians(sirina2 - sirina1);
    double dLng = Math.toRadians(duzina2 - duzina1);
    double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(Math.toRadians(sirina1))
        * Math.cos(Math.toRadians(sirina2)) * Math.sin(dLng / 2) * Math.sin(dLng / 2);
    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    float dist = (float) (earthRadius * c);
    dist = dist / 1000;

    gp.brojZahtjeva++;

    return String.valueOf(dist);
  }

  @Override
  public void interrupt() {
    this.gp.brojAktivnihDretvi--;
    if (this.gp.kraj)
      this.gp.ugasiPosluzitelj();
    super.interrupt();
  }

}
