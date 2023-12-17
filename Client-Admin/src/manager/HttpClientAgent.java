package manager;

import okhttp3.*;

public class HttpClientAgent {
    private final static OkHttpClient HTTP_CLIENT =
            new OkHttpClient.Builder().build();

    /**
     * Enqueues a http query to the server.
     *
     * @param url The url of the request we are trying to send.
     * @param callback A thread handling the response we get from the server.
     */
    public static void sendGetRequest(String url, Callback callback){
        Request request = new Request.Builder().url(url).get().build();

        enqueueRequest(request, callback);
    }

    /**
     * Sends a POST request to the given URL without a request body.
     */
    public static void sendPostRequest(String url, RequestBody requestBody, Callback callback){
        Request request = new Request.Builder().url(url).post(requestBody).build();

        enqueueRequest(request, callback);
    }

    /**
     * Sends a POST request to the given URL without a request body.
     */
    public static void sendPostRequest(String url, Callback callback){
        RequestBody requestBody = RequestBody.create(null, new byte[0]);
        Request request = new Request.Builder().url(url).post(requestBody).build();

        enqueueRequest(request, callback);
    }

    public static void sendPutRequest(String url, Callback callback, RequestBody requestBody){
        Request request = new Request.Builder().url(url).put(requestBody).build();

        enqueueRequest(request, callback);
    }

    public static void sendDeleteRequest(String url, Callback callback){
        Request request = new Request.Builder().url(url).delete().build();

        enqueueRequest(request, callback);
    }

    private static void enqueueRequest(Request request, Callback callback){
        System.out.println("New Request for: " + request.url());


        Call call = HTTP_CLIENT.newCall(request);

        call.enqueue(callback);
    }
}
