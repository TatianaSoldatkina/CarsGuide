package com.carsguide.activities;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.carsguide.Constants;
import com.carsguide.adapter.CarsAdapter;
import com.carsguide.events.ShowCarInfoEvent;
import com.carsguide.helper.AlertDialogHelper;
import com.carsguide.rest.contract.CarContract;
import com.carsguide.view.DividerItemDecoration;

import de.greenrobot.event.EventBus;
import jp.wasabeef.recyclerview.animators.FadeInAnimator;
import jp.wasabeef.recyclerview.animators.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.adapters.ScaleInAnimationAdapter;

public abstract class BaseCarsListActivity extends ActionBarActivity {

    public abstract String getCarsListSelection();
    public abstract String[] getCarsListSelectionArgs();

    protected static final int LOADER_ID_CARS = 1;

    protected RecyclerView mRecyclerView;

    protected CarsAdapter mAdapter;
    protected AlphaInAnimationAdapter mAlphaAdapter;
    protected ScaleInAnimationAdapter mSscaleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new CarsAdapter(getApplicationContext(), null);
        mAdapter.setOnItemClickListener(mOnItemClickListener);
        mAlphaAdapter = new AlphaInAnimationAdapter(mAdapter);
        mSscaleAdapter = new ScaleInAnimationAdapter(mAlphaAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    /* EventBus */

    public void onEvent(ShowCarInfoEvent event) {
        showDetailsInfo(event.getCarInfoUrl());
    }

    protected void setRecyclerView() {
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);

        if (mRecyclerView != null) {
            mRecyclerView.addItemDecoration(itemDecoration);
            mRecyclerView.setLayoutManager(layoutManager);
            mRecyclerView.setItemAnimator(new FadeInAnimator());
            mRecyclerView.setAdapter(mSscaleAdapter);
        }

    }

    protected void showDetailsInfo(String carDetailsUrl) {
        Intent intent = new Intent(BaseCarsListActivity.this, DetailsInfoActivity.class);
        intent.putExtra(Constants.EXTRAS_CAR_DETAILS_URL, carDetailsUrl);
        startActivity(intent);
    }

    protected LoaderManager.LoaderCallbacks<Cursor> mLoaderCallbacks = new LoaderManager.LoaderCallbacks<Cursor>() {

        @Override
        public Loader<Cursor> onCreateLoader(int loaderId, Bundle args) {
            switch (loaderId) {
                case LOADER_ID_CARS:
                    String selection = getCarsListSelection();
                    String[] selectionArgs = getCarsListSelectionArgs();
                    return new CursorLoader(BaseCarsListActivity.this, CarContract.CONTENT_URI, null, selection, selectionArgs, null);
                default:
                    return null;
            }
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
            switch (loader.getId()) {
                case LOADER_ID_CARS:
                    mAdapter.swapCursor(cursor);
                    mSscaleAdapter.notifyDataSetChanged();
                    break;
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            switch (loader.getId()) {
                case LOADER_ID_CARS:
                    mAdapter.swapCursor(null);
                    break;
            }
        }
    };

    protected CarsAdapter.OnItemClickListener mOnItemClickListener = new CarsAdapter.OnItemClickListener() {

        @Override
        public void onItemClick(View view, int position) {
            Cursor cursor = getItemsCursor();
            if (cursor.moveToPosition(position)) {
                showDetailsInfo(cursor.getString(cursor.getColumnIndex(CarContract.CAR_URL)));
            }
            cursor.close();
        }

        @Override
        public void onItemLongClick(View view, int position) {
            AlertDialogHelper.getInstance().showFavoritesAlert(BaseCarsListActivity.this, position, getItemsCursor());
        }

        private Cursor getItemsCursor() {
            return getContentResolver().query(CarContract.CONTENT_URI, null, getCarsListSelection(), getCarsListSelectionArgs(), null);
        }

    };


}
