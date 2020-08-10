package teamuno_CSCI201L_GroupProject;

public class MessageHandler extends Thread {
	private Game game;
	private String message;

	public MessageHandler(Game game, String message) {
		this.game = game;
		this.message = message;
	}

	@Override
	public void run() {
		this.game.threadedBroadcastMessage(this.message);
	}
}
