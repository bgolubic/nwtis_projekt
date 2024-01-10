package org.foi.nwtis.bgolubic.aplikacija_4.rest;

import org.foi.nwtis.Konfiguracija;
import org.foi.nwtis.podaci.Aerodrom;
import org.foi.nwtis.podaci.Lokacija;
import com.google.gson.Gson;
import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;

public class RestKlijentAerodroma {

  private static Konfiguracija konfig;

  public RestKlijentAerodroma(Konfiguracija konf) {
    konfig = konf;
  }

  public Lokacija getLokacija(String icao) {
    RestKKlijent rc = new RestKKlijent(konfig);
    Lokacija a = rc.getLokacija(icao);
    rc.close();
    return a;
  }

  static class RestKKlijent {

    private final WebTarget webTarget;
    private final Client client;
    private static volatile String BASE_URI;

    public RestKKlijent(Konfiguracija konf) {
      client = ClientBuilder.newClient();
      BASE_URI = konf.dajPostavku("adresaPosluziteljaAP2");
      webTarget = client.target(BASE_URI).path("aerodromi");
    }

    public Lokacija getLokacija(String icao) throws ClientErrorException {
      WebTarget resource = webTarget;
      resource = resource.path(java.text.MessageFormat.format("{0}", new Object[] {icao}));
      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);
      if (request.get(String.class).isEmpty()) {
        return null;
      }
      Gson gson = new Gson();
      Aerodrom aerodrom = gson.fromJson(request.get(String.class), Aerodrom.class);
      return aerodrom.getLokacija();
    }


    public void close() {
      client.close();
    }
  }
}
