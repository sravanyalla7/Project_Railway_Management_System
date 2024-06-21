package com.railway.userdetails;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.railway.dbconnection.DBUtil;

/**
 * Servlet implementation class LoginServlet
 */
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//Prevent caching
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
        
		response.setContentType("text/html");
		
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		
		Connection conn = null;
		
		PrintWriter out = response.getWriter();
		
		try {
			conn = DBUtil.getDBConnection();
			
			String query = "SELECT * FROM USER WHERE EMAIL=? AND PASSWORD=?";
			PreparedStatement pstmt = conn.prepareStatement(query);
			pstmt.setString(1, email);
			pstmt.setString(2, password);
			
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				int user_id = rs.getInt(1);
				String username = rs.getString(2);
				String e = rs.getString(3);
				String p = rs.getString(4);
				if(e.equalsIgnoreCase(email) && p.equals(password)) {
					
					//Creating a new session and store user information
					HttpSession session = request.getSession();
					
					if(session!=null) {
						session.setAttribute("username", username);
		                
						if(username.equalsIgnoreCase("admin")) {
							//Admin page
							RequestDispatcher rd = request.getRequestDispatcher("AdminServlet");
							rd.forward(request, response);
						} else {
							session.setAttribute("user_id", user_id);
							//User page
							RequestDispatcher rd = request.getRequestDispatcher("UserServlet");
							rd.forward(request, response);
						}
					} else {
						RequestDispatcher rd = request.getRequestDispatcher("login.html");
						rd.include(request, response);
						out.println("<h3><b>Session expired, please login</b></h3>");
					}
				} else {
					RequestDispatcher rd = request.getRequestDispatcher("login.html");
					rd.include(request, response);
					out.println("<h3><b>Invalid username or password, please try again</b></h3>");
				}
			} else {
				RequestDispatcher rd = request.getRequestDispatcher("login.html");
				rd.include(request, response);
				out.println("<h3><b>Invalid username or password, please try again</b></h3>");
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
		
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
