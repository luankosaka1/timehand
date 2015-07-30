package com.kosaka.luan.timehand;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.kosaka.luan.timehand.Service.HttpCadastroSender;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class CadastroActivity extends Activity {

    private EditText inputNome, inputEmail, inputSenha, inputTelefone;
    private Pattern pattern;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cadastro);

        inputNome = (EditText) findViewById(R.id.inputNome);
        inputEmail = (EditText) findViewById(R.id.inputEmail);
        inputSenha = (EditText) findViewById(R.id.inputSenha);
        inputTelefone = (EditText) findViewById(R.id.inputTelefone);
        inputTelefone.setText(getTelefone());
    }

    public boolean isEmailValid(String email) {
        String regExpn =
                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                        + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                        + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

        CharSequence inputStr = email;

        pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        if (matcher.matches())
            return true;
        else
            return false;
    }

    public void clickCadastrar(View view) {
        Boolean inputEmpty = false;

        if (inputNome.getText().toString().isEmpty()) {
            inputEmpty = true;
        } else if (inputEmail.getText().toString().isEmpty()) {
            inputEmpty = true;
        } else if (inputSenha.getText().toString().isEmpty()) {
            inputEmpty = true;
        } else if (inputTelefone.getText().toString().isEmpty()) {
            inputEmpty = true;
        }

        if (inputEmpty) {
            Toast.makeText(this, getString(R.string.preencha_todos_campos), Toast.LENGTH_LONG).show();
        } else {

            if (!isEmailValid(inputEmail.getText().toString())) {
                Toast.makeText(this, getString(R.string.email_invalido), Toast.LENGTH_LONG).show();
            } else {
                // salva o cadastro
                HttpCadastroSender cadastroSender = new HttpCadastroSender(getApplication(), view);
                cadastroSender.execute(
                        inputNome.getText().toString(),
                        inputEmail.getText().toString(),
                        inputTelefone.getText().toString(),
                        inputSenha.getText().toString()
                );

                startActivity(new Intent(this, MainActivity.class));
            }
        }
    }

    public void clickSouCadastrado(View view) {
        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cadastro, menu);
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

    private String getTelefone() {
        TelephonyManager tMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        return tMgr.getLine1Number();
    }
}
