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

@WebServlet("/UserServlet")
public class UserServlet extends HttpServlet {  // クラス名を UserServlet に変更
    private static final long serialVersionUID = 1L;
    private UserDAO userDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            DBManager dbManager = DBManager.getInstance();
            userDAO = new UserDAO(dbManager);
        } catch (ClassNotFoundException e) {
            throw new ServletException("JDBCドライバーのロードに失敗しました", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        response.setCharacterEncoding("Shift_JIS");

        PrintWriter out = response.getWriter();

        out.println("<html>");
        out.println("<head><title>User Information</title></head>");
        out.println("<body>");
        out.println("<h1>Hello, World!</h1>");
        out.println("<p>This is a simple servlet example with database interaction.</p>");

        int userId = 4;
        model.User user = userDAO.getUserById(userId);

        if (user != null) {
            out.println("<h2>ユーザー情報</h2>");
            out.println("<p>ユーザーID: " + user.getId() + "</p>");
            out.println("<p>ログインID: " + user.getLogin_id() + "</p>");  // ログインIDを追加
            out.println("<p>パスワード: " + user.getPassword() + "</p>");
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

        // コンソール出力を追加
        if (user != null) {
            System.out.println("ユーザー情報:");
            System.out.println("ユーザーID: " + user.getId());
            System.out.println("ログインID: " + user.getLogin_id());  // ログインIDを追加
            System.out.println("ユーザー名: " + user.getName());
            System.out.println("パスワード: " + user.getPassword());
            System.out.println("電話番号: " + user.getTelNumber());
            System.out.println("住所: " + user.getAddress());
            System.out.println("作成日: " + user.getCreateDate());
            System.out.println("更新日: " + user.getUpdateDate());
        } else {
            System.out.println("ユーザーが見つかりませんでした。");
        }
    }
}
