package se.trab.gescolecoes;

import org.json.JSONObject;

import se.trab.gescolecoes.DBAdapter;
import se.trab.gescolecoes.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class GesColecoes extends Activity {

	DBAdapter bd;

	Button capturar;
	Button listar;

	Activity act = this;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		bd = new DBAdapter(this);
		bd.open();
		// insereValoresTesteBD();
		
//		bd.deleteAllItems();
		ecranInicial();
	}

	public void ecranInicial() {
		setContentView(R.layout.main);

		findViewById(R.id.logo);
		capturar = (Button) findViewById(R.id.capturar);
		listar = (Button) findViewById(R.id.listar);

		capturar.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				capturarCodigo();
			}
		});

		listar.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				listarItems();
			}
		});
	}

	//
	// public void insereValoresTesteBD() {
	// bd.deleteAllItems();
	//
	// Item tmpItem = new Item("Filme",
	// "Senhor dos Aneis - A Irmandade do Anel", "autor1", "editor1",
	// "2001", "1a", "qrcode", " ", "DVD",
	// "Edi��o Colecionador Autografada", 1);
	// bd.insertItem(tmpItem);
	//
	// tmpItem = new Item("Filme", "Senhor dos Aneis - A Irmandade do Anel",
	// "autor1", "editor1", "2001", "1a", "qrcode", " ", "DVD",
	// "Edi��o Colecionador Autografada", 1);
	// bd.insertItem(tmpItem);
	// tmpItem = new Item("Filme", "Senhor dos Aneis - As Duas Torres",
	// "autor1", "editor1", "2002", "1a", "qrcode", " ", "DVD",
	// "Edi��o Colecionador Autografada", 1);
	// bd.insertItem(tmpItem);
	// tmpItem = new Item("Filme", "Senhor dos Aneis - O Regresso do Rei",
	// "autor1", "editor1", "2003", "1a", "qrcode", " ", "DVD",
	// "Edi��o Colecionador Autografada", 1);
	// bd.insertItem(tmpItem);
	// tmpItem = new Item("Filme", "O Hobbit ou L� e de Volta Outra Vez",
	// "autor2", "editor2", "2010", "Pre-Release", "qrcode", " ",
	// "DVD", "Edi��o Pirata", 1);
	// bd.insertItem(tmpItem);
	// tmpItem = new Item("Filme", "A Mascara de Zorro", "autor3", "editor3",
	// "2004", "qrcode", " ", " ", "DVD", "Ac��o e Cat. Zeta Jones", 1);
	// bd.insertItem(tmpItem);
	// tmpItem = new Item("Filme", "Pontes de Maddisson County", "autor3",
	// "editor3", "2007", " ", " ", " ", "DVD", "Granda Seca", 1);
	// bd.insertItem(tmpItem);
	// }

	public void capturarCodigo() {

		try {
			Intent scan = new Intent("com.google.zxing.client.android.SCAN");
			startActivityForResult(scan, 1);
		} catch (ActivityNotFoundException anfex) {
			Log.e("capturarCodigo", "N�o Encontrou o scanner", anfex);
		}

	}

	public void listarItems() {
		mostraLista();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		bd.close();
	}

	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode == 0) {
			// APAGAR
			resultCode = 0;
			trataResultadoActivityLogin(resultCode, intent);
		}
		if (requestCode == 1) {
			trataResultadoActivityScan(resultCode, intent);
		}
	} // onActivityResult (fim)

	public void trataResultadoActivityLogin(int resultCode, Intent intent) {
		if (resultCode == 1) {
			Toast toast = Toast.makeText(this, "@string/loginError",
					Toast.LENGTH_LONG);
			toast.setGravity(Gravity.TOP, 25, 400);
			toast.show();
		}
	}

	public void trataResultadoActivityScan(int resultCode, Intent intent) {
		if (resultCode == RESULT_OK) {
			
			
			final String contents = intent.getStringExtra("SCAN_RESULT");
			//final String contents = "972-618-425-5";
			String format = intent.getStringExtra("SCAN_RESULT_FORMAT");

			Cursor cqr;

			if (format.contains("QR_CODE")) {
				cqr = bd.getItem("qrcode", contents);
				if (cqr.getCount() > 0) {
					mostraCaptura1(cqr, contents, format);
				}
			}
			else// Os barcodes podem ter varios tipos de especifica��o e o
					// formato identifica-os
			{
				cqr = bd.getItem("barcode", contents);
				if (cqr.getCount() > 0) {
					mostraCaptura1(cqr, contents, format);
				}
			}
	
			if (cqr == null || cqr.getCount()==0){
				mostraCaptura1(null, contents, format);
			}

		} else if (resultCode == RESULT_CANCELED) {
			// Handle cancel
			Toast toast = Toast.makeText(this, "@string/scanCancelled",
					Toast.LENGTH_LONG);
			toast.setGravity(Gravity.TOP, 25, 400);
			toast.show();
		}
	}// trataResultadoActivityScan (fim)

	private void mostraCaptura1(Cursor cursor, String contents, String format) {
		setContentView(R.layout.captura1);
		ListView lista = (ListView) findViewById(R.id.listaIguais);
		TextView codigo = (TextView) findViewById(R.id.codigo);
		Button descartar = (Button) findViewById(R.id.descartar);
		Button adicionar = (Button) findViewById(R.id.adicionar);

		final String _contents = contents;
		final String _format = format;

		codigo.setText("C�digo " + contents + " em formato " + format);

		// verifica se h� items com o mesmo c�digo
		if (cursor != null && cursor.getCount() > 0) {

			String[] from = new String[] { "_id", "tipo", "titulo", "autor",
					"obs_pess" };
			int[] to = new int[] { R.id.id_item, R.id.type, R.id.title,
					R.id.author, R.id.obsTxt };

			SimpleCursorAdapter listaItens = new SimpleCursorAdapter(this,
					R.layout.item_igual, cursor, from, to);

			listaItens.notifyDataSetChanged();

			lista.setAdapter(listaItens);
		}

		descartar.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				ecranInicial();
			}
		});

		adicionar.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				mostraCaptura2(_contents, _format);
			}
		});

	}

	private void mostraCaptura2(String contents, String format) {

		setContentView(R.layout.captura2);
		ListView lista = (ListView) findViewById(R.id.listaSimilares);
		Button descartar = (Button) findViewById(R.id.descartar);
		Button adicionar = (Button) findViewById(R.id.adicionar);

		TextView tipo = (TextView) findViewById(R.id.tipo);
		TextView titulo = (TextView) findViewById(R.id.titulo);
		TextView autor = (TextView) findViewById(R.id.autor);
		TextView anoPub = (TextView) findViewById(R.id.anoPub);
		TextView edicao = (TextView) findViewById(R.id.edicao);
		TextView editor = (TextView) findViewById(R.id.editor);
		AcessoServidor as = new AcessoServidor();

		JSONObject jo = new JSONObject();

		final Item item;

		// final Item item = null;
		// TODO apagar o de teste e colocar o de cima
		// item de teste
		// final Item item = new Item("Filme",
		// "Senhor dos Aneis - A Irmandade do Anel",
		// "autor1", "editor1", "2001", "1a", "qrcode", " ", "DVD",
		// "Edi��o Colecionador Autografada", 1);
		if (format.contains("QR_CODE")) {

			jo = as.GetItemFromServer("qrcode", contents);

		} else// Os barcodes podem ter varios tipos de especifica��o e o
				// formato identifica-os
		{
			jo = as.GetItemFromServer("barcode", contents);

		}

		if (jo != null) {
			item = new Item(jo);

			tipo.setText(item.tipo + " " + item.ext_tipo);
			titulo.setText(item.titulo);
			autor.setText(item.autor);
			anoPub.setText(item.ano_pub.toString());
			edicao.setText(item.edicao);
			editor.setText(item.editor);
		}

		else {
			item = new Item();
			tipo.setText("N�o encontrado");
			titulo.setText("N�o encontrado");
			autor.setText("N�o encontrado");
			anoPub.setText("N�o encontrado");
			edicao.setText("N�o encontrado");
			editor.setText("N�o encontrado");
		}

		// procurar no servidor pelo codigo, verificar nisso se temos rede
		// TODO

		String[] from = new String[] { "_id", "tipo", "titulo", "autor",
				"ano_pub", "edicao", "obs_pess" };
		int[] to = new int[] { R.id.id_item, R.id.type, R.id.title,
				R.id.author, R.id.anoPub, R.id.edicao, R.id.obs_pess };

		// agora get items do servidor
		// procurar o fuzzy search, se existirem similares, apresentar
		// o cursor agora tem de ser da tabela similar

		Cursor cursor = bd.getItems();
		FuzzySearch fz = new FuzzySearch();

		// Cursor cursorFz = fz.GetMatchCursor(cursor, "Ficam os aneis",
		// "autor3, autor 1" );
		// colocar este depois
		Cursor cursorFz = fz.GetMatchCursor(cursor, item.titulo, item.autor);

		SimpleCursorAdapter listaItens = new SimpleCursorAdapter(this,
				R.layout.item_similar, cursorFz, from, to);

		listaItens.notifyDataSetChanged();
		lista.setAdapter(listaItens);

		descartar.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				ecranInicial();
			}
		});

		adicionar.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				mostraCaptura3(item);
			}
		});

	}

	private void mostraCaptura3(final Item item) {

		setContentView(R.layout.captura3_comdetail);
		Button descartar = (Button) findViewById(R.id.descartar);
		Button adicionar = (Button) findViewById(R.id.adicionar);
		final EditText obsInsTxt = (EditText) findViewById(R.id.insObsTxt);
		int intResult;

		TextView tipo = (TextView) findViewById(R.id.type);
		TextView titulo = (TextView) findViewById(R.id.title);
		TextView autor = (TextView) findViewById(R.id.author);
		TextView editor = (TextView) findViewById(R.id.editor);
		TextView ano_edicao = (TextView) findViewById(R.id.editionYear);

		tipo.setText(item.tipo + " " + item.ext_tipo);
		titulo.setText(item.titulo);
		autor.setText(item.autor);
		editor.setText(item.editor);
		ano_edicao.setText(item.ano_pub);

		descartar.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				ecranInicial();
			}
		});

		adicionar.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				// item.obs_pess = obsInsTxt.getText(); TODO resolver o problema
				// de n�o ser edit�vel

				final String obs = obsInsTxt.getText().toString();
				item.obs_pess = obs;
				int insertResult = (int) bd.insertItem(item);
				// inserir na BD
				// fazer toast com o resultado
				makeToast("Inserido registo na BD local com o id "
						+ insertResult);
				ecranInicial();
			}
		});

	}

	public void makeToast(String texto) {
		Toast toast = Toast.makeText(this, texto, Toast.LENGTH_LONG);
		toast.setGravity(Gravity.TOP, 25, 400);
		toast.show();
	}

	//
	// //antiga, sem parametros
	// //TODO retirar depois
	// private void mostraCaptura1() {
	// setContentView(R.layout.captura1);
	// ListView lista = (ListView) findViewById(R.id.listaIguais);
	// TextView codigo = (TextView) findViewById(R.id.codigo);
	// Button descartar = (Button) findViewById(R.id.descartar);
	// Button adicionar = (Button) findViewById(R.id.adicionar);
	//
	// codigo.setText("Teste c�digo");
	//
	// String[] from = new String[] { "_id", "tipo", "titulo", "autor" };
	// int[] to = new int[] { R.id.id_item, R.id.type, R.id.title, R.id.author
	// };
	//
	// Cursor cursor = bd.getItems();
	//
	// SimpleCursorAdapter listaItens = new SimpleCursorAdapter(this,
	// R.layout.item, cursor, from, to);
	//
	// listaItens.notifyDataSetChanged();
	//
	// lista.setAdapter(listaItens);
	//
	// lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
	// public void onItemClick(AdapterView<?> parent, View view,
	// int position, long id) {
	//
	// int i = Integer.parseInt(((TextView) ((LinearLayout) view)
	// .getChildAt(0)).getText().toString());
	// mostraDetalhe(i);
	// }
	// });
	//
	// descartar.setOnClickListener(new View.OnClickListener() {
	// public void onClick(View v) {
	// ecranInicial();
	// }
	// });
	//
	// adicionar.setOnClickListener(new View.OnClickListener() {
	// public void onClick(View v) {
	// mostraCaptura2();
	// }
	// });
	//
	// }
	//
	// private void mostraCaptura2() {
	// setContentView(R.layout.captura2);
	// ListView lista = (ListView) findViewById(R.id.listaSimilares);
	// Button descartar = (Button) findViewById(R.id.descartar);
	// Button adicionar = (Button) findViewById(R.id.adicionar);
	//
	// String[] from = new String[] { "_id", "tipo", "titulo", "autor" };
	// int[] to = new int[] { R.id.id_item, R.id.type, R.id.title, R.id.author
	// };
	//
	// Cursor cursor = bd.getItems();
	//
	// SimpleCursorAdapter listaItens = new SimpleCursorAdapter(this,
	// R.layout.item, cursor, from, to);
	//
	// listaItens.notifyDataSetChanged();
	//
	// lista.setAdapter(listaItens);
	//
	// lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
	// public void onItemClick(AdapterView<?> parent, View view,
	// int position, long id) {
	//
	// int i = Integer.parseInt(((TextView) ((LinearLayout) view)
	// .getChildAt(0)).getText().toString());
	// mostraDetalhe(i);
	// }
	// });
	//
	// descartar.setOnClickListener(new View.OnClickListener() {
	// public void onClick(View v) {
	// ecranInicial();
	// }
	// });
	//
	// adicionar.setOnClickListener(new View.OnClickListener() {
	// public void onClick(View v) {
	// mostraCaptura3();
	// }
	// });
	//
	// }

	private void mostraCaptura3() {
		setContentView(R.layout.captura3);
		Button descartar = (Button) findViewById(R.id.descartar);
		Button adicionar = (Button) findViewById(R.id.adicionar);
		EditText obsInsTxt = (EditText) findViewById(R.id.insObsTxt);

		descartar.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				ecranInicial();
			}
		});

		adicionar.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

			}
		});

	}

	public void mostraLista() {
		setContentView(R.layout.lista);
		Button voltar = (Button) findViewById(R.id.back);
		ListView lista = (ListView) findViewById(R.id.lista);

		String[] from = new String[] { "_id", "tipo", "titulo", "autor" };
		int[] to = new int[] { R.id.id_item, R.id.type, R.id.title, R.id.author };

		Cursor cursor = bd.getItems();

		// R.layout.item,

		SimpleCursorAdapter listaItens = new SimpleCursorAdapter(this,
				R.layout.item, cursor, from, to);

		listaItens.notifyDataSetChanged();

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

		voltar.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				ecranInicial();
			}
		});

	}

	public void mostraDetalhe(int i) {
		setContentView(R.layout.detail);

		Button back = (Button) findViewById(R.id.back);
		// Button backSistema = (Button)findViewById(R.id.back_button);

		TextView idItem = (TextView) findViewById(R.id.id_item);
		TextView type = (TextView) findViewById(R.id.type);
		TextView title = (TextView) findViewById(R.id.title);
		TextView author = (TextView) findViewById(R.id.author);
		TextView editor = (TextView) findViewById(R.id.editor);
		TextView year = (TextView) findViewById(R.id.editionYear);
		TextView edition = (TextView) findViewById(R.id.edition);
		TextView code = (TextView) findViewById(R.id.code);
		// TextView ext_type = (TextView) findViewById(R.id.ext_type);

		TextView idCollection = (TextView) findViewById(R.id.id_collection);

		Cursor cursor = bd.getItemById(i);
		cursor.moveToFirst();

		String typeComposto = cursor.getString(cursor.getColumnIndex("tipo"))
				+ ", " + cursor.getString(cursor.getColumnIndex("ext_tipo"));
		idItem.setText(cursor.getString(cursor.getColumnIndex("_id")));
		type.setText(typeComposto);
		title.setText(cursor.getString(cursor.getColumnIndex("titulo")));
		author.setText(cursor.getString(cursor.getColumnIndex("autor")));
		editor.setText(cursor.getString(cursor.getColumnIndex("editor")));
		year.setText(cursor.getString(cursor.getColumnIndex("ano_pub")));
		edition.setText(cursor.getString(cursor.getColumnIndex("edicao")));
		code.setText(cursor.getString(cursor.getColumnIndex("qrcode")));
		// ext_type.setText(cursor.getString(cursor.getColumnIndex("ext_tipo")));

		if (cursor.getString(cursor.getColumnIndex("obs_pess")) != null) {
			TextView obs_pess = (TextView) findViewById(R.id.observations);
			obs_pess.setText(cursor.getString(cursor.getColumnIndex("obs_pess")));
		}

		idCollection
				.setText(cursor.getString(cursor.getColumnIndex("id_coll")));

		// backSistema.setOnClickListener(new View.OnClickListener() {
		// public void onClick(View v) {
		// mostraLista();
		// }
		//
		// });

		back.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				mostraLista();
			}
		});

	}

	// Menu de op��es (bot�o esquerdo)
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_principal, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		BackupRestore br = new BackupRestore();

		switch (item.getItemId()) {
		case R.id.backup:
			br.backUp(act);
			return true;
		case R.id.restore:
			br.restore(act);
			return true;
		case R.id.sair:
			super.finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

}