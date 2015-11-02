package com.jeffndev.favoritemoviesmanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.jeffndev.favoritemoviesmanager.tasks.LoginTask;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity
        implements LoginTask.LoginTaskCallback{
    final String LOG_TAG =  LoginActivity.class.getSimpleName();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.

        Button btnDoLogin = (Button)findViewById(R.id.login_button);

        btnDoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText txtUserName = (EditText)findViewById(R.id.login_username_edit_text);
                EditText txtPassword = (EditText)findViewById(R.id.login_password_edit_text);
                LoginTask task = new LoginTask(LoginActivity.this);
                task.execute(new String [] {txtUserName.getText().toString(), txtPassword.getText().toString()});
            }
        });

    }

    @Override
    public void onLoginSessionNegotiated(Boolean succeeded, String sessionId, Integer apiUserId) {
        //TODO: create the intent for the main app's REAL starter activity, start the activity
        //Snackbar.make(findViewById(R.id.content), "LOGIN TASK FINISHED: " + sessionId, Snackbar.LENGTH_SHORT).show();
        if(succeeded) {
            Log.v(LOG_TAG, "LOGIN WAS SUCCESSFUL: " + succeeded + " preparing the intent");
            Intent intent = new Intent(this, MovieListByGenreActivity.class);
            startActivity(intent);
        }else {
            Log.v(LOG_TAG, "LOGIN FAILED...d'oh!!");
        }
    }
}

