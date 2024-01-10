package org.foi.nwtis.bgolubic.aplikacija_4.endpoints;

import java.time.Instant;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import jakarta.websocket.CloseReason;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;

@ServerEndpoint("/info")
public class InfoEndpoint {

  private static Set<Session> aktivneSesije = Collections.synchronizedSet(new HashSet<>());
  private static int brojAerodroma = 0;
  private static int brojKorisnika = 0;

  @OnOpen
  public void onOpen(Session sesija) {
    aktivneSesije.add(sesija);
  }

  @OnMessage
  public void onMessage(String poruka, Session sesija) {
    String obavijest = Instant.now() + ";" + brojKorisnika + ";" + brojAerodroma;

  }

  @OnClose
  public void onClose(Session sesija, CloseReason razlogZatvaranja) {
    aktivneSesije.remove(sesija);
  }

  @OnError
  public void onError(Session sesija, Throwable throwable) {
    System.out.println("WebSocket pogreska: ID Sesije: " + sesija.getId());
    throwable.printStackTrace();
  }

  private void posaljiPorukuSvima(String poruka) {
    for (Session s : aktivneSesije)
      s.getAsyncRemote().sendText(poruka);
  }

  public void povecajBrojKorisnika() {
    brojKorisnika++;
  }

  public void povecajBrojAerodroma() {
    brojAerodroma++;
  }
}
