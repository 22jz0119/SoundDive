package servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.DBManager;
import dao.UserDAO;
import model.User;

@WebServlet("/User2")
public class User2 extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserDAO userDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        DBManager dbManager = new DBManager(); // DBManagerのインスタンスを初期化
        userDAO = new UserDAO(dbManager); // UserDAOを初期化
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        response.setCharacterEncoding("Shift_JIS");

        PrintWriter out = response.getWriter();
        
        out.println("<html>");
        out.println("<head><title>Hello Servlet</title></head>");
        out.println("<body>");
        out.println("<h1>Hello, World!</h1>");
        out.println("<p>This is a simple servlet example.</p>");

        Long userId = 1L; // 取得したいユーザーのIDを指定
        User user = userDAO.getUserById(userId); // ユーザーを取得

        // ユーザー情報をコンソールに出力
        userDAO.printUser(user); // コンソールに出力

        // サーブレットの応答にユーザー情報を追加
        if (user != null) {
            out.println("<h2>ユーザー情報</h2>");
            out.println("<p>ユーザーID: " + user.getId() + "</p>");
            out.println("<p>ユーザー名: " + user.getName() + "</p>");
            out.println("<p>電話番号: " + user.getTelNumber() + "</p>");
            out.println("<p>住所: " + user.getAddress() + "</p>");
            out.println("<p>作成日: " + user.getCreateDate() + "</p>");
            out.println("<p>更新日: " + user.getUpdateDate() + "</p>");
        } else {
            out.println("<p>ユーザーが見つかりませんでした。</p>");
        }

        out.println("</body>");
        out.println("</html>");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
