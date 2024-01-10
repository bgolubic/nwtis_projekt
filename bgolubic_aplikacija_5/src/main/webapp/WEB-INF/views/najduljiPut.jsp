<%@page import="org.foi.nwtis.Konfiguracija"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Najdulji put države</title>
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
	<h1>Najdulji put</h1>
	Icao: ${najduljiPut.getIcao()} 
	<br/>
	Država: ${najduljiPut.getDrzava()}
	<br/>
	KM: ${najduljiPut.getKm()}
	<br/>
</body>
</html>