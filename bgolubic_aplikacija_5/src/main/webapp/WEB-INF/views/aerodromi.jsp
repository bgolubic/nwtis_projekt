<%@page import="org.foi.nwtis.Konfiguracija"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Aerodromi</title>
</head>
<body>
	<%
	ServletContext sc = request.getServletContext();
	Konfiguracija konf = (Konfiguracija) sc.getAttribute("konfig");
	%>
	<h1>Zaglavlje</h1>
	Autor:
	<%=konf.dajPostavku("autor.ime")%>
	<%=konf.dajPostavku("autor.prezime")%><br /> Predmet:
	<%=konf.dajPostavku("autor.predmet")%><br /> Godina:
	<%=konf.dajPostavku("aplikacija.godina")%><br /> Verzija aplikacije:
	<%=konf.dajPostavku("aplikacija.verzija")%><br />
	<button
		onclick="location.href='${pageContext.servletContext.contextPath}'"
		type="button">Početna stranica</button>
	<br />
	<br />
	<h1>Svi aerodromi</h1>
	<table border=1>
		<tr>
			<th>ICAO</th>
			<th>Naziv</th>
			<th>Država</th>
			<th>Lokacija</th>
			<th>Podaci</th>
		</tr>
		<c:forEach var="aerodrom" items="${aerodromi}">
			<tr>
				<td><c:out value="${aerodrom.icao}" /></td>
				<td><c:out value="${aerodrom.naziv}" /></td>
				<td><c:out value="${aerodrom.drzava}" /></td>
				<td>Lat: <c:out value="${aerodrom.lokacija.latitude}" />, Lon:
					<c:out value="${aerodrom.lokacija.longitude}" /></td>
				<td><a
					href="${pageContext.servletContext.contextPath}/mvc/aerodromi/icao?icao=${aerodrom.icao}">Podaci</a></td>
			</tr>
		</c:forEach>

	</table>
</body>
</html>