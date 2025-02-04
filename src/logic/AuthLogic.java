package logic;

import javax.servlet.http.HttpSession;

import org.mindrot.jbcrypt.BCrypt;

import dao.DBManager;
import dao.UserDAO;
import model.User;

public class AuthLogic {
    public User login(String tel_number, String password) {
        UserDAO dao = new UserDAO(DBManager.getInstance());
        User user = dao.findByTelNumber(tel_number);

        if (user != null && BCrypt.checkpw(password, user.getPassword())) {
            return user;
        }
        return null;
    }

    public void loginUserSession(HttpSession session, User user) {
        session.setAttribute("loginUser", user);
    }

    public void logout(HttpSession session) {
        if (isLoggedIn(session)) {
            session.removeAttribute("loginUser");
        }
    }

    public boolean isLoggedIn(HttpSession session) {
        return session.getAttribute("loginUser") != null;
    }
}
