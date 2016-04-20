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
        out.println("<html><head><meta http-equiv=\"refresh\" content =\"30\"/><title>Query Results</title></head><body>");
        out.println("<h2>Thanks for your query.</h2>");
        out.println("<p>Hello</p>");

        try{
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/BEEKEEPER","root","root");
            String sql = "SELECT * FROM BEEKEEPER.HIVE1 ORDER BY date DESC, time DESC LIMIT 50";
            out.println("<p>Your Query is: " + sql + "</p>");
            int count = 0;
            find = connection.prepareStatement(sql);
            ResultSet rset = find.executeQuery();
            out.println("<table><tr><th>Temperature</th><th>Weight</th><th>Humidity</th><th>Time</th><th>Date</th></tr>");
            while(rset.next()){
                out.println("<tr><td>"+rset.getInt("Temperature")
                        +"</td><td>" + rset.getInt("Weight")
                        +"</td><td>" + rset.getInt("Humidity")
                        +"</td><td>" + rset.getTime("Time")
                        +"</td><td>" + rset.getDate("Date") +"</td></tr>");
                count++;
            }
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

