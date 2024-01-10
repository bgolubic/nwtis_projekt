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

public class Tester {
  public static void main(String[] args) {
    var gk = new Tester();

    gk.spojiSeNaPosluzitelj("localhost", 8000, args[0]);
  }

  private void spojiSeNaPosluzitelj(String adresa, Integer mreznaVrata, String arg) {
    try {
      var mreznaUticnica = new Socket(adresa, mreznaVrata);
      mreznaUticnica.setSoTimeout(10000);
      var citac = new BufferedReader(
          new InputStreamReader(mreznaUticnica.getInputStream(), Charset.forName("UTF-8")));
      var pisac = new BufferedWriter(
          new OutputStreamWriter(mreznaUticnica.getOutputStream(), Charset.forName("UTF-8")));
      var poruka = new StringBuilder();
      String zahtjev;
      switch (arg) {
        case "1":
          zahtjev = "STATUS";
          break;
        case "2":
          zahtjev = "KRAJ";
          break;
        case "3":
          zahtjev = "INIT";
          break;
        case "4":
          zahtjev = "PAUZA";
          break;
        case "5":
          zahtjev = "INFO DA";
          break;
        case "6":
          zahtjev = "INFO NE";
          break;
        case "7":
          zahtjev = "UDALJENOST 46.30771 16.33808 46.02419 15.90968";
          break;
        default:
          zahtjev = "";
      }
      pisac.write(zahtjev);
      pisac.flush();
      mreznaUticnica.shutdownOutput();
      while (true) {
        var red = citac.readLine();
        if (red == null)
          break;
        Logger.getGlobal().log(Level.INFO, red);

        poruka.append("RED: " + red);
      }
      Logger.getGlobal().log(Level.INFO, "Odgovor: " + poruka);
      mreznaUticnica.shutdownInput();
      mreznaUticnica.close();
    } catch (IOException e) {
      Logger.getGlobal().log(Level.SEVERE, e.getMessage());
    }
  }
}
