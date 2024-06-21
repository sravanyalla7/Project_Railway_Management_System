package com.railway.userdetails;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
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
 * Servlet implementation class FavoriteCrossingServlet
 */
public class FavoriteCrossingServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FavoriteCrossingServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		
		Connection conn = null;
		
		HttpSession session = request.getSession(false);
		
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
			
			RequestDispatcher rd = request.getRequestDispatcher("favorite_crossing.html");
			rd.include(request, response);
			
			try {
				//Prevent caching
                response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
                response.setHeader("Pragma", "no-cache");
                response.setDateHeader("Expires", 0);
                
				conn = DBUtil.getDBConnection();
				
				String query = "SELECT A.*, B.RAILWAY_ID, C.USER_ID FROM RAILWAY_CROSSING A INNER JOIN FAVORITE_CROSSING B ON A.RAILWAY_ID = B.RAILWAY_ID INNER JOIN USER C ON C.USER_ID = B.USER_ID WHERE C.USER_ID=?";
				
				PreparedStatement pstmt = conn.prepareStatement(query);
				pstmt.setInt(1, user_id);
				ResultSet rs = pstmt.executeQuery();
				
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
					
					out.println("<form action='DeleteFavoriteCrossingServlet' method='post'>");
					out.println("<input type='hidden' name='removeFavorite_railwayId' value='" +rs.getInt(7) +"'>");
					out.println("<input type='hidden' name='removeFavorite_userId' value='" +rs.getInt(8) +"'>");
					out.println("<button type='submit' style='color:grey; background-color:white; border:grey 0.2px solid; border-radius:15px'>REMOVE FROM FAVORITE</button> <br>");
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
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		
		int addFavorite_railwayId = Integer.parseInt(request.getParameter("addFavorite_railwayId"));
		int addFavorite_userId = Integer.parseInt(request.getParameter("addFavorite_userId"));
		
		Connection conn = null;
		
		HttpSession session = request.getSession(false);
		
		if(session!=null) {
			try {
				//Prevent caching
                response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
                response.setHeader("Pragma", "no-cache");
                response.setDateHeader("Expires", 0);
                
				conn = DBUtil.getDBConnection();
				PreparedStatement pstmt = null;
				
				String selectQuery = "SELECT * FROM FAVORITE_CROSSING WHERE USER_ID=? AND RAILWAY_ID=?";
				pstmt = conn.prepareStatement(selectQuery);
				pstmt.setInt(1, addFavorite_userId);
				pstmt.setInt(2, addFavorite_railwayId);
				ResultSet rs = pstmt.executeQuery();
				
				if(rs.next()) {
					if(rs.getInt(2)==addFavorite_railwayId) {
						out.println("<html> <body>");
						
						out.println("<div style=\"position:relative; left:30%; border:1px solid grey; padding:10px; width:50%; height:10%; text-align:center; background-color:lightyellow;\">");
						out.println("It is already present in the Favorite Crossing <br>");
						out.println("<button onclick=\"window.location.href='UserServlet'\">OK</button>");
						out.println("</div>");
						
						out.println("</body> </html>");
					}
				} else {
					String insertQuery = "INSERT INTO FAVORITE_CROSSING VALUES(?, ?)";
					pstmt = conn.prepareStatement(insertQuery);
					pstmt.setInt(1, addFavorite_userId);
					pstmt.setInt(2, addFavorite_railwayId);
					int rowsInserted = pstmt.executeUpdate();
					
					if(rowsInserted>0) {
						out.println("<html> <body>");
						
						out.println("<div style=\"position:relative; left:30%; border:1px solid grey; padding:10px; width:50%; height:10%; text-align:center; background-color:lightyellow;\">");
						out.println("Successfully added to Favorite Crossing <br>");
						out.println("<button onclick=\"window.location.href='UserServlet'\">OK</button>");
						out.println("</div>");
						
						out.println("</body> </html>");
					} else {
						out.println("<html> <body>");
						
						out.println("<div style=\"position:relative; left:30%; border:1px solid grey; padding:10px; width:50%; height:10%; text-align:center; background-color:lightyellow;\">");
						out.println("Not added <br>");
						out.println("<button onclick=\"window.location.href='UserServlet'\">OK</button>");
						out.println("</div>");
						
						out.println("</body> </html>");
					}
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

}
