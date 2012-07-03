package se.trab.gescolecoes;

import java.util.Hashtable;

import org.json.JSONObject;

import se.trab.gescolecoes.HTTP.HttpData;
import se.trab.gescolecoes.HTTP.HttpRequest;
import android.widget.Toast;

public class AcessoServidor {
	
	public JSONObject GetItemFromServer(String label, String value) {
		JSONObject jsobj = null;
    	String url = "http://10.0.2.2/GetViewGeral.ashx";
    	Hashtable<String,String> ht = new Hashtable<String,String>();
    	ht.put(label, value);
    	try {
    		
		    HttpData dados = HttpRequest.post(url, ht);
	    
		    if (!dados.content.contains("{"))
		    	 jsobj = null;
			else
				jsobj = new JSONObject(dados.content);
		    
		} catch (Exception e) {
			
			e.printStackTrace();
		}
        return jsobj;
    }

}
