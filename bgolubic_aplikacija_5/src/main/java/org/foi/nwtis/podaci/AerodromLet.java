package org.foi.nwtis.podaci;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor()
public class AerodromLet {
  @Getter
  @Setter
  private String icao;
  @Getter
  @Setter
  private int status;

  public AerodromLet() {}
}
