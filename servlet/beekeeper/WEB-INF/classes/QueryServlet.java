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
                out.println("<html><head><title>Query Results</title></head><body>");
                out.println("<h2>Thanks for your query.</h2>");
		out.println("<p>Hello</p>");
                try{
			Class.forName("com.mysql.jdbc.Driver");
                        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/BEEKEEPER","Administrator","admin");
                        String sql = "SELECT * FROM BEEKEEPER.HIVE1";
                        
                        out.println("<p>Your Query is: " + sql + "</p>");
                        int count = 0;
			find = connection.prepareStatement(sql);
                        ResultSet rset = find.executeQuery();
                        while(rset.next()){
                                out.println("<p>"+rset.getInt("Temperature")
                                                +"," + rset.getInt("Weight")
                                                +"," + rset.getInt("Humidity")
                                                +"," + rset.getTime("Time")
                                                +"," + rset.getDate("Date") +"</p>");
                                count++;
                        }
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

