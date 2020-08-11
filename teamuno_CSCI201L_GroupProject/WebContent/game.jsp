<!DOCTYPE html>
<html lang="en">
<head>
<title>UNO</title>
<meta charset="ISO-8859-1">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>

<link
	href="https://fonts.googleapis.com/css2?family=Varta:wght@300;400;600&display=swap"
	rel="stylesheet">
<link href="CSS/game.css" rel="stylesheet">
<script src="websocket.js" async></script>
<!-- <script src="game.js" async> -->
</head>
<style>
/* The Modal (background) */
.modal {
	display: none; /* Hidden by default */
	position: fixed; /* Stay in place */
	z-index: 1; /* Sit on top */
	left: 0;
	top: 0;
	width: 100%; /* Full width */
	height: 100%; /* Full height */
	overflow: auto; /* Enable scroll if needed */
	background-color: rgb(0, 0, 0); /* Fallback color */
	background-color: rgba(0, 0, 0, 0.4); /* Black w/ opacity */
}

/* Modal Content/Box */
.modal-content {
	font-family: "Varta";
	font-size: 24px;
	font-weight: 300;
	background-color: #fefefe;
	margin: 15% auto; /* 15% from the top and centered */
	padding: 20px;
	border: 1px solid #888;
	width: 80%; /* Could be more or less, depending on screen size */
	align-content: center;
	border-radius: 10px;
}

.modal-content p {
	text-align: center;
}

.modal-content-buttons {
	margin: auto;
	width: 495px;
}

/* The Close Button */
.close {
	color: #aaa;
	float: right;
	font-size: 28px;
	font-weight: bold;
}

.close:hover, .close:focus {
	color: black;
	text-decoration: none;
	cursor: pointer;
}
</style>
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
	<!-- Used for popups  -->
	<div id="myModal" class="modal notification">
		<!-- Modal content -->
		<div class="modal-content">
			<span onclick="closeNotification()" class="close">&times;</span>
			<span id="notificationText"></span>
		</div>
	</div>
	
	<!-- The Modal -->
	<div id="myModal" class="modal selectWildCard">
		<!-- Modal content -->
		<div class="modal-content">
			<span onclick="closePopUp('selectWildCard')" class="close">&times;</span>
			<p>Select your Wild Card color</p>
			<div class="modal-content-buttons">
				<button id="wildRedButton" onclick="playWild('Red')">RED</button>
				<button id="wildYellowButton" onclick="playWild('Yellow')">YELLOW</button>
				<button id="wildGreenButton" onclick="playWild('Green')">GREEN</button>
				<button id="wildBlueButton" onclick="playWild('Blue')">BLUE</button>
			</div>

		</div>
	</div>

	<!-- Wild Four Selector -->
	<div id="myModal" class="modal selectWildFourCard">
		<!-- Modal content -->
		<div class="modal-content selectWildFourCard">
			<span onclick="closePopUp('selectWildFourCard')"class="close">&times;</span>
			<p>Select your Wild Four Card color</p>
			<div class="modal-content-buttons">
				<button id="wildRedButton" onclick="playWildFour('Red')">RED</button>
				<button id="wildYellowButton" onclick="playWildFour('Yellow')">YELLOW</button>
				<button id="wildGreenButton" onclick="playWildFour('Green')">GREEN</button>
				<button id="wildBlueButton" onclick="playWildFour('Blue')">BLUE</button>
			</div>
		</div>
	</div>

	<!-- Contains all of the information about the game -->
	<div class="game-info-container">
		<h1 class="page-title">UNO</h1>
		<div class="game-info">
			Game Room ID: <span id="gameRoomID"><%=theRoomID%></span>
		</div>
		<div class="game-info">
			Turn: <span id="players_turn"></span>
		</div>
		<div class="game-info">
			Current Player: <span id="currentPlayer"></span>
		</div>
		<div class="game-info">
			Players: <span id="players"></span>
		</div>
		<div class="game-info-players">
			<div class="game-info">
				<span><b id="playerID"><%=user.getUsername()%></b><b>
						(YOU)</b> <t style = "float:right">Cards in hand: <t id = "player-id-<%=user.getUsername()%>">5</t></t></span>
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
	<div class="game-container"></div>
	<div class="game-container game-buttons">
		<button id="joinButton" onclick="joinGame()">Join</button>
		<button id="readyButton" onclick="ready()">Ready</button>
		<button id="drawButton" onclick="draw()" style="display:none;">Draw</button>
		<button id="unoButton" onclick="uno()" style="display:none;">Uno!</button>
	</div>
</body>
</html>