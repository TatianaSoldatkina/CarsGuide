package com.carsguide.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.carsguide.Constants;
import com.carsguide.R;
import com.carsguide.rest.CarsGuideServiceHelper;

public class MainCarsListActivity extends BaseCarsListActivity {

    private static final int START_ITEM_POSITION = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        mRecyclerView = (RecyclerView) findViewById(R.id.cars_recycler_view);
        setRecyclerView();

        getLoaderManager().initLoader(LOADER_ID_CARS, null, mLoaderCallbacks);
        CarsGuideServiceHelper.getInstance().getCars(START_ITEM_POSITION, Constants.CARS_PER_PAGE);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mAdapter.setShouldUsePagination(true);
        if (mLoaderCallbacks != null) {
            getLoaderManager().restartLoader(LOADER_ID_CARS, null, mLoaderCallbacks);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case  R.id.favorites:
                navigateToFavorites();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void navigateToFavorites() {
        Intent intent = new Intent(MainCarsListActivity.this, FavoriteCarsListActivity.class);
        startActivity(intent);
    }


    @Override
    public String getCarsListSelection() {
        return null;
    }

    @Override
    public String[] getCarsListSelectionArgs() {
        return null;
    }
}
