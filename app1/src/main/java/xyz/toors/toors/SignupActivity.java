package xyz.toors.toors;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {

    EditText ed_fullname,ed_ingamename, ed_email, ed_password,ed_whatsapp,ed_gpay;
    String str_fullname,str_ingamename,str_email,str_password,str_whatsapp,str_gpay;
     String URL_REGISTER = "https://apptoors.000webhostapp.com/signup.php";
    private AdView mAdView,madView2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ed_fullname=findViewById(R.id.ed_fullname);
        ed_ingamename=findViewById(R.id.ed_ingamename);
        ed_email=findViewById(R.id.ed_email) ;
        ed_password=findViewById(R.id.ed_password);
        ed_whatsapp=findViewById(R.id.ed_whatsapp);
        ed_gpay=findViewById(R.id.ed_gpay); MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = findViewById(R.id.adView3);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        madView2 = findViewById(R.id.adView4);
        AdRequest adRequest1 = new AdRequest.Builder().build();
        madView2.loadAd(adRequest1);
    }
    public void saveData(){
        str_ingamename=ed_ingamename.getText().toString().trim();
        SharedPreferences sharedPreferences = getSharedPreferences("saveingamename",MODE_PRIVATE);
        SharedPreferences.Editor  editor = sharedPreferences.edit();
        editor.putString("ingamename",str_ingamename);
        editor.apply();
    }
    @Override
    protected void onPause(){
        super.onPause();
        saveData();
    }
    public void register(View view)
    {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        if(ed_fullname.getText().toString().equals(""))
        {
            Toast.makeText(this, "Enter Full Name", Toast.LENGTH_SHORT).show();
        }else if(ed_ingamename.getText().toString().equals(""))
        {
            Toast.makeText(this, "Enter In Game Name", Toast.LENGTH_SHORT).show();
        }
        else if( ed_email.getText().toString().equals(""))
        {
            Toast.makeText(this, "Enter Email", Toast.LENGTH_SHORT).show();
        }
        else if(ed_password.getText().toString().equals(""))
        {
            Toast.makeText(this, "Enter Password", Toast.LENGTH_SHORT).show();
        }
        else if(ed_whatsapp.getText().toString().equals(""))
        {
            Toast.makeText(this, "Enter Whatsapp Number", Toast.LENGTH_SHORT).show();
        }
        else if(ed_gpay.getText().toString().equals(""))
        {
            Toast.makeText(this, "Enter Paytm/PhonePe/GPay Number", Toast.LENGTH_SHORT).show();
        }
        else
        {
            progressDialog.show();
            str_fullname=ed_fullname.getText().toString().trim();
            str_ingamename=ed_ingamename.getText().toString().trim();
            str_email=ed_email.getText().toString();
            str_password=ed_password.getText().toString().trim();
            str_whatsapp=ed_whatsapp.getText().toString().trim();
            str_gpay=ed_gpay.getText().toString().trim();

            StringRequest request = new StringRequest(Request.Method.POST, URL_REGISTER, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    progressDialog.dismiss();
                    if(response.equalsIgnoreCase("registered successfully")) {
                        Toast.makeText(SignupActivity.this, response, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                    }
                    else
                    {
                        Toast.makeText(SignupActivity.this,response, Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    Toast.makeText(SignupActivity.this,error.getMessage().toString(),Toast.LENGTH_SHORT).show();
                }
            }
            ){
                @Override
                protected Map<String,String> getParams() throws AuthFailureError{
                    Map<String,String> params=new HashMap<String,String>();
                    params.put("fullname",str_fullname);
                    params.put("ingamename",str_ingamename);
                    params.put("email",str_email);
                    params.put("password",str_password);
                    params.put("whatsapp",str_whatsapp);
                    params.put("gpay",str_gpay);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(SignupActivity.this);
            requestQueue.add(request);
        }
    }


    public void login(View view){
        finish();
        Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
        startActivity(intent);
    }

}