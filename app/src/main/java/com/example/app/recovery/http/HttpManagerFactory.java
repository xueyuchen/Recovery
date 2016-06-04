package com.example.app.recovery.http;

import java.util.ArrayList;
import java.util.Iterator;

import com.example.app.recovery.utils.MLog;
import android.support.v4.app.Fragment;

public class HttpManagerFactory {
	private final String TAG=getClass().getSimpleName();
	
	private static HttpManagerFactory httpManagerFactory;
	private HttpManagerFactory(){}
	public static synchronized HttpManagerFactory getInstance(){
		if(httpManagerFactory==null){
			httpManagerFactory=new HttpManagerFactory();
		}
		return httpManagerFactory;
	}
	
	private ArrayList<HttpManager> managerList=null;
	
	public HttpManager getHttpManager(){
		HttpManager httpManager=new HttpManager();
		addManager(httpManager);
		return httpManager;
	}
	
	private void addManager(HttpManager httpManager){
		if(managerList==null)
			managerList=new ArrayList<HttpManager>();
		managerList.add(httpManager);
		MLog.d(TAG, "add httpmanager,current managers number="+managerList.size());
	}
	
	public HttpManager getHttpManager(Fragment fragment){
		HttpManager httpManager=new HttpManager(fragment);
		addManager(httpManager);
		return httpManager;
	}
	
	/**
	 * cancel requests of the fragment.
	 * 
	 * @param fragment
	 */
	public void cancelRequestByFragment(Fragment fragment){
		if(managerList!=null){
			Iterator<HttpManager> iterator=managerList.iterator();
			while(iterator.hasNext()){
				HttpManager httpManager=iterator.next();
				if(httpManager!=null&&httpManager.getFragment()!=null&&httpManager.getFragment()==fragment){
				    httpManager.cancel();
					iterator.remove();
				}
			}
		}
	}
	
	public void cancelManager(HttpManager httpManager){
		if(httpManager!=null){
			httpManager.cancel();
			managerList.remove(httpManager);
			httpManager=null;
		}
		MLog.d(TAG, "cancel httpmanager,current managers number="+managerList.size());
	}
	
	/**
	 * cancel all requests
	 */
	public void cancelAllRequests(){
		if(managerList!=null){
            Iterator<HttpManager> iterator=managerList.iterator();
            while(iterator.hasNext()){
                HttpManager httpManager=iterator.next();
                if(httpManager!=null){
                    httpManager.cancel();
                    iterator.remove();
                }
            }
        }
	}
	
}
