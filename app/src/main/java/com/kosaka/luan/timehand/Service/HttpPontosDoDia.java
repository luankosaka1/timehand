package com.kosaka.luan.timehand.Service;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.kosaka.luan.timehand.CadastroActivity;
import com.kosaka.luan.timehand.HomeActivity;
import com.kosaka.luan.timehand.MainActivity;
import com.kosaka.luan.timehand.R;
import com.kosaka.luan.timehand.SemInternet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by root on 30/07/15.
 */
public class HttpPontosDoDia extends AsyncTask<String, String, String> {

    private final String URL_TIMEHAND = "http://timehand.luankosaka.com.br/api/registro/ponto/telefone/";
    private Context context;
    private HomeActivity view;
    private TextView labelHorasDoDia;

    public HttpPontosDoDia(Context context, HomeActivity view) {
        this.context = context;
        this.view = view;

        labelHorasDoDia = (TextView) view.findViewById(R.id.labelHorasDoDia);
    }

    @Override
    protected String doInBackground(String... params) {
        HttpURLConnection httpConnection = null;
        BufferedReader reader = null;

        Uri builtUri = Uri.parse(URL_TIMEHAND + params[0]).buildUpon().build();

        publishProgress();

        try {
            URL url = new URL(builtUri.toString());

            httpConnection = (HttpURLConnection) url.openConnection();
            httpConnection.setRequestMethod("GET");
            httpConnection.setConnectTimeout(5000);
            httpConnection.connect();

            InputStream inputStream = httpConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();

            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            return buffer.toString().trim();
        } catch (MalformedURLException e) {
            ;
            Log.e("HTTP MSG Malformed", e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("HTTP MSG IOException", e.getMessage());
        }

        return "{\"error\": true, \"timeout\": true, \"message\": \"Erro desconhecido, tente novamente mais tarde\"}";
    }

    @Override
    protected void onPostExecute(String s) {
        try {
            JSONObject json = new JSONObject(s);
            String message = json.get("message").toString();

            JSONArray horasArray = new JSONArray(message);

            String lista = "";

            for (int i = 0; i < horasArray.length(); i++) {
                String ponto = horasArray.get(i).toString();
                JSONObject jsonPonto = new JSONObject(ponto);
                lista += jsonPonto.get("dataPonto").toString() + "\n";
            }

            labelHorasDoDia.setText(lista);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
