package com.kosaka.luan.timehand.Service;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.kosaka.luan.timehand.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpCadastroSender extends AsyncTask<String, String, String> {

    private final String URL_TIMEHAND = "http://timehand.luankosaka.com.br/api/registrar/usuario";
    private final String FIELD_NOME = "data[name]";
    private final String FIELD_EMAIL = "data[email]";
    private final String FIELD_TELEFONE = "data[telefone]";
    private final String FIELD_PASSWORD = "data[password]";
    private Context context;
    private View view;
    private Button btnCadastrar;

    public HttpCadastroSender(Context context, View view) {
        this.context = context;
        this.view = view;

        btnCadastrar = (Button) view.findViewById(R.id.btnCadastrar);
    }

    @Override
    protected String doInBackground(String... params) {
        HttpURLConnection httpConnection = null;
        BufferedReader reader = null;

        Uri builtUri = Uri.parse(URL_TIMEHAND)
                .buildUpon()
                .appendQueryParameter(FIELD_NOME, params[0])
                .appendQueryParameter(FIELD_EMAIL, params[1])
                .appendQueryParameter(FIELD_TELEFONE, params[2])
                .appendQueryParameter(FIELD_PASSWORD, params[3])
                .build();

        publishProgress();

        try {
            URL url = new URL(builtUri.toString());

            httpConnection = (HttpURLConnection) url.openConnection();
            httpConnection.setRequestMethod("POST");
            httpConnection.setConnectTimeout(5000);
            httpConnection.connect();

            InputStream inputStream = httpConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();

            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
                Log.i("HTTP MSG", line);
            }

            return buffer.toString().trim();
        } catch (MalformedURLException e) {            ;
            Log.e("HTTP MSG Malformed", e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("HTTP MSG IOException", e.getMessage());
        }

        return "{\"error\": true, \"message\": \"Erro desconhecido, tente novamente mais tarde\"}";
    }

    @Override
    protected void onPostExecute(String s) {
        try {
            JSONObject json = new JSONObject(s);
            String error = json.get("error").toString();
            String message = json.get("message").toString();

            btnCadastrar.setEnabled(true);

            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onProgressUpdate(String... values) {
        btnCadastrar.setEnabled(false);
    }
}
