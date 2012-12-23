<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>DigNews</title>
<link href="css/style.css" rel="stylesheet" type="text/css" />
<link href="css/font.css" rel="stylesheet" type="text/css" />
<script type="text/javascript">
	function allnews() {
		document.form1.action = "Display?category=International";
		document.form1.submit();
	}
</script>

</head>
<body>
    <div class="container">
        <div class="content">
            <div class="container">
                <div class="container">
                    <div align="center">
                        <h1 style="font-family: 'Dancing Script', cursive; font-size: 100px;">DigNews</h1>
                    </div>
                </div>
                <div class="container">
                    <form id="form1" name="form1" method="post" action="search">
                        <p align="center">
                            <input type="text" name="SearchBox" id="SearchBox" size="80" style="height: 25px; font-size: 15px;" x-webkit-speech />
                        </p>
                        <p align="center">
                            <input type="button" name="AllNews" id="AllNews" value="All News" onclick="allnews();" style="height: 25px; font-size: 16px;" /> <input type="submit" name="Search"
                                id="Search" value="Dig" style="height: 25px; font-size: 16px;" />
                        </p>
                        ​
                    </form>
                </div>
            </div>

            <div class="container">
                <div align="right" class="footer">
                    <a href="about.jsp">About DigNews</a>
                </div>
            </div>
        </div>
    </div>
</body>
</html>