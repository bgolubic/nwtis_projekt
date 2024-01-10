package org.foi.nwtis.podaci;

import java.io.Serializable;

/**
 * @author Dragutin Kermek
 * @author Matija Novak
 * @version 2.3.0
 */
public class Lokacija implements Serializable {

  private static final long serialVersionUID = 1L;

  private String latitude;

  private String longitude;

  public Lokacija() {}

  public Lokacija(String latitude, String longitude) {
    this.latitude = latitude;
    this.longitude = longitude;
  }


  public void postavi(String latitude, String longitude) {
    this.latitude = latitude;
    this.longitude = longitude;
  }


  public String getLatitude() {
    return latitude;
  }

  public void setLatitude(String latitude) {
    this.latitude = latitude;
  }

  public String getLongitude() {
    return longitude;
  }

  public void setLongitude(String longitude) {
    this.longitude = longitude;
  }

}
