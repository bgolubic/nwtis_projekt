<%@page import="org.foi.nwtis.Konfiguracija"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Odabir svih udaljenosti</title>
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
	<form method="get" action="${pageContext.servletContext.contextPath}/mvc/aerodromi/udaljenosti">
		<label for="icao">ICAO:</label>
		<input name="icao" type="text" /><br><br>
		<input type="submit" value="Prikaži udaljenosti od aerodroma" /><br>
		</form>
</body>
</html>