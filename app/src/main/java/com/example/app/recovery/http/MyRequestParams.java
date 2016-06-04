package com.example.app.recovery.http;

import com.loopj.android.http.RequestParams;

import java.util.List;
import java.util.Map;

import org.apache.http.message.BasicNameValuePair;

/**
 * RequestParamsの内部情報にアクセスするためのラッパークラス。
 */
public class MyRequestParams extends RequestParams {

    private static final long serialVersionUID = 1L;

    public MyRequestParams() {
        super();
    }

    public MyRequestParams(Map<String, String> source) {
        super(source);
    }

    protected List<BasicNameValuePair> getParamsList() {
        return super.getParamsList();
    }
}
