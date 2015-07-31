package com.kosaka.luan.timehand;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.kosaka.luan.timehand.Service.HttpPontoSender;
import com.kosaka.luan.timehand.Service.HttpPontosDoDia;
import com.kosaka.luan.timehand.Service.Util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class HomeActivity extends Activity {

    private TextView data;
    private Calendar calendario;
    private EditText editDataAtual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        data = (TextView) findViewById(R.id.data);
        data.setText(getDataAtual("dd/MM/yyyy"));

        editDataAtual = (EditText) findViewById(R.id.editDataAtual);
        editDataAtual.setText(getDataAtual("HH:mm:ss"));

        HttpPontosDoDia pontosDoDia = new HttpPontosDoDia(getApplication(), this);
        pontosDoDia.execute(Util.getTelefone(getApplicationContext()));
    }

    private String getDataAtual(String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.getDefault());
        String currentDate = simpleDateFormat.format(new Date());

        return currentDate.toString();
    }

    public void clickRegistraPonto(View view) {
        String dataAtual = data.getText().toString() + " " + editDataAtual.getText().toString();
        HttpPontoSender http = new HttpPontoSender(getApplication(), view);
        http.execute(dataAtual, Util.getTelefone(getApplicationContext()));

        startActivity(new Intent(this, MainActivity.class));
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
