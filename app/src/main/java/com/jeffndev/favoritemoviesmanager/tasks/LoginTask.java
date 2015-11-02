package com.jeffndev.favoritemoviesmanager.tasks;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by jeffreynewell1 on 11/1/15.
 */
public class LoginTask extends AsyncTask<String, Void, Boolean > {
    private final String LOG_TAG = LoginTask.class.getSimpleName();

    private LoginTaskCallback mCaller;
    //private String mUserName;
    private String mSessionId;
    private int mUserId;

    public interface LoginTaskCallback {
        void onLoginSessionNegotiated(Boolean succeeded, String sessionId, Integer apiUserId);
    }
    public LoginTask(LoginTaskCallback caller){
        mCaller = caller;
    }
    @Override
    protected Boolean doInBackground(String... params) {
        String uname = params[0];
        String pword = params[1];
        //temporary:
        //mUserName = uname;
        final String BASE_URL = "https://api.themoviedb.org/3/";
        //final String BASE_URL = "http://api.themoviedb.org/3/";
        final String NEW_AUTH_TOKEN_METHOD = "authentication/token/new";
        final String API_LOGIN_METHOD = "authentication/token/validate_with_login";
        final String NEW_LOGIN_SESSION_METHOD = "authentication/session/new";
        final String GET_USER_ACCOUNT_METHOD = "account";
        final String API_KEY_PARAM = "api_key";
        final String API_KEY_VALUE = "21ac243915da272c3c6b92a9958a3650";
        final String REQUEST_TOKEN_PARAM = "request_token";
        final String SESSION_ID_PARAM = "session_id";
        final String USER_NAME_PARAM = "username";
        final String PASSWORD_PARAM = "password";

        BufferedReader reader = null;
        HttpsURLConnection urlConnection = null;
        String authKeyJson = null;
        String authToken = null;
        Uri builtUri = Uri.parse(BASE_URL+NEW_AUTH_TOKEN_METHOD).buildUpon()
                .appendQueryParameter(API_KEY_PARAM, API_KEY_VALUE)
                .build();
        try {
            URL url = new URL(builtUri.toString());
            urlConnection = (HttpsURLConnection)url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if(inputStream == null){
                Log.e(LOG_TAG, "INPUT STREAM from http request returned nothing");
                return false;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String ln;
            while((ln = reader.readLine()) != null){
                buffer.append(ln + "\n");
            }
            if(buffer.length() == 0){
                Log.e(LOG_TAG, "empty buffer from http request, no data found");
                return false;
            }
            authKeyJson = buffer.toString();
        }catch(IOException e){
            Log.e(LOG_TAG, "Error ", e);
            return false;
        }
        try {
            JSONObject authKeyObject = new JSONObject(authKeyJson);
            if(!authKeyObject.getBoolean("success")){
                Log.e(LOG_TAG, "JSON RETURNED unsuccessful result");
                return false;
            }
            authToken = authKeyObject.getString("request_token");
        }catch(JSONException e){
            Log.e(LOG_TAG, "Error: ", e);
            return false;
        }
        //PHASE 2:
        builtUri = Uri.parse(BASE_URL+API_LOGIN_METHOD).buildUpon()
                .appendQueryParameter(API_KEY_PARAM, API_KEY_VALUE)
                .appendQueryParameter(REQUEST_TOKEN_PARAM, authToken)
                .appendQueryParameter(USER_NAME_PARAM, uname)
                .appendQueryParameter(PASSWORD_PARAM, pword)
                .build();

        String loginValidateJson = null;
        String recheckAuthKey = null;
        try {
            URL url = new URL(builtUri.toString());
            urlConnection = (HttpsURLConnection)url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if(inputStream == null){
                Log.e(LOG_TAG, "INPUT STREAM from http request returned nothing");
                return false;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String ln;
            while((ln = reader.readLine()) != null){
                buffer.append(ln + "\n");
            }
            if(buffer.length() == 0){
                Log.e(LOG_TAG, "empty buffer from http request, no data found");
                return false;
            }
            loginValidateJson = buffer.toString();
        }catch(IOException e){
            Log.e(LOG_TAG, "Error ", e);
            return false;
        }
        try {
            JSONObject loginValidateObject = new JSONObject(loginValidateJson);
            if(!loginValidateObject.getBoolean("success")){
                Log.e(LOG_TAG, "JSON RETURNED unsuccessful result");
                return false;
            }
            recheckAuthKey = loginValidateObject.getString("request_token");
            if(!recheckAuthKey.equals(authToken)) {
                Log.e(LOG_TAG, "login validation token returned not the same, aborting login");
                return false;
            }
        }catch(JSONException e){
            Log.e(LOG_TAG, "Error: ", e);
            return false;
        }
        //PHASE 3: final login setup, get a session id

        builtUri = Uri.parse(BASE_URL + NEW_LOGIN_SESSION_METHOD).buildUpon()
                .appendQueryParameter(API_KEY_PARAM, API_KEY_VALUE)
                .appendQueryParameter(REQUEST_TOKEN_PARAM, authToken)
                .build();
        String loginSessionJson = null;
        String sessionId = null;
        try {
            URL url = new URL(builtUri.toString());
            urlConnection = (HttpsURLConnection)url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if(inputStream == null){
                Log.e(LOG_TAG, "INPUT STREAM from http request returned nothing");
                return false;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String ln;
            while((ln = reader.readLine()) != null){
                buffer.append(ln + "\n");
            }
            if(buffer.length() == 0){
                Log.e(LOG_TAG, "empty buffer from http request, no data found");
                return false;
            }
            loginSessionJson = buffer.toString();
        }catch(IOException e){
            Log.e(LOG_TAG, "Error ", e);
            return false;
        }
        try {
            JSONObject loginSessionObject = new JSONObject(loginSessionJson);
            if(!loginSessionObject.getBoolean("success")){
                Log.e(LOG_TAG, "JSON RETURNED unsuccessful result");
                return false;
            }
            sessionId = loginSessionObject.getString(SESSION_ID_PARAM);
        }catch(JSONException e){
            Log.e(LOG_TAG, "Error: ", e);
            return false;
        }
        //PHASE 4:
        int userId = -1;
        String userAccountJson = null;
        builtUri = Uri.parse(BASE_URL+GET_USER_ACCOUNT_METHOD).buildUpon()
                .appendQueryParameter(API_KEY_PARAM, API_KEY_VALUE)
                .appendQueryParameter(SESSION_ID_PARAM, sessionId)
                .build();
        try {
            URL url = new URL(builtUri.toString());
            urlConnection = (HttpsURLConnection)url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if(inputStream == null){
                Log.e(LOG_TAG, "INPUT STREAM from http request returned nothing");
                return false;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String ln;
            while((ln = reader.readLine()) != null){
                buffer.append(ln + "\n");
            }
            if(buffer.length() == 0){
                Log.e(LOG_TAG, "empty buffer from http request, no data found");
                return false;
            }
            userAccountJson = buffer.toString();
        }catch(IOException e){
            Log.e(LOG_TAG, "Error ", e);
            return false;
        }
        try {
            JSONObject userAccountObject = new JSONObject(userAccountJson);

            userId = userAccountObject.getInt("id");
        }catch(JSONException e){
            Log.e(LOG_TAG, "Error: ", e);
            return false;
        }
        //SET THE DATA, done...
        mSessionId = sessionId;
        mUserId = userId;
        return true;
    }

    @Override
    protected void onPostExecute(Boolean loginSucceeded) {
        super.onPostExecute(loginSucceeded);

        if(mCaller != null) mCaller.onLoginSessionNegotiated(loginSucceeded, mSessionId, mUserId);
    }
}
