package org.foi.nwtis.podaci;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Dragutin Kermek
 * @version 2.3.0
 */
@AllArgsConstructor()
public class Aerodrom {

    @Getter
    @Setter
    private String icao;
    @Getter
    @Setter
    private String naziv;
    @Getter
    @Setter
    private String drzava;
    @Getter
    @Setter
    private Lokacija lokacija;

    public Aerodrom() {
    }
}
