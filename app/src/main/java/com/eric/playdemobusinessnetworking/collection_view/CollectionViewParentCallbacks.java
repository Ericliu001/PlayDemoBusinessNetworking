package com.eric.playdemobusinessnetworking.collection_view;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

public interface CollectionViewParentCallbacks <T>{
	        View newCollectionItemView(Context context, ViewGroup parent, T data);
	        void bindCollectionItemView(Context context, View view, int position, T data);
	        void refreshTotalItemInList(int size);
}
