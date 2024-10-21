package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Oser
 */
@WebServlet("/User2")
public class User2 extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/html");
		response.setCharacterEncoding("Shift_JIS");
		
		PrintWriter out = response.getWriter();
		
        out.println("<html>");
        out.println("<head><title>Hello Servlet</title></head>");
        out.println("<body>");
        out.println("<h1>Hello, World!</h1>");
        out.println("<p>This is a simple servlet example.</p>");
        String DATABASE_NAME = "databaseg09";
        String PROPERTIES = "?characterEncoding=UTF-8&enabledTLSProtocols=TLSv1.2";
//        String PROPERTIES = "?characterEncoding=UTF-8&serverTimezone=Asia/Tokyo&enabledTLSProtocols=TLSv1.2";
        String URL = "jdbc:mysql://158.101.151.242:3306/" + DATABASE_NAME + PROPERTIES;
//        String URL = "jdbc:mysql://158.101.151.242:3306/databaseg09?characterEncoding=UTF-8&enabledTLSProtocols=TLSv1.2";
        //DB接続用・ユーザ定数
        String USER = "new_root";
        String PASS = "#98MxdslOf;lg09";
        out.println(URL);
        try {
            //MySQL に接続する
            Class.forName("com.mysql.cj.jdbc.Driver");
            //データベースに接続
            Connection conn = DriverManager.getConnection(URL, USER, PASS);

            // データベースに対する処理
            out.println("データベースに接続に成功");
            
            // 接続を閉じる
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        out.println("</body>");
        out.println("</html>");
		
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
