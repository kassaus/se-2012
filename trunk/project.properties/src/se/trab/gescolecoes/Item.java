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

	public Item(String tipo, String titulo, String autor, String editor,
			String ano_pub, String edicao, String qrcode, String barcode,
			String ext_tipo, String obs_pess, int id_coll) {

		this.tipo = tipo;
		this.titulo = titulo;
		this.autor = autor;
		this.editor = editor;
		this.ano_pub = ano_pub;
		this.edicao = edicao;
		this.qrcode = qrcode;
		this.barcode = barcode;
		this.ext_tipo = ext_tipo;
		this.obs_pess = obs_pess;
		this.id_coll = id_coll;
	}

	public Item() {

		this.tipo = "NA";
		this.titulo = "NA";
		this.autor = "NA";
		this.editor = "NA";
		this.ano_pub = "NA";
		this.edicao = "NA";
		this.qrcode = "NA";
		this.barcode = "NA";
		this.ext_tipo = "NA";
		this.obs_pess = "NA";
		this.id_coll = 0;
	}

	public Item(String UserID, JSONObject object) {
		try {
			this.tipo = object.getString("TipoItem");
			this.titulo = object.getString("titulo");
			this.autor = object.getString("Autores");
			this.editor = object.getString("Editor");
			this.ano_pub = object.getString("ano");
			this.edicao = object.getString("edicao");
			this.qrcode = object.getString("qrcode");
			this.barcode = object.getString("barcode");
			this.ext_tipo = object.getString("ExtTipoItem");
			this.obs_pess = object.getString("Obs");
			// this.id_coll=object.getInt("id_coll");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public Item(JSONObject object) {
		try {
			// {"nomeEditor":"Editor1",
			// "titulo":"Teste",
			// "edicao":5,
			// "qrcode":"1234567890",
			// "idItem":1,
			// "barcode":"1234567890                                        ",
			// "ano":2010,
			// "desTipoItem":"Livro",
			// "desExpTipoItem":"Livros Capa dura"}
			this.tipo = object.getString("desTipoItem");
			this.titulo = object.getString("titulo");
			this.autor = object.getString("autores");
			this.editor = object.getString("nomeEditor");
			this.ano_pub = String.valueOf(object.getInt("ano"));
			this.edicao = String.valueOf(object.getInt("edicao"));
			this.qrcode = object.getString("qrcode");
			this.barcode = object.getString("barcode");
			this.ext_tipo = object.getString("desExpTipoItem");
			// this.obs_pess=object.getString("obs_pess");
			// this.id_coll=object.getInt("id_coll");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public JSONObject GetJSON() {
		JSONObject object = new JSONObject();
		try {
			object.put("tipo", tipo);
			object.put("titulo", titulo);
			object.put("autor", autor);
			object.put("editor", editor);
			object.put("ano_pub", ano_pub);
			object.put("edicao", edicao);
			object.put("qrcode", qrcode);
			object.put("barcode", barcode);
			object.put("ext_tipo", ext_tipo);
			object.put("obs_pess", obs_pess);
			object.put("id_coll", id_coll);

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return object;
	}

}
