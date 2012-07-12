package se.trab.gescolecoes;

import java.util.Hashtable;

import se.trab.gescolecoes.HTTP.HttpData;
import se.trab.gescolecoes.HTTP.HttpRequest;
import se.trab.gescolecoes.HTTP.PostData;
import se.trab.gescolecoes.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends Activity {
    EditText user, pass;
    Button login;
    Login act;
    int result=-1; 

    
    public static final int REQUEST_WIRELESS = 1; //Acção que pretendemos realizar
    public static final int REQUEST_WIRELESS_2 = 2;//Outra acção
	
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        requestWindowFeature(Window.FEATURE_PROGRESS);
        setContentView(R.layout.login);
        user = (EditText)findViewById(R.id.user);
        pass = (EditText)findViewById(R.id.pass);
        login = (Button)findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				testeConnWeb();
			}
		});
    }
    
    public void testeConnWeb() {
    	boolean haRede = isNetworkAvailable();
    	if(!haRede) {
    		ativaRede(REQUEST_WIRELESS);
    	} else {
    		entrar();
    	}

    	
    }
    
    public void entrar() {
    	String u = user.getText().toString();
    	String p = pass.getText().toString();
    	String url = "http://10.0.2.2:81/login/log.php";
    	Hashtable<String,String> ht = new Hashtable<String,String>();
    	ht.put("user", u);
    	ht.put("pass", p);
    	try {
    		
    		act=this;
    		setProgressBarIndeterminateVisibility(true);
    		
 	
		    HttpData dados = HttpRequest.post(url, ht);
		    setProgressBarIndeterminateVisibility(false);
		    
		    Toast.makeText(this, dados.content, Toast.LENGTH_LONG).show();
		    
		    if ((dados.content=="")||(dados.content.contains("ERRO")))
				setResult(1);
			else
				setResult(0);
		    
		} catch (Exception e) {
			setResult(1);
			e.printStackTrace();
		}
    	finish();
    }
    
    public boolean isNetworkAvailable() {
	    ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null;
	}
    
    public void ativaRede(final int codigo) {
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setMessage(getResources().getString(R.string.activateNetwork))
    	       .setCancelable(false)
    	       .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener(){
    	           public void onClick(DialogInterface dialog, int id) {
    	                ativaRede2(codigo);
    	               
    	           }
    	       })
    	       .setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
    	           public void onClick(DialogInterface dialog, int id) {
    	                dialog.cancel();
    	                setResult(1);
    	           }
    	       });
    	AlertDialog alert = builder.create();
    	alert.show();
    }

    public void ativaRede2(int codigo) {
    	Intent i = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
            startActivityForResult(i,codigo);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        switch(requestCode) {
        case REQUEST_WIRELESS:
          	Log.d("onActivityResult", "REQUEST_WIRELESS");
          	entrar();
           	break;
        }
    }



}
