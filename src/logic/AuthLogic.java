package logic;

import javax.servlet.http.HttpSession;

import org.mindrot.jbcrypt.BCrypt;

import dao.DBManager;
import dao.UserDAO;
import model.User;

public class AuthLogic {
    public User login(String tel_number, String password) {
        System.out.println("Attempting to log in user with tel_number: " + tel_number);

        UserDAO dao = new UserDAO(DBManager.getInstance());
        User user = dao.findByTelNumber(tel_number); // ユーザー情報を先に取得

        if (user != null) {
            System.out.println("User found: " + user.getName());
            boolean passwordMatches = BCrypt.checkpw(password, user.getPassword());
            System.out.println("Password matches: " + passwordMatches);

            if (passwordMatches) {
                System.out.println("Login successful for user: " + user.getName());
                return user;
            } else {
                System.out.println("Invalid password for tel_number: " + tel_number);
            }
        } else {
            System.out.println("No user found with tel_number: " + tel_number);
        }
        return null;
    }

    public void loginUserSession(HttpSession session, User user) {
        System.out.println("Storing user in session.");
        session.setAttribute("loginUser", user);
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
