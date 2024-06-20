package com.railway.userdetails;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.railway.dbconnection.DBUtil;

/**
 * Servlet implementation class UserServlet
 */
public class UserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		
		
		//Getting the session if it exists - getSession(false) gives us the session if exists otherwise it returns null
		HttpSession session = request.getSession(false);
		
		Connection conn = null;
		
		if(session!=null) {
			/*
			 * Request is passed from LoginServlet with user_id attribute so retrieve it here as:-
			 * 		--> getAttribute() method returns the Object so we are down casting it to Integer
			 * 		--> We are using session.getAttribute() method instead of request.getAttribute() because once we refresh the page
			 * 			or go back then request.getAttribute() will loose the value and returns null but session.getAttribute() 
			 * 			will hold the value until the session is invalidated or expires.
			 * 
			 * 		--> session.getAttribute() returns an Object so down casting it to Integer
			 */
			Integer user_id_Wrapper = (Integer)session.getAttribute("user_id");
			
			// Unboxing: Convert Integer to int
			int user_id = user_id_Wrapper != null ? user_id_Wrapper.intValue() : 0;
			
			//We are setting it again to pass it to FavoriteCrossingServlet and will retrieve it there
			session.setAttribute("user_id", user_id);
			
    		RequestDispatcher rd = request.getRequestDispatcher("user_home.html");
    		rd.include(request, response);
    		
    		//out.println("<a href='FavoriteCrossingServlet' class='btn btn-primary'>Favorite Crossing</a>");
    		out.println("<button onclick=\"window.location.href='FavoriteCrossingServlet?param1=" +user_id +" '\" class='btn btn-primary'>Favorite Crossing</button>");
    		out.println("<br><br>");
			
			try {
				//Prevent caching
                response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
                response.setHeader("Pragma", "no-cache");
                response.setDateHeader("Expires", 0);
		        
				conn = DBUtil.getDBConnection();
				
				Statement stmt = conn.createStatement();
				String query = "SELECT * FROM RAILWAY_CROSSING";
				ResultSet rs = stmt.executeQuery(query);
				
				while(rs.next()) {
					out.println("<div class='box'>");
					out.println("<b>" +rs.getString(2) +"</b> <br>");
					
					String status = rs.getString(6);
					if(status.equalsIgnoreCase("Open")) {
						out.println("Crossing Status: <span style='background-color:limegreen; color:white; text-transform:uppercase; border-radius:15px'>" +rs.getString(6) +"</span> <br>");
					} else {
						out.println("Crossing Status: <span style='background-color:red; color:white; text-transform:uppercase; border-radius:15px'>" +rs.getString(6) +"</span> <br>");
					}
					
					out.println("Person In-Charge: <b>" +rs.getString(5) +"</b> <br>");
					out.println("Train Schedule: <b>" +rs.getString(4) +"</b> <br>");
					out.println("Address: <b>" +rs.getString(3) +"</b> <br>");
					
					out.println("<form action='FavoriteCrossingServlet' method='post'>");
					out.println("<input type='hidden' name='addFavorite_railwayId' value='" +rs.getInt(1) +"'>");
					out.println("<input type='hidden' name='addFavorite_userId' value='" +user_id +"'>");
					out.println("<button type='submit' style='color:grey; background-color:white; border:grey 0.2px solid; border-radius:15px'>ADD TO FAVORITE</button> <br>");
					out.println("</form>");
					
					out.println("</div>");
				}
				
			} catch(SQLException e) {
				e.printStackTrace();
			} catch(ClassNotFoundException e) {
				e.printStackTrace();
			} finally {
				if(conn!=null) {
					try {
						conn.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		} else {
			response.sendRedirect("login.html");
		}
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
