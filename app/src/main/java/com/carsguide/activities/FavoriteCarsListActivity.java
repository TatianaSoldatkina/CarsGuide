package com.carsguide.activities;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;

import com.carsguide.R;
import com.carsguide.rest.contract.CarContract;

public class FavoriteCarsListActivity extends BaseCarsListActivity implements android.support.v7.widget.SearchView.OnQueryTextListener {

    protected String mSearchFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        setTitle(getString(R.string.favorites));

        Toolbar toolbar = (Toolbar) findViewById(R.id.favorites_toolbar);
        setSupportActionBar(toolbar);

        mRecyclerView = (RecyclerView) findViewById(R.id.cars_favorite_recycler_view);

        setRecyclerView();

        getLoaderManager().initLoader(LOADER_ID_CARS, null, mLoaderCallbacks);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mAdapter.setShouldUsePagination(false);
        if (mLoaderCallbacks != null) {
            getLoaderManager().restartLoader(LOADER_ID_CARS, null, mLoaderCallbacks);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_favorites, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);

        SearchView searchView = null;
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
            searchView.setOnQueryTextListener(FavoriteCarsListActivity.this);
        }
        return super.onCreateOptionsMenu(menu);

    }

    /* OnQueryTextListener */

    @Override
    public boolean onQueryTextChange(String s) {
        mSearchFilter = s.length() > 0 ? ("%" + s + "%") : "";
        getLoaderManager().restartLoader(LOADER_ID_CARS, null, mLoaderCallbacks);
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    public String getCarsListSelection() {
        String selection = null;
        if (TextUtils.isEmpty(mSearchFilter)) {
            selection = CarContract.IS_FAVORITE + " = ?";
        } else {
            selection = CarContract.IS_FAVORITE + " = ? AND " + CarContract.CAR_TITLE + " LIKE ?";
        }
        return selection;
    }

    public String[] getCarsListSelectionArgs() {
        String[] selectionArgs;
        if (TextUtils.isEmpty(mSearchFilter)) {
            selectionArgs = new String[1];
            selectionArgs[0] = String.valueOf(1);
        } else {
            selectionArgs = new String[2];
            selectionArgs[0] = String.valueOf(1);
            selectionArgs[1] = mSearchFilter;
        }
        return selectionArgs;
    }

}
