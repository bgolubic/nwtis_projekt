package org.foi.nwtis.bgolubic.aplikacija_5.filteri;

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
import org.foi.nwtis.bgolubic.aplikacija_5.rest.RestKlijentDnevnika;
import org.foi.nwtis.podaci.ZapisDnevnik;
import jakarta.inject.Inject;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;

public class AplikacijskiFilter implements Filter {
  @Inject
  ServletContext context;

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {

    HttpServletRequest req = (HttpServletRequest) request;

    ZapisDnevnik zapis = new ZapisDnevnik(0, "AP5", req.getRequestURI(), null);

    Konfiguracija konfig = (Konfiguracija) context.getAttribute("konfig");
    RestKlijentDnevnika rca = new RestKlijentDnevnika(konfig);

    rca.postZapis(zapis);

    if (dohvatiStatus().equals("OK 0")) {
      return;
    }

    chain.doFilter(request, response);
  }

  private String dohvatiStatus() {
    Konfiguracija konf = (Konfiguracija) context.getAttribute("konfig");
    var poruka = new StringBuilder();

    try {
      var mreznaUticnica = new Socket(konf.dajPostavku("adresaPosluziteljaAP1"),
          Integer.parseInt(konf.dajPostavku("mreznaVrataPosluziteljaAP1")));
      mreznaUticnica.setSoTimeout(Integer.parseInt(konf.dajPostavku("maksCekanje")));
      var citac = new BufferedReader(
          new InputStreamReader(mreznaUticnica.getInputStream(), Charset.forName("UTF-8")));
      var pisac = new BufferedWriter(
          new OutputStreamWriter(mreznaUticnica.getOutputStream(), Charset.forName("UTF-8")));

      String zahtjev = "STATUS";
      pisac.write(zahtjev);
      pisac.flush();
      mreznaUticnica.shutdownOutput();
      while (true) {
        var red = citac.readLine();
        if (red == null)
          break;
        poruka.append(red);
      }
      mreznaUticnica.shutdownInput();
      mreznaUticnica.close();
    } catch (IOException e) {
      Logger.getGlobal().log(Level.SEVERE, e.getMessage());
    }
    return poruka.toString();
  }
}
