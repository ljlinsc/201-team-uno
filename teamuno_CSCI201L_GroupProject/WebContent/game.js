if (document.readyState === 'loading') {
	document.addEventListener('DOMContentLoaded', ready());
} else {
	ready();
}

class AudioController {
	constructor() {
		this.bgMusic = new Audio('AUDIO/game.mp3');
		this.bgMusic.volume = 0.5;
		this.bgMusic.loop = true;
		// Other audio options for future versions
		this.flipSound = null;
		this.gameOverSound = null;
		this.startGame = null;
	}
	
	startMusic() {
		this.bgMusic.play();
	}
	
	stopMusic() {
		this.bgMusic.pause();
		this.bgMusic.currentTime = 0;
	}
	/*
	 flip(){}
	 */
	gameOver() {
		this.stopMusic();
	}
}

class Uno {
	constructor(startTime, cards) {
		this.cardsArray = cards;
		this.time = startTime;
		this.cardToCheck = null;
	}
	
	startGame() {
		
	}
}

function ready() {
	let cards = Array.from(document.getElementsByClassName('card'));
	
	cards.forEach(card => {
		card.addEventListener('click', () => {
			// REMOVE CARD HERE
			game.remove(card);
		})
	});
	
	
}

let audioController = new AudioController();