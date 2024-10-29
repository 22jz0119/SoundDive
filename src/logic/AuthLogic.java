package logic;

import javax.servlet.http.HttpSession;

import org.mindrot.jbcrypt.BCrypt;

import dao.DBManager;
import dao.UserDAO;
import model.User;

/**
 * ログイン・ログアウト処理を行うクラス
 * 
 * @author d.sugawara
 *
 */
public class AuthLogic {
	/**
	 * ログイン処理を行う
	 * 
	 * @param email
	 * @param password
	 * @return 成功時はログインしたユーザ、失敗時はnull
	 */
	public User login(String login_id, String password) {
		UserDAO dao = new UserDAO(DBManager.getInstance());
		User user = dao.findByLoginIdAndPassword(login_id, password);

		System.out.println(user);
		
		if ((user != null) && (BCrypt.checkpw(password, user.getPassword()))) {
			
			return user;
		}

		return null;
	}
	
	/**
	 * ログアウト処理を行う
	 * 
	 * @return なし
	 */
	public void logout(HttpSession session) {
		if (isLoggedIn(session)) {
			session.removeAttribute("loginUser");
		}
	}

	/**
	 * ログイン状態を確認する
	 * 
	 * @param session
	 * @return ログインしていれば true、していなければ false
	 */
	public boolean isLoggedIn(HttpSession session) {
		return session.getAttribute("loginUser") != null;
	}
	


	/**
	 * ログイン状態を確認する
	 * @param session
	 * @return ログインしていれば true、していなければ false
	 */
//	public boolean isLoggedInK(HttpSession session) {
//		return session.getAttribute("loginUser") != null;
//	}
}