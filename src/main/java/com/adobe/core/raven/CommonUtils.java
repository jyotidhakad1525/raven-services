package com.adobe.core.raven;

import okhttp3.*;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

public class CommonUtils {

    public static String convertToMD5(String text) throws NoSuchAlgorithmException {
        //CommonFunctions.text = text;

        MessageDigest m = MessageDigest.getInstance("MD5");
        m.reset();
        m.update(text.getBytes());
        byte[] digest = m.digest();
        BigInteger bigInt = new BigInteger(1,digest);
        String hashtext = bigInt.toString(16);
        // Now we need to zero pad it if you actually want the full 32 chars.
        while(hashtext.length() < 32 ){
            hashtext = "0"+hashtext;
        }

        return hashtext;
    }


    public static String callPostUrl(String url, String bodyRequest, String authorizationKey) throws IOException {

        OkHttpClient client = new OkHttpClient();
        client.newBuilder().callTimeout(2, TimeUnit.MINUTES).readTimeout(2, TimeUnit.MINUTES);

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, bodyRequest);
        Request request = null;
        if(authorizationKey != null) {
            request = new Request.Builder().url(url).post(body).addHeader("authorization", authorizationKey)
                    .addHeader("Content-Type", "application/json").addHeader("cache-control", "no-cache").build();
        } else {

            request = new Request.Builder().url(url).post(body).addHeader("Content-Type", "application/json")
                    .addHeader("cache-control", "no-cache").build();
        }
        Response response = client.newCall(request).execute();
        String workfrontResponseStr = response.body().string();

        return workfrontResponseStr;
    }
}
