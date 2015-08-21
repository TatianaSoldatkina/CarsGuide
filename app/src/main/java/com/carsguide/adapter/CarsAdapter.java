package com.carsguide.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.carsguide.CarsGuideApplication;
import com.carsguide.Constants;
import com.carsguide.R;
import com.carsguide.helper.ImageDownloader;
import com.carsguide.rest.CarsGuideServiceHelper;
import com.carsguide.rest.contract.CarContract;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class CarsAdapter extends CursorRecyclerViewAdapter<CarsAdapter.ViewHolder>  {

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }

    private OnItemClickListener onItemClickListener;
    private DisplayImageOptions options;
    private boolean shouldUsePagination;

    public CarsAdapter(Context context, Cursor cursor) {
        super(context, cursor);
        options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .build();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private final ImageView imageViewCarIcon;
        private final TextView textViewCarTitle;
        private ImageView imageViewIsCarFavorite;


        public ViewHolder(View v) {
            super(v);
            imageViewCarIcon = (ImageView) v.findViewById(R.id.car_icon);
            textViewCarTitle = (TextView) v.findViewById(R.id.car_title);
            imageViewIsCarFavorite = (ImageView) v.findViewById(R.id.car_is_favorite);

            v.setOnClickListener(this);
            v.setOnLongClickListener(this);

        }

        public ImageView getImageViewCarIcon() {
            return imageViewCarIcon;
        }

        public TextView getTextViewCarTitle() {
            return textViewCarTitle;
        }

        public ImageView getImageViewIsCarFavorite() {
            return imageViewIsCarFavorite;
        }

        @Override
        public void onClick(View view) {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(view, getPosition());
            }
        }

        @Override
        public boolean onLongClick(View view) {
            if (onItemClickListener != null) {
                onItemClickListener.onItemLongClick(view, getPosition());
            }
            return false;
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.cell_car, viewGroup, false);

        return new ViewHolder(v);
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, Cursor cursor) {

        String imageUrl = cursor.getString(cursor.getColumnIndex(CarContract.CAR_IMAGE_URL));
        String carTitle = cursor.getString(cursor.getColumnIndex(CarContract.CAR_TITLE));
        boolean isFavorite = cursor.getInt(cursor.getColumnIndex(CarContract.IS_FAVORITE)) > 0;

        ImageLoader.getInstance().displayImage(imageUrl, viewHolder.getImageViewCarIcon(), options, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {

            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {
                if (ImageDownloader.getInstance().getFile(s).exists()) {
                    String path = "file://" + ImageDownloader.getInstance().getImagePath(s);
                    ImageLoader.getInstance().displayImage(path, viewHolder.getImageViewCarIcon(),
                            options);
                }
            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {

            }

            @Override
            public void onLoadingCancelled(String s, View view) {

            }
        });

        viewHolder.getTextViewCarTitle().setText(carTitle);
        viewHolder.getImageViewIsCarFavorite().setVisibility(isFavorite ? View.VISIBLE : View.INVISIBLE);

        SharedPreferences sharedPreferences = CarsGuideApplication.getAppContext().getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);

        int itemsCount = cursor.getCount();
        int currentPosition = cursor.getPosition();
        int totalCarsCount = sharedPreferences.getInt(Constants.SHARED_PREFERENCES_CARS_COUNT, 0);

        if (currentPosition == itemsCount - 1 && itemsCount < totalCarsCount && shouldUsePagination()) {
            CarsGuideServiceHelper.getInstance().getCars(currentPosition, Constants.CARS_PER_PAGE);
        }
    }

    public boolean shouldUsePagination() {
        return shouldUsePagination;
    }

    public void setShouldUsePagination(boolean shouldUsePagination) {
        this.shouldUsePagination = shouldUsePagination;
    }
}