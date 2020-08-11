/*
 * Format of JSON Messages:
 * {
 * 		action : String,
 * 		username : String,
 * 		roomID: String
 * }
 **********************************************************************************************
 * action denotes one of a variety of actions:
 * 		action : "createRoom"
 * 		action : "joinRoom"
 * 		action : "makeTurn"
 * 		action : "uno"
 * 		action : "ready"
 **********************************************************************************************
 * Based on the action determines the server response:
 * "createRoom"
 * {
 * 		action : "createRoom",
 * 		nickname : String,
 * 		username : String,
 * 		roomID: NULL
 * }
 * This will create a new room for the user. Additionally, will send the user the userRoom file.
 **********************************************************************************************
 * "joinRoom"
 * {
 * 		action : "joinRoom",
 * 		nickname : String,
 * 		username : String,
 * 		roomID: String
 * }
 * This will simply add the user to the room while also sending the user the userRoom file.
 **********************************************************************************************
 * "makeTurn"
 * {
 * 		action : "makeTurn",
 * 		username : String,
 * 		nickname : String,
 * 		roomID: String,
 * 		card: Integer,
 * 		color: String,
 * 		endTurn: boolean
 * }
 * This will make the user turn, and then broadcast to all other users the result of the action.
 **********************************************************************************************
 * "uno"
 * {
 * 		action : "uno"
 * 		username : Stirng,
 * 		nickname : String,
 * 		roomID : String,
 * }
 * 
 * "draw" 
 * {
 * 		action : draw",
 *		username: String,
 *		nickname: String,
 *		roomOD: String
 * }
 * "ready"
 * {
 * 		action: "ready",
 * 		username : String,
 * 		nicknake : String,
 * 		roomID : String
 * }
  * This will make the user turn, and then broadcast to all other users the result of the action.
 **********************************************************************************************
 * Uno card notation format:
 * Draw Card:
 * 		card: 0
 * 		color: null
 * Regular Cards:
 * 		card: 1-9 (depending on value of card)
 * 		color: color of card
 * Reverse:
 * 		card: 10
 * 		color: color of card
 * Skip:
 * 		card: 11
 * 		color: color of card
 * Draw Two:
 * 		card: 12
 * 		color: color of card
 * Wild:
 * 		card: 13
 * 		color: users color choice
 * Wild Draw Four:
 * 		card: 14
 * 		color: users color choice
 * 
 * 
 * 
 * 
 * 
 */
var connectionURL = "ws://localhost:8083/teamuno_CSCI201L_GroupProject/RoomSocket";
class GameWebSocket{
	constructor(URL){
		this.socket = new WebSocket(URL);
		this.socket.onopen = function(event) {
			console.log("Succesfully connected to" )
		}
		this.socket.onmessage = function(event) {
			console.log("New Message: <"+event.data+">");
			processMessage(event.data);
		}
		this.socket.onclose = function(event) {
			
		}
	}
	sendMessage(message){
		this.socket.send(message);
	}
	
}

var game;
/*
 * REPONSE FUNCTIONS (MESSAGES FROM SERVER)
 */
/*
	REPONSE FORMAT FROM SERVER
	{
		"type" : String, [valid values = "error", "content-change"]
		message : String,
		"contentChangeType" : "addCard" "changeTopCard" "drawCard" "newUser"
	}
	// When a user puts down a card successfully
	// Send to all users
	{
	
	}
	
	"newUser"
	{
		"type" : "content-change",
		"contentChangeType" : "newUser",
		"newUserID" : String (userid)
	}
*/
function processMessage(message) {
	var text = JSON.parse(message);
	if (text.type === "error") {
		alert(text.message);
	} else if (text.type === "content-change") {
		console.log("<"+text.contentChangeType+">");
		if (text.contentChangeType === "takeTurnCallback") {
			takeTurnCallback(text);
		} else if (text.contentChangeType === "changeTopCard") {
			changeTopCard(text.message);
		} else if (text.contentChangeType === "drawCard") {
			addCardToHand(text);
		} else if (text.contentChangeType === "initCards") {
			initCards(text);
		} else if (text.contentChangeType === "newUser") {
			addNewPlayer(text);
		} else if (text.contentChangeType === "currentPlayer") {
			changeCurrentPlayer(text);
		}
	} else if (text.type === "notification") {
			openNotification(text.message);
	}
	else {
		console.log("from processMessage(): Could not recongnize message type " + text.type);
		
	}
}

function draw() {
	console.log("draw()");
	var username = document.getElementById("playerID").innerHTML;
	var roomID = document.getElementById("gameRoomID").innerHTML;
	var data = {
			"action" : "draw",
			"username" : username,
			"nickname" : username,
			"roomID" : roomID
	};
	game.socket.send(JSON.stringify(data));
}


function placeCard(card) {
	console.log("test");
	// Determine card color and value
	// CHECK IF WE ARE INCLUDING DRAW TWO COLORED!!!!
	previousCardDraw = card;
	var title = card.title;

	if (title.includes("Wild_Four")) {
		console.log("Wild_Four");
		var wildFour = document.getElementsByClassName("selectWildFourCard")[0];
		wildFour.style.display  = "block";
		console.log("Wild_Four: " + wildFour.style.display);

	} else if (title.includes("Wild")) {
		var wild = document.getElementsByClassName("selectWildCard")[0];
		wild.style.display = "block";
		console.log("Wild: " + wild.style.display);
	} else {		
		var playerID = document.getElementById("playerID").innerHTML;
		var roomID = document.getElementById("gameRoomID").innerHTML;
		var color;
		var value;
		var title = card.title;
		if (title.includes("Blue")) {
			color = "Blue";
		} else if (title.includes("Red")) {
			color = "Red";
		} else if (title.includes("Yellow")) {
			color = "Yellow";
		} else if (title.includes("Green")) {
			color = "Green";
		} else if (title.includes("Wild")) {
			color = "Wild";
		}
		
		if (title.includes("DrawTwo")) {
			value = "DrawTwo";
		} else if (title.includes("Zero")) {
			value = "Zero";
		} else if (title.includes("One")) {
			value = "One";
		} else if (title.includes("Two")) {
			value = "Two";
		} else if (title.includes("Three")) {
			value = "Three";
		} else if (title.includes("Four")) {
			value = "Four";
		} else if (title.includes("Five")) {
			value = "Five";
		} else if (title.includes("Six")) {
			value = "Six";
		} else if (title.includes("Seven")) {
			value = "Seven";
		} else if (title.includes("Eight")) {
			value = "Eight";
		} else if (title.includes("Nine")) {
			value = "Nine";
		} else if (title.includes("Ten")) {
			value = "Ten";
		} else if (title.includes("Reverse")) {
			value = "Reverse";
		} else if (title.includes("Skip")) {
			value = "Skip";
		} else if (title.includes("Wild_Four")) {
			value = "Wild_Four";
		} else if (title.includes("Wild")) {
			value = "Wild";
		}
		
		// Send data through socket
		var playCardInstructions = {
				"action" : "makeTurn",
				"username" : playerID,
				"nickname" : playerID,
				"roomID" : roomID,
				"cardColor" : color,
				"cardValue" : value
		}
		console.log("cardColor="+color +" value="+value);
		game.socket.send(JSON.stringify(playCardInstructions));
	}
	
}

/**CallBack function from Server initiating cards for all players
 * {
 * 	"action" : "initCards",
 * 	"message" : [cardPNG]
 * }
 * @param JSONData
 * @returns
 */
function initCards(JSONData) {
	console.log("initCards()");
	document.getElementById("readyButton").style.display = "none";
	var cardHolder = document.getElementsByClassName("game-container");
	
	var cardData = 
	"<div onclick=\"placeCard(this)\" class=\"card " + JSONData.message +"\" title=\"" + JSONData.message +"\>\n" + 
	"				<div class=\"card-back card-face\">\n" + 
	"					<img  class=\"uno\" src=\"IMG/" + JSONData.message + ".png\">\n" + 
	"				</div>\n" + 
	"			</div>";
	
	cardHolder[1].innerHTML += cardData; 
}

// addCard callback function
var previousCardDraw;
function takeTurnCallback(HTMLData) {
	var cardHolder = document.getElementsByClassName("game-container");
	
	var topCard = HTMLData.topCard;
	var nextPlayer = HTMLData.nextPlayer;
	var requestSentBy = HTMLData.requestSentBy;
	var gameDirection = HTMLData.gameDirection;
	var cardToRemove = HTMLData.cardToRemove;
	
	var playerID = document.getElementById("playerID").innerHTML;
	
	// Remove card from the player that put down that card on Screen
	if (playerID === requestSentBy) {
		previousCardDraw.remove();
//		var cardsWithClassName = document.getElementsByClassName(cardToRemove);
//		cardsWithClassName[0].remove(cardsWithClassName[0].selectedIndex);
	}
	
	// Update next player on Screen
	document.getElementById("currentPlayer").innerHTML = nextPlayer;
	
	// Change Top Card
	changeTopCard(topCard);
}


function changeTopCard(topCardName) {
	console.log("changeTopCard()");
	var cardHolder = document.getElementsByClassName("game-container");
	var cardData =
	"<div class=\"card\">\n" + 
	"				<div class=\"card-back card-face\">\n" + 
	"					<img class=\"uno\" src=\"IMG/" + topCardName + ".png\">\n" + 
	"				</div>\n" + 
	"			</div>";
	cardHolder[0].innerHTML = cardData;
}

// drawCard Callback Function (info From Server)
function addCardToHand(JSONData) {
	var cardHolder = document.getElementsByClassName("game-container");
	var nextPlayer = JSONData.nextPlayer;
	var requestSentBy = JSONData.requestSentBy;
	var cardToAdd = JSONData.message;
	var cardData = 
	"<div onclick=\"placeCard(this)\" class=\"card " + JSONData.message +"\" title=\"" + JSONData.message +"\>\n" + 
	"				<div class=\"card-back card-face\">\n" + 
	"					<img  class=\"uno\" src=\"IMG/" + JSONData.message + ".png\">\n" + 
	"				</div>\n" + 
	"			</div>";
	var playerID = document.getElementById("playerID").innerHTML;

	// Update next player on Screen
	document.getElementById("currentPlayer").innerHTML = nextPlayer;
	
	// Update Card Hand for Player that sent request
	if (playerID === requestSentBy) {
		cardHolder[1].innerHTML += cardData;		
	}
}

// 
function changeCurrentPlayer(JSONData) {
	var currentPlayer = JSONData.currentPlayer;
	document.getElementById("currentPlayer").innerHTML = currentPlayer;
}

// Add new Player Callback Function
function addNewPlayer(JSONData) {
	var newUserID = JSONData.newUserID;
	var playerData = "<div class=\"game-info\">\n" + 
	"				<span>" + newUserID+ "</span>\n" + 
	"			</div>" 
	
	var gamePlayerContainer = document.getElementsByClassName("game-info-players")[0];
	gamePlayerContainer.innerHTML += playerData;
}
/*
 * REQUEST FUNCTIONS
 * User requests an action to the server
 */
function ready() {
	console.log("ready()");
	var playerID = document.getElementById("playerID").innerHTML;
	var roomID = document.getElementById("gameRoomID").innerHTML;
	
	var readyRequest = {
			"action" : "ready",
			"username" : playerID,
			"nickname" : playerID,
			"roomID" : roomID
	}
	document.getElementById("readyButton").style.backgroundColor = "#55aa55";
	game.socket.send(JSON.stringify(readyRequest));
}

function connect() {
	game = new GameWebSocket(connectionURL);
	
	// Game ID is embedded in HTML
	var gameID = document.getElementById("gameRoomID").innerHTML;
	

}

function joinGame() {
	var gameID = document.getElementById("gameRoomID").innerHTML;
	var playerID = document.getElementById("playerID").innerHTML;
	var joinInstructions = {
			"action" : "joinRoom",
			"username" : playerID,
			"nickname" : playerID,
			"roomID" : gameID
	}
	console.log(gameID + "sending..." + joinInstructions);
	document.getElementById("joinButton").style.display = "none";
	game.sendMessage(JSON.stringify(joinInstructions));
}

function uno() {
	game.socket.send("uno");
}

function playWild(cardSelection) {
	var color;
	var value = "Wild";
	
	if (cardSelection === 'Red') {
		color = "Red";
	} else if (cardSelection === 'Blue') {
		color = "Blue";
	} else if (cardSelection === 'Yellow') {
		color = "Yellow";
	} else if (cardSelection === 'Green') {
		color = "Green;"
	}
	
	var playerID = document.getElementById("playerID").innerHTML;
	var roomID = document.getElementById("gameRoomID").innerHTML;
	var playCardInstructions = {
			"action" : "makeTurn",
			"username" : playerID,
			"nickname" : playerID,
			"roomID" : roomID,
			"cardColor" : color,
			"cardValue" : value
	}
		
	var wild = document.getElementsByClassName("selectWildCard")[0];
	wild.style.display = "none";
	game.socket.send(JSON.stringify(playCardInstructions));
 }

function playWildFour(cardSelection) {
	var color;
	var value = "Wild_Four";
	
	if (cardSelection === 'Red') {
		color = "Red";
	} else if (cardSelection === 'Blue') {
		color = "Blue";
	} else if (cardSelection === 'Yellow') {
		color = "Yellow";
	} else if (cardSelection === 'Green') {
		color = "Green;"
	}
	var playerID = document.getElementById("playerID").innerHTML;
	var roomID = document.getElementById("gameRoomID").innerHTML;
	var playCardInstructions = {
			"action" : "makeTurn",
			"username" : playerID,
			"nickname" : playerID,
			"roomID" : roomID,
			"cardColor" : color,
			"cardValue" : value
	}
	
	
	var wildFour = document.getElementsByClassName("selectWildFourCard")[0];
	wildFour.style.display = "none";
	game.socket.send(JSON.stringify(playCardInstructions));
}

function closePopUp(ID) {
	var popup = document.getElementsByClassName(ID)[0];
	popup.style.display = "none";
}

function openNotification(message) {
	document.getElementById("notificationText").innerHTML = message;
	document.getElementsByClassName("notification")[0].style.display = "block";
}

function closeNotification() {
	document.getElementsByClassName("notification")[0].style.display = "none";
}