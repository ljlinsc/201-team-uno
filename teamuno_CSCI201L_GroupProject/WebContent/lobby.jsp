<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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
			<h1>Welcome, ${user.getNickname()}</h1>
		</div>
		<div class="lobby-content">
			<div class="new-game">
				<c:choose>
					<c:when test="${user.isRegistered()}">
						<form name="create-game-room" method="POST"
							action="game.jsp?createGame=true">
							<input class="yellow-button" style="width: 280px" type="submit"
								name="create-game" value="Create a new game" />
						</form>
					</c:when>
					<c:otherwise>
						<input class="yellow-button"
							style="background-color: #F0F0F0; width: 280px" type="submit"
							name="create-game" value="Create a new game" />
					</c:otherwise>
				</c:choose>

			</div>
			<div class="divider-vert"></div>
			<div class="enter-game">
				<p class="header">Enter an existing game room:</p>
				<form name="enter-game-room" method="POST" action="EnterGameServlet">
					<input class="gray-text-input" type="text"
						placeholder="hexadecimal code" name="game-id" /> <input
						class="submit" type="submit" name="game-code-submit" value="Enter" />
				</form>
			</div>
			<b>${message}</b>
		</div>
	</div>
</body>
</html>