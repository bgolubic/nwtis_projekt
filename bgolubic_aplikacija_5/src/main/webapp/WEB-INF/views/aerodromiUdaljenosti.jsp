<%@page import="org.foi.nwtis.Konfiguracija"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/fmt" prefix = "fmt"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Aerodrom udaljenosti</title>
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
	<h1>Udaljenosti</h1>
	<table border=1>
	<tr><th>Država</th><th>Km</th></tr>
	<c:set var="ukupnaUdaljenost" value="0" scope="page" />
	<c:forEach var="aerodromUdaljenost" items="${aerodromiUdaljenost}">
   		<tr><td><c:out value="${aerodromUdaljenost.getDrzava()}"/></td><td><c:out value="${aerodromUdaljenost.getKm()}"/></td></tr>
   		<c:set var="ukupnaUdaljenost" value="${ukupnaUdaljenost + aerodromUdaljenost.getKm()}" scope="page"/>
 	</c:forEach>
 	<tr><td>Ukupno: </td><td><fmt:formatNumber type = "number" minFractionDigits="2" maxFractionDigits="5" value = "${ukupnaUdaljenost}" /></td></tr>
</body>
</html>