package com.railway.userdetails;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class LogoutServlet
 */
public class LogoutServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LogoutServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		
		//It returns the pre-existing session
		HttpSession session = request.getSession();
		
		PrintWriter out = response.getWriter();
		
		//Invalidating the session
		if(session!=null) {
			out.println("<h1 style='text-align:center'><b> Thank you " +session.getAttribute("username") +"!!!! Successfully logged out </b></h1>");
			out.println("<h3 style='text-align:center'><b> Please log back in </b></h3>");
			out.println("<br>");
			
			session.invalidate();
		}
		
		//Prevent caching
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
		
		//We can use any of the below : RequestDispatcher or sendRedirect()
		RequestDispatcher rd = request.getRequestDispatcher("login.html");
		rd.include(request, response);
		
		//response.sendRedirect("login.html");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
