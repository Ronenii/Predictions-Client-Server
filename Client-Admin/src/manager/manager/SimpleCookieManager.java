package manager.manager;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class SimpleCookieManager implements CookieJar {

    private final static String CACHE_MANAGER_PREFIX = "    [Cookie Manager] ---> ";
    private final Map<String, Map<String, Cookie>> cookies = new HashMap<>();

    @NotNull
    @Override
    public List<Cookie> loadForRequest(@NotNull HttpUrl httpUrl) {
        String host = httpUrl.host();
        System.out.print(CACHE_MANAGER_PREFIX + "Fetching cookies for domain: [" + host + "]...");
        List<Cookie> cookiesPerDomain = Collections.emptyList();
        synchronized (this) {
            if (cookies.containsKey(host)) {
                cookiesPerDomain = new ArrayList<>(cookies.get(host).values());
            }
        }
        System.out.println(" Total of " + cookiesPerDomain.size() + " cookie(s) will be loaded !");
        return cookiesPerDomain;
    }

    @Override
    public void saveFromResponse(@NotNull HttpUrl httpUrl, @NotNull List<Cookie> responseCookies) {
        String host = httpUrl.host();
        synchronized (this) {
            // "computeIfAbsent" - if the cookie map does not exist, create a new one).
            Map<String, Cookie> cookiesMap = cookies.computeIfAbsent(host, key -> new HashMap<>());
            responseCookies
                    .forEach(cookie -> {
                        System.out.println(CACHE_MANAGER_PREFIX + "Storing cookie [" + cookie.name() + "] --> [" + cookie.value() + "]");
                        cookiesMap.put(cookie.name(), cookie);
                    });
        }
    }

}