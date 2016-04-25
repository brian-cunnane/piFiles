import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;


public class QueryServlet extends HttpServlet{
    Connection connection = null;
    PreparedStatement find = null;
    PrintWriter out = null;
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        response.setContentType("text/html");
        out = response.getWriter();
        out.println("<html><head><meta http-equiv=\"refresh\" content =\"30\"/><title>Query Results</title>" +
                "<script src=\"http://code.jquery.com/jquery-latest.js\"></script>" +
                "<script src=\"http://code.highcharts.com/highcharts.js\"></script>" +
                "</head><body>");
        try{
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/BEEKEEPER","root","root");
            String sql = "SELECT * FROM BEEKEEPER.HIVE1 ORDER BY date DESC, time DESC LIMIT 100";
            int count = 0;
            find = connection.prepareStatement(sql);
            ResultSet rset = find.executeQuery();
            //out.println("");
            out.println("<div id=\"container\" style=\"width:100%; height:1000px;\"></div>");
            out.println("<script src =\"./scripts/chart.js\"></script>" +
                    "<script type=\"text/javascript\" src=\"/js/themes/gray.js\"></script>" +
                    "<script type=\"text/javascript\" src=\"http://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js\"></script>");
            out.println("<table><tr><th>Temperature</th><th>Weight</th><th>Humidity</th><th>Time</th><th>Date</th></tr>");
            JSONArray jsonArray = new JSONArray();
            JSONArray datesArray = new JSONArray();
            JSONArray tempsArray = new JSONArray();
            JSONArray weightsArray = new JSONArray();
            JSONArray humidityArray = new JSONArray();
            JSONArray timesArray = new JSONArray();
            JSONObject jsonTimes = new JSONObject();
            JSONObject jsonDates = new JSONObject();
            JSONObject jsonTemps = new JSONObject();
            JSONObject jsonWeights = new JSONObject();
            JSONObject jsonHumidity = new JSONObject();

            while(rset.next()){
		try{
	                tempsArray.put(rset.getInt("Temperature"));
	                weightsArray.put(rset.getDouble("Weight"));
	                humidityArray.put(rset.getInt("Humidity"));
	                timesArray.put(rset.getTime("Time"));
	                datesArray.put(rset.getDate("Date"));
	
	                out.println("<tr><td>"+rset.getInt("Temperature")
	                        +"</td><td>" + rset.getDouble("Weight")
	                        +"</td><td>" + rset.getInt("Humidity")
	                        +"</td><td>" + rset.getTime("Time")
	                        +"</td><td>" + rset.getDate("Date") +"</td></tr>");
                	count++;
		}catch(JSONException JE){JE.printStackTrace();}
            }
            try {
                jsonDates.put("data",datesArray);
                jsonDates.put("name","dates");

                jsonTimes.put("data",timesArray);
                jsonTimes.put("name","times");

                jsonTemps.put("name", "temps");
                jsonTemps.put("data", tempsArray);

                jsonWeights.put("name", "weights");
                jsonWeights.put("data", weightsArray);

                jsonHumidity.put("name", "humidity");
                jsonHumidity.put("data", humidityArray);

                jsonArray.put(jsonDates);
                jsonArray.put(jsonTimes);
                jsonArray.put(jsonTemps);
                jsonArray.put(jsonWeights);
                jsonArray.put(jsonHumidity);

            }catch(JSONException e){e.printStackTrace();}

            try{
                String relativePath = "/scripts/JSONData.json";
                String absolutePath = getServletContext().getRealPath(relativePath);
                
                File file = new File(absolutePath);
                file.createNewFile();
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write((jsonArray.toString()).getBytes());
                fileOutputStream.close();

            }catch(IOException IOE){
		IOE.printStackTrace();
		IOE.printStackTrace(out);
		out.println(stackTraceForWeb(IOE));}
            out.println("</table>");
            out.println("<p>======" + count + " records found =====</p>");
            out.println("</body></hmtl>");
        }catch(SQLException SQLE){
            out.println("<p>SQL ERROR</p>");
            SQLE.printStackTrace();
            SQLE.printStackTrace(out);
            out.println(stackTraceForWeb(SQLE));
        }catch(ClassNotFoundException CNFE){
            CNFE.printStackTrace();
        }
    }

    public String stackTraceForWeb(Throwable t){
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        t.printStackTrace(pw);
        String stackTrace = sw.toString();
        pw.close();

        return stackTrace.replace(System.getProperty("line.seperator"),"\n");
    }

    public void destroy(){
        try{
            find.close();
            connection.close();
            out.close();
        }catch (SQLException E){E.printStackTrace();}
    }
}

