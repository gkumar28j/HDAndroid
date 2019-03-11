package com.mcn.honeydew.data.network;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by amit on 15/2/18.
 */

@Singleton
public class ApiInterceptor implements Interceptor {

    private static final String TAG = "ApiInterceptor";

    private ApiHeader mApiHeader;

    @Inject
    public ApiInterceptor(final ApiHeader header) {
        mApiHeader = header;
    }

    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {
        final Request request = chain.request();
        final Request.Builder builder = request.newBuilder();
        String apiAuthType = request.header(ApiHeader.API_AUTH_TYPE);
        if (apiAuthType == null) {
            apiAuthType = ApiHeader.PROTECTED_API;
        }

         switch (apiAuthType) {
            case ApiHeader.PROTECTED_API:
                builder.addHeader(ApiHeader.HEADER_PARAM_API_KEY, mApiHeader.getmApiKey());
                builder.addHeader(ApiHeader.HEADER_PARAM_AUTHRIZATION, mApiHeader.getmTokenType() + " " + mApiHeader.getmAccessToken());
                builder.addHeader(ApiHeader.HEADER_PARAM_REFRESH_TOKEN, mApiHeader.getmRefreshToken());
            case ApiHeader.PUBLIC_API:
            default:
                builder.addHeader(ApiHeader.HEADER_PARAM_API_KEY, mApiHeader.getmApiKey());
        }

        Response response = chain.proceed(builder.build());
        String newAccessToken = response.header("access_token");
        String newRefreshToken = response.header("refresh_token");
        String newTokenType = response.header("token_type");
        if (newAccessToken != null) {

            //  mApiHeader.setmAccessToken();
        }
        return response;
    }
}

