package org.foi.nwtis.bgolubic.aplikacija_5.mvc;

import org.foi.nwtis.bgolubic.aplikacija_4.ws.WsMeteo.endpoint.Meteo;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.mvc.Controller;
import jakarta.mvc.Models;
import jakarta.mvc.View;
import jakarta.servlet.ServletContext;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.xml.ws.WebServiceRef;

@Controller
@Path("meteo")
@RequestScoped
public class KontrolerMeteo {
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
}
