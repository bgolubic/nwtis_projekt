package org.foi.nwtis.bgolubic.aplikacija_2.filteri;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.Charset;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.Konfiguracija;
import jakarta.annotation.Resource;
import jakarta.inject.Inject;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AplikacijskiFilter implements Filter {

  @Resource(lookup = "java:app/jdbc/nwtis_bp")
  javax.sql.DataSource ds;

  @Inject
  ServletContext context;

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {

    HttpServletRequest req = (HttpServletRequest) request;
    HttpServletResponse res = (HttpServletResponse) response;

    String query = String.format(
        "INSERT INTO DNEVNIK (FILTER, REQUEST_URI, DATETIME) VALUES('%s', '%s', '%s')", "AP2",
        req.getRequestURI(), Timestamp.from(Instant.now()).toString());

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

    String putanja = req.getRequestURI();

    if ((putanja.contains("aerodromi") || putanja.contains("dnevnik"))
        && dohvatiStatus().equals("OK 0")) {
      res.sendError(403);
      return;
    }

    chain.doFilter(request, response);
  }

  private String dohvatiStatus() {
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

      String zahtjev = "STATUS";
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
