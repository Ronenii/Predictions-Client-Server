package manager;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class HttpClientAgent {
    private final static OkHttpClient HTTP_CLIENT =
            new OkHttpClient.Builder().build();

    /**
     * Enqueues a http query to the server.
     *
     * @param url The url of the request we are trying to send.
     * @param callback A thread handling the response we get from the server.
     */
    public static void sendRequest(String url, Callback callback){
        Request request = new Request.Builder().url(url).build();

        Call call = HTTP_CLIENT.newCall(request);

        call.enqueue(callback);
    }
}
