package servlet.livehouse;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Livehouse_application;

/**
 * Servlet implementation class Application_approval
 */
@WebServlet("/Application_approval")
public class Application_approval extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
	public Application_approval() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	 @Override
	    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	        int reservationId = Integer.parseInt(request.getParameter("id")); // クエリパラメータから予約IDを取得

	        // DAOを使ってデータベースから予約者名と予約日時を取得
	        Livehouse_application reservation = dao.getReservationById(reservationId);

	        if (reservation != null) {
	            // JSPにデータを渡す
	            request.setAttribute("reservationName", reservation.getName());
	            request.setAttribute("reservationDateTime", reservation.getDatetime());

	            // JSPにフォワード
	            request.getRequestDispatcher("../reservation.jsp").forward(request, response);
	        } else {
	            response.getWriter().println("予約情報が見つかりませんでした。");
	        }
	    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response); // doPostからdoGetを呼び出す
	}

}
