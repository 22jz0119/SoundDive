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

    	System.out.println("[DEBUG] doGet method entered"); // ログを追加
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
            
         // 申請詳細の取得後にgroupIdを表示する
            int groupId = applicationDetails.getGroupId();  // `groupId` を取得
            System.out.println("[DEBUG] groupId: " + groupId);  // groupIdをログに出力

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

            // 画像パスをリクエストから取得して渡す
         // 画像パスをリクエストから取得して渡す
            String imagePath = request.getParameter("imagePath");
            if (imagePath != null && !imagePath.isEmpty()) {
                request.setAttribute("imagePath", imagePath); // 画像パスをリクエストにセット
                System.out.println("[DEBUG] Image path received: " + imagePath); // 画像パスをログに出力
            } else {
                System.out.println("[DEBUG] No image path received from request.");
            }
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
    	System.out.println("[DEBUG] Entered addGroupImageToMap for groupId: " + groupId); // メソッドの開始を確認
        // グループIDがマップにすでに存在する場合、画像は取得しない
        if (!pictureImageMap.containsKey(groupId)) {
            // 画像を取得
            String groupImage = artistGroupDAO.getPictureImageMovieByArtistGroupId(groupId);
            
            // 画像が空またはnullの場合、デフォルト画像を使用
            if (groupImage == null || groupImage.isEmpty()) {
                groupImage = "/uploads/default_image.png"; // デフォルト画像を設定
                System.out.println("[DEBUG] Group image is null or empty. Using default image.");
            } else {
                System.out.println("[DEBUG] Group image found for groupId " + groupId + ": " + groupImage);
            }
            // マップに画像パスを保存
            pictureImageMap.put(groupId, groupImage);
            System.out.println("[DEBUG] Image path added to pictureImageMap for groupId " + groupId + ": " + groupImage);
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