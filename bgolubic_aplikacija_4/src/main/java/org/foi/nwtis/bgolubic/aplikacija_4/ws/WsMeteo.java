package org.foi.nwtis.bgolubic.aplikacija_4.ws;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.Konfiguracija;
import org.foi.nwtis.bgolubic.aplikacija_4.iznimke.PogresnaAutentikacija;
import org.foi.nwtis.bgolubic.aplikacija_4.pomocnici.AutentikacijaKorisnika;
import org.foi.nwtis.bgolubic.aplikacija_4.rest.RestKlijentAerodroma;
import org.foi.nwtis.podaci.Lokacija;
import org.foi.nwtis.rest.klijenti.NwtisRestIznimka;
import org.foi.nwtis.rest.klijenti.OWMKlijent;
import org.foi.nwtis.rest.podaci.MeteoPodaci;
import jakarta.annotation.Resource;
import jakarta.inject.Inject;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;
import jakarta.servlet.ServletContext;
import jakarta.xml.bind.annotation.XmlElement;

@WebService(serviceName = "meteo")
public class WsMeteo {
  @Resource(lookup = "java:app/jdbc/nwtis_bp")
  javax.sql.DataSource ds;

  @Inject
  private ServletContext context;

  @WebMethod
  public MeteoPodaci dajMeteo(
      @WebParam(name = "korisnik") @XmlElement(required = true) String korisnik,
      @WebParam(name = "lozinka") @XmlElement(required = true) String lozinka,
      @WebParam(name = "icao") @XmlElement(required = true) String icao)
      throws PogresnaAutentikacija {
    if (AutentikacijaKorisnika.provjeraKorisnika(ds, korisnik, lozinka)) {
      Konfiguracija konf = (Konfiguracija) context.getAttribute("konfig");
      RestKlijentAerodroma rca = new RestKlijentAerodroma(konf);
      OWMKlijent klijent = new OWMKlijent(konf.dajPostavku("OpenWeatherMap.apikey"));

      Lokacija lokacija = rca.getLokacija(icao);

      try {
        MeteoPodaci podaci =
            klijent.getRealTimeWeather(lokacija.getLongitude(), lokacija.getLatitude());
        return podaci;
      } catch (NwtisRestIznimka e) {
        Logger.getGlobal().log(Level.SEVERE, e.getMessage());
      }
    }
    return null;
  }
}
