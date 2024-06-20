package com.railway.userdetails;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.railway.dbconnection.DBUtil;

/**
 * Servlet implementation class AddRailwayCrossingServlet
 */
public class AddRailwayCrossingServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddRailwayCrossingServlet() {
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
		
		//Getting the values from the "add_railway_crossing.html"
		String name = request.getParameter("name");
		String address = request.getParameter("address");
		
		String train_schedule = request.getParameter("train_schedule");
		//LocalTime train_time = LocalTime.parse(train_schedule, DateTimeFormatter.ofPattern("HH:mm"));
		
		String personInCharge = request.getParameter("person_InCharge");
		String crossing_status = request.getParameter("status");
		
		//Get the database connection
		Connection conn = null;
		
		PrintWriter out = response.getWriter();
		
		//Getting the session if it exists
		HttpSession session = request.getSession(false);
		
		if(session!=null) {
			try {
				conn = DBUtil.getDBConnection();
				String query = "INSERT INTO RAILWAY_CROSSING(NAME, ADDRESS, TRAIN_SCHEDULE, PERSON_IN_CHARGE, STATUS) VALUES (?, ?, ?, ?, ?)";
				PreparedStatement pstmt = conn.prepareStatement(query);
				pstmt.setString(1, name);
				pstmt.setString(2, address);
				pstmt.setString(3, train_schedule);
				pstmt.setString(4, personInCharge);
				pstmt.setString(5, crossing_status);
				
				int rowsInserted = 0;
				rowsInserted = pstmt.executeUpdate();
				
				if(rowsInserted>0) {
					out.println("<h4 style='text-align:center'><b>" +rowsInserted +" rows inserted & added railway crossing successfully </b></h4>");
					RequestDispatcher rd = request.getRequestDispatcher("AdminServlet");
					rd.include(request, response);
				} else {
					out.println("<h4 style='text-align:center'><b> No rows inserted </b></h4>");
					RequestDispatcher rd = request.getRequestDispatcher("AdminServlet");
					rd.include(request, response);
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
