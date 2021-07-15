package com.repkap11.repweather;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.gson.Gson;
import com.repkap11.repweather.api.rest.Weather;
import com.repkap11.repweather.widget.CurrentWeatherWidget;
import com.repkap11.repweather.widget.CurrentWeatherWidgetConfigureActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class GrabWeatherWorker extends Worker {
    private static final String TAG = GrabWeatherWorker.class.getSimpleName();

    private static final String OUTPUT_WEATHER_PREFIX_ = "OUTPUT_WEATHER_PREFIX_";

    public GrabWeatherWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        Log.d(TAG, "GrabWeatherWorker() called with: context = [" + context + "], workerParams = [" + workerParams + "]");
    }

    public static void broadcastToWidgets(Context context) {
        Intent intent = new Intent(context, CurrentWeatherWidget.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        // Use an array and EXTRA_APPWIDGET_IDS instead of AppWidgetManager.EXTRA_APPWIDGET_ID,
        // since it seems the onUpdate() is only fired on that:
        int[] ids = AppWidgetManager.getInstance(context).getAppWidgetIds(new ComponentName(context, CurrentWeatherWidget.class));
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        context.sendBroadcast(intent);
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.d(TAG, "doWork() called");

        OkHttpClient oKHttpClient = new OkHttpClient.Builder().build();
        CurrentWeatherWidgetConfigureActivity.deleteAllWeatherData(getApplicationContext());

        String[] zipCodes = CurrentWeatherWidgetConfigureActivity.getAllTitlePrefs(getApplicationContext());
        if (zipCodes == null || zipCodes.length == 0) {
            Log.d(TAG, "doWork: No zipcodes");
            return Result.success();
        }
        for (String zipCode : zipCodes) {
            try {
                Request request = new Request.Builder().url("https://api.repkam09.com/api/weather/current/zip/" + zipCode).build();
                String jsonWeather = oKHttpClient.newCall(request).execute().body().string();
                Weather weather = new Gson().fromJson(jsonWeather, Weather.class);

                String iconName = weather.weather.get(0).icon;
                File iconFile = CurrentWeatherWidget.iconNameToFile(getApplicationContext(), iconName);
                if (iconFile.isDirectory()) {
                    iconFile.delete();
                }
                iconFile.delete();
                if (!iconFile.exists()) {
                    iconFile.getParentFile().mkdirs();
                    String url = "https://openweathermap.org/img/wn/" + iconName + "@4x.png";
                    Log.i(TAG, "doWork: url:" + url);
                    Request iconRequest = new Request.Builder().url(url).build();
                    FileOutputStream fos = new FileOutputStream(iconFile);
                    InputStream is = oKHttpClient.newCall(iconRequest).execute().body().byteStream();
                    copyStream(is, fos);
                    fos.close();
                }

                Log.i(TAG, "doWork: Got iconFile:" + iconFile.getAbsolutePath());
                CurrentWeatherWidgetConfigureActivity.saveWeatherData(getApplicationContext(), zipCode, jsonWeather);
            } catch (IOException e) {
                e.printStackTrace();
                return Result.failure();
            }
        }
        long unixTime = System.currentTimeMillis();
        CurrentWeatherWidgetConfigureActivity.sateLastUpdateTimeStamp(getApplicationContext(), Long.toString(unixTime));
        broadcastToWidgets(getApplicationContext());
        return Result.success();
    }

    void copyStream(InputStream source, OutputStream target) throws IOException {
        byte[] buf = new byte[8192];
        int length;
        while ((length = source.read(buf)) > 0) {
            target.write(buf, 0, length);
        }
    }
}
