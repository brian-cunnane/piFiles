
import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class QueryServlet extends HttpServlet{
        @Override
        public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
                response.setContentType("text/html");
                PrintWriter out = response.getWriter();
                Connection connection = null;
                Statement statement = null;
                try{
                        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/BEEKEEPER","admin","Administrator");
                        statement = connection.createStatement();
                        String sql = "SELECT * FROM BEEKEEPER.HIVE1";
                        out.println("<html><head><title>Query Results</title></head><body>");
                        out.println("<h2>Thanks for your query.</h2>");
                        out.println("<p>Your Query is: " + sql + "</p>");
                        int count = 0;
                        ResultSet rset = statement.executeQuery(sql);
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
                }catch(SQLException SQLE){SQLE.printStackTrace();}
	}
}

