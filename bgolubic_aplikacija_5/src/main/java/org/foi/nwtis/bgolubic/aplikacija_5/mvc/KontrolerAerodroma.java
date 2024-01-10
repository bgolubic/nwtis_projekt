package org.foi.nwtis.bgolubic.aplikacija_5.mvc;


import org.foi.nwtis.Konfiguracija;
import org.foi.nwtis.bgolubic.aplikacija_4.ws.WsAerodromi.endpoint.Aerodromi;
import org.foi.nwtis.bgolubic.aplikacija_4.ws.WsMeteo.endpoint.Meteo;
import org.foi.nwtis.bgolubic.aplikacija_5.rest.RestKlijentAerodroma;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.mvc.Controller;
import jakarta.mvc.Models;
import jakarta.mvc.View;
import jakarta.servlet.ServletContext;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import jakarta.xml.ws.WebServiceRef;

/**
 *
 * @author NWTiS
 */
@Controller
@Path("aerodromi")
@RequestScoped
public class KontrolerAerodroma {

  @WebServiceRef(wsdlLocation = "http://localhost:8080/bgolubic_aplikacija_4/aerodromi?wsdl")
  private Aerodromi serviceAerodromi;

  @WebServiceRef(wsdlLocation = "http://localhost:8080/bgolubic_aplikacija_4/meteo?wsdl")
  private Meteo serviceMeteo;

  @Inject
  ServletContext context;

  @Inject
  private Models model;

  @GET
  @Path("pocetak")
  @View("index.jsp")
  public void pocetak() {}

  @GET
  @Path("meni")
  @View("meniAerodromi.jsp")
  public void meniAerodromi() {}

  @GET
  @Path("svi")
  @View("aerodromi.jsp")
  public void getAerodromi(@QueryParam("naziv") String naziv, @QueryParam("drzava") String drzava) {
    try {
      Konfiguracija konfig = (Konfiguracija) context.getAttribute("konfig");
      RestKlijentAerodroma rca = new RestKlijentAerodroma(konfig);
      var aerodromi = rca.getAerodromi(naziv, drzava);
      model.put("aerodromi", aerodromi);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @GET
  @Path("icao")
  @View("aerodrom.jsp")
  public void getAerodrom(@QueryParam("icao") String icao) {
    try {
      Konfiguracija konfig = (Konfiguracija) context.getAttribute("konfig");
      RestKlijentAerodroma rca = new RestKlijentAerodroma(konfig);
      var port = serviceMeteo.getWsMeteoPort();
      var podaci = port.dajMeteo("bgolubic", "lozinka123", icao);
      var aerodrom = rca.getAerodrom(icao);
      model.put("aerodrom", aerodrom);
      model.put("podaci", podaci);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @GET
  @Path("preuzimanje")
  @View("aerodromiPreuzimanje.jsp")
  public void getAerodromiPreuzimanje() {
    try {
      var port = serviceAerodromi.getWsAerodromiPort();
      var aerodromi = port.dajAerodromeZaLetove("bgolubic", "lozinka123");
      model.put("aerodromi", aerodromi);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @GET
  @Path("pauza")
  @View("aerodromiPreuzimanje.jsp")
  public void pauzaAerodrom(@QueryParam("icao") String icao) {
    try {
      var port = serviceAerodromi.getWsAerodromiPort();
      port.pauzirajAerodromZaLetove("bgolubic", "lozinka123", icao);
      var aerodromi = port.dajAerodromeZaLetove("bgolubic", "lozinka123");
      model.put("aerodromi", aerodromi);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @GET
  @Path("nastavi")
  @View("aerodromiPreuzimanje.jsp")
  public void nastaviAerodrom(@QueryParam("icao") String icao) {
    try {
      var port = serviceAerodromi.getWsAerodromiPort();
      port.aktivirajAerodromZaLetove("bgolubic", "lozinka123", icao);
      var aerodromi = port.dajAerodromeZaLetove("bgolubic", "lozinka123");
      model.put("aerodromi", aerodromi);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @GET
  @Path("odabirUdaljenostiOdDo")
  @View("odabirUdaljenostiOdDo.jsp")
  public void odabirUdaljenostiOdDo() {}

  @GET
  @Path("udaljenosti")
  @View("udaljenosti.jsp")
  public void udaljenosti(@QueryParam("icaoOd") String icaoOd,
      @QueryParam("icaoDo") String icaoDo) {
    try {
      Konfiguracija konfig = (Konfiguracija) context.getAttribute("konfig");
      RestKlijentAerodroma rca = new RestKlijentAerodroma(konfig);
      var udaljenosti = rca.getAerodromiUdaljenosti(icaoOd, icaoDo);
      model.put("udaljenosti", udaljenosti);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @GET
  @Path("odabirUdaljenost")
  @View("odabirUdaljenost.jsp")
  public void odabirUdaljenost() {}

  @GET
  @Path("udaljenost")
  @View("udaljenost.jsp")
  public void udaljenost(@QueryParam("icaoOd") String icaoOd, @QueryParam("icaoDo") String icaoDo) {
    try {
      Konfiguracija konfig = (Konfiguracija) context.getAttribute("konfig");
      RestKlijentAerodroma rca = new RestKlijentAerodroma(konfig);
      var udaljenost = rca.getAerodromiUdaljenost(icaoOd, icaoDo);
      model.put("udaljenost", udaljenost);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @GET
  @Path("odabirUdaljenostDrzava")
  @View("odabirUdaljenostDrzava.jsp")
  public void odabirUdaljenostDrzava() {}

  @GET
  @Path("udaljenostDrzava")
  @View("udaljenostDrzava.jsp")
  public void udaljenostDrzava(@QueryParam("icaoOd") String icaoOd,
      @QueryParam("icaoDo") String icaoDo) {
    try {
      Konfiguracija konfig = (Konfiguracija) context.getAttribute("konfig");
      RestKlijentAerodroma rca = new RestKlijentAerodroma(konfig);
      var udaljenosti = rca.getAerodromiUdaljenostDrzava(icaoOd, icaoDo);
      model.put("udaljenosti", udaljenosti);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @GET
  @Path("odabirUdaljenostDrzavaKm")
  @View("odabirUdaljenostDrzavaKm.jsp")
  public void odabirUdaljenostDrzavaKm() {}

  @GET
  @Path("udaljenostDrzavaKm")
  @View("udaljenostDrzavaKm.jsp")
  public void udaljenostDrzavaKm(@QueryParam("icaoOd") String icaoOd,
      @QueryParam("drzava") String drzava, @QueryParam("km") String km) {
    try {
      Konfiguracija konfig = (Konfiguracija) context.getAttribute("konfig");
      RestKlijentAerodroma rca = new RestKlijentAerodroma(konfig);
      var udaljenosti = rca.getAerodromiUdaljenostDrzavaKm(icaoOd, drzava, km);
      model.put("udaljenosti", udaljenosti);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
