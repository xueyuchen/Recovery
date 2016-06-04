package com.example.app.recovery.http;


import android.content.Context;
import com.example.app.recovery.utils.ProgressManager;

public class HttpResponseHandler {
//	private final String TAG="ResponseHandler";

	private Context mContext;
	private boolean showDialog;
	private Class<?> responseClass;
	private HttpManager httpManager;

	public Context getContext(){
		return mContext;
	}

	public Class<?> getResponseClass(){
		return responseClass;
	}
	
	/**
	 *if context is null ,http request can not pass auth,and if there is activity,set context activity.
	 */
	public HttpResponseHandler(Context context,boolean showDialog,HttpManager httpManager){
		this.mContext=context;
		this.showDialog=showDialog;
		this.httpManager=httpManager;
	}

	/**
	 *context can not be null.
	 */
	public HttpResponseHandler(Context context,boolean showDialog,HttpManager httpManager,Class<?> responseClass){
		this.mContext=context;
		this.showDialog=showDialog;
		this.responseClass=responseClass;
		this.httpManager=httpManager;
	}

	public void onStart() {
		if(showDialog){
			ProgressManager.getInstance().showProgress(mContext,new ProgressManager.DialogCancelListener() {
                
                @Override
                public void onCancel() {
                    if(httpManager!=null){
                      httpManager.cancel();
                   }
                }
            },false);
		}
	}

//	/**
//	 * always be called while success.
//	 * @param statusCode
//	 * @param headers
//	 * @param responseBody
//	 */
//	public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//		if(responseBody==null)
//			responseBody=new byte[]{};
//		String response=new String(responseBody);
//		MLog.i(TAG, "onSuccess statusCode="+statusCode+"\nresponseBody length="+responseBody.length+"\nresponse=\n"+response);
//	}

	/**
	 *  always be called while success.
	 * @param json
	 */
	public void onSuccess(String json){
	    ProgressManager.getInstance().cancelProgress();
	}
	
	/**
	 * this method just be called when the response object is not null in ResponseHandler.
	 * @param object
	 */
	public void onSuccess(Object object) {
	    ProgressManager.getInstance().cancelProgress();
	}

//	public void onFailure(String json){
//	}

	public void onFailure(int statusCode,String code,String msg,String json){
	    ProgressManager.getInstance().cancelProgress();
	}
//	public void onFailure(int status,int result,String msg){
//		MLog.i(TAG, "onFailure statusCode="+status+" result="+result+" msg="+msg);
//	}
	
//	public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//		if(responseBody==null)
//			responseBody=new byte[]{};
//		if(error==null)
//			error=new Throwable();
//		MLog.i(TAG, "onFailure statusCode="+statusCode+"\nresponseBody length="+responseBody.length+"\nerror message=\n"+error.getMessage());
//	}

	public void onProgress(int bytesWritten, int totalSize) {
//		if(totalSize>1)
//		MLog.i(TAG, "onProgress complete "+(int)(100*(float)bytesWritten/totalSize)+"% bytesWritten="+bytesWritten/1024f+" kb, totalSize="+totalSize/1024f+"kb");
	}

	public void onCancel() {
//		MLog.i(TAG, "onCancel");
	}

	public void onRetry(int retryNo) {
//		MLog.i(TAG, "retryNo "+retryNo);
	}

	public void onFinish() {
//		MLog.i(TAG, "onFinish");
	    ProgressManager.getInstance().cancelProgress();
	}

	

	
}
