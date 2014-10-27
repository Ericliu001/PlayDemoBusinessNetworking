package com.eric.playdemobusinessnetworking.collection_view;

import java.util.List;

public interface DbListViewCallbacks<T> extends CollectionViewParentCallbacks<T>{
	List<T> loadMoreFromDB(int skip, int top);
	int getDBListSize();
}
