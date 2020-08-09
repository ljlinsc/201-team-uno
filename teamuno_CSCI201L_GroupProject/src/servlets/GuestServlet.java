package servlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import teamuno_CSCI201L_GroupProject.User;

/**
 * Servlet implementation class GuestServlet
 */
@WebServlet("/GuestServlet")
public class GuestServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static int numGuests = 0;
    
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User user = new User("guest" + numGuests, "guest" + numGuests, false);
		numGuests++;
		HttpSession session = request.getSession();
		session.setAttribute("user", user);
		RequestDispatcher dispatch = getServletContext().getRequestDispatcher("/lobby.jsp");
		dispatch.forward(request, response);
	}

}
