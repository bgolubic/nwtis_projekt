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
import org.foi.nwtis.podaci.Korisnik;
import jakarta.annotation.Resource;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;
import jakarta.xml.bind.annotation.XmlElement;

@WebService(serviceName = "korisnici")
public class WsKorisnici {
  @Resource(lookup = "java:app/jdbc/nwtis_bp")
  javax.sql.DataSource ds;

  @WebMethod
  public List<Korisnik> dajKorisnike(
      @WebParam(name = "korisnik") @XmlElement(required = true) String korisnik,
      @WebParam(name = "lozinka") @XmlElement(required = true) String lozinka,
      @WebParam(name = "traziImeKorisnika") String traziImeKorisnika,
      @WebParam(name = "traziPrezimeKorisnika") String traziPrezimeKorisnika)
      throws PogresnaAutentikacija {
    if (AutentikacijaKorisnika.provjeraKorisnika(ds, korisnik, lozinka)) {
      List<Korisnik> korisnici = new ArrayList<>();

      if (traziImeKorisnika.equals("?"))
        traziImeKorisnika = "";
      if (traziPrezimeKorisnika.equals("?"))
        traziPrezimeKorisnika = "";

      String query = "";
      if (traziImeKorisnika.equals("") && traziPrezimeKorisnika.equals(""))
        query = "SELECT * FROM KORISNICI";
      else if (!traziImeKorisnika.equals("") && traziPrezimeKorisnika.equals(""))
        query = "SELECT * FROM KORISNICI WHERE IME LIKE '%" + traziImeKorisnika + "%'";
      else if (traziImeKorisnika.equals("") && !traziPrezimeKorisnika.equals(""))
        query = "SELECT * FROM KORISNICI WHERE PREZIME LIKE '%" + traziPrezimeKorisnika + "%'";
      else
        query = "SELECT * FROM KORISNICI WHERE IME LIKE '%" + traziImeKorisnika
            + "%' PREZIME LIKE '%" + traziPrezimeKorisnika + "%'";

      PreparedStatement pstmt = null;
      try (var con = ds.getConnection()) {
        pstmt = con.prepareStatement(query);

        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
          String korisnickoIme = rs.getString("KORIME");
          String lozinkaKorisnika = rs.getString("LOZINKA");
          String ime = rs.getString("IME");
          String prezime = rs.getString("PREZIME");
          String email = rs.getString("EMAIL");
          Korisnik ko = new Korisnik(korisnickoIme, ime, prezime, lozinkaKorisnika, email);
          korisnici.add(ko);
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
      return korisnici;
    }
    return null;
  }

  @WebMethod
  public Korisnik dajKorisnika(
      @WebParam(name = "korisnik") @XmlElement(required = true) String korisnik,
      @WebParam(name = "lozinka") @XmlElement(required = true) String lozinka,
      @WebParam(name = "traziKorisnika") @XmlElement(required = true) String traziKorisnika)
      throws PogresnaAutentikacija {
    if (AutentikacijaKorisnika.provjeraKorisnika(ds, korisnik, lozinka)) {
      Korisnik dohvacenKorisnik = null;

      String query = "";
      query = "SELECT * FROM KORISNICI WHERE KORIME = ?";

      PreparedStatement pstmt = null;
      try (var con = ds.getConnection()) {
        pstmt = con.prepareStatement(query);

        pstmt.setString(1, traziKorisnika);

        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
          String korisnickoIme = rs.getString("KORIME");
          String lozinkaKorisnika = rs.getString("LOZINKA");
          String ime = rs.getString("IME");
          String prezime = rs.getString("PREZIME");
          String email = rs.getString("EMAIL");
          dohvacenKorisnik = new Korisnik(korisnickoIme, ime, prezime, lozinkaKorisnika, email);
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
      return dohvacenKorisnik;
    }
    return null;
  }

  @WebMethod
  public boolean dodajKorisnika(
      @WebParam(name = "korisnik") @XmlElement(required = true) Korisnik korisnik) {

    String query = "";
    query = String.format(
        "INSERT INTO KORISNICI (KORIME, LOZINKA, IME, PREZIME, EMAIL) VALUES ('%s', '%s', '%s', '%s', '%s')",
        korisnik.getKorIme(), korisnik.getLozinka(), korisnik.getIme(), korisnik.getPrezime(),
        korisnik.getEmail());

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

    posaljiPorukuNaWebSocket(dohvatiBrojKorisnika());

    return true;
  }

  private String dohvatiBrojKorisnika() {
    String brojKorisnika = "";

    String query = "SELECT COUNT(*) AS BROJ FROM KORISNICI";

    PreparedStatement pstmt = null;
    try (var con = ds.getConnection()) {
      pstmt = con.prepareStatement(query);

      ResultSet rs = pstmt.executeQuery();
      while (rs.next()) {
        brojKorisnika = rs.getString("BROJ");
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
    return brojKorisnika;
  }

  private void posaljiPorukuNaWebSocket(String brojKorisnika) {
    // KorisniciEndpoint.posaljiPorukuSvima(brojKorisnika);
  }
}
