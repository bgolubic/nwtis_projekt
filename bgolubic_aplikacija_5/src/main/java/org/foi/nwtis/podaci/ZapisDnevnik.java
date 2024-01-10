package org.foi.nwtis.podaci;

import java.sql.Timestamp;

public record ZapisDnevnik(int id, String filter, String requestUri, Timestamp dateTime) {

}
