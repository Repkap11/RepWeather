package com.repkap11.repweather.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.work.BackoffPolicy;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.google.gson.Gson;
import com.repkap11.repweather.GrabWeatherWorker;
import com.repkap11.repweather.R;
import com.repkap11.repweather.api.rest.Weather;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link CurrentWeatherWidgetConfigureActivity CurrentWeatherWidgetConfigureActivity}
 */
public class CurrentWeatherWidget extends AppWidgetProvider {

    private static final String TAG = CurrentWeatherWidget.class.getSimpleName();

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        String zipCode = CurrentWeatherWidgetConfigureActivity.loadWidgetZipCodePref(context, appWidgetId);
        String jsonWeather = CurrentWeatherWidgetConfigureActivity.loadWeatherData(context, zipCode);
        Weather weather = new Gson().fromJson(jsonWeather, Weather.class);
        String unixTime = CurrentWeatherWidgetConfigureActivity.loadLastUpdateTimeStamp(context);
        if (weather == null || unixTime == null) {
            Log.w(TAG, "updateAppWidget: Weather null");
            return;
        }
        String lastUpdate = CurrentWeatherWidgetConfigureActivity.loadLastUpdateTimeStamp(context);
        Log.i(TAG, "updateAppWidget: Weather:" + weather.name);

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.current_weather_widget);
        views.setTextViewText(R.id.widget_location, weather.name);
        DecimalFormat df = new DecimalFormat("#.0");
        views.setTextViewText(R.id.widget_temp_c, df.format(k_to_c(weather.main.temp)) + "°C");
        views.setTextViewText(R.id.widget_temp_f, df.format(k_to_f(weather.main.temp)) + "°F");
        views.setTextViewText(R.id.widget_cloud_cover, "Cloudiness:  " + weather.clouds.all * 100 + "%");
        File iconFile = iconNameToFile(context.getApplicationContext(), weather.weather.get(0).icon);
        Log.i(TAG, "updateAppWidget: iconPath:" + iconFile.getAbsolutePath() + " exists:" + iconFile.exists());
        Bitmap iconBitmap = BitmapFactory.decodeFile(iconFile.getAbsolutePath());
        Log.i(TAG, "updateAppWidget: icon:" + iconBitmap);
        views.setImageViewBitmap(R.id.widget_icon, iconBitmap);


        long unixTimeLong = Long.parseLong(unixTime);
        Date date = new Date(unixTimeLong);
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM d  h:mm:ss", Locale.getDefault());
        String dateString = dateFormat.format(date);
        Log.i(TAG, "updateAppWidget: timestamp:" + unixTimeLong + " DateString:" + dateString);

        views.setTextViewText(R.id.widget_last_update, dateString);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    public static File iconNameToFile(Context context, String iconName) {
        File iconFile = new File(context.getFilesDir(), "weather_icons" + File.separator + iconName + ".png");
        return iconFile;
    }

    private static double k_to_f(double temp_k) {
        return (9.0 / 5.0) * k_to_c(temp_k) + 32.0;
    }

    private static double k_to_c(double temp_k) {
        return (temp_k - 273.15);
    }

    public static void queueWork(Context context, boolean forceRefresh) {
        WorkManager workManager = WorkManager.getInstance(context);
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();
        PeriodicWorkRequest work = new PeriodicWorkRequest.Builder(GrabWeatherWorker.class, 1, TimeUnit.HOURS)
                .setConstraints(constraints)
                .addTag(GrabWeatherWorker.class.getSimpleName())
                .setBackoffCriteria(BackoffPolicy.LINEAR, OneTimeWorkRequest.DEFAULT_BACKOFF_DELAY_MILLIS, TimeUnit.MILLISECONDS)
                .build();

        workManager.enqueueUniquePeriodicWork(GrabWeatherWorker.class.getSimpleName(), forceRefresh ? ExistingPeriodicWorkPolicy.REPLACE : ExistingPeriodicWorkPolicy.KEEP, work);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.d(TAG, "onUpdate() called with: context = [" + context + "], appWidgetManager = [" + appWidgetManager + "], appWidgetIds = [" + appWidgetIds + "]");
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
        queueWork(context, false);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        for (int appWidgetId : appWidgetIds) {
            CurrentWeatherWidgetConfigureActivity.deleteWidgetZipCodePref(context, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        Log.d(TAG, "onEnabled() called with: context = [" + context + "]");
        queueWork(context, true);
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        Log.d(TAG, "onDisabled() called with: context = [" + context + "]");
        WorkManager.getInstance(context).cancelAllWorkByTag(GrabWeatherWorker.class.getSimpleName());
        // Enter relevant functionality for when the last widget is disabled
    }
}