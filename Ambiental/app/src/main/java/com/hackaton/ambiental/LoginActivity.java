package com.hackaton.ambiental;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hackaton.ambiental.DataBase.Catalogo.UsuariosDO;
import com.hackaton.ambiental.DataBase.Controler.Sesion;
import com.hackaton.ambiental.Layout.Inicio;
import com.hackaton.ambiental.Variables.AmbientalC;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity{

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private Sesion sesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    IrInicio();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                IrInicio();
            }
        });
        sesion= new Sesion(this);
        //sesion.getPreguntas();
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }


    private void IrInicio(){
        AmbientalC.user= UsuariosDO.SERVICIO;
        Intent i = new Intent(this, Inicio.class);
        startActivity(i);
        overridePendingTransition(R.anim.zoom_forward_in, R.anim.zoom_forward_out);
        //saveAll(0,new Component[]{nuevoUsuario(0, "THMOG", "34RYUD","LUIS FERNANDO HERNANDEZ MENDEZ","",""), nuevoSeccion(0, "ZXY001"), nuevoCasilla(0, "ABC001")});
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

    }
}

