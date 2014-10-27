package com.eric.playdemobusinessnetworking;


import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eric.playdemobusinessnetworking.collection_view.DbListViewCallbacks;
import com.eric.playdemobusinessnetworking.collection_view.DbListViewFragment;
import com.eric.playdemobusinessnetworking.data.Planet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class PeopleFragment extends DbListViewFragment<String> implements DbListViewCallbacks<String> {
    Planet[] planets = Planet.values();
    List<Planet> listPlanet = Arrays.asList(planets);

    public PeopleFragment() {
        // Required empty public constructor
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dbListView.loadFirstRound();
    }

    @Override
    public List<String> loadMoreFromDB(int skip, int top) {
        if (skip >= listPlanet.size() || skip > top) {
            return null;
        }

        int indexStart = skip;
        int indexEnd = top < listPlanet.size() ? top : listPlanet.size();

        List<Planet> chosen = listPlanet.subList(indexStart, indexEnd);
        List<String> data = new ArrayList<String>();

        for (Planet planet : chosen) {
            data.add(planet.toString());
        }

        return data;
    }

    @Override
    public int getDBListSize() {

        return planets.length;
    }

    @Override
    public View newCollectionItemView(Context context, ViewGroup parent, String data) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
    }

    @Override
    public void bindCollectionItemView(Context context, View view, int position, String data) {
        TextView tv1 = (TextView) view.findViewById(android.R.id.text1);
        tv1.setText(data);

    }

    @Override
    public void refreshTotalItemInList(int size) {

    }

    @Override
    protected DbListViewCallbacks<String> getCollectionViewCallbacks() {
        return PeopleFragment.this;
    }
}
