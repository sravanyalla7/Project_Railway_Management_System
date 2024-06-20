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
 * Servlet implementation class UpdateRailwayCrossingServlet
 */
public class UpdateRailwayCrossingServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UpdateRailwayCrossingServlet() {
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
		
		Connection conn = null;
		
		HttpSession session = request.getSession(false);
		
		if(session!=null) {
			try {
				conn = DBUtil.getDBConnection();
				
				String query = "SELECT * FROM RAILWAY_CROSSING WHERE RAILWAY_ID=?";
				PreparedStatement pstmt = conn.prepareStatement(query);
				pstmt.setInt(1, id);
				ResultSet rs = pstmt.executeQuery();
				
				if(rs.next()) {
					out.println("<html>");
					out.println("<head>");
					out.println("<link rel='stylesheet' href='https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css'>");
					out.println("<script src='https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js'></script>");
					out.println("</head>");
					
					out.println("<body style='text-align:center;'>");
					out.println("<h3><b>Railway Crossing</b></h3>");
					out.println("<p>Update railway crossing information</p>");
					
					out.println("<form action='UpdateRailwayCrossingServlet' method='post'>");
					
					//type='hidden' is used to send a value without displaying it on the html file
					out.println("<input type='hidden' name='id' value='" +rs.getString(1) +"'>");
					
					out.println("<b>Enter name</b> <br>");
					out.println("<input type='text' name='name' value='" +rs.getString(2) +"'> <br><br>");
					out.println("<b>Address</b> <br>");
					out.println("<input type='text' name='address' value='" +rs.getString(3) +"'> <br><br>");
					out.println("<b>Train Schedule</b> <br>");
					out.println("<input type='text' name='train_schedule' value='" +rs.getString(4) +"'> <br><br>");
					out.println("<b>Person In-charge</b> <br>");
					out.println("<input type='text' name='personInCharge' value='" +rs.getString(5) +"'> <br><br>");
					
					out.println("<b>Crossing Status</b> <br>");
					out.println("Currently: <b>" +rs.getString(6) +"</b> <br>");
					out.println("<input type='radio' name='status' value='Open' required>Open <br>");
					out.println("<input type='radio' name='status' value='Close' required>Close <br> <br>");
					
					out.println("<input class='btn btn-success' type='submit' value='Update'> <br><br>");
					//out.println("<button onclick=\"window.location.href='update.html'\">Update</button>");
					out.println("</body>");
					
					out.println("</html>");
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

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		
		//Getting the values from the "UpdateRailwayCrossingServlet" doGet() method
		int id = Integer.parseInt(request.getParameter("id"));
		String name = request.getParameter("name");
		String address = request.getParameter("address");
		
		String train_schedule = request.getParameter("train_schedule");
		//LocalTime train_time = LocalTime.parse(train_schedule, DateTimeFormatter.ofPattern("HH:mm"));
		
		String personInCharge = request.getParameter("personInCharge");
		String status = request.getParameter("status");
		
		PrintWriter out = response.getWriter();
		
		Connection conn = null;
		
		HttpSession session = request.getSession(false);
		
		if(session!=null) {
			try {
				conn = DBUtil.getDBConnection();
				
				String query = "UPDATE RAILWAY_CROSSING SET NAME=?, ADDRESS=?, TRAIN_SCHEDULE=?, PERSON_IN_CHARGE=?, STATUS=? WHERE RAILWAY_ID=?;";
				PreparedStatement pstmt = conn.prepareStatement(query);
				pstmt.setString(1, name);
				pstmt.setString(2, address);
				pstmt.setString(3, train_schedule);
				pstmt.setString(4, personInCharge);
				pstmt.setString(5, status);
				pstmt.setInt(6, id);
				
				int rowsUpdated = 0;
				rowsUpdated = pstmt.executeUpdate();
				
				if(rowsUpdated>0) {
					out.println("<h4 style='text-align:center'><b>" +rowsUpdated +" rows updated successfully </b></h4>");
					RequestDispatcher rd = request.getRequestDispatcher("AdminServlet");
					rd.include(request, response);
				} else {
					out.println("<h4 style='text-align:center'><b> No rows inserted </b></h4>");
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
