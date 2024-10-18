package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class User
 */
@WebServlet("/User")
public class User extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/html");
		
		PrintWriter out = response.getWriter();
		
        out.println("<html>");
        out.println("<head><title>Hello Servlet</title></head>");
        out.println("<body>");
        out.println("<h1>Hello, World!</h1>");
        out.println("<p>This is a simple servlet example.</p>");
        String DATABASE_NAME = "information_schema";
        String PROPERTIES = "?characterEncoding=UTF-8&serverTimezone=Asia/Tokyo";
        String URL = "jdbc:mysql://localhost:3306/" + DATABASE_NAME + PROPERTIES;
        //DB接続用・ユーザ定数
        String USER = "root";
        String PASS = "";

        try {
            //MySQL に接続する
            Class.forName("com.mysql.cj.jdbc.Driver");
            //データベースに接続
            Connection conn = DriverManager.getConnection(URL, USER, PASS);

            // データベースに対する処理
            out.println("データベースに接続に成功");
            
            // 接続を閉じる
            conn.close();

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        out.println("</body>");
        out.println("</html>");
		
		RequestDispatcher dispatcher = request.getRequestDispatcher("userjsp.jsp");
        dispatcher.forward(request, response);
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

	
}
