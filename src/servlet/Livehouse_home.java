package servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;

import dao.DBManager;
import dao.Livehouse_applicationDAO;
import model.Notice;

@WebServlet("/Livehouse_home")
public class Livehouse_home extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private Livehouse_applicationDAO dao;
    private static final Logger LOGGER = Logger.getLogger(Livehouse_home.class.getName());

    @Override
    public void init() throws ServletException {
        DBManager dbManager = DBManager.getInstance();
        dao = new Livehouse_applicationDAO(dbManager);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");

        if (userId == null) {
            LOGGER.warning("[WARN] ユーザーが未ログインのため、トップページへリダイレクト");
            response.sendRedirect(request.getContextPath() + "/Top");
            return;
        }

        LOGGER.info("[INFO] 取得したユーザーID: " + userId);

        try {
            // **ライブハウスIDの取得**
            int livehouseId = dao.getLivehouseIdByUserId(userId);
            if (livehouseId == -1) {
                LOGGER.warning("[WARN] 該当するライブハウス情報が見つかりません。userId: " + userId);
                request.setAttribute("errorMessage", "ライブハウス情報が見つかりません");
                request.getRequestDispatcher("/WEB-INF/jsp/livehouse/livehouse_home.jsp").forward(request, response);
                return;
            }

            // **未読通知の取得**
            List<Notice> notifications = new ArrayList<>();
            try (Connection conn = DBManager.getInstance().getConnection()) {
                String sql = "SELECT id, livehouse_application_id, create_date, update_date, message, is_read, user_id FROM notice_table WHERE user_id = ? AND is_read = 0 ORDER BY create_date DESC";
                LOGGER.info("[INFO] 実行するSQLクエリ: " + sql);

                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setInt(1, userId);
                    ResultSet rs = stmt.executeQuery();

                    while (rs.next()) {
                        Notice notice = new Notice(
                            rs.getInt("id"),
                            rs.getInt("livehouse_application_id"),
                            rs.getTimestamp("create_date").toLocalDateTime(),
                            rs.getTimestamp("update_date") != null ? rs.getTimestamp("update_date").toLocalDateTime() : null,
                            rs.getString("message"),
                            rs.getBoolean("is_read"),
                            rs.getInt("user_id")
                        );
                        notifications.add(notice);
                        LOGGER.info("[INFO] 取得した通知: " + notice.getMessage() + " (ID: " + notice.getId() + ")");
                    }
                }
            }

            if (notifications.isEmpty()) {
                LOGGER.info("[INFO] 未読通知はありません");
            } else {
                LOGGER.info("[INFO] 取得した未読通知数: " + notifications.size());
            }

            request.setAttribute("notifications", notifications);

            // **カレンダーの予約情報を取得**
            Map<String, Integer> reservationCounts = dao.getReservationCountsByLivehouse(LocalDateTime.now().getYear(), LocalDateTime.now().getMonthValue(), userId);
            String reservationStatusJson = new Gson().toJson(reservationCounts);
            request.setAttribute("reservationStatus", reservationStatusJson);
            request.setAttribute("year", LocalDateTime.now().getYear());
            request.setAttribute("month", LocalDateTime.now().getMonthValue());

            request.getRequestDispatcher("/WEB-INF/jsp/livehouse/livehouse_home.jsp").forward(request, response);
            LOGGER.info("[INFO] Successfully forwarded to JSP.");

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "[ERROR] SQLエラー: " + e.getMessage(), e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SQLエラーが発生しました。");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String type = request.getParameter("type"); // "request" or "approval"
        int reservationId = Integer.parseInt(request.getParameter("reservationId"));
        String datetime = request.getParameter("datetime");
        int userId = Integer.parseInt(request.getParameter("userId"));

        LOGGER.info("[INFO] 通知処理開始 - type: " + type + ", reservationId: " + reservationId + ", datetime: " + datetime + ", userId: " + userId);

        DBManager dbManager = DBManager.getInstance();
        try (Connection conn = dbManager.getConnection()) {
            conn.setAutoCommit(false);

            String message = "request".equals(type) 
                ? "新しい予約申請が届きました: 予約ID " + reservationId + ", 予約日時: " + datetime
                : "あなたの予約 (ID: " + reservationId + ") が承認されました";

            String notifySql = "INSERT INTO notice_table (livehouse_application_id, create_date, update_date, message, is_read, user_id) VALUES (?, NOW(), NULL, ?, 0, ?)";
            LOGGER.info("[INFO] 実行するSQLクエリ: " + notifySql);

            try (PreparedStatement notifyStmt = conn.prepareStatement(notifySql)) {
                notifyStmt.setInt(1, reservationId);
                notifyStmt.setString(2, message);
                notifyStmt.setInt(3, userId);
                notifyStmt.executeUpdate();
                LOGGER.info("[INFO] 通知をデータベースに保存 - message: " + message);
            }

            conn.commit();
            response.setStatus(HttpServletResponse.SC_OK);
            LOGGER.info("[INFO] 通知処理完了 - 成功");

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "[ERROR] 通知の作成に失敗しました: " + e.getMessage(), e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "通知の作成に失敗しました");
        }
    }
}
