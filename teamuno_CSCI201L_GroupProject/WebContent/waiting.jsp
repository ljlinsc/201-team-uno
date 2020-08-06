<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<link
	href="https://fonts.googleapis.com/css2?family=Varta:wght@300;400;600&display=swap"
	rel="stylesheet">
<link rel="stylesheet" type="text/css" href="CSS/main.css">
<title>201 UNO</title>
</head>
<body>
	<div class="lobby-wrapper">
		<div class="greeting">
			<h1>You've entered the game room, ${user.getNickname()}</h1>
			<h1>Currently waiting for other players . . .</h1>
			<!-- maybe I should list the name of players that have joined the game as well? -->
			<!-- and maybe we should display # of players needed to start the game? -->
		</div>
		
	</div>
</body>
</html>