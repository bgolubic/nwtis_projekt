package org.foi.nwtis;

import java.util.Properties;

/**
 * Klasa PostavkeBazaPodataka za rad s postakama baze podataka.
 */
public class PostavkeBazaPodataka extends KonfiguracijaApstraktna implements KonfiguracijaBP {

  /**
   * Instancira klasu
   *
   * @param nazivDatoteke naziv datoteke postavki
   */
  public PostavkeBazaPodataka(String nazivDatoteke) {
    super(nazivDatoteke);
  }

  /**
   * Učitaj konfiguraciju.
   *
   * @throws NeispravnaKonfiguracija ako je neispravna konfiguracija
   */
  public void ucitajKonfiguraciju() throws NeispravnaKonfiguracija {
    Konfiguracija konfig = KonfiguracijaApstraktna.preuzmiKonfiguraciju(this.nazivDatoteke);
    this.postavke = konfig.dajSvePostavke();
  }

  /**
   * Spremi konfiguraciju.
   *
   * @param datoteka naziv datoteka
   * @throws NeispravnaKonfiguracija ako je neispravna konfiguracija
   */
  public void spremiKonfiguraciju(String datoteka) throws NeispravnaKonfiguracija {
    Konfiguracija konfig = KonfiguracijaApstraktna.dajKonfiguraciju(datoteka);
    Properties props = this.dajSvePostavke();
    for (Object o : props.keySet()) {
      String k = (String) o;
      String v = this.dajPostavku(k);
      konfig.spremiPostavku(k, v);
    }
    konfig.spremiKonfiguraciju();
  }

  /**
   * Vraća naziv baze podataka za administratora baze podataka.
   *
   * @return naziv baze podataka
   */
  public String getAdminDatabase() {
    return this.dajPostavku("admin.database");
  }

  /**
   * Vraća korisničku lozinku administratora baze podataka.
   *
   * @return korisnička lozinka
   */
  public String getAdminPassword() {
    return this.dajPostavku("admin.password");
  }

  /**
   * Vraća korisničko ime administratora baze podataka.
   *
   * @return korisničko imee
   */
  public String getAdminUsername() {
    return this.dajPostavku("admin.username");
  }

  /**
   * Vraća naziv upravljačkog programa za važeću bazu podataka.
   *
   * @return naziv upravljačkog programa
   */
  public String getDriverDatabase() {
    return this.getDriverDatabase(this.getServerDatabase());
  }

  /**
   * Vraća naziv upravljačkog programa za baze podataka na temelju njenog url-a
   *
   * @param urlBazePodataka url baze podataka
   * @return naziv upravljačkog programa
   */
  public String getDriverDatabase(String urlBazePodataka) {
    String bp = urlBazePodataka;
    String protokol = "";
    if (bp.indexOf("//") != -1) {
      protokol = bp.substring(0, bp.indexOf("//") - 1).replace(":", ".");
    } else {
      protokol = bp;
    }
    return this.dajPostavku(protokol);
  }

  /**
   * Vraća nazive svih upravljačkih programa baza podataka.
   *
   * @return nazive svih upravljačkih programa
   */
  public Properties getDriversDatabase() {
    Properties p = new Properties();
    String protokol = "jdbc.";
    for (Object o : this.dajSvePostavke().keySet()) {
      String k = (String) o;
      if (k.startsWith(protokol)) {
        p.setProperty(k, this.dajPostavku(k));
      }
    }
    return p;
  }

  /**
   * Vraća url poslužitelja baza podataka
   *
   * @return url poslužitelja baza podataka
   */
  public String getServerDatabase() {
    return this.dajPostavku("server.database");
  }

  /**
   * Vraća naziv korinsičke baze podataka.
   *
   * @return naziv korinsičke baze podataka.
   */
  public String getUserDatabase() {
    return this.dajPostavku("user.database");
  }

  /**
   * Vraća korisničku lozinku
   *
   * @return korisnička lozinka
   */
  public String getUserPassword() {
    return this.dajPostavku("user.password");
  }

  /**
   * Vraća korisničko ime
   *
   * @return korisničko ime
   */
  public String getUserUsername() {
    return this.dajPostavku("user.username");
  }

}
