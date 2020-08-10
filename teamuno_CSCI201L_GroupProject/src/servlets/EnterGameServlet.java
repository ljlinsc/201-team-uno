package servlets;

import java.io.IOException;
import java.util.HashSet;
import java.util.Random;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import teamuno_CSCI201L_GroupProject.User;
import teamuno_CSCI201L_GroupProject.RoomSocket;


@WebServlet("/EnterGameServlet")
public class EnterGameServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static HashSet<String> allRoomID = null;
    public EnterGameServlet() {
        super();
        if(EnterGameServlet.allRoomID == null) {
        	EnterGameServlet.allRoomID = new HashSet<String>();
        }
        allRoomID.add("111");
    }

    //Process get request
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
		//String to store roomID
		String roomID;
		//Get session and user
		String msg = request.getParameter("game-id");
		HttpSession session = request.getSession();
		User usr = (User) session.getAttribute("user");
		String next_page;
		if(RoomSocket.roomsExists(msg)) { // Registered or unregistered users can enter game
			roomID = msg;
			usr.setRoomID(roomID);
			
			next_page = "/game.jsp?roomID="+roomID;
		}
		else if(usr.isRegistered() && msg == null) { // Creating new room here?
			roomID = generateHex(); // Is this gauranteed to be unique?
			usr.setRoomID(roomID);
			next_page = "/game.jsp?roomID="+roomID;
		}
		else if (allRoomID.contains(msg)) {
			roomID = msg;
			usr.setRoomID(msg);
			next_page = "/game.jsp?roomID="+roomID;
		}
		else {
			request.setAttribute("enterGameRoomMessage", "invalid game room code");
			next_page = "/lobby.jsp";
		}
		
		RequestDispatcher dispatch = getServletContext().getRequestDispatcher(next_page);
		dispatch.forward(request, response);
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	private String generateHex() {
		Random rand = new Random();
		int randomInt = rand.nextInt();
		String Hexa = Integer.toHexString(randomInt);
		return Hexa;
	}

}
