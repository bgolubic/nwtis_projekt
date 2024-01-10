package org.foi.nwtis.bgolubic.aplikacija_5.mvc;

import org.foi.nwtis.Konfiguracija;
import org.foi.nwtis.bgolubic.aplikacija_5.rest.RestKlijentDnevnika;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.mvc.Controller;
import jakarta.mvc.Models;
import jakarta.mvc.View;
import jakarta.servlet.ServletContext;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@Controller
@Path("dnevnik")
@RequestScoped
public class KontrolerDnevnik {
  @Inject
  ServletContext context;

  @Inject
  private Models model;

  @GET
  @Path("pregled")
  @View("dnevnik.jsp")
  public void getZapisi() {
    try {
      Konfiguracija konfig = (Konfiguracija) context.getAttribute("konfig");
      RestKlijentDnevnika rcd = new RestKlijentDnevnika(konfig);
      var zapisi = rcd.getZapisi();
      model.put("zapisi", zapisi);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
