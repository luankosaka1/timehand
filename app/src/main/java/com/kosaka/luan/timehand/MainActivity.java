package com.kosaka.luan.timehand;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.kosaka.luan.timehand.Service.HttpUsuarioExisteSender;
import com.kosaka.luan.timehand.Service.Util;


public class MainActivity extends Activity {

    LinearLayout layoutValidacaoTelefone;
    EditText inputTelefone;

    private Button btnAcessar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        String phoneNumber = Util.getTelefone(getApplicationContext());

        if (phoneNumber.isEmpty()) {
            if (Util.PHONE_NUMBER.isEmpty()) {
                layoutValidacaoTelefone = (LinearLayout) findViewById(R.id.layoutValidacaoTelefone);
                layoutValidacaoTelefone.setVisibility(View.VISIBLE);
            } else {
                btnAcessar = (Button) findViewById(R.id.btnAcessar);
                btnAcessar.setEnabled(true);
            }
        } else {
            HttpUsuarioExisteSender usuarioExisteSender = new HttpUsuarioExisteSender(getApplication(), this);
            usuarioExisteSender.execute(phoneNumber);
        }
    }

    public void clickVerificarTelefone(View view) {
        inputTelefone = (EditText) findViewById(R.id.inputTelefone);

        if (inputTelefone.getText().toString().isEmpty()) {
            Toast.makeText(this, getString(R.string.preencher_telefone), Toast.LENGTH_LONG).show();
        } else {
            HttpUsuarioExisteSender usuarioExisteSender = new HttpUsuarioExisteSender(getApplication(), this);
            usuarioExisteSender.execute(inputTelefone.getText().toString());
        }
    }

    public void clickCadastrar(View view) {
        startActivity(new Intent(this, CadastroActivity.class));
    }

    public void clickLogin(View view) {
        startActivity(new Intent(this, HomeActivity.class));
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
