package se.trab.gescolecoes;

import java.util.Hashtable;

import org.json.JSONArray;
import org.json.JSONObject;

import se.trab.gescolecoes.HTTP.HttpData;
import se.trab.gescolecoes.HTTP.HttpRequest;
import android.R.string;
import android.widget.Toast;

public class AcessoServidor {
	
	public JSONObject GetItemFromServer(String label, String value) {
		JSONObject jsobj = null;
    	String url = "http://192.168.1.111/GetViewGeral.ashx";
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

	public JSONArray GetBackupFromServer(String value) {
		JSONArray jsArr = null;
    	String url = "http://192.168.1.111/ListUserFullBackup.ashx";
    	Hashtable<String,String> ht = new Hashtable<String,String>();
    	ht.put("idUser", value);
    	try {
    		
		    HttpData dados = HttpRequest.post(url, ht);
	    
		    if (!dados.content.contains("{"))
		    	 jsArr= null;
			else
				jsArr = new JSONArray(dados.content);
		    
		} catch (Exception e) {
			
			e.printStackTrace();
		}
        return jsArr;
    }
	
	public int SetBackuptoServer(String user, JSONArray value) {
		int res = -1;
		
    	String url = "http://192.168.1.111/SetUserFullBackup.ashx";
    	Hashtable<String,String> ht = new Hashtable<String,String>();
    	//http://gfiptnmanso/SetUserFullBackup.ashx?idUser=1&data=>>>>dados<<<< 
    	ht.put("idUser", user);
    	ht.put("data",value.toString());
    	try {
    		
		    HttpData dados = HttpRequest.post(url, ht);
	    
		    if (dados.content.contains("Error"))
		    	res= -1;
			else
				res = Integer.parseInt(dados.content);
		    
		} catch (Exception e) {
			e.printStackTrace();
		}
        return res;
    }
}
