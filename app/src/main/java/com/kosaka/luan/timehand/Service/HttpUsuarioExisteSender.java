package com.kosaka.luan.timehand.Service;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.kosaka.luan.timehand.CadastroActivity;
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
public class HttpUsuarioExisteSender extends AsyncTask<String, String, String> {

    private final String URL_TIMEHAND = "http://timehand.luankosaka.com.br/api/usuario/existe";
    private final String FIELD_TELEFONE = "data[telefone]";
    private Context context;
    private MainActivity view;
    private Button btnAcessar, btnCadastrar;
    private TextView labelBemVindo;
    private LinearLayout layoutValidacaoTelefone;

    public HttpUsuarioExisteSender(Context context, MainActivity view) {
        this.context = context;
        this.view = view;

        btnAcessar = (Button) view.findViewById(R.id.btnAcessar);
        btnCadastrar = (Button) view.findViewById(R.id.btnCadastrar);
        labelBemVindo = (TextView) view.findViewById(R.id.labelBemVindo);
        layoutValidacaoTelefone = (LinearLayout) view.findViewById(R.id.layoutValidacaoTelefone);
    }

    @Override
    protected String doInBackground(String... params) {
        HttpURLConnection httpConnection = null;
        BufferedReader reader = null;

        Uri builtUri = Uri.parse(URL_TIMEHAND)
                .buildUpon()
                .appendQueryParameter(FIELD_TELEFONE, params[0])
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
            Boolean error = Boolean.valueOf(json.get("error").toString());
            String message = json.get("message").toString();

            if (error) {
                if (json.has("timeout")) {
                    if (Boolean.valueOf(json.get("timeout").toString())) {
                        showLayoutTimeout();
                    } else {
                        view.startActivity(new Intent(view, CadastroActivity.class));
                    }
                } else {
                    view.startActivity(new Intent(view, CadastroActivity.class));
                }
            } else {
                btnAcessar.setEnabled(true);

                JSONArray jsonArray = new JSONArray(message);
                String user = jsonArray.get(0).toString();

                JSONObject jsonUser = new JSONObject(user);
                String username = jsonUser.get("name").toString();
                String telefone = jsonUser.get("telefone").toString();

                labelBemVindo.setText(context.getString(R.string.bem_vindo) + ", " + username + "\n" + telefone);

                layoutValidacaoTelefone.setVisibility(View.INVISIBLE);

                // definir o numero do telefone como variavel global
                Util.PHONE_NUMBER = telefone;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showLayoutTimeout() {
        view.startActivity(new Intent(context, SemInternet.class));
    }

}
