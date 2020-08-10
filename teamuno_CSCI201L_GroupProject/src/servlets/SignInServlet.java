package servlets;

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

@WebServlet("/SignInServlet")
public class SignInServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String next = "/index.jsp";
		User user = null;
		try {
			user = JDBC_Core.verifySignIn(username, password);
		} catch (ClassNotFoundException e1) {
			System.out.println("Error while retrieving the user information.");
		}
		if (username != null && password != null && user != null) {
			HttpSession session = request.getSession();
			session.setAttribute("user", user);
			next = "/lobby.jsp";
		} else {
			request.setAttribute("message", "invalid user/pass");
		}
		RequestDispatcher dispatch = getServletContext().getRequestDispatcher(next);
		dispatch.forward(request, response);
	}
}
