package brycewedig.integration.helpers;

import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import static brycewedig.integration.helpers.TogglConstants.*;

public class TogglAuthInterceptor implements Interceptor {

    private final String token;

    public TogglAuthInterceptor(String token) {
        this.token = token;
    }

    @NotNull
    public Response intercept(Interceptor.Chain chain) throws IOException {

        String credential = Credentials.basic(token, "api_token");

        Request originalRequest = chain.request();
        Request requestWithUserAgent = originalRequest
                .newBuilder()
                .header("Authorization", credential)
                .header(CONTENT_TYPE, APPLICATION_JSON)
                .build();

        return chain.proceed(requestWithUserAgent);
    }
}
