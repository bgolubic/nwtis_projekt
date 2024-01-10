package org.foi.nwtis.bgolubic.aplikacija_2.rest;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.podaci.ZapisDnevnik;
import com.google.gson.Gson;
import jakarta.annotation.Resource;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.servlet.ServletContext;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("dnevnik")
@RequestScoped
public class RestDnevnik {

  @Resource(lookup = "java:app/jdbc/nwtis_bp")
  javax.sql.DataSource ds;

  @Inject
  ServletContext context;

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response dajSveAerodrome(@QueryParam("odBroja") int odBroja, @QueryParam("broj") int broj,
      @QueryParam("vrsta") String vrsta) {

    if (odBroja <= 0 || broj <= 0) {
      odBroja = 1;
      broj = 20;
    }

    List<ZapisDnevnik> podaciDnevnik = new ArrayList<>();
    String query = "";
    if (vrsta == null) {
      query = "SELECT * FROM DNEVNIK LIMIT " + broj + " OFFSET " + odBroja;
    } else {
      query = "SELECT * FROM DNEVNIK WHERE FILTER = '" + vrsta + "' LIMIT " + broj + " OFFSET "
          + odBroja;
    }

    PreparedStatement pstmt = null;
    try (var con = ds.getConnection()) {
      pstmt = con.prepareStatement(query);

      ResultSet rs = pstmt.executeQuery();
      while (rs.next()) {
        int id = rs.getInt("ID");
        String filter = rs.getString("FILTER");
        String requestUri = rs.getString("REQUEST_URI");
        Timestamp dateTime = rs.getTimestamp("DATETIME");
        ZapisDnevnik zd = new ZapisDnevnik(id, filter, requestUri, dateTime);
        podaciDnevnik.add(zd);
      }
      rs.close();
      pstmt.close();
      con.close();
    } catch (SQLException e) {
      e.printStackTrace();
      Logger.getGlobal().log(Level.SEVERE, e.getMessage());
    } finally {
      try {
        if (pstmt != null && !pstmt.isClosed())
          pstmt.close();
      } catch (SQLException e) {
        Logger.getGlobal().log(Level.SEVERE, e.getMessage());
      }
    }
    var gson = new Gson();
    var jsonAerodromi = gson.toJson(podaciDnevnik);

    var odgovor = Response.ok().entity(jsonAerodromi).build();

    return odgovor;
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  public void spremiZapis(ZapisDnevnik zapis) {

    String query = String.format(
        "INSERT INTO DNEVNIK (FILTER, REQUEST_URI, DATETIME) VALUES('%s', '%s', '%s')",
        zapis.filter(), zapis.requestUri(), Timestamp.from(Instant.now()).toString());

    PreparedStatement pstmt = null;
    try (var con = ds.getConnection()) {
      pstmt = con.prepareStatement(query);

      pstmt.execute();

      pstmt.close();
      con.close();
    } catch (SQLException e) {
      e.printStackTrace();
      Logger.getGlobal().log(Level.SEVERE, e.getMessage());
    } finally {
      try {
        if (pstmt != null && !pstmt.isClosed())
          pstmt.close();
      } catch (SQLException e) {
        Logger.getGlobal().log(Level.SEVERE, e.getMessage());
      }
    }
  }
}
