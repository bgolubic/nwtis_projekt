package org.foi.nwtis.bgolubic.aplikacija_2.rest;

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
import org.foi.nwtis.podaci.OdgovorPosluzitelj;
import com.google.gson.Gson;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.servlet.ServletContext;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("nadzor")
@RequestScoped
public class RestNadzor {

  @Inject
  ServletContext context;

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response posaljiStatus() {

    var poruka = posaljiNaPosluzitelj("STATUS");

    OdgovorPosluzitelj odgPosluzitelj;

    if (poruka.startsWith("OK"))
      odgPosluzitelj = new OdgovorPosluzitelj(200, poruka);
    else
      odgPosluzitelj = new OdgovorPosluzitelj(400, poruka);

    var gson = new Gson();

    var jsonOdgovor = gson.toJson(odgPosluzitelj);

    var odgovor = Response.ok().entity(jsonOdgovor).build();

    return odgovor;
  }

  @Path("{komanda}")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response posaljiKomandu(@PathParam("komanda") String komanda) {

    if (komanda.equals("KRAJ") || komanda.equals("INIT") || komanda.equals("PAUZA")) {
      var poruka = posaljiNaPosluzitelj(komanda);

      OdgovorPosluzitelj odgPosluzitelj;

      if (poruka.startsWith("OK"))
        odgPosluzitelj = new OdgovorPosluzitelj(200, poruka);
      else
        odgPosluzitelj = new OdgovorPosluzitelj(400, poruka);

      var gson = new Gson();

      var jsonOdgovor = gson.toJson(odgPosluzitelj);

      var odgovor = Response.ok().entity(jsonOdgovor).build();

      return odgovor;
    } else {
      var odgovor = Response.ok().entity("Neispravna komanda").build();
      return odgovor;
    }
  }

  @Path("INFO/{vrsta}")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response posaljiInfo(@PathParam("vrsta") String vrsta) {

    if (vrsta.equals("DA") || vrsta.equals("NE")) {
      var poruka = posaljiNaPosluzitelj("INFO " + vrsta);

      OdgovorPosluzitelj odgPosluzitelj;

      if (poruka.startsWith("OK"))
        odgPosluzitelj = new OdgovorPosluzitelj(200, poruka);
      else
        odgPosluzitelj = new OdgovorPosluzitelj(400, poruka);

      var gson = new Gson();

      var jsonOdgovor = gson.toJson(odgPosluzitelj);

      var odgovor = Response.ok().entity(jsonOdgovor).build();

      return odgovor;
    } else {
      var odgovor = Response.ok().entity("Neispravna vrsta").build();
      return odgovor;
    }
  }

  private String posaljiNaPosluzitelj(String komanda) {
    Konfiguracija konf = (Konfiguracija) context.getAttribute("konfig");
    var poruka = new StringBuilder();

    try {
      var mreznaUticnica = new Socket(konf.dajPostavku("adresaPosluzitelja"),
          Integer.parseInt(konf.dajPostavku("mreznaVrataPosluzitelja")));
      mreznaUticnica.setSoTimeout(Integer.parseInt(konf.dajPostavku("maksCekanje")));
      var citac = new BufferedReader(
          new InputStreamReader(mreznaUticnica.getInputStream(), Charset.forName("UTF-8")));
      var pisac = new BufferedWriter(
          new OutputStreamWriter(mreznaUticnica.getOutputStream(), Charset.forName("UTF-8")));

      String zahtjev = komanda;
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
