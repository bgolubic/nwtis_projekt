package org.foi.nwtis.bgolubic.aplikacija_5.rest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.foi.nwtis.Konfiguracija;
import org.foi.nwtis.podaci.ZapisDnevnik;
import com.google.gson.Gson;
import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

public class RestKlijentDnevnika {
  private static Konfiguracija konfig;

  public RestKlijentDnevnika(Konfiguracija konf) {
    konfig = konf;
  }

  public List<ZapisDnevnik> getZapisi(int odBroja, int broj) {
    RestKKlijent rc = new RestKKlijent(konfig);
    ZapisDnevnik[] json_Zapisi = rc.getZapisi(odBroja, broj);
    List<ZapisDnevnik> zapisi;
    if (json_Zapisi == null) {
      zapisi = new ArrayList<>();
    } else {
      zapisi = Arrays.asList(json_Zapisi);
    }
    rc.close();
    return zapisi;
  }

  public List<ZapisDnevnik> getZapisi() {
    return this.getZapisi(1, Integer.parseInt(konfig.dajPostavku("stranica.brojRedova")));
  }

  public void postZapis(ZapisDnevnik zapis) {
    RestKKlijent rc = new RestKKlijent(konfig);
    rc.postZapis(zapis);
  }

  static class RestKKlijent {

    private final WebTarget webTarget;
    private final Client client;
    private static volatile String BASE_URI;

    public RestKKlijent(Konfiguracija konf) {
      client = ClientBuilder.newClient();
      BASE_URI = konf.dajPostavku("adresa.ap2");
      webTarget = client.target(BASE_URI).path("dnevnik");
    }

    public ZapisDnevnik[] getZapisi(int odBroja, int broj) throws ClientErrorException {
      WebTarget resource = webTarget;

      resource = resource.queryParam("odBroja", odBroja);
      resource = resource.queryParam("broj", broj);
      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);
      if (request.get(String.class).isEmpty()) {
        return null;
      }
      Gson gson = new Gson();
      ZapisDnevnik[] zapisi = gson.fromJson(request.get(String.class), ZapisDnevnik[].class);

      return zapisi;
    }

    public void postZapis(ZapisDnevnik zapis) {
      WebTarget resource = webTarget;

      Gson gson = new Gson();
      String requestBody = gson.toJson(zapis);

      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);

      Response response = request.post(Entity.json(requestBody));
    }

    public void close() {
      client.close();
    }
  }
}
