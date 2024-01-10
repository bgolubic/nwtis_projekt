package org.foi.nwtis.podaci;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Dragutin Kermek
 * @author Matija Novak
 * @version 2.3.0
 */
@AllArgsConstructor()
public class Lokacija {

    @Getter
    @Setter
    private String latitude;
    @Getter
    @Setter    
    private String longitude;

    public Lokacija() {
    }

    public void postavi(String latitude, String longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
