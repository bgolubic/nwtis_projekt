package org.foi.nwtis.bgolubic.aplikacija_4.web;

import java.io.IOException;
import org.foi.nwtis.bgolubic.aplikacija_4.zrna.JmsPosiljatelj;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "SaljeJmsPoruku", urlPatterns = {"/SaljeJmsPoruku"})
public class SaljeJmsPoruku extends HttpServlet {

  private static final long serialVersionUID = -1891542720668449089L;

  @EJB
  JmsPosiljatelj jmsPosiljatelj;

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    var poruka = req.getParameter("poruka");
    if (poruka != null && !poruka.isEmpty()) {
      if (jmsPosiljatelj.saljiPoruku(poruka)) {
        System.out.println("Poruka je poslana!");
        return;
      }
      System.out.println("Greška kod slanja");
    }
    System.out.println("Poruka nema tekst!");
  }

}
