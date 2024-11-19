package servlet;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;


/**
 * Servlet implementation class Livehouse_home
 */
@WebServlet("/Livehouse_home")
public class Livehouse_home extends HttpServlet {
	private static final long serialVersionUID = 1L;
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int year = Integer.parseInt(request.getParameter("year"));
        int month = Integer.parseInt(request.getParameter("month")); // 1月が1であると仮定

        Map<Integer, Integer> reservationCounts = dao.getReservationCountByMonth(year, month);

        // JSON形式に変換
        String json = new Gson().toJson(reservationCounts);
        response.setContentType("application/json");
        response.getWriter().write(json);
		
		
		
		// TODO Auto-generated method stub
		request.getRequestDispatcher("WEB-INF/jsp/livehouse_home.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
