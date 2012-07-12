package se.trab.gescolecoes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.database.Cursor;
import android.widget.Toast;

public class BackupRestore {
	DBAdapter bd;
	
	public void backUp(Context ctx){
		bd = new DBAdapter(ctx);
		bd.open();
		
		Cursor cur = bd.getItems();
		//[{"id":12,"idUser":1,"barcode":"123456                                            ",
		//  "TipoItem":"Livro","titulo":"Teste",
		//   "Editor":"Editor1","Autores":"Ze, Paulo, Jimbas",
		//   "ano":"2011","edicao":"1","qrcode":"qwertyu112345",
		//   "ExtTipoItem":"Livro Capa Dura","Obs":"Assinado pelos autores"
		// },
		// {"id":13,"idUser":1,"barcode":"09876543312344                                    ",
		//  "TipoItem":"Livro","titulo":"Livro do 1º Teste",
		//  "Editor":"Editor do Livro","Autores":"Nelson, António, Paulo",
		//  "ano":"2012","edicao":"12","qrcode":"SE-2012",
		//  "ExtTipoItem":"Livro","Obs":"Granda Malha"
		// }
		//]
		//[{"id":"295","Editor":"editor1","titulo":"Senhor dos Aneis - A Irmandade do Anel","Obs":"Edição Colecionador Autografada","edicao":"1a","TipoItem":"Filme","ExtTipoItem":"DVD","qrcode":"931983712973","ano":"2001","barcode":"1234567890","idUser":"1","Autores":"autor1"},{"id":"295","Editor":"editor1","titulo":"Senhor dos Aneis - A Irmandade do Anel","Obs":"Edição Colecionador Autografada","edicao":"1a","TipoItem":"Filme","ExtTipoItem":"DVD","qrcode":"931983712973","ano":"2001","barcode":"1234567890","idUser":"1","Autores":"autor1"}]
		
		// "_id", "tipo", "titulo",
		//"autor", "editor", "ano_pub", "edicao", "qrcode", "barcode",
		//"ext_tipo", "obs_pess" 
		
		JSONArray jsArr= new JSONArray();
		cur.moveToFirst();
		for (int numRow = 0; numRow < cur.getCount(); numRow++)
		{
			JSONObject json = new JSONObject();	
			try {
				
				json.put("id",cur.getString(cur.getColumnIndex("_id")));
				json.put("idUser","1");
				json.put("barcode",cur.getString(cur.getColumnIndex("barcode")));
				json.put("TipoItem",cur.getString(cur.getColumnIndex("tipo")));
				json.put("titulo",cur.getString(cur.getColumnIndex("titulo")));
				json.put("Editor",cur.getString(cur.getColumnIndex("editor")));
				json.put("Autores",cur.getString(cur.getColumnIndex("autor")));
				json.put("ano",cur.getString(cur.getColumnIndex("ano_pub")));
				json.put("edicao",cur.getString(cur.getColumnIndex("edicao")));
				json.put("qrcode",cur.getString(cur.getColumnIndex("qrcode")));
				json.put("ExtTipoItem",cur.getString(cur.getColumnIndex("ext_tipo")));
				json.put("Obs",cur.getString(cur.getColumnIndex("obs_pess")));
			} catch (JSONException e) {
				e.printStackTrace();
			}
	        jsArr.put(json);
	        cur.moveToNext();
		}
		cur.close();
		
		AcessoServidor as = new AcessoServidor();
		
		int res = as.SetBackuptoServer("1", jsArr);
		
		if (res<0)
		{
			Toast.makeText(ctx, "ERRO no Backup", Toast.LENGTH_LONG);
		}
		else if (res==0)
		{
			Toast.makeText(ctx, "Não foram armazenados registos", Toast.LENGTH_LONG);
		}
		else
		{
			Toast.makeText(ctx, "Foram armazenados " + res +" registos", Toast.LENGTH_LONG);
		}
		
		bd.close();
	}

	public void restore(Context ctx){
		
		AcessoServidor as = new AcessoServidor();
		JSONArray jarr = as.GetBackupFromServer("1");
		
		
		if (jarr!=null)
		{
			bd = new DBAdapter(ctx);
			bd.open();
			bd.deleteAllItems();
			for (int numRow = 0; numRow < jarr.length(); numRow++)
			{
				try {
					
					JSONObject jo = jarr.getJSONObject(numRow);
					
					Item it = new Item("1",jo);
					
					bd.insertItem(it);
					
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			
			
			if (jarr.length()==0)
			{
				Toast.makeText(ctx, "Não foram recuperados registos", Toast.LENGTH_LONG);
			}
			else
			{
				Toast.makeText(ctx, "Foram recuperados " + jarr.length() +" registos", Toast.LENGTH_LONG);
			}
			bd.close();
		}else
			Toast.makeText(ctx, "ERRO no restore", Toast.LENGTH_LONG);

	}
	
}
