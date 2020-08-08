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
	<!-- need to connect this page to the server to keep clients updated -->
	<div class="game-room-info">
		<div class="header" style="width:100px;">Code: ###</div>
		<div class="subheader" style="width:100px;">Players: </div>
		<ul>
			<li>###</li>
			<li>###</li>
			<li>###</li>
			<li>###</li>
			<li>###</li>
		</ul>
	</div>
	<div class="lobby-wrapper">
		<div class="greeting">
			<h1>You've entered the game room, ${user.getNickname()}</h1>
			<h1>Currently waiting for other players . . .</h1>
		</div>
		
	</div>
</body>
</html>