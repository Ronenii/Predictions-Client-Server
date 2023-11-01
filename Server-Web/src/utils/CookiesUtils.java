package utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CookiesUtils {
    public static String getSavedValueOnCookie(HttpServletRequest request, String constant) {
        String savedValue = null;

        if(request.getCookies() != null) {
            for(Cookie cookie : request.getCookies()) {
                if(cookie.getName().contains(constant)) {
                    savedValue = cookie.getValue();
                    break;
                }
            }
        }

        return savedValue;
    }

    public static void createAndSaveNewCookie(HttpServletResponse response, String value, String constant) {
        response.addCookie(new Cookie(constant, value));
    }

    public static void updateValueOnCookie(HttpServletRequest request, HttpServletResponse response, String value, String constant) {
        if(request.getCookies() != null){
            for(Cookie cookie : request.getCookies()) {
                if(cookie.getName().contains(constant)) {
                    cookie.setValue(value);
                    response.addCookie(cookie);
                    break;
                }
            }
        }
    }
}
