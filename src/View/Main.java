package View;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Data.ExceptionManager;
import Data.Manager;
import Data.SessionData;
import Data.StatusException;
import Data.User;
import Data.UserPrivilege;

/**
 * Servlet implementation class Main
 */
@WebServlet("/rest/*")
public class Main extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Manager manager;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Main() 
    {
        super();
        // TODO Auto-generated constructor stub
        manager = new Manager();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
//		setAccessControlHeaders(response);
		PrintWriter pw = response.getWriter();
		String pathInfo = request.getPathInfo();
		String jsonBody = getBody(request);
		response.setContentType("application/json");
		
		try
		{
			if(pathInfo.compareTo("/status") == 0)
			{
				Object token = tryGetParameter(request, "token");
				if(token != null)
					pw.append(manager.checkSessionStatus(token.toString()));
				else
					pw.append(manager.checkServerStatus());					

				return;
			}
			
			if(pathInfo.compareTo("/session_data") == 0)
			{
				SessionData sessionData = manager.checkAndgetSessionDataFromParameter(request);
				pw.append(sessionData.session_data);
				return;
			}
			
			if(pathInfo.compareTo("/session_current_time") == 0)
			{
				SessionData sessionData = manager.checkAndgetSessionDataFromParameter(request);
				pw.append(Manager.toJson("current_time", sessionData.current_time));
				return;
			}
			
			if(pathInfo.compareTo("/user_data") == 0)
			{
				SessionData sessionData = manager.checkAndgetSessionDataFromParameter(request);
				UserPrivilege userPrivilege = manager.gson.fromJson(sessionData.session_data, UserPrivilege.class);
				User user = manager.getUser(userPrivilege.user_id);
				pw.append(manager.gson.toJson(user));
				return;
			}
			
		}
		catch(StatusException ex)
		{
			statusHandler(ex, response);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		PrintWriter pw = response.getWriter();
		String pathInfo = request.getPathInfo();
		String jsonBody = getBody(request);
		response.setContentType("application/json");
		
		try
		{
			
			if(pathInfo.compareTo("/register_website") == 0)
			{
				manager.checkValidDevKey(jsonBody);
				manager.registerWebsite(jsonBody);
				response.setStatus(201);
				return;
			}
			
			if(pathInfo.compareTo("/register_user_group") == 0)
			{
				manager.checkValidDevKey(jsonBody);
				manager.registerUserGroup(jsonBody);
				response.setStatus(201);
				return;
			}
			
			if(pathInfo.compareTo("/register_user") == 0)
			{
				int user_id = manager.registerUser(jsonBody);
				pw.append(Manager.toJson("user_id", user_id));
				response.setStatus(201);
				return;
			}
			
			if(pathInfo.compareTo("/login") == 0)
			{
				pw.append(Manager.toJson("token", manager.login(jsonBody)));
				response.setStatus(201);
				return;
			}
		}
		catch(StatusException ex)
		{
			statusHandler(ex, response);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPut(HttpServletRequest, HttpServletResponse)
	 */
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		PrintWriter pw = response.getWriter();
		String pathInfo = request.getPathInfo();
		String jsonBody = getBody(request);
		response.setContentType("application/json");
		
		try
		{	
			if(pathInfo.compareTo("/sql") == 0)
			{
				manager.setSQL(jsonBody);
				response.setStatus(201);
				pw.append(manager.checkServerStatus());
				return;
			}
			
			if(pathInfo.compareTo("/renew_session") == 0)
			{
				manager.reNewSession(jsonBody);
				return;
			}
			
		}		
		catch(StatusException ex)
		{
			statusHandler(ex, response);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doDelete(HttpServletRequest, HttpServletResponse)
	 */
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		PrintWriter pw = response.getWriter();
		String pathInfo = request.getPathInfo();
		String jsonBody = getBody(request);
		response.setContentType("application/json");
		
		try
		{	
			if(pathInfo.compareTo("/logout") == 0)
			{
				manager.logout(jsonBody);
				return;
			}
		}		
		catch(StatusException ex)
		{
			statusHandler(ex, response);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doHead(HttpServletRequest, HttpServletResponse)
	 */
	protected void doHead(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		PrintWriter pw = response.getWriter();
		String pathInfo = request.getPathInfo();
		String body = getBody(request);
		
		response.setContentType("application/json");
		pw.append("{\"method\":\"Head\"}");
	}

	/**
	 * @see HttpServlet#doOptions(HttpServletRequest, HttpServletResponse)
	 */
	protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		PrintWriter pw = response.getWriter();
		String pathInfo = request.getPathInfo();
		String body = getBody(request);
		
		response.setContentType("application/json");
		pw.append("{\"method\":\"Option\"}");
	}
	
	private String getBody(HttpServletRequest request) throws IOException
	{
		StringBuilder sb = new StringBuilder();
	    BufferedReader reader = request.getReader();
	    try 
	    {
	        String line;
	        while ((line = reader.readLine()) != null) 
	        {
	            sb.append(line).append('\n');
	        }
	    } 
	    finally 
	    {
	        reader.close();
	    }
	    
	    return sb.toString();
	}

	private void setAccessControlHeaders(HttpServletResponse resp) 
	{
		resp.addHeader("Access-Control-Allow-Origin", "*");
		resp.addHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE, HEAD");
		resp.addHeader("Access-Control-Allow-Headers", "X-PINGOTHER, Origin, X-Requested-With, Content-Type, Accept");
		resp.addHeader("Access-Control-Max-Age", "1728000");
		resp.setContentType("application/json");
	}
	
	private void statusHandler(Exception exception, HttpServletResponse res)
	{
		try 
		{
			ExceptionManager exceptionManager = manager.gson.fromJson(exception.getMessage(), ExceptionManager.class);
			
			if(exceptionManager == null || exceptionManager.code < 0)
			{
				res.sendError(500);
				return;
			}
			
			
			if(exceptionManager.message.compareTo("") != 0)
				res.sendError(exceptionManager.code, exceptionManager.message);
			else
				res.sendError(exceptionManager.code);
			
			
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private Object tryGetParameter(HttpServletRequest request, String parameterName)
	{
		try
		{
			Object obj = request.getParameter(parameterName);
			return obj;
		}
		catch(Exception ex)
		{
			return null;
		}
	}
}
