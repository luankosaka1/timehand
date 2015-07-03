package com.kosaka.luan.timehand;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class HomeActivity extends Activity {

    private TextClock horario;
    private TextView data;
    private Calendar calendario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        data = (TextView) findViewById(R.id.data);
        data.setText(getDataAtual("dd/MM/yyyy"));
    }

    private String getDataAtual(String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.getDefault());
        String currentDate = simpleDateFormat.format(new Date());

        return currentDate;
    }

    public void clickRegistraPonto(View view) {
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost("http://timehand.luankosaka.com.br/api/ponto/adicionar");

        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(1);

        nameValuePair.add(new BasicNameValuePair("data", getDataAtual("yyyy-MM-dd H:mm:ss")));
        nameValuePair.add(new BasicNameValuePair("imei", getIMEI()));
        nameValuePair.add(new BasicNameValuePair("sim", getSIM()));

        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
        } catch (UnsupportedEncodingException e) {
            Log.e("HttpPost", e.getMessage());
        }

        try {
            HttpResponse response = httpClient.execute(httpPost);
            Toast.makeText(this, response.toString(), Toast.LENGTH_SHORT).show();

            Log.d("Http Post Response:", response.toString());
        } catch (ClientProtocolException e) {
            Log.e("HttpResponse", e.getMessage());
        } catch (IOException e) {
            Log.e("HttpResponse", e.getMessage());
        }
    }

    private String getSIM() {
        TelephonyManager telemamanger = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String getSimSerialNumber = telemamanger.getSimSerialNumber();
        String getSimNumber = telemamanger.getLine1Number();

        if (getSimNumber.isEmpty()) {
            return getSimSerialNumber;
        }

        return getSimNumber;
    }

    private String getIMEI() {
        TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
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
