package org.foi.nwtis.bgolubic.aplikacija_2.rest;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.Charset;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.Konfiguracija;
import org.foi.nwtis.podaci.Aerodrom;
import org.foi.nwtis.podaci.Lokacija;
import org.foi.nwtis.podaci.Udaljenost;
import org.foi.nwtis.podaci.UdaljenostAerodrom;
import com.google.gson.Gson;
import jakarta.annotation.Resource;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.servlet.ServletContext;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("aerodromi")
@RequestScoped
public class RestAerodromi {

  @Resource(lookup = "java:app/jdbc/nwtis_bp")
  javax.sql.DataSource ds;

  @Inject
  ServletContext context;

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response dajSveAerodrome(@QueryParam("odBroja") int odBroja, @QueryParam("broj") int broj,
      @QueryParam("traziNaziv") String nazivParam, @QueryParam("traziDrzavu") String drzavaParam) {

    if (odBroja <= 0 || broj <= 0) {
      odBroja = 1;
      broj = 20;
    }

    List<Aerodrom> aerodromi = new ArrayList<>();
    String query = "";
    if (nazivParam == null && drzavaParam == null) {
      query = "SELECT ICAO, NAME, ISO_COUNTRY, COORDINATES FROM AIRPORTS LIMIT " + broj + " OFFSET "
          + odBroja;
    } else if (nazivParam == null && drzavaParam != null) {
      query = "SELECT ICAO, NAME, ISO_COUNTRY, COORDINATES FROM AIRPORTS WHERE ISO_COUNTRY ='"
          + drzavaParam + "' LIMIT " + broj + " OFFSET " + odBroja;
    } else if (nazivParam != null && drzavaParam == null) {
      query = "SELECT ICAO, NAME, ISO_COUNTRY, COORDINATES FROM AIRPORTS WHERE NAME LIKE '%"
          + nazivParam + "%' LIMIT " + broj + " OFFSET " + odBroja;
    } else {
      query = "SELECT ICAO, NAME, ISO_COUNTRY, COORDINATES FROM AIRPORTS WHERE NAME LIKE '%"
          + nazivParam + "%' AND ISO_COUNTRY = '" + drzavaParam + "' LIMIT " + broj + " OFFSET "
          + odBroja;
    }

    PreparedStatement pstmt = null;
    try (var con = ds.getConnection()) {
      pstmt = con.prepareStatement(query);

      ResultSet rs = pstmt.executeQuery();
      while (rs.next()) {
        String icao = rs.getString("ICAO");
        String naziv = rs.getString("NAME");
        String drzava = rs.getString("ISO_COUNTRY");
        String[] koordinate = (rs.getString("COORDINATES").split(","));
        Lokacija lokacija = new Lokacija(koordinate[0].trim(), koordinate[1].trim());
        Aerodrom a = new Aerodrom(icao, naziv, drzava, lokacija);
        aerodromi.add(a);
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
    var jsonAerodromi = gson.toJson(aerodromi);

    var odgovor = Response.ok().entity(jsonAerodromi).build();

    return odgovor;
  }

  @Path("{icao}")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response dajAerodrom(@PathParam("icao") String icao) {
    Aerodrom aerodrom = null;

    String query = "SELECT NAME, ISO_COUNTRY, COORDINATES FROM AIRPORTS WHERE ICAO = ?";

    PreparedStatement pstmt = null;
    try (var con = ds.getConnection()) {
      pstmt = con.prepareStatement(query);
      pstmt.setString(1, icao);

      ResultSet rs = pstmt.executeQuery();
      while (rs.next()) {
        String naziv = rs.getString("NAME");
        String drzava = rs.getString("ISO_COUNTRY");
        String[] koordinate = (rs.getString("COORDINATES").split(","));
        Lokacija lokacija = new Lokacija(koordinate[0].trim(), koordinate[1].trim());
        aerodrom = new Aerodrom(icao, naziv, drzava, lokacija);
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

    if (aerodrom == null) {
      return Response.status(404).build();
    }

    var gson = new Gson();
    var jsonAerodrom = gson.toJson(aerodrom);

    var odgovor = Response.ok().entity(jsonAerodrom).build();

    return odgovor;
  }

  @Path("{icaoOd}/{icaoDo}")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response dajUdaljenostiAerodroma(@PathParam("icaoOd") String icaoOd,
      @PathParam("icaoDo") String icaoDo) {
    var udaljenosti = new ArrayList<Udaljenost>();

    String query =
        "SELECT COUNTRY, DIST_CTRY FROM AIRPORTS_DISTANCE_MATRIX WHERE ICAO_FROM = ? AND ICAO_TO = ?";

    PreparedStatement pstmt = null;
    try (var con = ds.getConnection()) {
      pstmt = con.prepareStatement(query);
      pstmt.setString(1, icaoOd);
      pstmt.setString(2, icaoDo);

      ResultSet rs = pstmt.executeQuery();
      while (rs.next()) {
        String drzava = rs.getString("COUNTRY");
        float udaljenost = rs.getFloat("DIST_CTRY");
        var u = new Udaljenost(drzava, udaljenost);
        udaljenosti.add(u);
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
    var jsonUdaljenosti = gson.toJson(udaljenosti);

    var odgovor = Response.ok().entity(jsonUdaljenosti).build();

    return odgovor;
  }

  @Path("{icao}/udaljenosti")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response dajUdaljenostiAerodromaPremaOstalima(@PathParam("icao") String icao,
      @QueryParam("odBroja") int odBroja, @QueryParam("broj") int broj) {
    var udaljenosti = new ArrayList<UdaljenostAerodrom>();

    if (odBroja <= 0 || broj <= 0) {
      odBroja = 1;
      broj = 20;
    }

    String query =
        "SELECT ICAO_TO, DIST_CTRY FROM AIRPORTS_DISTANCE_MATRIX WHERE ICAO_FROM = ? LIMIT " + broj
            + " OFFSET " + odBroja;

    PreparedStatement pstmt = null;
    try (var con = ds.getConnection()) {
      pstmt = con.prepareStatement(query);
      pstmt.setString(1, icao);

      ResultSet rs = pstmt.executeQuery();
      while (rs.next()) {
        String icaoTo = rs.getString("ICAO_TO");
        float udaljenost = rs.getFloat("DIST_CTRY");
        var u = new UdaljenostAerodrom(icaoTo, udaljenost);
        udaljenosti.add(u);
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
    var jsonUdaljenosti = gson.toJson(udaljenosti);

    var odgovor = Response.ok().entity(jsonUdaljenosti).build();

    return odgovor;
  }

  @Path("{icaoOd}/izracunaj/{icaoDo}")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response izracunajUdaljenosti(@PathParam("icaoOd") String icaoOd,
      @PathParam("icaoDo") String icaoDo) {
    var koordinate = new ArrayList<String>();

    String query = "SELECT ICAO, COORDINATES FROM AIRPORTS WHERE ICAO = ? OR ICAO = ?";

    PreparedStatement pstmt = null;
    try (var con = ds.getConnection()) {
      pstmt = con.prepareStatement(query);
      pstmt.setString(1, icaoOd);
      pstmt.setString(2, icaoDo);

      ResultSet rs = pstmt.executeQuery();
      while (rs.next()) {
        String koord = rs.getString("COORDINATES").replaceAll("\\,", "");
        koordinate.add(koord);
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

    float udaljenost = izracunUdaljenosti(koordinate);

    var udaljenostAerodrom = new UdaljenostAerodrom(icaoDo, udaljenost);

    var gson = new Gson();
    var jsonUdaljenosti = gson.toJson(udaljenostAerodrom);

    var odgovor = Response.ok().entity(jsonUdaljenosti).build();

    return odgovor;
  }

  @Path("{icaoOd}/udaljenost1/{icaoDo}")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response dajUdaljenostiDrzavaManje(@PathParam("icaoOd") String icaoOd,
      @PathParam("icaoDo") String icaoDo) {
    var koordinate = new ArrayList<String>();
    var udaljenosti = new ArrayList<UdaljenostAerodrom>();

    String query = "SELECT COORDINATES FROM AIRPORTS WHERE ICAO = ? OR ICAO = ?";

    PreparedStatement pstmt = null;
    try (var con = ds.getConnection()) {
      pstmt = con.prepareStatement(query);
      pstmt.setString(1, icaoOd);
      pstmt.setString(2, icaoDo);

      ResultSet rs = pstmt.executeQuery();
      while (rs.next()) {
        String koord = rs.getString("COORDINATES").replaceAll("\\,", "");
        koordinate.add(koord);
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

    float udaljenost = izracunUdaljenosti(koordinate);

    udaljenosti = dajAerodromeZaManjuUdaljenost(koordinate.get(0), icaoDo, udaljenost);

    var gson = new Gson();
    var jsonUdaljenosti = gson.toJson(udaljenosti);

    var odgovor = Response.ok().entity(jsonUdaljenosti).build();

    return odgovor;
  }

  private ArrayList<UdaljenostAerodrom> dajAerodromeZaManjuUdaljenost(String koordinateOd,
      String icaoDo, float udaljenostIzmedu) {
    var koordinate = new ArrayList<String>();
    var udaljenosti = new ArrayList<UdaljenostAerodrom>();
    String query =
        "SELECT ICAO, COORDINATES FROM AIRPORTS WHERE ISO_COUNTRY = (SELECT ISO_COUNTRY FROM PUBLIC.AIRPORTS WHERE ICAO = ?)";

    PreparedStatement pstmt = null;
    try (var con = ds.getConnection()) {
      pstmt = con.prepareStatement(query);
      pstmt.setString(1, icaoDo);

      ResultSet rs = pstmt.executeQuery();
      while (rs.next()) {
        String icao = rs.getString("ICAO");
        String koord = rs.getString("COORDINATES").replaceAll("\\,", "");
        koordinate.add(koordinateOd);
        koordinate.add(koord);
        var udaljenost = izracunUdaljenosti(koordinate);
        if (udaljenost < udaljenostIzmedu) {
          udaljenosti.add(new UdaljenostAerodrom(icao, udaljenost));
        }
        koordinate.clear();
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

    return udaljenosti;
  }

  @Path("{icaoOd}/udaljenost2")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response dajUdaljenostiDrzavaKm(@PathParam("icaoOd") String icaoOd,
      @QueryParam("drzava") String drzava, @QueryParam("km") float km) {
    var koordinate = new ArrayList<String>();
    var udaljenosti = new ArrayList<UdaljenostAerodrom>();

    String query = "SELECT COORDINATES FROM AIRPORTS WHERE ICAO = ?";

    PreparedStatement pstmt = null;
    try (var con = ds.getConnection()) {
      pstmt = con.prepareStatement(query);
      pstmt.setString(1, icaoOd);

      ResultSet rs = pstmt.executeQuery();
      while (rs.next()) {
        String koord = rs.getString("COORDINATES").replaceAll("\\,", "");
        koordinate.add(koord);
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

    udaljenosti = dajAerodromeZaDrzavuKm(koordinate.get(0), drzava, km);

    var gson = new Gson();
    var jsonUdaljenosti = gson.toJson(udaljenosti);

    var odgovor = Response.ok().entity(jsonUdaljenosti).build();

    return odgovor;
  }

  private ArrayList<UdaljenostAerodrom> dajAerodromeZaDrzavuKm(String koordinateOd, String drzava,
      float km) {
    var koordinate = new ArrayList<String>();
    var udaljenosti = new ArrayList<UdaljenostAerodrom>();
    String query = "SELECT ICAO, COORDINATES FROM AIRPORTS WHERE ISO_COUNTRY = ?";

    PreparedStatement pstmt = null;
    try (var con = ds.getConnection()) {
      pstmt = con.prepareStatement(query);
      pstmt.setString(1, drzava);

      ResultSet rs = pstmt.executeQuery();
      while (rs.next()) {
        String icao = rs.getString("ICAO");
        String koord = rs.getString("COORDINATES").replaceAll("\\,", "");
        koordinate.add(koordinateOd);
        koordinate.add(koord);
        var udaljenost = izracunUdaljenosti(koordinate);
        if (udaljenost < km) {
          udaljenosti.add(new UdaljenostAerodrom(icao, udaljenost));
        }
        koordinate.clear();
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

    return udaljenosti;
  }

  private float izracunUdaljenosti(ArrayList<String> koordinate) {

    var poruka = posaljiNaPosluzitelj("UDALJENOST " + koordinate.get(0) + " " + koordinate.get(1));

    String[] podijeljenaPoruka = poruka.split("\\s+");

    return Float.parseFloat(podijeljenaPoruka[1]);
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
