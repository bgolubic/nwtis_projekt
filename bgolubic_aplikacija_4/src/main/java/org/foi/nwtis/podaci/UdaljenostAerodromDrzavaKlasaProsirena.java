package org.foi.nwtis.podaci;

import java.io.Serializable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "AIRPORTS_DISTANCE_MATRIX")
public class UdaljenostAerodromDrzavaKlasaProsirena extends UdaljenostAerodromDrzavaKlasa
    implements Serializable {
  private static final long serialVersionUID = 1L;
  @Id
  @Column(name = "ICAO_FROM")
  private String icaoOd;
  @Id
  @Column(name = "ICAO_TO")
  private String icao;
  @Id
  @Column(name = "COUNTRY")
  private String drzava;
  @Column(name = "DIST_CTRY")
  private float km;

  public UdaljenostAerodromDrzavaKlasaProsirena() {}

  public UdaljenostAerodromDrzavaKlasaProsirena(String icaoOd, String icaoDo, String drzava,
      float km) {
    this.icaoOd = icaoOd;
    this.icao = icaoDo;
    this.drzava = drzava;
    this.km = km;
  }

  public String getIcaoOd() {
    return icaoOd;
  }

  public void setIcaoOd(String icaoOd) {
    this.icaoOd = icaoOd;
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
