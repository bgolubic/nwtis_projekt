package org.foi.nwtis.bgolubic.aplikacija_4.iznimke;

public class PogresnaAutentikacija extends Exception {

  private static final long serialVersionUID = 1L;

  public PogresnaAutentikacija(String tekst) {
    super(tekst);
  }
}
