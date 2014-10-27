package com.eric.playdemobusinessnetworking.collection_view;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eric.playdemobusinessnetworking.R;

public abstract class DbListViewFragment<T> extends Fragment {
	protected DbListView<T> dbListView;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_db_listview,
				container, false);

        dbListView =  (DbListView<T>) rootView.findViewById(R.id.lvDbList);
        dbListView.setCallbacks(getCollectionViewCallbacks());
        return rootView;
	}


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
    }

    protected abstract DbListViewCallbacks<T> getCollectionViewCallbacks();

    protected void fetchData() {
		dbListView.loadFirstRound();
	}

}
