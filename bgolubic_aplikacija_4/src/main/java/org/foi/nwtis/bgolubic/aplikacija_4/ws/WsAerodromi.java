package org.foi.nwtis.bgolubic.aplikacija_4.ws;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.bgolubic.aplikacija_4.iznimke.PogresnaAutentikacija;
import org.foi.nwtis.bgolubic.aplikacija_4.pomocnici.AutentikacijaKorisnika;
import org.foi.nwtis.podaci.AerodromLet;
import jakarta.annotation.Resource;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;
import jakarta.xml.bind.annotation.XmlElement;

@WebService(serviceName = "aerodromi")
public class WsAerodromi {
  @Resource(lookup = "java:app/jdbc/nwtis_bp")
  javax.sql.DataSource ds;

  @WebMethod
  public List<AerodromLet> dajAerodromeZaLetove(
      @WebParam(name = "korisnik") @XmlElement(required = true) String korisnik,
      @WebParam(name = "lozinka") @XmlElement(required = true) String lozinka)
      throws PogresnaAutentikacija {
    if (AutentikacijaKorisnika.provjeraKorisnika(ds, korisnik, lozinka)) {
      List<AerodromLet> aerodromi = new ArrayList<>();

      String query = "";
      query = "SELECT * FROM AERODROMI_LETOVI";

      PreparedStatement pstmt = null;
      try (var con = ds.getConnection()) {
        pstmt = con.prepareStatement(query);

        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
          String icao = rs.getString("ICAO");
          int status = rs.getInt("STATUS");
          AerodromLet a = new AerodromLet(icao, status);
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
      return aerodromi;
    }
    return null;
  }

  @WebMethod
  public boolean dodajAerodromZaLetove(
      @WebParam(name = "korisnik") @XmlElement(required = true) String korisnik,
      @WebParam(name = "lozinka") @XmlElement(required = true) String lozinka,
      @WebParam(name = "icao") @XmlElement(required = true) String icao)
      throws PogresnaAutentikacija {
    if (AutentikacijaKorisnika.provjeraKorisnika(ds, korisnik, lozinka)) {
      String query = "";
      query = String.format("INSERT INTO AERODROMI_LETOVI (ICAO, STATUS) VALUES ('%s', 1)", icao);

      PreparedStatement pstmt = null;
      try (var con = ds.getConnection()) {
        pstmt = con.prepareStatement(query);

        pstmt.execute();

        pstmt.close();
        con.close();
      } catch (SQLException e) {
        e.printStackTrace();
        Logger.getGlobal().log(Level.SEVERE, e.getMessage());
        return false;
      } finally {
        try {
          if (pstmt != null && !pstmt.isClosed())
            pstmt.close();
        } catch (SQLException e) {
          Logger.getGlobal().log(Level.SEVERE, e.getMessage());
          return false;
        }
      }

      // InfoEndpoint.posaljiPorukuSvima(dohvatiBrojAerodroma());

      return true;
    }
    return false;
  }

  private String dohvatiBrojAerodroma() {
    String brojAerodroma = "";

    String query = "SELECT COUNT(*) AS BROJ FROM AERODROMI_LETOVI";

    PreparedStatement pstmt = null;
    try (var con = ds.getConnection()) {
      pstmt = con.prepareStatement(query);

      ResultSet rs = pstmt.executeQuery();
      while (rs.next()) {
        brojAerodroma = rs.getString("BROJ");
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
    return brojAerodroma;
  }

  private void posaljiPorukuNaWebSocket(String brojAerodroma) {}

  @WebMethod
  public boolean pauzirajAerodromZaLetove(
      @WebParam(name = "korisnik") @XmlElement(required = true) String korisnik,
      @WebParam(name = "lozinka") @XmlElement(required = true) String lozinka,
      @WebParam(name = "icao") @XmlElement(required = true) String icao)
      throws PogresnaAutentikacija {
    if (AutentikacijaKorisnika.provjeraKorisnika(ds, korisnik, lozinka)) {
      String query = "";
      query = "UPDATE AERODROMI_LETOVI SET STATUS = 0 WHERE ICAO = ?";

      PreparedStatement pstmt = null;
      try (var con = ds.getConnection()) {
        pstmt = con.prepareStatement(query);
        pstmt.setString(1, icao);
        pstmt.executeUpdate();

        pstmt.close();
        con.close();
      } catch (SQLException e) {
        e.printStackTrace();
        Logger.getGlobal().log(Level.SEVERE, e.getMessage());
        return false;
      } finally {
        try {
          if (pstmt != null && !pstmt.isClosed())
            pstmt.close();
        } catch (SQLException e) {
          Logger.getGlobal().log(Level.SEVERE, e.getMessage());
          return false;
        }
      }
      return true;
    }
    return false;
  }

  @WebMethod
  public boolean aktivirajAerodromZaLetove(
      @WebParam(name = "korisnik") @XmlElement(required = true) String korisnik,
      @WebParam(name = "lozinka") @XmlElement(required = true) String lozinka,
      @WebParam(name = "icao") @XmlElement(required = true) String icao)
      throws PogresnaAutentikacija {
    if (AutentikacijaKorisnika.provjeraKorisnika(ds, korisnik, lozinka)) {
      String query = "";
      query = "UPDATE AERODROMI_LETOVI SET STATUS = 1 WHERE ICAO = ?";

      PreparedStatement pstmt = null;
      try (var con = ds.getConnection()) {
        pstmt = con.prepareStatement(query);
        pstmt.setString(1, icao);
        pstmt.executeUpdate();

        pstmt.close();
        con.close();
      } catch (SQLException e) {
        e.printStackTrace();
        Logger.getGlobal().log(Level.SEVERE, e.getMessage());
        return false;
      } finally {
        try {
          if (pstmt != null && !pstmt.isClosed())
            pstmt.close();
        } catch (SQLException e) {
          Logger.getGlobal().log(Level.SEVERE, e.getMessage());
          return false;
        }
      }
      return true;
    }
    return false;
  }
}
