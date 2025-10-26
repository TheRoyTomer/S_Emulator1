package Client_UTILS;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.OkHttpClient;

public class ClientConstants
{
    public static final Gson GSON = new GsonBuilder()
            .registerTypeAdapterFactory(new OptionalTypeAdapterFactory())
            .create();

    public static final OkHttpClient HTTP_CLIENT = new OkHttpClient.Builder()
            .cookieJar(new SimpleCookieManager())
            .build();
    public static final String SERVER_URL = "http://localhost:8080/web_war";

    public static final int REFRESH_RATE = 2000;







}
