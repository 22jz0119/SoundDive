package servlet;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.Artist_groupDAO;
import dao.DBManager;
import dao.Livehouse_applicationDAO;
import model.LivehouseApplicationWithGroup;
import model.Member;

@WebServlet("/Application_approval")
public class Application_approval extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String DEFAULT_DATE_TIME = "未定";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("Application_approval servlet is called.");

        String applicationIdParam = request.getParameter("id");
        if (applicationIdParam == null || applicationIdParam.isEmpty()) {
            handleError(request, response, "申請IDが指定されていません");
            return;
        }

        try {
            int applicationId = Integer.parseInt(applicationIdParam);
            System.out.println("Application ID received: " + applicationId);

            DBManager dbManager = DBManager.getInstance();
            Livehouse_applicationDAO livehouseApplicationDAO = new Livehouse_applicationDAO(dbManager);
            Artist_groupDAO artistGroupDAO = Artist_groupDAO.getInstance(dbManager); // Artist_groupDAOインスタンスを追加
            LivehouseApplicationWithGroup applicationDetails = livehouseApplicationDAO.getApplicationDetailsById(applicationId);

            if (applicationDetails == null) {
                handleError(request, response, "指定された申請データが見つかりません");
                return;
            }
            
            // 日時のフォーマット
            LocalDateTime dateTime = applicationDetails.getDatetime();
            String formattedDateTime = (dateTime != null) ? dateTime.format(DATE_TIME_FORMATTER) : DEFAULT_DATE_TIME;
            System.out.println("Formatted Datetime: " + formattedDateTime);

            // 画像処理
            Map<Integer, String> pictureImageMap = new HashMap<>();
            addGroupImageToMap(applicationDetails.getGroupId(), artistGroupDAO, pictureImageMap);
            
            // リクエストスコープに申請詳細をセット
            request.setAttribute("application", applicationDetails);
            request.setAttribute("formattedDateTime", formattedDateTime);

            // `cogig_or_solo` の取得
            int cogigOrSolo = livehouseApplicationDAO.getCogigOrSoloByApplicationId(applicationId);
            request.setAttribute("cogigOrSolo", cogigOrSolo);

            // **常にartistGroupIdを取得してリクエストに渡す**
            int artistGroupId = livehouseApplicationDAO.getArtistGroupIdByApplicationId(applicationId);
            System.out.println("[DEBUG] artistGroupId: " + artistGroupId);  // デバッグログ
            if (artistGroupId > 0) {
                // 対バングループのメンバー情報を取得
                List<Member> artistMembers = livehouseApplicationDAO.getMembersByGroupId(artistGroupId);
                System.out.println("[DEBUG] artistMembers: " + (artistMembers != null ? artistMembers.size() : "null"));
                request.setAttribute("artistMembers", artistMembers);

                // 対バングループの詳細情報を取得
                LivehouseApplicationWithGroup artistGroup = livehouseApplicationDAO.getGroupDetailsById(artistGroupId);
                request.setAttribute("artistGroup", artistGroup);

                // デバッグ用ログ
                System.out.println("[DEBUG] artistGroupId: " + artistGroupId);
                System.out.println("[DEBUG] artistGroup Name: " + (artistGroup != null ? artistGroup.getAccountName() : "NULL"));
                System.out.println("[DEBUG] artistGroup Genre: " + (artistGroup != null ? artistGroup.getGroupGenre() : "NULL"));

                // 対バングループの画像も取得
                addGroupImageToMap(artistGroupId, artistGroupDAO, pictureImageMap);
            } else {
                System.out.println("[DEBUG] artistGroupId is invalid or zero.");
            }

            // artistGroupIdを常にリクエストに渡す
            request.setAttribute("artistGroupId", artistGroupId);

            // 画像データをJSPに渡す
            request.setAttribute("pictureImageMap", pictureImageMap);

            // JSP にフォワード
            request.getRequestDispatcher("WEB-INF/jsp/livehouse/application_approval.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            handleError(request, response, "無効な申請ID形式です");
            e.printStackTrace();
        } catch (Exception e) {
            handleError(request, response, "エラーが発生しました");
            e.printStackTrace();
        }
    }

    private void addGroupImageToMap(int groupId, Artist_groupDAO artistGroupDAO, Map<Integer, String> pictureImageMap) {
        if (!pictureImageMap.containsKey(groupId)) {
            String groupImage = artistGroupDAO.getPictureImageMovieByArtistGroupId(groupId);
            if (groupImage == null || groupImage.isEmpty()) {
                groupImage = "/uploads/default_image.png"; // デフォルト画像を設定
            }
            pictureImageMap.put(groupId, groupImage);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    /**
     * エラー処理を共通化
     */
    private void handleError(HttpServletRequest request, HttpServletResponse response, String errorMessage) throws ServletException, IOException {
        System.err.println("Error: " + errorMessage);
        request.setAttribute("error", errorMessage);
        request.getRequestDispatcher("WEB-INF/jsp/livehouse/application_approval.jsp").forward(request, response);
    }
}
