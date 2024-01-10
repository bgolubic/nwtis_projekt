package org.foi.nwtis.podaci;

public class UdaljenostAerodromDrzavaKlasa {
  private String icao;
  private String drzava;
  private float km;

  public UdaljenostAerodromDrzavaKlasa() {}

  public UdaljenostAerodromDrzavaKlasa(String icao, String drzava, float km) {
    this.icao = icao;
    this.drzava = drzava;
    this.km = km;
  }

  public String getIcao() {
    return icao;
  }

  public void setIcao(String icao) {
    this.icao = icao;
  }

  public String getDrzava() {
    return drzava;
  }

  public void setDrzava(String drzava) {
    this.drzava = drzava;
  }

  public float getKm() {
    return km;
  }

  public void setKm(float km) {
    this.km = km;
  }
}
