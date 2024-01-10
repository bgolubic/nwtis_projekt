package org.foi.nwtis.podaci;

import java.io.Serializable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "AIRPORTS_DISTANCE_MATRIX")
public class UdaljenostKlasaProsirena extends UdaljenostKlasa implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @Column(name = "ICAO_FROM")
  private String icaoOd;
  @Id
  @Column(name = "ICAO_TO")
  private String icaoDo;
  @Id
  @Column(name = "COUNTRY")
  private String drzava;
  @Id
  @Column(name = "DIST_CTRY")
  private float km;

  public UdaljenostKlasaProsirena() {}

  public UdaljenostKlasaProsirena(String icaoOd, String icaoDo, String drzava, float km) {
    this.icaoOd = icaoOd;
    this.icaoDo = icaoDo;
    this.drzava = drzava;
    this.km = km;
  }

  public String getIcaoOd() {
    return icaoOd;
  }

  public void setIcaoOd(String icaoOd) {
    this.icaoOd = icaoOd;
  }

  public String getIcaoDo() {
    return icaoDo;
  }

  public void setIcaoDo(String icaoDo) {
    this.icaoDo = icaoDo;
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
