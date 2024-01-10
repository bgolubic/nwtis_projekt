package org.foi.nwtis.bgolubic.aplikacija_5.rest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.foi.nwtis.Konfiguracija;
import org.foi.nwtis.rest.podaci.LetAviona;
import com.google.gson.Gson;
import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;

public class RestKlijentLetova {

  private static Konfiguracija konfig;

  public RestKlijentLetova(Konfiguracija konf) {
    konfig = konf;
  }

  public List<LetAviona> getPolasciOdAerodroma(String icao, String dan) {
    RestKKlijent rc = new RestKKlijent(konfig);
    LetAviona[] jsonPolasci = rc.getPolasciOdAerodroma(icao, dan);
    List<LetAviona> polasci;
    if (jsonPolasci == null) {
      polasci = new ArrayList<>();
    } else {
      polasci = Arrays.asList(jsonPolasci);
    }
    rc.close();
    return polasci;
  }

  public List<LetAviona> getPolasciOdDo(String icaoOd, String icaoDo, String dan) {
    RestKKlijent rc = new RestKKlijent(konfig);
    LetAviona[] jsonPolasci = rc.getPolasciOdDo(icaoOd, icaoDo, dan);
    List<LetAviona> polasci;
    if (jsonPolasci == null) {
      polasci = new ArrayList<>();
    } else {
      polasci = Arrays.asList(jsonPolasci);
    }
    rc.close();
    return polasci;
  }

  public List<LetAviona> getSpremljeni() {
    RestKKlijent rc = new RestKKlijent(konfig);
    LetAviona[] jsonPolasci = rc.getSpremljeni();
    List<LetAviona> polasci;
    if (jsonPolasci == null) {
      polasci = new ArrayList<>();
    } else {
      polasci = Arrays.asList(jsonPolasci);
    }
    rc.close();
    return polasci;
  }

  static class RestKKlijent {

    private final WebTarget webTarget;
    private final Client client;
    private static volatile String BASE_URI;

    public RestKKlijent(Konfiguracija konf) {
      client = ClientBuilder.newClient();
      BASE_URI = konf.dajPostavku("adresa.wa_1");
      webTarget = client.target(BASE_URI).path("letovi");
    }

    public LetAviona[] getPolasciOdAerodroma(String icao, String dan) throws ClientErrorException {
      WebTarget resource = webTarget;
      resource = resource.path(java.text.MessageFormat.format("{0}", icao));
      resource = resource.queryParam("dan", dan);
      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);
      if (request.get(String.class).isEmpty()) {
        return null;
      }
      Gson gson = new Gson();
      LetAviona[] polasci = gson.fromJson(request.get(String.class), LetAviona[].class);
      return polasci;
    }

    public LetAviona[] getPolasciOdDo(String icaoOd, String icaoDo, String dan)
        throws ClientErrorException {
      WebTarget resource = webTarget;
      resource = resource.path(java.text.MessageFormat.format("{0}/{1}", icaoOd, icaoDo));
      resource = resource.queryParam("dan", dan);
      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);
      if (request.get(String.class).isEmpty()) {
        return null;
      }
      Gson gson = new Gson();
      LetAviona[] polasci = gson.fromJson(request.get(String.class), LetAviona[].class);
      return polasci;
    }

    public LetAviona[] getSpremljeni() throws ClientErrorException {
      WebTarget resource = webTarget;
      resource = resource.path(java.text.MessageFormat.format("{0}", "spremljeni"));
      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);
      if (request.get(String.class).isEmpty()) {
        return null;
      }
      Gson gson = new Gson();
      LetAviona[] polasci = gson.fromJson(request.get(String.class), LetAviona[].class);
      return polasci;
    }

    public void close() {
      client.close();
    }
  }

}
