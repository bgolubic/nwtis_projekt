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
	<h1>Svi zapisi</h1>
	<table border=1>
		<tr>
			<th>ID</th>
			<th>Filter</th>
			<th>RequestUri</th>
			<th>DateTime</th>
		</tr>
		<c:forEach var="zapis" items="${zapisi}">
			<tr>
				<td><c:out value="${zapis.id()}" /></td>
				<td><c:out value="${zapis.filter()}" /></td>
				<td><c:out value="${zapis.requestUri()}" /></td>
				<td><c:out value="${zapis.dateTime()}" /></td>
			</tr>
		</c:forEach>

	</table>
</body>
</html>