package com.sms.servlet;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.sms.service.LoginService;

public class LoginController extends HttpServlet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2700854818500150822L;
	
	@SuppressWarnings("resource")
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException{
		ApplicationContext applicationContext = 
				new ClassPathXmlApplicationContext("/com/sms/resource/applicationContext.xml");
		
		LoginService loginService = 
				(LoginService) applicationContext.getBean("loginService");
		
		String page = "";
		try {
			loginService.getUserInfo(request);
			
			switch ((Integer)request.getAttribute("loginStatus")) {
			case 0:
				page = "login.jsp";
				request.setAttribute("error", "Invalid user name and/or password.");
				break;
				
			case 1:
				response.setStatus(201);
				page = "pages/home.jsp";
				break;
				
			case 2:
				page = "login.jsp";
				request.setAttribute("error", "Account locked. Please contact the administrator.");
				break;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			RequestDispatcher rd = request.getRequestDispatcher(page);
			rd.forward(request, response);
		}
	}
	
	@SuppressWarnings("resource")
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException{
		HttpSession session = request.getSession();
		ApplicationContext applicationContext = 
				new ClassPathXmlApplicationContext("/com/sms/resource/applicationContext.xml");
		LoginService loginService = 
				(LoginService) applicationContext.getBean("loginService");
		
		loginService.logout();
		session.invalidate();
	}
}