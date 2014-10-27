package com.eric.playdemobusinessnetworking.collection_view;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public abstract class CollectionViewParent<T>  extends ListView {

	public static final int NUMBER_OF_ITEMS_TO_LOAD = 20;

    // data List
    protected ArrayList<T> dataList = new ArrayList<T>();

    // The callback to supply View Layout and populate data into rows
    protected CollectionViewParentCallbacks mCallbacks = null;
    protected SwipeRefreshLayout swipeContainer;
    protected FrameLayout mFootContainer;

    protected MyListAdapter mListAdapter = null;

    protected static final int VIEW_TYPE_LOADING = 0;
    protected static final int VIEW_TYPE_ACTIVE = 1;
    protected int serverListSize = -1;
    protected boolean isLoading = false;

	protected boolean hasError = false;

	protected String errorMsg = "数据读取出现错误";
	

	
	
	protected abstract void loadMoreResult(int skip, int top);
	
	
	public CollectionViewParent(Context context) {
		this(context, null, 0);
	}
	
	public CollectionViewParent(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public CollectionViewParent(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);

        init();
	}

	private void init() {
		mListAdapter = new MyListAdapter();
        setAdapter(mListAdapter);
        setOnScrollListener(new MyScrollListener());
	}

	
	
	
	/*------------------------------------------*/
	
	

    public void refresh(){
	    dataList.clear();
	    serverListSize = -1;
	    hasError = false;
//	    mListAdapter = new MyListAdapter(); // new adapter
//	    setAdapter(mListAdapter);
	    mListAdapter.notifyDataSetChanged();
	}

	/**
     * User must call this method before using any methods of CollectionView,
     * Otherwise the CollectionView won't be able to display anything other than
     * 'reached the last row'
     * @param callbacks
     */
    public void setCallbacks(CollectionViewParentCallbacks callbacks){
        this.mCallbacks = callbacks;
    }
    
    public void setSwipeContainer(SwipeRefreshLayout swipe){
    	swipeContainer = swipe;
    }

   
    
    public interface IdCallback<T>{
    	int getId(T data);
    }
    
    public ArrayList<Integer> getAllIds(IdCallback<T> callback){
    	ArrayList<Integer> allIdList = new ArrayList<Integer>();
    	for(T t: dataList){
    		allIdList.add(callback.getId(t));
    	}
    	
    	return allIdList;
    }
    
    


   

    public void loadFirstRound(){
    	refresh();
    	loadMoreResult(0, NUMBER_OF_ITEMS_TO_LOAD);
    }

    protected class MyListAdapter extends BaseAdapter {


        @Override
        public boolean areAllItemsEnabled() {
            return false;
        }

        @Override
        public boolean isEnabled(int position) {
            return getItemViewType(position) == VIEW_TYPE_ACTIVE;
        }

        @Override
        public int getCount() {
            return dataList.size() + 1;
        }


        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public int getItemViewType(int position) {
            return (position >= dataList.size()) ? VIEW_TYPE_LOADING : VIEW_TYPE_ACTIVE;
        }

        @Override
        public T getItem(int position) {
            return (getItemViewType(position) == VIEW_TYPE_ACTIVE) ? dataList.get(position) : null;
        }

        @Override
        public long getItemId(int position) {
            return (getItemViewType(position) == VIEW_TYPE_ACTIVE) ? position
                    : -1;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {


        	
            if (getItemViewType(position) == VIEW_TYPE_LOADING){
                // indicate that the List is loading data
                return getFooterView(position, convertView, parent);
            }


            View dataRow =  getRowView(position, convertView, parent);


            return dataRow;
        }



    }

    private View getFooterView(int position, View convertView, ViewGroup parent) {
    	
    	if (hasError) {
			return getErrorView();
		}
    	
        if (position >= serverListSize && serverListSize >=0){
            // indicate that the list has reached the last row.
            return getLastRowView();
        }else {
            return getLoadingView();
        }




    }

    private View getErrorView() {
    	TextView tvError = new TextView(getContext());
        tvError.setText(errorMsg);
        tvError.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
        tvError.setGravity(Gravity.CENTER);
        
        FrameLayout footContainer = new FrameLayout(getContext());
        footContainer.addView(tvError);
        
        mFootContainer = footContainer;
        return footContainer;
	}

	private View getLoadingView() {
        ProgressBar bar = new ProgressBar(getContext());
        
        FrameLayout footContainer = new FrameLayout(getContext());
        footContainer.addView(bar);
        
        mFootContainer = footContainer;
        return footContainer;
    }

    private View getLastRowView() {
        TextView tvLastRow = new TextView(getContext());
        tvLastRow.setText("已到最后一行");
        tvLastRow.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
        tvLastRow.setGravity(Gravity.CENTER);
        FrameLayout footContainer = new FrameLayout(getContext());
        footContainer.addView(tvLastRow);
        
        mFootContainer = footContainer;
        return footContainer;
    }

    private View getRowView(int position, View convertView, ViewGroup parent) {
        if (mCallbacks == null){
            return convertView != null ? convertView : new View(getContext());
        }

        if (convertView == null){
            convertView = mCallbacks.newCollectionItemView(getContext(), parent, dataList.get(position));
        }

        
        mCallbacks.bindCollectionItemView(getContext(), convertView,position, dataList.get(position));

        return convertView;
    }


    private class MyScrollListener implements OnScrollListener {

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {

        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if (! isLoading
                    && (dataList.size() < serverListSize)
                    && (firstVisibleItem + visibleItemCount >= totalItemCount - 1)

                    ) {

            	Log.d("eric", "skip: " + dataList.size() + " , " + " top: " + NUMBER_OF_ITEMS_TO_LOAD);
                   loadMoreResult(dataList.size(), NUMBER_OF_ITEMS_TO_LOAD);
                   
            }
        }
    }

    public void appendToDataList(List<T> items){
        isLoading = false;
        dataList.addAll(items);
        mListAdapter.notifyDataSetChanged();
    }
    
    
    /**
     * To replace a data item in the List;
     * @param tOld: the item to be replaced
     * @param tNew: the new item to be put into the List
     */
    public void replaceItem(T tOld, T tNew){
    	dataList.set(dataList.indexOf(tOld), tNew);
    }
    
    public void replaceItem(int position, T t){
    	dataList.set(position, t);
    }

    public void removeItemAtPosition(int position) {
		dataList.remove(position);
	}
    
    public void removeItem(T t){
    	dataList.remove(t);
    }
    
    public List<T> getDataList(){
    	return dataList;
    }

	public void notifyDataSetChanged() {
		mListAdapter.notifyDataSetChanged();
	}
	
	/*------------------------------------------*/

}
