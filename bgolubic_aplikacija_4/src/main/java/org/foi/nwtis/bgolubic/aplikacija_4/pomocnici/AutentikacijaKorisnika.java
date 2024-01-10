package org.foi.nwtis.bgolubic.aplikacija_4.pomocnici;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;
import org.foi.nwtis.bgolubic.aplikacija_4.iznimke.PogresnaAutentikacija;
import org.foi.nwtis.podaci.Korisnik;

public class AutentikacijaKorisnika {
  public static boolean provjeraKorisnika(DataSource ds, String korisnik, String lozinka)
      throws PogresnaAutentikacija {
    Korisnik dohvacenKorisnik = null;

    String query = "";
    query = "SELECT * FROM KORISNICI WHERE KORIME = ? AND LOZINKA = ?";

    PreparedStatement pstmt = null;
    try (var con = ds.getConnection()) {
      pstmt = con.prepareStatement(query);

      pstmt.setString(1, korisnik);
      pstmt.setString(2, lozinka);

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
    if (dohvacenKorisnik != null)
      return true;
    else
      throw new PogresnaAutentikacija("Neispravni korisnički podaci!");
  }
}
