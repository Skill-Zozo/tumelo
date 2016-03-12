import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;

/**
 * Servlet implementation class Sever
 */
@WebServlet("/Sever")
public class Sever extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private boolean authentic = false;
    private DBI database;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Sever() {
    	super();
    	database = new DBI();
        
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
		String param = request.getParameter("type");
		if(param.equalsIgnoreCase("pull_data")) {
			String usrname = request.getParameter("username");
			String password = request.getParameter("password");
			String user = getDataFromDB(usrname, password, response);
			writeToResponse("state", "ok-user", response);
			writeToResponse("user" ,user, response);
		} else if (param.equalsIgnoreCase("add") && authentic) {
			String user = request.getParameter("user");
			String dev = request.getParameter("device");
			String rm = request.getParameter("room");
			String location = request.getParameter("location");
			writeToResponse("state" ,"ok", response);
			database.appendRow(user, location, rm, dev);
		} else if (param.equalsIgnoreCase("signup")){
			String usr = request.getParameter("username");
			String pwd = request.getParameter("password");
			database.registerUser(usr, pwd);
			writeToResponse("state" ,"ok", response);
		} else if (param.equalsIgnoreCase("login")) {
			String usr = request.getParameter("username");
			String pwd = request.getParameter("password");
			String resp = getDataFromDB(usr, pwd, response);
			if(resp.equalsIgnoreCase("")) authentic = true;
		}
	}

	private void writeToResponse(String param ,String attributes, HttpServletResponse response) {
		// TODO Auto-generated method stub
		JSONObject dets = new JSONObject();
		try {
			dets.append(param, attributes);
			response.setContentType("application/json");
			PrintWriter out = response.getWriter();
			out.print(dets);
			out.flush();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private String getDataFromDB(String usrname, String password, HttpServletResponse resp) {
		String response =  database.authenticate(usrname, password);
		if(response.equalsIgnoreCase("success")) {
			writeToResponse("state", "ok", resp);
			return database.getData(usrname);
		} else if (response.equalsIgnoreCase("failed to login")){
			String state = "incorrect password";
			writeToResponse("state", state, resp);
		} else 
			writeToResponse("state", "user does not exist", resp);
		return "";
	}

}