package se.trab.gescolecoes.HTTP;

import java.util.Hashtable;

public class PostData {
	public String url;
	public Hashtable<String,String> dados;
	
	public PostData(String url, Hashtable<String,String> dados){
		this.url = url;
		this.dados = dados;
	}
}

