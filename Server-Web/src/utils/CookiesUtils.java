package utils;

import constant.Constants;
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
                }
            }
        }

        return savedValue;
    }

    public static void saveValueOnCookie(HttpServletResponse response, String value, String constant) {
        response.addCookie(new Cookie(constant, value));
    }
}
