package com.example.farmshop;

import android.os.Build;

import androidx.annotation.RequiresApi;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Base64;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Network {

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String getAccessToken() throws JSONException, IOException {
        // Constructing the credentials for authentication
        String key = SandBox.consumerKey + ":" + SandBox.consumerSecret;
        byte[] bytes = key.getBytes("ISO-8859-1");
        String encoded = Base64.getEncoder().encodeToString(bytes);

        // Creating an OkHttpClient instance
        OkHttpClient client = new OkHttpClient();

        // Building the request to obtain access token
        Request request = new Request.Builder()
                .url(SandBox.access_token_url)
                .get()
                .addHeader("authorization", "Basic "+encoded)
                .addHeader("cache-control", "no-cache")
                .build();

        // Executing the request and parsing the response to get the access token
        Response response = client.newCall(request).execute();
        JSONObject jsonObject = new JSONObject(response.body().string());
        System.out.println(jsonObject.getString("access_token"));
        return jsonObject.getString("access_token");
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String getRequest(String requestJson, String url) throws JSONException, IOException {
        // Creating an OkHttpClient instance
        OkHttpClient client = new OkHttpClient();

        // Setting the media type and request body for the POST request
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, requestJson);

        // Building the request for making a POST request
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("content-type", "application/json")
                .addHeader("authorization", "Bearer "+getAccessToken())
                .addHeader("cache-control", "no-cache")
                .build();

        // Executing the request and processing the response
        Response response = client.newCall(request).execute();
        String s = response.body().string();
        System.out.println(s);

        // Parsing the response and updating Result class fields
        JSONObject jsonObject = new JSONObject(s);
        if (jsonObject.getString("ResponseCode").equals("0")) {
            Result.ResponseCode = jsonObject.getString("ResponseCode");
            Result.MerchantRequestID = jsonObject.getString("MerchantRequestID");
            Result.CheckoutRequestID = jsonObject.getString("CheckoutRequestID");
            Result.ResponseDescription = jsonObject.getString("ResponseDescription");
            Result.CustomerMessage = jsonObject.getString("CustomerMessage");
        } else {
            Result.ResponseCode = "1";
        }

        // Returning the response body as a string
        return response.body().toString();
    }
}
