package logic;

import javax.servlet.http.HttpSession;

import org.mindrot.jbcrypt.BCrypt;

import dao.DBManager;
import dao.UserDAO;
import model.User;

public class AuthLogic {
    public User login(String login_id, String password) {
        System.out.println("Attempting to log in user with ID: " + login_id);

        UserDAO dao = new UserDAO(DBManager.getInstance());
        User user = dao.findByLoginId(login_id); // ユーザー情報を先に取得

        if (user != null) {
            System.out.println("User found: " + user.getName());
            // パスワードを確認
            boolean passwordMatches = BCrypt.checkpw(password, user.getPassword());
            System.out.println("Password matches: " + passwordMatches);
            System.out.println("Entered password: " + password); // 追加
            System.out.println("Stored hashed password: " + user.getPassword()); // 追加

            if (passwordMatches) {
                System.out.println("Login successful for user: " + user.getName());
                return user;
            } else {
                System.out.println("Invalid password for user ID: " + login_id);
            }
        } else {
            System.out.println("No user found with ID: " + login_id);
        }
        return null;
    }

    public void logout(HttpSession session) {
        if (isLoggedIn(session)) {
            System.out.println("Logging out user.");
            session.removeAttribute("loginUser");
        } else {
            System.out.println("No user is currently logged in.");
        }
    }

    public boolean isLoggedIn(HttpSession session) {
        boolean loggedIn = session.getAttribute("loginUser") != null;
        System.out.println("Is user logged in? " + loggedIn);
        return loggedIn;
    }
}
