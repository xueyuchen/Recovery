package com.example.app.recovery.service;

/**
 * Created by app on 16/5/29.
 */
public class PaperService {
    /**
     * 获取残值单号
     * @return
     */
    public final static String getNewPaperCode() {
        return String.valueOf(Math.random() * 100);
    }
}
