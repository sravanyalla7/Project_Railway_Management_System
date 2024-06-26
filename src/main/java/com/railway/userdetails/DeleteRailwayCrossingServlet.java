package com.railway.userdetails;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.railway.dbconnection.DBUtil;

/**
 * Servlet implementation class DeleteRailwayCrossingServlet
 */
public class DeleteRailwayCrossingServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeleteRailwayCrossingServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		
		int id = Integer.parseInt(request.getParameter("param1"));
		
		PrintWriter out = response.getWriter();
		
		HttpSession session = request.getSession(false);
		
		if(session!=null) {
			out.println("<html> <body>");
			
			out.println("<div style=\"position:relative; left:30%; border:1px solid grey; padding:10px; width:50%; min-height:10%; text-align:center; background-color:lightyellow; font-size:15px;\">");
			out.println("Do you want to delete it, please confirm? <br>");
			
			out.println("<form action='DeleteRailwayCrossingServlet' method='post'>");
			out.println("<input type='hidden' name='id' value='"+ id +"'>");
			out.println("<input type='submit' value='YES'>");
			out.println("</form> <br>");
			
			out.println("<button onclick=\"window.location.href='AdminServlet'\">NO</button>");
			out.println("</div>");
			
			out.println("</body> </html>");
		} else {
			response.sendRedirect("login.html");
		}		
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		
		int id = Integer.parseInt(request.getParameter("id"));
		
		PrintWriter out = response.getWriter();
		
		Connection conn = null;
		
		HttpSession session = request.getSession(false);
		
		if(session!=null) {
			try {
				conn = DBUtil.getDBConnection();
				
				String query = "DELETE FROM RAILWAY_CROSSING WHERE RAILWAY_ID=?";
				PreparedStatement pstmt = conn.prepareStatement(query);
				pstmt.setInt(1, id);
				
				int rowsDeleted = pstmt.executeUpdate();
				if(rowsDeleted>0) {
					out.println("<h4 style='text-align:center'><b>" +rowsDeleted +" rows deleted successfully </b></h4>");
					RequestDispatcher rd = request.getRequestDispatcher("AdminServlet");
					rd.include(request, response);
				} else {
					out.println("<h4 style='text-align:center'><b> No rows deleted </b></h4>");
					RequestDispatcher rd = request.getRequestDispatcher("AdminServlet");
					rd.include(request, response);
				}
			} catch(ClassNotFoundException e) {
				e.printStackTrace();
			} catch(SQLException e) {
				e.printStackTrace();
			} finally {
				if(conn!=null) {
					try {
						conn.close();
					} catch(SQLException e) {
						e.printStackTrace();
					}
				}
			}
		} else {
			response.sendRedirect("login.html");
		}
	}

}
