package com.railway.userdetails;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.railway.dbconnection.DBUtil;

/**
 * Servlet implementation class DeleteFavoriteCrossingServlet
 */
public class DeleteFavoriteCrossingServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeleteFavoriteCrossingServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		
		int removeFavorite_railwayId = Integer.parseInt(request.getParameter("removeFavorite_railwayId"));
		int removeFavorite_userId = Integer.parseInt(request.getParameter("removeFavorite_userId"));
		
		//RequestDispatcher rd = request.getRequestDispatcher("UserServlet");
		//rd.include(request, response);
		
		Connection conn = null;
		
		HttpSession session = request.getSession(false);
		
		if(session!=null) {
			try {
				conn = DBUtil.getDBConnection();
				PreparedStatement pstmt = null;
				
				String selectQuery = "SELECT * FROM FAVORITE_CROSSING WHERE USER_ID=? AND RAILWAY_ID=?";
				pstmt = conn.prepareStatement(selectQuery);
				pstmt.setInt(1, removeFavorite_userId);
				pstmt.setInt(2, removeFavorite_railwayId);
				ResultSet rs = pstmt.executeQuery();
				
				if(rs.next()) {
					if(rs.getInt(2)==removeFavorite_railwayId) {
						
						String deleteQuery = "DELETE FROM FAVORITE_CROSSING WHERE USER_ID=? AND RAILWAY_ID=?";
						pstmt = conn.prepareStatement(deleteQuery);
						pstmt.setInt(1, removeFavorite_userId);
						pstmt.setInt(2, removeFavorite_railwayId);
						int rowsDeleted = pstmt.executeUpdate();
						
						if(rowsDeleted>0) {
							out.println("<html> <body>");
							
							out.println("<div style=\"position:relative; left:30%; border:1px solid grey; padding:10px; width:50%; height:10%; text-align:center; background-color:lightyellow;\">");
							out.println("Successfully deleted from Favorite Crossing <br>");
							out.println("<button onclick=\"window.location.href='FavoriteCrossingServlet'\">OK</button>");
							out.println("</div>");
							
							out.println("</body> </html>");
						} else {
							out.println("<html> <body>");
							
							out.println("<div style=\"position:relative; left:30%; border:1px solid grey; padding:10px; width:50%; height:10%; text-align:center; background-color:lightyellow;\">");
							out.println("Not deleted <br>");
							out.println("<button onclick=\"window.location.href='FavoriteCrossingServlet'\">OK</button>");
							out.println("</div>");
							
							out.println("</body> </html>");
						}
					}
				} else {
					out.println("<html> <body>");
					
					out.println("<div style=\"position:relative; left:30%; border:1px solid grey; padding:10px; width:50%; height:10%; text-align:center; background-color:lightyellow;\">");
					out.println("It is not present in the Favorite Crossing <br>");
					out.println("<button onclick=\"window.location.href='FavoriteCrossingServlet'\">OK</button>");
					out.println("</div>");
					
					out.println("</body> </html>");	
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
