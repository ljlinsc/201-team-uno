package teamuno_CSCI201L_GroupProject.servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import teamuno_CSCI201L_GroupProject.JDBC_Core;
import teamuno_CSCI201L_GroupProject.User;

/**
 * Servlet implementation class CreateUserServlet
 */
@WebServlet("/CreateUserServlet")
public class CreateUserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String nickname = request.getParameter("nickname");
		String next = "/newuser.jsp";
		User user = null;
		try {
			user = JDBC_Core.createUser(username, password, nickname);
		} catch (ClassNotFoundException e1) {
			System.out.println("Error while retrieving the user information.");
		}
		if (username != null && password != null && user != null) {
			HttpSession session = request.getSession();
			session.setAttribute("user", user);
			next = "/lobby.jsp";
		}
		request.setAttribute("message", "username already taken");
		RequestDispatcher dispatch = getServletContext().getRequestDispatcher(next);
		dispatch.forward(request, response);
	}

}
