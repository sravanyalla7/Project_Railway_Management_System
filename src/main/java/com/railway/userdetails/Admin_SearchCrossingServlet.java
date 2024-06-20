package com.railway.userdetails;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.railway.dbconnection.DBUtil;

/**
 * Servlet implementation class Admin_SearchCrossingServlet
 */
public class Admin_SearchCrossingServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Admin_SearchCrossingServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		
		String search = request.getParameter("search");
		
		PrintWriter out = response.getWriter();
		
		Connection conn = null;
		
		HttpSession session = request.getSession(false);
		
		if(session!=null) {
			try {
				conn = DBUtil.getDBConnection();
				
				Statement stmt = conn.createStatement();
				String query = "SELECT * FROM RAILWAY_CROSSING WHERE NAME LIKE '%" +search +"%'";
				ResultSet rs = stmt.executeQuery(query);
				
				RequestDispatcher rd = request.getRequestDispatcher("admin_home.html");
				rd.include(request, response);
				
				out.println("<table class='table'>");
				out.println("<thead>");
				out.println("<tr>");
				out.println("<th>Sr No</th>");
				out.println("<th>Name</th>");
				out.println("<th>Address</th>");
				out.println("<th>Train Schedule</th>");
				out.println("<th>Person In-Charge</th>");
				out.println("<th>Status</th>");
				out.println("<th>Action</th>");
				out.println("</tr>");
				out.println("</thead>");
				
				while(rs.next()) {
					out.println("<tbody");
					out.println("<tr>");
					out.println("<td>" +rs.getInt(1) +"</td>");
					out.println("<td>" +rs.getString(2) +"</td>");
					out.println("<td>" +rs.getString(3) +"</td>");
					out.println("<td>" +rs.getTime(4) +"</td>");
					out.println("<td>" +rs.getString(5) +"</td>");
					out.println("<td>" +rs.getString(6) +"</td>");
					out.println("<td>" +"<button onclick=\"window.location.href='UpdateRailwayCrossingServlet?param1=" +rs.getInt(1) +" '\">Update</button> <button onclick=\"window.location.href='DeleteRailwayCrossingServlet?param1=" +rs.getInt(1) +" '\">Delete</button>" +"</td>");
					out.println("</tr>");
					out.println("</tbody>");
				}
				out.println("</table>");
				
			} catch(Exception e) {
				e.printStackTrace();
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
