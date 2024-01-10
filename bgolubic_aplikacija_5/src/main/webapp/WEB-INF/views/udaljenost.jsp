<%@page import="org.foi.nwtis.Konfiguracija"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/fmt" prefix = "fmt"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Udaljenosti</title>
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
	<h1>Udaljenost</h1>
	<table border=1>
	<tr><th>ICAO</th><th>Km</th></tr>
	<tr><td><c:out value="${udaljenost.icao()}"/></td><td><c:out value="${udaljenost.km()}"/></td></tr>
</body>
</html>