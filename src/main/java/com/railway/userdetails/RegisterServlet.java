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

import com.railway.dbconnection.DBUtil;

/**
 * Servlet implementation class RegisterServlet
 */
public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	//private Connection conn = null;

    /**
     * Default constructor. 
     */
    public RegisterServlet() {
//    	try {
//    		//Getting the database connection
//    		conn = DBUtil.getDBConnection();
//    	} catch(Exception e) {
//    		e.printStackTrace();
//    	}
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
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException  {
		response.setContentType("text/html");
		
		String username = request.getParameter("username");
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		int rowsInserted = 0;
		
		Connection conn = null;
		
		//Sending the response using the PrintWriter
		PrintWriter out = response.getWriter();
		
		try {
			conn = DBUtil.getDBConnection();
			
			//Getting the database connection through constructor and create statement to store the values in the database
			String query = "INSERT INTO USER (USERNAME, EMAIL, PASSWORD) VALUES(?, ?, ?)";
			
			PreparedStatement pstmt = conn.prepareStatement(query);
			pstmt.setString(1, username);
			pstmt.setString(2, email);
			pstmt.setString(3, password);
			
			rowsInserted = pstmt.executeUpdate();
			
			//System.out.println("Rows inserted successfully: " +rowsInserted);
			if(rowsInserted>0) {
				RequestDispatcher rd = request.getRequestDispatcher("login.html");
				rd.include(request, response);
				out.println("<h4 style='text-align:center'><b>" +rowsInserted +" rows inserted & registered successfully - Please login now </b></h4>");
			} else {
				out.println("<h4 style='text-align:center'><b>No rows inserted </b></h4>");
			}	
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(conn!=null) {
				try {
					conn.close();
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
		
	}

}
