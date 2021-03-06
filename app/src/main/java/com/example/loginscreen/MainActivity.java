package com.example.loginscreen;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.AsyncTask;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;
import com.soundcloud.api.*;
import com.google.gson.*;

public class MainActivity extends Activity {
    public final static String EXTRA_MESSAGE = "com.example.loginscreen.MESSAGE";
    private EditText  username=null;
    private EditText  password=null;
    private TextView attempts;
    private Button login;
    int counter = 3;
    boolean authorized = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        username = (EditText)findViewById(R.id.editText1);
        password = (EditText)findViewById(R.id.editText2);
        attempts = (TextView)findViewById(R.id.textView5);
        attempts.setText(Integer.toString(counter));
        login = (Button)findViewById(R.id.button1);
    }

    public void login(View view){

        System.out.println("user: " +username.getText().toString()+ " pass: " +password.getText().toString());

        authUser task = new authUser();
        task.execute(new String[]{username.getText().toString(), password.getText().toString()});
        System.out.println("authorized: " + authorized);
        if(authorized) {
            Intent loggedin = new Intent(this, HubActivity.class);
            String message = "this is the string passed the new activity";
            loggedin.putExtra(EXTRA_MESSAGE, message);
            startActivity(loggedin);
        }



       /* if(username.getText().toString().equals("admin") &&
                password.getText().toString().equals("admin")){
            Toast.makeText(getApplicationContext(), "Redirecting...",
                    Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(getApplicationContext(), "Wrong Credentials",
                    Toast.LENGTH_SHORT).show();
            attempts.setBackgroundColor(Color.RED);
            counter--;
            attempts.setText(Integer.toString(counter));
            if(counter==0){
                login.setEnabled(false);
            }
        } */

    }

    private class authUser extends AsyncTask<String, Void, Boolean> {
        protected Boolean doInBackground(String... params) {
            boolean authd = false;
            try {
                ApiWrapper soundcloud = new ApiWrapper(
                        "7b0837389ef02bedd507d700b962f5a2",
                        "e81408f1564aab7c90af0a65d99e6d1e",
                        null, null
                );
                Token token = soundcloud.login(
                        params[0],
                        params[1]
                );
                authd = token.valid();
                System.out.println("logged in: " +authd);
            } catch (Exception e) {
                System.out.println(e.toString());
            }

            return authd;
        }
        protected void onPostExecute (Boolean result){
            // this is run on the main (UI) thread, after doInBackground returns
            authorized = result;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

}