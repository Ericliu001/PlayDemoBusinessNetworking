package com.eric.playdemobusinessnetworking.collection_view;

import android.content.Context;
import android.util.AttributeSet;

import java.util.List;

public class DbListView<T> extends CollectionViewParent<T> {

	
	
	public DbListView(Context context) {
		this(context, null);
	}
	
	public DbListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}


	
	
	
	protected void loadMoreResult(int skip, int top){
    	if ( ! (mCallbacks instanceof DbListViewCallbacks)) {
			try {
				throw new Exception("You must pass in an instance of DbListViewCallbacks.");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
    	
        if (mCallbacks != null && isLoading == false){


            isLoading = true;
//            hasError = false;
            List<T> loadList = ((DbListViewCallbacks<T>)mCallbacks).loadMoreFromDB(skip, top);
            
            serverListSize = ((DbListViewCallbacks<T>)mCallbacks).getDBListSize();
            appendToDataList(loadList);
        }
    }
	
}
