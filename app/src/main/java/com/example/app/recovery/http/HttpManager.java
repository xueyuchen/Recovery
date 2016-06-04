package com.example.app.recovery.http;

import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.example.app.recovery.tools.ApiConstants;
import com.example.app.recovery.utils.MLog;
import com.example.app.recovery.utils.ProgressManager;
import com.example.app.recovery.utils.Tools;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.MySSLSocketFactory;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;

public class HttpManager {
    private final String TAG = "HttpUtil";

    private final int HTTP_TIMEOUT = 60000;

    private AsyncHttpClient client = null;

    public HttpManager() {
        init();
    }

    private Fragment fragment;

    public HttpManager(Fragment fragment) {
        init();
        this.fragment = fragment;
    }

    public Fragment getFragment() {
        return fragment;
    }

    private void init() {
        client = new AsyncHttpClient();
        client.setTimeout(HTTP_TIMEOUT);
        client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
    }

    public AsyncHttpClient getClient() {
        return client;
    }

    private boolean isCanceled = false;

    public void cancel() {
        isCanceled = true;
    }

    public void active() {
        isCanceled = false;
    }

    public boolean isCanceled() {
        return isCanceled;
    }

    private static final byte POST = 0;
    private static final byte GET = 1;
    private static final byte DELETE = 2;
    private static final byte PUT = 3;

    /**
     * http post request,default for need deal the response.
     *
     * @param context         the context of the request,used for cancel request by context.
     * @param url             the request url.
     * @param params          parameters for the request.
     * @param responseHandler handler of response.
     */
    public void post(String url, RequestParams params, final HttpResponseHandler responseHandler) {
        request(POST, true, url, params, responseHandler);
    }

    /**
     * http get request,default for need deal the response.
     *
     * @param context         the context of the request,used for cancel request by context.
     * @param url             the request url.
     * @param params          parameters for the request.
     * @param responseHandler handler of response.
     */
    public void get(String url, RequestParams params, final HttpResponseHandler responseHandler) {
        request(GET, true, url, params, responseHandler);
    }

    /**
     * http post request
     * @param context the context of the request,used for cancel request by context.
     * @param needDealResponse if this request need deal the response after get the response json.
     * @param url the request url.
     * @param params parameters for the request.
     * @param responseHandler handler of response.
     */
//	public void post(boolean needDealResponse,String url,RequestParams params,final HttpResponseHandler responseHandler){
//		request(POST, needDealResponse, url, params, responseHandler);
//	}

    /**
     * http get request
     *
     * @param context          the context of the request,used for cancel request by context.
     * @param needDealResponse if this request need deal the response after get the response json.
     * @param url              the request url.
     * @param params           parameters for the request.
     * @param responseHandler  handler of response.
     */
//	public void get(boolean needDealResponse,String url,RequestParams params,final HttpResponseHandler responseHandler){
//		request(GET, needDealResponse, url, params, responseHandler);
//	}
    public void put(String url, RequestParams params, final HttpResponseHandler responseHandler) {
        request(PUT, true, url, params, responseHandler);
    }

    public void delete(String url, RequestParams params, final HttpResponseHandler responseHandler) {
        request(DELETE, true, url, params, responseHandler);
    }

    /**
     * Async http request method.
     *
     * @param method           request method post,get,delete,put.
     * @param needDealResponse if this request need deal the response after get the response json.
     * @param url              the request url.
     * @param params           parameters for the request.
     * @param responseHandler  handler of response,must not be null.
     */
    public void request(byte method, final boolean needDealResponse, final String url, final RequestParams params, final HttpResponseHandler responseHandler) {
        if (responseHandler == null || responseHandler.getContext() == null) {
            return;
        }

        final Context context = responseHandler.getContext();

        //set user agent to client.
//        String ua = getUserAgent(context);
//        if (TextUtils.isEmpty(ua)) {
//            ua = setUserAgent(context);
//        }
//        client.setUserAgent(ua);

        //restore cookie informations while it's not sign activity and user had login.
//	    if(!(context instanceof SignActivity)&&LoginUserInfo.getInstance(context).isLogin()){
//	        MCookieManager.getInstance().restoreCookie(context, client);
//	    }

        active();

        if (checkNetWork(url, responseHandler)) {
            return;
        }

        String methodName = null;
        switch (method) {
            case POST:
                methodName = "POST";
                break;
            case GET:
                methodName = "GET";
                break;
            case PUT:
                methodName = "PUT";
                break;
            case DELETE:
                methodName = "DELETE";
                break;
        }
        MLog.i(TAG, "Method =" + methodName);
        MLog.i(TAG, "Request url=" + url);
        if (params == null) {
            MLog.i(TAG, "Request Params=");
        } else {
            MLog.i(TAG, "Request Params=" + params.toString());
        }

        AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                if (isCanceled) {
                    MLog.d(TAG, "onStart but is canceled.");
                    ProgressManager.getInstance().cancelProgress();
                    return;
                }
                MLog.d(TAG, "onStart");
                responseHandler.onStart();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (isCanceled) {
                    MLog.d(TAG, "onSuccess but is canceled.");
                    ProgressManager.getInstance().cancelProgress();
                    return;
                }

                MLog.d(TAG, "onSuccess statusCode＝" + statusCode);

                if (responseBody == null || responseBody.length == 0) {
                    responseBody = new byte[]{};
                }

                String json = new String(responseBody);
                MLog.i(TAG, "response=" + json);

                //not need deal with response ,return directly.
                if (!needDealResponse) {
                    responseHandler.onSuccess(json);
                } else {
                    if (responseHandler.getResponseClass() != null) {
                        Object object = new Gson().fromJson(json, responseHandler.getResponseClass());
                        responseHandler.onSuccess(object);
                    }
                    responseHandler.onSuccess(json);
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                if (isCanceled) {//if the http request is canceled return directly.
                    MLog.d(TAG, "onFailure but is canceled.");
                    ProgressManager.getInstance().cancelProgress();
                    return;
                }

                MLog.i(TAG, "onFailure statusCode=" + statusCode);
                if (responseBody == null || responseBody.length == 0) {
                    responseBody = new byte[]{};
                }

                String json = new String(responseBody);
                MLog.i(TAG, "onFailure message=" + json);

                if (error != null) {
                    error.printStackTrace();
                }

                //if status code is 0,maybe is local error.
                if (statusCode == 0) {
                    if (error != null) {
                        if (error instanceof SocketTimeoutException) {
                            statusCode = ErrorCode.SOCKET_TIMEOUT;
                        }
                    }
                    //show network error dialog.
                    if (checkNetWork(url, responseHandler)) {
                        return;
                    }
                } else if (statusCode == ErrorCode.AUTH_FAILED_CODE) {//auth failed
//                    if (context instanceof MainActivity) {
//                        MLog.e(TAG, "401 auth failed,url=" + url + " params=" + (params == null ? "" : params.toString()));
//                        String msg = null;
//                        if (!TextUtils.isEmpty(json)) {
//                            try {
//                                JSONObject jsonObject = new JSONObject(json);
//                                msg = jsonObject.getString("message");
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                        showAuthFailed((Activity) context, msg);
//                        return;
//                    } else if (context instanceof SignActivity) {//this will not happen except that server with wrong logic.
//                        clearLoiginCache(context);
//                    } else {
//                        logout(context);
//                        return;
//                    }
                } else {
                    //if the content is html source,it is server error.
                    if (!TextUtils.isEmpty(json) && json.contains("<head>") && json.contains("</head>")) {
                        showErrorDialog("出错了!", responseHandler);
                        return;
                    }
                }
                ;

                //if no need to deal response or json is empty,call back onFailure directly.
                if (!needDealResponse) {
                    responseHandler.onFailure(statusCode, null, null, json);
                } else {
                    //check if response is empty.
                    if (TextUtils.isEmpty(json)) {
                        showErrorDialog("无返回值", responseHandler);
                        responseHandler.onFailure(statusCode, null, null, json);
                    } else {
                        //parse json
                        String msg = null;
                        String code = null;
                        String errors = null;

                        try {
                            JSONObject jsonObject = new JSONObject(json);
                            msg = jsonObject.getString("message");
                            code = jsonObject.getString("code");
                            if (jsonObject.has("errors")) {
                                errors = jsonObject.getString("errors");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //check if there is errors message
                        if (!TextUtils.isEmpty(errors)) {
                            //if there is errors get the first error message and show.
                            try {
                                JSONArray jsonArray = new JSONArray(errors);
                                if (jsonArray.length() > 0) {
                                    JSONObject object = jsonArray.getJSONObject(0);
                                    String message = object.getString("message");
                                    showErrorDialog(message, responseHandler);
                                } else {
                                    showErrorDialog(TextUtils.isEmpty(msg) ? json : msg, responseHandler);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                showErrorDialog(TextUtils.isEmpty(msg) ? json : msg, responseHandler);
                            }
                        } else {
                            showErrorDialog(TextUtils.isEmpty(msg) ? json : msg, responseHandler);
                        }

                        responseHandler.onFailure(statusCode, code, msg, json);
                    }

                }

            }

            @Override
            public void onProgress(int bytesWritten, int totalSize) {
                if (isCanceled) {
                    MLog.d(TAG, "onProgress but is canceled.");
                    ProgressManager.getInstance().cancelProgress();
                    return;
                }
                responseHandler.onProgress(bytesWritten, totalSize);
            }

            @Override
            public void onCancel() {
                if (isCanceled) {
                    MLog.d(TAG, "onCancel but is canceled.");
                    ProgressManager.getInstance().cancelProgress();
                    return;
                }
                MLog.d(TAG, "onCancel");
                responseHandler.onCancel();
            }

            @Override
            public void onRetry(int retryNo) {
                if (isCanceled) {
                    MLog.d(TAG, "onRetry but is canceled.");
                    ProgressManager.getInstance().cancelProgress();
                    return;
                }

                MLog.d(TAG, "onRetry");
                responseHandler.onRetry(retryNo);
            }

            @Override
            public void onFinish() {
                if (isCanceled) {
                    MLog.d(TAG, "onFinish but is canceled.");
                    ProgressManager.getInstance().cancelProgress();
                    return;
                }
                MLog.d(TAG, "onFinish");
                responseHandler.onFinish();
            }
        };

//        String currentTime = String.valueOf(System.currentTimeMillis() / 1000);
//        client.addHeader("X-MOTTAINAI-TRAN-TIME", currentTime);
//        client.addHeader("X-MOTTAINAI-TOKEN", createHash(url, params, currentTime));

        switch (method) {
            case POST:
                client.post(url, params, handler);
                break;
            case GET:
                client.get(url, params, handler);
                break;
            case DELETE:
                client.delete(url, handler);
                break;
            case PUT:
                client.put(url, params, handler);
                break;
        }

    }

//    private String createHash(String urlString, RequestParams params, String currentTime) {
//        try {
//            URL url = new URL(urlString);
//
//            StringBuilder hashSource = new StringBuilder();
//            hashSource.append(url.getPath());
////            hashSource.append(ApiConstants.VALIDATION_SALT);
//            hashSource.append(currentTime);
//            hashSource.append(createSortedParamString(params));
//
//            String hash = toSha256(hashSource.toString());
//            MLog.d(TAG, hash);
//            return hash;
//        } catch (MalformedURLException e) {
//            return null;
//        }
//    }

//    private String toSha256(String source) {
//        try {
//            MessageDigest md = MessageDigest.getInstance("SHA-256");
//            md.update(source.getBytes());
//            byte[] digest = md.digest();
//
//            StringBuilder hashString = new StringBuilder();
//            for (byte b : digest) {
//                hashString.append(String.format("%02x", b));
//            }
//            return hashString.toString();
//        } catch (NoSuchAlgorithmException e) {
//            // 通常の端末であれば発生しないはずなので、そのまま投げる
//            throw new RuntimeException(e);
//        }
//    }
//
//    private String createSortedParamString(RequestParams params) {
//        if (params == null) {
//            return "";
//        }
//
//        List<BasicNameValuePair> rawParams = ((MyRequestParams) params).getParamsList();
//        Collections.sort(rawParams, new Comparator<BasicNameValuePair>() {
//            @Override
//            public int compare(BasicNameValuePair lhs, BasicNameValuePair rhs) {
//                return lhs.getName().compareTo(rhs.getName());
//            }
//        });
//
//        StringBuilder queryString = new StringBuilder();
//        for (BasicNameValuePair pair : rawParams) {
//            if (queryString.length() != 0) {
//                queryString.append("&");
//            }
//            queryString.append(pair.getName() + "=" + pair.getValue());
//        }
//        return queryString.toString();
//    }

    private void showErrorDialog(String message, HttpResponseHandler responseHandler) {
        AlertDialog alert = new AlertDialog.Builder(responseHandler.getContext()).create();
        alert.setMessage(message);
        alert.show();
    }

    private boolean checkNetWork(String url, HttpResponseHandler responseHandler) {
        if (responseHandler != null &&
                responseHandler.getContext() != null &&
                !Tools.isNetworkConnected(responseHandler.getContext())) {
            String message = "网络没连接!";
            responseHandler.onFailure(ErrorCode.NO_NETWORK, null, null, null);
            responseHandler.onFinish();
            final Context context = responseHandler.getContext();
            showErrorDialog(message, responseHandler);
//            if (context instanceof Activity) {
//                AlertBtnClickListener listener = null;
//                if (url.equals(ApiConstants.GOODS_METAS_VERSION_URL)) {
//                    listener = new AlertBtnClickListener() {
//
//                        @Override
//                        public void cancelClick(boolean dealCancel) {
//                        }
//
//                        @Override
//                        public void buttonClick(boolean isPositiveClick) {
//                            if (context instanceof BaseActivity) {
//                                ((BaseActivity) context).exitLogic();
//                            }
//                        }
//                    };
//                }
//                DialogManage.showErrorDialog((Activity) context, message, listener);
//            }
            return true;
        }
        return false;
    }

    /**
     * logout to sign
     *
     * @param context
     */
//    public static void logout(Context context) {
//        HttpManagerFactory.getInstance().cancelAllRequests();
//        clearLoiginCache(context);
//        Intent loginIntent = new Intent(context, SignActivity.class);
//        loginIntent.putExtra(SignActivity.TO_TAG, SignActivity.TO_LOGIN);
//        context.startActivity(loginIntent);
//    }

//    public static void clearLoiginCache(Context context) {
//        MCookieManager.getInstance().clearCookies(context);
//        LoginUserInfo.getInstance(context).clearLoginInfo();
//    }

//    public static void showAuthFailed(final Activity activity, String msg) {
//        if (TextUtils.isEmpty(msg)) {
//            msg = activity.getString(R.string.auth_failed);
//        }
//        HttpManagerFactory.getInstance().cancelAllRequests();
//        clearLoiginCache(activity);
//        DialogManageGCM.showAlertDialogSingleButton(Tools.getRootView(activity), R.string.error, msg, new DialogManageGCM.AlertBtnClickListener() {
//
//            @Override
//            public void cancelClick(boolean dealCancel) {
//
//            }
//
//            @Override
//            public void buttonClick(boolean isPositiveClick) {
//                logout(activity);
//            }
//        });
//    }

//    private String getUserAgent(Context context) {
//        String ua = SPUtil.getSPString(context, SPUtil.USER_AGENT);
//        MLog.i("useragent", ua);
//        return ua;
//    }

//    private String setUserAgent(Context context) {
////        WebView webview = new WebView(context);
////        webview.layout(0, 0, 0, 0);
////        WebSettings settings = webview.getSettings();
////        String ua = settings.getUserAgentString();
////        MLog.i("useragent", ua);
//        StringBuilder ua = new StringBuilder();
//        ua.append(context.getString(R.string.app_name));
//        ua.append(" app version:");
//        ua.append(Tools.getAppVersionName(context));
//        ua.append(" device:");
//        ua.append(android.os.Build.MODEL);
//        ua.append(" os version:");
//        ua.append(android.os.Build.VERSION.RELEASE);
//        SPUtil.setSPString(context, SPUtil.USER_AGENT, ua.toString());
//        return ua.toString();
//    }


//	public static void showMessage(final Activity activity,String msg){
//        DialogManageGCM.showAlertDialogSingleButton(Tools.getRootView(activity), 0, msg, new DialogManageGCM.AlertBtnClickListener() {
//            
//            @Override
//            public void cancelClick(boolean dealCancel) {
//                
//            }
//            
//            @Override
//            public void buttonClick(boolean isPositiveClick) {
//                if(activity  instanceof MainActivity){
//                     ((MainActivity)activity).check(R.id.radio_message);
//                }
//            }
//        });
//    }

}
