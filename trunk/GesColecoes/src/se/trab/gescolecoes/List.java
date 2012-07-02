package se.trab.gescolecoes;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class List extends Activity {

	Button back;
	ListView listView;
	DBAdapter bd;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		bd = new DBAdapter(this);
		bd.open();

		mostraLista();

	}

	public void mostraLista() {
		setContentView(R.layout.lista);
		Button voltar = (Button) findViewById(R.id.back);
		ListView lista = (ListView) findViewById(R.id.lista);

		String[] from = new String[] { "_id", "tipo", "titulo", "autor" };
		int[] to = new int[] { R.id.id_item, R.id.type, R.id.title, R.id.author };

		Cursor cursor = bd.getItems();

		SimpleCursorAdapter listaItens = new SimpleCursorAdapter(this,
				R.layout.item, cursor, from, to);
		listaItens.notifyDataSetChanged();

		lista.setAdapter(listaItens);

		lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				int i = Integer.parseInt(((TextView) ((LinearLayout) view)
						.getChildAt(0)).getText().toString());
				mostraDetalhe(i);
			}
		});

	}
	
	public void mostraDetalhe(int i) {
		setContentView(R.layout.detail);

		Button back = (Button) findViewById(R.id.back);
		Button backSistema = (Button)findViewById(R.id.back_button);
		
		TextView idItem = (TextView)findViewById(R.id.id_item);
		TextView type = (TextView)findViewById(R.id.type);
//		TextView idItem = (TextView)findViewById(R.id.id_item);
//		TextView idItem = (TextView)findViewById(R.id.id_item);
//		TextView idItem = (TextView)findViewById(R.id.id_item);

		String from[] = new String[] { "_id", "tipo", "titulo",
				"autor", "editor", "ano_pub", "edicao", "ext_tipo",
				"obs_pess" };

		int to[] = new int[] { R.id.id_item, R.id.type, R.id.title,
				R.id.author, R.id.editor, R.id.year, R.id.edition,
				R.id.ext_type, R.id.obs_pess };

		Cursor cursor = bd.getItem("_id", Integer.toString(i));
		
		idItem.setText(cursor.getString(0));
		type.setText(cursor.getString(1));
		
		final String contents = "teste";
		
	  	backSistema.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					mostraLista();
					

				}

	
			});
	  	
	  	back.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					mostraLista();
				}
			});
		
		
//		SimpleCursorAdapter detail; 
//				
//		detail = new SimpleCursorAdapter(this, R.layout.detail, cursor, from, to);
		
		

	}
	

}
