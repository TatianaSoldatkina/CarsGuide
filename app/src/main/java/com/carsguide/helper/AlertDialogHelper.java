package com.carsguide.helper;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;

import com.carsguide.CarsGuideApplication;
import com.carsguide.R;
import com.carsguide.activities.BaseCarsListActivity;
import com.carsguide.rest.contract.CarContract;
import com.carsguide.events.ShowCarInfoEvent;
import com.carsguide.rest.CarsProcessor;

import de.greenrobot.event.EventBus;

public class AlertDialogHelper {

    private static AlertDialogHelper instance;

    private AlertDialogHelper() {
       // empty
    }

    public static AlertDialogHelper getInstance() {
        if (instance == null) {
            instance = new AlertDialogHelper();
        }

        return instance;
    }

    public void showFavoritesAlert(BaseCarsListActivity activity, final int position, Cursor carCursor) {
        if (carCursor.moveToPosition(position)) {

            final boolean isFavorite = carCursor.getInt(carCursor.getColumnIndex(CarContract.IS_FAVORITE)) > 0;
            final int carId = carCursor.getInt(carCursor.getColumnIndex(CarContract.ID));
            final String carUrl = carCursor.getString(carCursor.getColumnIndex(CarContract.CAR_URL));

            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            String buttonText = isFavorite ? CarsGuideApplication.getResourceString(R.string.delete_from_favorites) : CarsGuideApplication.getResourceString(R.string.add_to_favorites);

            builder.setTitle(CarsGuideApplication.getResourceString(R.string.favorites));

            builder.setPositiveButton(buttonText, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    CarsProcessor.setCarFavorite(carId, !isFavorite);
                }
            });

            builder.setNegativeButton(CarsGuideApplication.getResourceString(R.string.close_button), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    EventBus.getDefault().post(new ShowCarInfoEvent(carUrl));
                }
            });

            builder.setCancelable(false);
            builder.show();

        }
        carCursor.close();

    }
}
