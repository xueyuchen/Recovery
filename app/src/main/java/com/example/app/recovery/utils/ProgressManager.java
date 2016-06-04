package com.example.app.recovery.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;

public class ProgressManager {
    
    private  ProgressDialog progressDialog=null;
    private static ProgressManager instance;
    private ProgressManager(){
    };
    
    public static ProgressManager getInstance(){
        if(instance==null){
            instance=new ProgressManager();
        }
        return instance;
    }
    
    public void showProgress(Context mContext,final DialogCancelListener listener,boolean cancelable){
        cancelProgress();
        progressDialog=new ProgressDialog(mContext);
        progressDialog.setMessage("加载中...");
        progressDialog.setCancelable(cancelable);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface arg0) {
                 if(listener!=null&&progressDialog!=null){
                     listener.onCancel();
                 }
            }
        });
        progressDialog.show();
    }

    public void cancelProgress(){
        if(progressDialog!=null){
            progressDialog.dismiss();
            progressDialog=null;
        }
    }
    
    public interface DialogCancelListener{
        public void onCancel();
    }
}
