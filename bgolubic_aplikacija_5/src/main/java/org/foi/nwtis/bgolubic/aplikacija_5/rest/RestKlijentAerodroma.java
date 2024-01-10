package org.foi.nwtis.bgolubic.aplikacija_5.rest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.foi.nwtis.Konfiguracija;
import org.foi.nwtis.podaci.Aerodrom;
import org.foi.nwtis.podaci.Udaljenost;
import org.foi.nwtis.podaci.UdaljenostAerodrom;
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

  public List<Aerodrom> getAerodromi(String naziv, String drzava, int odBroja, int broj) {
    RestKKlijent rc = new RestKKlijent(konfig);
    Aerodrom[] json_Aerodromi = rc.getAerodromi(naziv, drzava, odBroja, broj);
    List<Aerodrom> aerodromi;
    if (json_Aerodromi == null) {
      aerodromi = new ArrayList<>();
    } else {
      aerodromi = Arrays.asList(json_Aerodromi);
    }
    rc.close();
    return aerodromi;
  }

  public List<Aerodrom> getAerodromi(String naziv, String drzava) {
    return this.getAerodromi(naziv, drzava, 1,
        Integer.parseInt(konfig.dajPostavku("stranica.brojRedova")));
  }

  public Aerodrom getAerodrom(String icao) {
    RestKKlijent rc = new RestKKlijent(konfig);
    Aerodrom a = rc.getAerodrom(icao);
    rc.close();
    return a;
  }

  public List<Udaljenost> getAerodromiUdaljenosti(String icaoFrom, String icaoTo) {
    RestKKlijent rc = new RestKKlijent(konfig);
    Udaljenost[] json_AerodromiUdaljenosti = rc.getAerodromiUdaljenosti(icaoFrom, icaoTo);
    List<Udaljenost> aerodromiUdaljenosti;
    if (json_AerodromiUdaljenosti == null) {
      aerodromiUdaljenosti = new ArrayList<>();
    } else {
      aerodromiUdaljenosti = Arrays.asList(json_AerodromiUdaljenosti);
    }
    rc.close();
    return aerodromiUdaljenosti;
  }

  public UdaljenostAerodrom getAerodromiUdaljenost(String icaoFrom, String icaoTo) {
    RestKKlijent rc = new RestKKlijent(konfig);
    UdaljenostAerodrom udaljenost = rc.getAerodromiUdaljenost(icaoFrom, icaoTo);
    rc.close();
    return udaljenost;
  }

  public List<UdaljenostAerodrom> getAerodromiUdaljenostDrzava(String icaoFrom, String icaoTo) {
    RestKKlijent rc = new RestKKlijent(konfig);
    UdaljenostAerodrom[] json_AerodromiUdaljenosti =
        rc.getAerodromiUdaljenostDrzava(icaoFrom, icaoTo);
    List<UdaljenostAerodrom> aerodromiUdaljenosti;
    if (json_AerodromiUdaljenosti == null) {
      aerodromiUdaljenosti = new ArrayList<>();
    } else {
      aerodromiUdaljenosti = Arrays.asList(json_AerodromiUdaljenosti);
    }
    rc.close();
    return aerodromiUdaljenosti;
  }

  public List<UdaljenostAerodrom> getAerodromiUdaljenostDrzavaKm(String icaoFrom, String drzava,
      String km) {
    RestKKlijent rc = new RestKKlijent(konfig);
    UdaljenostAerodrom[] json_AerodromiUdaljenosti =
        rc.getAerodromiUdaljenostDrzavaKm(icaoFrom, drzava, km);
    List<UdaljenostAerodrom> aerodromiUdaljenosti;
    if (json_AerodromiUdaljenosti == null) {
      aerodromiUdaljenosti = new ArrayList<>();
    } else {
      aerodromiUdaljenosti = Arrays.asList(json_AerodromiUdaljenosti);
    }
    rc.close();
    return aerodromiUdaljenosti;
  }

  static class RestKKlijent {

    private final WebTarget webTarget;
    private final Client client;
    private static volatile String BASE_URI;

    public RestKKlijent(Konfiguracija konf) {
      client = ClientBuilder.newClient();
      BASE_URI = konf.dajPostavku("adresa.ap2");
      webTarget = client.target(BASE_URI).path("aerodromi");
    }

    public Aerodrom[] getAerodromi(String naziv, String drzava, int odBroja, int broj)
        throws ClientErrorException {
      WebTarget resource = webTarget;

      resource = resource.queryParam("traziNaziv", naziv);
      resource = resource.queryParam("traziDrzavu", drzava);
      resource = resource.queryParam("odBroja", odBroja);
      resource = resource.queryParam("broj", broj);
      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);
      if (request.get(String.class).isEmpty()) {
        return null;
      }
      Gson gson = new Gson();
      Aerodrom[] aerodromi = gson.fromJson(request.get(String.class), Aerodrom[].class);

      return aerodromi;
    }

    public Aerodrom getAerodrom(String icao) throws ClientErrorException {
      WebTarget resource = webTarget;
      resource = resource.path(java.text.MessageFormat.format("{0}", new Object[] {icao}));
      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);
      if (request.get(String.class).isEmpty()) {
        return null;
      }
      Gson gson = new Gson();
      Aerodrom aerodrom = gson.fromJson(request.get(String.class), Aerodrom.class);
      return aerodrom;
    }

    public Udaljenost[] getAerodromiUdaljenosti(String icaoFrom, String icaoTo)
        throws ClientErrorException {
      WebTarget resource = webTarget;
      resource = resource.path(java.text.MessageFormat.format("{0}/{1}", icaoFrom, icaoTo));
      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);
      if (request.get(String.class).isEmpty()) {
        return null;
      }
      Gson gson = new Gson();
      Udaljenost[] udaljenost = gson.fromJson(request.get(String.class), Udaljenost[].class);
      return udaljenost;
    }

    public UdaljenostAerodrom getAerodromiUdaljenost(String icaoFrom, String icaoTo)
        throws ClientErrorException {
      WebTarget resource = webTarget;
      resource =
          resource.path(java.text.MessageFormat.format("{0}/izracunaj/{1}", icaoFrom, icaoTo));
      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);
      if (request.get(String.class).isEmpty()) {
        return null;
      }
      Gson gson = new Gson();
      UdaljenostAerodrom udaljenost =
          gson.fromJson(request.get(String.class), UdaljenostAerodrom.class);
      return udaljenost;
    }

    public UdaljenostAerodrom[] getAerodromiUdaljenostDrzava(String icaoFrom, String icaoTo)
        throws ClientErrorException {
      WebTarget resource = webTarget;
      resource =
          resource.path(java.text.MessageFormat.format("{0}/udaljenost1/{1}", icaoFrom, icaoTo));
      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);
      if (request.get(String.class).isEmpty()) {
        return null;
      }
      Gson gson = new Gson();
      UdaljenostAerodrom[] udaljenost =
          gson.fromJson(request.get(String.class), UdaljenostAerodrom[].class);
      return udaljenost;
    }

    public UdaljenostAerodrom[] getAerodromiUdaljenostDrzavaKm(String icaoFrom, String drzava,
        String km) throws ClientErrorException {
      WebTarget resource = webTarget;
      resource = resource.path(java.text.MessageFormat.format("{0}/udaljenost2", icaoFrom));
      resource = resource.queryParam("drzava", drzava);
      resource = resource.queryParam("km", km);
      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);
      if (request.get(String.class).isEmpty()) {
        return null;
      }
      Gson gson = new Gson();
      UdaljenostAerodrom[] udaljenost =
          gson.fromJson(request.get(String.class), UdaljenostAerodrom[].class);
      return udaljenost;
    }

    public void close() {
      client.close();
    }
  }

}
