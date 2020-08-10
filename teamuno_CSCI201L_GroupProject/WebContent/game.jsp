<!DOCTYPE html>
<html lang="en">
<head>
<title>UNO</title>
<meta charset="ISO-8859-1">
<link href="https://fonts.googleapis.com/css2?family=Varta:wght@300;400;600&display=swap" rel="stylesheet">
<link href="CSS/game.css" rel="stylesheet">
<script src="websocket.js" async></script>
<!-- <script src="game.js" async> -->
</head>
<%
teamuno_CSCI201L_GroupProject.User user = (teamuno_CSCI201L_GroupProject.User) session.getAttribute("user");
String theRoomID = (String) request.getParameter("roomID");
String createGame = (String) request.getParameter("createGame");

// Create New Room
if (createGame != null && createGame.equals("true")) {
	java.util.Random rand = new java.util.Random();
	int randomInt = rand.nextInt();
	String Hexa = Integer.toHexString(randomInt);
	while(teamuno_CSCI201L_GroupProject.RoomSocket.roomsExists(Hexa))
	{
		randomInt = rand.nextInt();
		Hexa = Integer.toHexString(randomInt);
	}
	teamuno_CSCI201L_GroupProject.RoomSocket.addNewRoom(Hexa);
	theRoomID = Hexa;
}
%>
<body onload="connect()">
	<!-- Contains all of the information about the game -->
	<div class="game-info-container">
		<h1 class="page-title">UNO</h1>
		<div class="game-info">
			Game Room ID: <span id="gameRoomID"><%=theRoomID%></span>
		</div>
		<!-- Do we need this? <div class="game-info">
			Time: <span id="time">0</span>
		</div> -->
		<div class="game-info">
				Current Player: <span id="currentPlayer"></span>
		</div>
		<div class="game-info">
			Players: <span id="players">0</span>
		</div>
		<div class="game-info-players">
			<div class="game-info">
				<span><b id="playerID"><%=user.getUsername()%></b><b> (YOU)</b></span>
			</div>
		</div>
	</div>

	<!-- Contains the current top card and other player's card's backsides -->
	<div class="game-container">
		<div class="card">
			<div class="card-back card-face">
				<img class="uno" id="topCard" src="IMG/Blue_Zero.png">
			</div>
		</div>
	</div>
	
	<!-- User's cards -->
	<div class="game-container">
	</div>
	<div class="game-container game-buttons">
		<button id="joinButton" onclick="joinGame()">Join</button>
		<button id="readyButton" onclick="ready()">Ready</button>
		<button id="drawButton" onclick="draw()">Draw</button>
		<button id="unoButton" onclick="uno()">Uno!</button>
	</div>
</body>
</html>