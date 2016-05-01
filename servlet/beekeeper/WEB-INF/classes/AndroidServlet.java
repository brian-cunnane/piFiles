import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletContext;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.*;
import java.io.*;

/**
 *Author: Brian Cunnane 
 *Date: 18/04/2016.
 *Description: servlet to create json data for android app.
 */
public class AndroidServlet extends HttpServlet {
    Connection connection = null;
    PreparedStatement find = null;
    PrintWriter out = null;
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String parameter = "";
        //parameter = request.getParameter("test");
        if (request.getParameter("Hive1") != null){
            response.setContentType("application/json");
            out = response.getWriter();
            try{
                Class.forName("com.mysql.jdbc.Driver");
                connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/BEEKEEPER","root","root");
                String query = "SELECT * FROM BEEKEEPER.HIVE1 ORDER BY date DESC, time DESC LIMIT 50";
                String jsonString = null;
                find = connection.prepareStatement(query);
                JSONObject json = new JSONObject();
                //json.put("key","Value");
                ResultSet rset = find.executeQuery();
                JSONArray jsonArray = new JSONArray();
                while(rset.next()){
                    json.put("temperature",rset.getInt("Temperature"));
                    json.put("weight",rset.getDouble("Weight"));
                    json.put("humidity",rset.getInt("Humidity"));
                    json.put("time",rset.getTime("Time"));
                    json.put("date",rset.getDate("Date"));
                    jsonArray.put(json);
                    json = new JSONObject();
                }
                out.println(jsonArray.toString());
            }catch(JSONException JE){
                JE.printStackTrace();
            }catch(SQLException SQLE){
                out.println("<p>SQL ERROR</p>");
                SQLE.printStackTrace();
                SQLE.printStackTrace(out);
                out.println(stackTraceForWeb(SQLE));
            }catch(ClassNotFoundException CNFE){
                CNFE.printStackTrace();
            }
        }
	else if(request.getParameter("Hive2") != null){
		response.setContentType("application/json");
            	out = response.getWriter();
           	try{
			String relativePath = "/scripts/hive2.json";
	                ServletContext context = this.getServletContext();
			InputStream IS = context.getResourceAsStream(relativePath);
			InputStreamReader ISR = new InputStreamReader(IS);
			BufferedReader BR = new BufferedReader(ISR);
			String data = "";
			while((data = BR.readLine()) != null){
				out.println(data);
			}
		
		}catch(IOException IOE){IOE.printStackTrace();}
	}
        else{
            out.println("<br>parameter is: "+parameter+"</br");
            out.println("<br>Something is wrong</br>");
        }
    }
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        doGet(request, response);
    }
    public String stackTraceForWeb(Throwable t){
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        t.printStackTrace(pw);
        String stackTrace = sw.toString();
        pw.close();

        return stackTrace.replace(System.getProperty("line.seperator"),"\n");
    }

}

