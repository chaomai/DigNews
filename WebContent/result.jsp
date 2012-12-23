<%@page import="java.util.Set"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.HashMap"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:set var="result" value="${result}" />
<c:set var="intRowCount" value="${result.size()}" />
<%
	if (session.getAttribute("result") == null) {
		HashMap<String, String> result = (HashMap<String, String>) pageContext
				.getAttribute("result");
		session.setAttribute("result", result);
	}
	int intPageSize;
	int intPage;
	int lastMapPos;
	intPageSize = 10;
	String strPage = request.getParameter("page");
	String strlastMapPos = request.getParameter("lastMapPos");

	if (strlastMapPos == null) {
		lastMapPos = 0;
	} else {
		lastMapPos = Integer.parseInt(strlastMapPos);
	}

	if (strPage == null) {
		intPage = 1;
	} else {
		intPage = Integer.parseInt(strPage);
		if (intPage < 1) {
			intPage = 1;
		}
	}

	int intRowCount = (Integer) pageContext.getAttribute("intRowCount");
	int intPageCount = (intRowCount + intPageSize - 1) / intPageSize;
	if (intPage > intPageCount)
		intPage = intPageCount;
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>DigNews</title>
<link href="css/style.css" rel="stylesheet" type="text/css" />
<link href="css/font.css" rel="stylesheet" type="text/css" />
</head>

<body>
    <div class="apDiv1">
        <div class="content">
            <form id="form1" name="form1" method="post" action="search">
                <input type="text" name="SearchBox" id="SearchBox" size="60" style="height: 25px; font-size: 15px;" x-webkit-speech /> <input type="submit" name="Search" id="Search" value="Dig"
                    style="height: 25px; font-size: 16px;" />
            </form>
        </div>
    </div>
    <div class="container">
        <div class="header">
            <h1>
                <a style="font-family: 'Dancing Script', cursive; font-size: 60px; color: #000000;" href="/DigNews">DigNews</a>
            </h1>
            <hr />
        </div>
        <div class="content">
            <div class="container">
                <%
                	int i = 0;
                	HashMap<String, String> result = (HashMap<String, String>) session
                			.getAttribute("result");
                	Set<String> resultSet = result.keySet();
                	Iterator<String> resultIterator = resultSet.iterator();

                	if (intPageCount > 0) {
                		while (i < lastMapPos && resultIterator.hasNext()) {
                			resultIterator.next();
                			i = i + 1;
                		}

                		i = 0;
                		while (i < intPageSize && resultIterator.hasNext()) {
                			String url = resultIterator.next();
                			out.println("<p><a href=\"" + url + "\">" + result.get(url)
                					+ "</a>");
                			out.println("<br />");
                			out.println(url);
                			out.println("</p>");
                			i = i + 1;
                		}
                		lastMapPos = lastMapPos + i;
                	}

                	out.println("<br />");
                	out.println("<p>Page:" + intPage + " " + "Total:" + intPageCount
                			+ "</p>");

                	out.println("<p>");
                	if (intPage > 1) {
                		out.println("<a href=\"");
                		out.println("result.jsp?page=" + (intPage - 1));
                		out.println("&lastMapPos="
                				+ (intPage * intPageSize - 2 * intPageSize) + "\">");
                		out.println("Previous Page" + "</a>");
                	}

                	if (intPage < intPageCount) {
                		out.println("<a href=\"");
                		out.println("result.jsp?page=" + (intPage + 1));
                		out.println("&lastMapPos=" + lastMapPos + "\">");
                		out.println("Next Page" + "</a>");
                	}
                	out.println("</p>");
                %>
            </div>
        </div>

        <div class="container">
            <div align="right" class="footer">
                <a href="about.jsp">About DigNews</a>
            </div>
        </div>
    </div>
</body>
</html>
