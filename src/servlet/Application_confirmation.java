package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.Artist_groupDAO;
import dao.DBManager;
import dao.MemberDAO;
import model.Artist_group;
import model.Member;

@WebServlet("/Application_confirmation")
public class Application_confirmation extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // DBManagerインスタンスを取得してDAOを作成
        DBManager dbManager = DBManager.getInstance();
        Artist_groupDAO artistGroupDAO = new Artist_groupDAO(dbManager);
        MemberDAO memberDAO = new MemberDAO(dbManager);

        // サンプルとして、IDが1のアーティストグループ情報とそのメンバー情報を取得
        int artistGroupId = 1;  // 取得したいArtist_groupのIDを指定
        Artist_group artistGroup = artistGroupDAO.getArtist_groupById(artistGroupId);

        // アーティストグループ情報が見つかれば、そのメンバー情報も取得
        if (artistGroup != null) {
            String accountName = artistGroup.getAccount_name(); // アーティストグループのaccount_name属性を取得
            request.setAttribute("accountName", accountName); // JSPに渡す

            // メンバー情報を取得（例としてID 1のメンバーを取得）
            int memberId = 1;  // 必要に応じて取得したいメンバーIDを指定
            Member member = memberDAO.getMemberById(memberId);

            // メンバーが見つかれば、その名前とポジションをJSPに渡す
            if (member != null) {
                request.setAttribute("memberName", member.getMember_name());
                request.setAttribute("memberPosition", member.getMember_potision());
            } else {
                request.setAttribute("memberError", "メンバー情報が見つかりません");
            }
        } else {
            request.setAttribute("error", "アーティストグループ情報が見つかりません");
        }

        // JSPページにフォワード
        request.getRequestDispatcher("WEB-INF/jsp/application_confirmation.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
