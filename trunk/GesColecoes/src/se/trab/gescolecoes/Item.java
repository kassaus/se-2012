package se.trab.gescolecoes;

import org.json.JSONException;
import org.json.JSONObject;

public class Item {
    public String tipo; 
    public String titulo; 
    public String autor;
    public String editor;
    public String ano_pub;
    public String edicao;
    public String qrcode;
    public String barcode;
    public String ext_tipo;
    public String obs_pess;
    public int id_coll;
    
    public Item ( 	String tipo,
    				String titulo,
    				String autor,
    				String editor,
    				String ano_pub,
    				String edicao,
    				String qrcode,
    				String barcode,
    				String ext_tipo,
    				String obs_pess,
					int id_coll
				){
    	
    	this.tipo=tipo; 
    	this.titulo=titulo; 
    	this.autor=autor;
    	this.editor=editor;
    	this.ano_pub=ano_pub;
    	this.edicao=edicao;
    	this.qrcode=qrcode;
    	this.barcode=barcode;
    	this.ext_tipo=ext_tipo;
    	this.obs_pess=obs_pess;
    	this.id_coll=id_coll;
    }
    
    
    public Item ( JSONObject object){
    	try {
			this.tipo=object.getString("tipo");
	    	this.titulo=object.getString("titulo"); 
	    	this.autor=object.getString("autor");
	    	this.editor=object.getString("editor");
	    	this.ano_pub=object.getString("ano_pub");
	    	this.edicao=object.getString("edicao");
	    	this.qrcode=object.getString("qrcode");
	    	this.barcode=object.getString("barcode");
	    	this.ext_tipo=object.getString("ext_tipo");
	    	this.obs_pess=object.getString("obs_pess");
	    	this.id_coll=object.getInt("id_coll");
    	} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

    }
    
    public JSONObject GetJSON() {
		JSONObject object = new JSONObject();
		try {
			object.put("tipo",tipo); 
			object.put("titulo",titulo); 
			object.put("autor",autor);
			object.put("editor",editor);
			object.put("ano_pub",ano_pub);
			object.put("edicao",edicao);
			object.put("qrcode",qrcode);
			object.put("barcode",barcode);
			object.put("ext_tipo",ext_tipo);
			object.put("obs_pess",obs_pess);
			object.put("id_coll",id_coll);
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return object;
	}
    
}
