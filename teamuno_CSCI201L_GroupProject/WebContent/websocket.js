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
var connectionURL = "ws://localhost:8080/teamuno_CSCI201L_GroupProject/RoomSocket";
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
function connect() {
	game = new GameWebSocket(connectionURL);
}

/*
	REPONSE FORMAT FROM SERVER
	{
		"type" : String, [valid values = "error", "content-change"]
		message : String,
		"contentChangeType" : "addCard";
	}
*/
function processMessage(message) {
	var text = JSON.parse(message);
	if (text.type === "error") {
		alert(text.message);
	} else if (text.type === "content-change") {
		if (text.contentChangeType === "addCard") {
			addCard(text.message);
		} 
	} else {
		console.log("from processMessage(): Could not recongnize message type " + text.type);
		
	}
}

function draw() {
	console.log("draw()");
	var data = {
			"action" : "draw",
			"username" : "testUsername",
			"roomID" : "testID"
	};
	
	game.socket.send(JSON.stringify(data));
}

function addCard(HTMLData) {
	var cardHolder = document.getElementsByClassName("cardContainer");
	var cardData = "<div class=\"card\">" +
	"<div class=\"card\">\n" + 
	"				<div class=\"card-back card-face\">\n" + 
	"					<img class=\"uno\" src=\"IMG/" + HTMLData + "\">\n" + 
	"				</div>\n" + 
	"				<div class=\"card-front card-face\">\n" + 
	"				\n" + 
	"				</div>\n" + 
	"			</div>";
	cardHolder[0].innerHTML += cardData;
}

function ready() {
	console.log("ready()");
	game.socket.send("ready");
}

function uno() {
	game.socket.send("uno");
}