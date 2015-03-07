package com.macc.mobile.retrofit;

import android.app.Service;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.macc.mobile.retrofit.Objetos.RestClient;
import com.macc.mobile.retrofit.Objetos.Utilidades;
import com.macc.mobile.retrofit.Objetos.WeatherResponse;

import org.apache.http.Header;

import java.io.File;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class MainActivity extends ActionBarActivity {

    TextView cityText,condDescr,temp,hum,press,windSpeed,windDeg;
    ImageView imgView;
    EditText edCiudad;
    ImageButton btnSearch;
    ProgressBar proimgtie;
    LinearLayout lnTemperatura,lnDataClima;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSearch = (ImageButton) findViewById(R.id.btnSearch);
        edCiudad = (EditText) findViewById(R.id.edCiudad);
        lnTemperatura = (LinearLayout) findViewById(R.id.lnTemperatura);
        lnDataClima = (LinearLayout) findViewById(R.id.lnDataClima);
        lnTemperatura.setVisibility(View.GONE);
        lnDataClima.setVisibility(View.GONE);

        proimgtie = (ProgressBar) findViewById(R.id.proimgtie);

        cityText = (TextView) findViewById(R.id.cityText);
        condDescr = (TextView) findViewById(R.id.condDescr);
        temp = (TextView) findViewById(R.id.temp);
        hum = (TextView) findViewById(R.id.hum);
        press = (TextView) findViewById(R.id.press);
        windSpeed = (TextView) findViewById(R.id.windSpeed);
        windDeg = (TextView) findViewById(R.id.windDeg);
        imgView = (ImageView) findViewById(R.id.condIcon);


        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utilidades.ShowProgress(MainActivity.this);
                InputMethodManager imm = (InputMethodManager)getSystemService(Service.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edCiudad.getWindowToken(), 0);

                RestClient.get().getWeather(edCiudad.getText().toString(),"metric", new Callback<WeatherResponse>() {
                    @Override
                    public void success(WeatherResponse weatherResponse, Response response) {
                        // success!
                        Utilidades.DismissProgress();
                        Log.i("App", weatherResponse.getBase());
                        Log.i("App", weatherResponse.getWeather()[0].getMain());
                        Log.i("App", weatherResponse.getWeather()[0].getDescription());

                        cityText.setText(edCiudad.getText().toString().toUpperCase());
                        condDescr.setText(weatherResponse.getWeather()[0].getDescription().toUpperCase()    );
                        temp.setText(weatherResponse.getMain().getTemp().toString());
                        hum.setText(weatherResponse.getMain().getHumidity()+"");
                        press.setText(weatherResponse.getMain().getPressure()+"");
                        windSpeed.setText(weatherResponse.getWind().getSpeed().toString());
                        windDeg.setText(weatherResponse.getWind().getDeg()+"");
                        // you get the point...
                        DowloadImg(weatherResponse.getWeather()[0].getIcon());
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        // something went wrong
                        Log.e("Error Retro", error.toString());

                        Toast.makeText(MainActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

    }

    public void DowloadImg(String url){
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://openweathermap.org/img/w/" + url + ".png", null, new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                // Initiated the request
                imgView.setVisibility(View.GONE);
                proimgtie.setVisibility(View.VISIBLE);
                lnTemperatura.setVisibility(View.GONE);
                lnDataClima.setVisibility(View.GONE);
            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Bitmap image = BitmapFactory.decodeByteArray(responseBody, 0, responseBody.length);
                imgView.setImageBitmap(image);
                imgView.setVisibility(View.VISIBLE);
                lnTemperatura.setVisibility(View.VISIBLE);
                proimgtie.setVisibility(View.GONE);
                lnDataClima.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                imgView.setVisibility(View.VISIBLE);
                proimgtie.setVisibility(View.GONE);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
