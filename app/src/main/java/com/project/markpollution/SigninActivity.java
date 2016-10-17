package com.project.markpollution;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.HashMap;
import java.util.Map;

public class SigninActivity extends AppCompatActivity  implements GoogleApiClient.OnConnectionFailedListener {
    private GoogleApiClient googleApiClient;
    private String url_insert = "http://2dev4u.com/dev/markpollution/InsertUser.php";
    private String url_checkUser = "http://2dev4u.com/dev/markpollution/RetrieveUserByEmail.php?email=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder()
                .requestEmail()
                .requestProfile()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .enableAutoManage(this, this)
                .build();

        SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setScopes(gso.getScopeArray());
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
    }

    private void signIn() {
        Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(intent, 111);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 111){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if(result.isSuccess()){
                final GoogleSignInAccount acc = result.getSignInAccount();

                String urlCheckUserExistOrNot = url_checkUser + acc.getEmail();
                StringRequest stringReq = new StringRequest(Request.Method.GET, urlCheckUserExistOrNot, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(!response.equals("user doesn't exist")){
                            // Save id_user to SharedPreferences
                            SharedPreferences sharedPreferences = getSharedPreferences("sharedpref_id_user",MODE_PRIVATE);
                            SharedPreferences.Editor edit = sharedPreferences.edit();
                            edit.putString("sharedpref_id_user", response);
                            edit.commit();

                            Intent i = new Intent(SigninActivity.this, MainActivity.class);
                            i.putExtra("name", acc.getDisplayName());
                            i.putExtra("email", acc.getEmail());
                            i.putExtra("avatar", acc.getPhotoUrl().toString());
                            startActivity(i); finish();
                        }else{
                            insertUser(acc.getDisplayName(), acc.getEmail(), acc.getPhotoUrl().toString());
                            Intent i = new Intent(SigninActivity.this, MainActivity.class);
                            i.putExtra("name", acc.getDisplayName());
                            i.putExtra("email", acc.getEmail());
                            i.putExtra("avatar", acc.getPhotoUrl().toString());
                            startActivity(i); finish();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SigninActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

                Volley.newRequestQueue(this).add(stringReq);

            }
        }else{
            Toast.makeText(this, "Login failed", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveUserIDtoSharedPreferences(String email){
        String urlCheck = url_checkUser + email;
        StringRequest stringReq = new StringRequest(Request.Method.GET, urlCheck, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                SharedPreferences sharedPreferences = getSharedPreferences("sharedpref_id_user",MODE_PRIVATE);
                SharedPreferences.Editor edit = sharedPreferences.edit();
                edit.putString("sharedpref_id_user", response);
                edit.commit();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SigninActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        Volley.newRequestQueue(this).add(stringReq);
    }

    private void insertUser(final String name, final String email, final String avatar){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_insert, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equals("insert success")){
                    Toast.makeText(SigninActivity.this, "Account has registered successful", Toast.LENGTH_SHORT).show();
                    saveUserIDtoSharedPreferences(email);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SigninActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String,String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("name_user", name);
                params.put("email", email);
                params.put("avatar", avatar);
                params.put("is_admin", "0");

                return params;
            }
        };

        RequestQueue reqQueue = Volley.newRequestQueue(this);
        reqQueue.add(stringRequest);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
