package com.repkap11.repweather;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.repkap11.repweather.api.Weather;
import com.repkap11.repweather.api.rest.CurrentWeather;

import java.io.IOException;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainFragment extends Fragment {

    private static final String TAG = MainFragment.class.getSimpleName();
    private HandlerThread mBackgroundHandlerThread;
    private Handler mBackgroundHandler;
    private Handler mMainHandler;
    private State mState;
    private Ui mUi;
    private Weather mWeather;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setRetainInstance(true);
        mState = new State();
        mBackgroundHandlerThread = new HandlerThread(TAG);
        mBackgroundHandlerThread.start();
        mBackgroundHandler = new Handler(mBackgroundHandlerThread.getLooper());
        mMainHandler = new Handler(Looper.getMainLooper());

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.repkam09.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();
        mWeather = retrofit.create(Weather.class);


        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        mBackgroundHandlerThread.quitSafely();
        mBackgroundHandlerThread = null;
        mBackgroundHandler = null;
        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        mUi = new Ui();
        mUi.message = rootView.findViewById(R.id.fragment_main_result_text);
        Button button = rootView.findViewById(R.id.fragment_main_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBackgroundHandler.post(() -> {
                    try {
                        CurrentWeather currentWeather = mWeather.getCurrentWeather("14586").execute().body();
                        if (currentWeather != null) {
                            mMainHandler.post(() -> {
                                mState.message = currentWeather.name;
                                updateUi();
                            });
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                });
            }
        });
        updateUi();
        return rootView;
    }

    @Override
    public void onDestroyView() {
        mUi = null;
        super.onDestroyView();
    }

    private void updateUi() {
        if (mUi != null) {
            updateStaticUi(mUi, mState);
        }
    }

    private void updateStaticUi(Ui ui, State state) {
        ui.message.setText(state.message);
    }

    private static class State {
        public String message;
    }

    private static class Ui {
        TextView message;
    }
}