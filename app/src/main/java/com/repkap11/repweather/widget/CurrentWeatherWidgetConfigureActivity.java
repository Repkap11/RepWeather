package com.repkap11.repweather.widget;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.repkap11.repweather.BuildConfig;
import com.repkap11.repweather.R;
import com.repkap11.repweather.databinding.CurrentWeatherWidgetConfigureBinding;

import java.util.HashSet;
import java.util.Map;

/**
 * The configuration screen for the {@link CurrentWeatherWidget CurrentWeatherWidget} AppWidget.
 */
public class CurrentWeatherWidgetConfigureActivity extends Activity {

    private static final String PREFS_NAME_ID_ZIP_MAP = BuildConfig.APPLICATION_ID + ".PREFS_NAME_ID_ZIP_MAP";
    private static final String PREFS_NAME_ZIP_WEATHER_DATA = BuildConfig.APPLICATION_ID + ".PREFS_NAME_ZIP_WEATHER_DATA";
    private static final String PREF_WIDGET_ID_PREFIX_ = "PREF_WIDGET_ID_PREFIX_";
    private static final String PREF_ZIP_PREFIX_ = "PREF_WIDGET_ID_PREFIX_";
    private static final String PREF_ZIP_WEATHER_UPDATED_TIMESTAMP = "PREF_ZIP_WEATHER_UPDATED_TIMESTAMP_";
    private static final String TAG = CurrentWeatherWidgetConfigureActivity.class.getSimpleName();
    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    EditText mAppWidgetText;
    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            final Context context = CurrentWeatherWidgetConfigureActivity.this;

            // When the button is clicked, store the string locally
            String widgetText = mAppWidgetText.getText().toString();
            saveWidgetZipCodePref(context, mAppWidgetId, widgetText);

            // It is the responsibility of the configuration activity to update the app widget
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
//            CurrentWeatherWidget.updateAppWidget(context, appWidgetManager, mAppWidgetId);
            CurrentWeatherWidget.queueWork(CurrentWeatherWidgetConfigureActivity.this, true);

            // Make sure we pass back the original appWidgetId
            Intent resultValue = new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
            setResult(RESULT_OK, resultValue);
            finish();
        }
    };
    private CurrentWeatherWidgetConfigureBinding binding;

    public CurrentWeatherWidgetConfigureActivity() {
        super();
    }

    // Write the prefix to the SharedPreferences object for this widget
    static void saveWidgetZipCodePref(Context context, int appWidgetId, String zipCode) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME_ID_ZIP_MAP, 0).edit();
        prefs.putString(PREF_WIDGET_ID_PREFIX_ + appWidgetId, zipCode);
        Log.i(TAG, "saveWidgetZipCodePref: Saving zipcode:" + zipCode);
        prefs.apply();
    }

    public static void saveWeatherData(Context context, String zipCode, String jsonWeather) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME_ZIP_WEATHER_DATA, 0).edit();
        prefs.putString(PREF_ZIP_PREFIX_ + zipCode, jsonWeather);
        Log.i(TAG, "saveWeatherData: Saving zipcode:" + zipCode + "'s weather");
        prefs.apply();
    }

    static String loadWidgetZipCodePref(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME_ID_ZIP_MAP, 0);
        String titleValue = prefs.getString(PREF_WIDGET_ID_PREFIX_ + appWidgetId, null);
        if (titleValue != null) {
            return titleValue;
        } else {
            return context.getString(R.string.appwidget_text);
        }
    }

    public static String loadWeatherData(Context context, String zipCode) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME_ZIP_WEATHER_DATA, 0);
        String weatherJson = prefs.getString(PREF_ZIP_PREFIX_ + zipCode, null);
        return weatherJson;
    }

    static void deleteWidgetZipCodePref(Context context, int appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME_ID_ZIP_MAP, 0).edit();
        prefs.remove(PREF_WIDGET_ID_PREFIX_ + appWidgetId);
        prefs.apply();
    }

    public static void deleteAllWeatherData(Context context) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME_ZIP_WEATHER_DATA, 0).edit();
        prefs.clear();
        prefs.apply();
    }


    public static String[] getAllTitlePrefs(Context context) {
        Map<String, ?> prefs = context.getSharedPreferences(PREFS_NAME_ID_ZIP_MAP, 0).getAll();
        HashSet<String> allZips = new HashSet<>(prefs.size());
        for (Map.Entry<String, ?> element : prefs.entrySet()) {
            allZips.add((String) element.getValue());
        }
        return allZips.toArray(new String[0]);
    }

    public static void sateLastUpdateTimeStamp(Context context, String timestamp) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME_ZIP_WEATHER_DATA, 0).edit();
        prefs.putString(PREF_ZIP_WEATHER_UPDATED_TIMESTAMP, timestamp);
        prefs.apply();
    }

    public static String loadLastUpdateTimeStamp(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME_ZIP_WEATHER_DATA, 0);
        return prefs.getString(PREF_ZIP_WEATHER_UPDATED_TIMESTAMP, null);
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED);

        binding = CurrentWeatherWidgetConfigureBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAppWidgetText = binding.appwidgetText;
        binding.addButton.setOnClickListener(mOnClickListener);

        // Find the widget id from the intent.
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }

        mAppWidgetText.setText(loadWidgetZipCodePref(CurrentWeatherWidgetConfigureActivity.this, mAppWidgetId));
    }
}