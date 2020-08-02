package teamuno_CSCI201L_GroupProject.servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import teamuno_CSCI201L_GroupProject.JDBC_Core;

@WebServlet("/SignInServlet")
public class SignInServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String next = "/index.jsp";
		try {
			if (username != null && password != null && JDBC_Core.verifySignIn(username, password)) {
				next = "/lobby.html";
			}
		} catch (ClassNotFoundException e) {
			System.out.println("cnfe error.");
		}
		request.setAttribute("message", "invalid user/pass");
		RequestDispatcher dispatch = getServletContext().getRequestDispatcher(next);
		dispatch.forward(request, response);
	}
}
