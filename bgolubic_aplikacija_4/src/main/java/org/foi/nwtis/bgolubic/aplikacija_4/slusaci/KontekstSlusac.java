package org.foi.nwtis.bgolubic.aplikacija_4.slusaci;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.Konfiguracija;
import org.foi.nwtis.KonfiguracijaApstraktna;
import org.foi.nwtis.NeispravnaKonfiguracija;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;

public class KontekstSlusac implements ServletContextListener {
  private ServletContext context = null;

  @Override
  public void contextInitialized(ServletContextEvent event) {
    context = event.getServletContext();

    Logger.getGlobal().log(Level.INFO, "Kreiraj kontekst: " + context.getContextPath());
    ucitajKonfiguraciju();
  }


  private void ucitajKonfiguraciju() {
    String path = context.getRealPath("/WEB-INF") + java.io.File.separator;
    String datoteka = context.getInitParameter("konfiguracija");
    try {
      Konfiguracija konf = KonfiguracijaApstraktna.preuzmiKonfiguraciju(path + datoteka);

      context.setAttribute("konfig", konf);
      Logger.getGlobal().log(Level.INFO, "Ucitana konfiguracija!");
    } catch (NeispravnaKonfiguracija e) {
      Logger.getGlobal().log(Level.SEVERE, "Problem s konfiguracijom!");
    }
  }


  @Override
  public void contextDestroyed(ServletContextEvent event) {
    context = event.getServletContext();
    Logger.getGlobal().log(Level.INFO, "Obrisan kontekst: " + context.getContextPath());
  }
}
