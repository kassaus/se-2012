package se.trab.gescolecoes;

import org.json.JSONException;
import org.json.JSONObject;

public class User {

	private String user;
	private String pass;
	
	public User(String us, String pa){
		user=us;
		pass=pa;
	}
	
	public JSONObject GetJSON() {
		JSONObject object = new JSONObject();
		try {
			object.put("user", user);
			object.put("pass", pass);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return object;
	}

}
