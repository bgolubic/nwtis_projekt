<%@page import="org.foi.nwtis.Konfiguracija"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Odabir udaljenosti od-do</title>
</head>
<body>
<% ServletContext sc = request.getServletContext();
    Konfiguracija konf = (Konfiguracija) sc.getAttribute("konfig"); %>
	<h1>Zaglavlje</h1>
	Autor: <%= konf.dajPostavku("autor.ime") %> <%= konf.dajPostavku("autor.prezime") %><br/>
	Predmet: <%= konf.dajPostavku("autor.predmet") %><br/>
	Godina: <%= konf.dajPostavku("aplikacija.godina") %><br/>
	Verzija aplikacije: <%= konf.dajPostavku("aplikacija.verzija") %><br/>
	<button onclick="location.href='${pageContext.servletContext.contextPath}'" type="button">Početna stranica</button><br/><br/>
	<form method="get" action="${pageContext.servletContext.contextPath}/mvc/aerodromi/udaljenostDrzavaKm">
		<label for="icaoOd">ICAO od:</label>
		<input name="icaoOd" type="text" /><br>
		<label for="drzava">Država:</label>
		<input name="drzava" type="text" /><br>
		<label for="km">Km:</label>
		<input name="km" type="text" /><br><br>
		<input type="submit" value="Prikaži udaljenosti" /><br>
		</form>
</body>
</html>