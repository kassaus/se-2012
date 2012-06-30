package se.trab.gescolecoes;

import se.trab.gescolecoes.DBAdapter;
import se.trab.gescolecoes.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
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

	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);       
       
       findViewById(R.id.logo);        
        capturar = (Button)findViewById(R.id.capturar);
        listar = (Button)findViewById(R.id.listar);
        
        bd = new DBAdapter(this);
        bd.open();
    	
//    	Intent login = new Intent(this,Login.class);  
//        startActivityForResult(login, 0);
     
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
        
		bd.deleteAllItems();
		
		Item tmpItem = new Item("Filme", "Senhor dos Aneis - A Irmandade do Anel", "autor1", "editor1", "2001", "1a", "qrcode", "", "DVD", "Edição Colecionador Autografada", 1);
		bd.insertItem(tmpItem);
		

		tmpItem = new Item("Filme", "Senhor dos Aneis - A Irmandade do Anel", "autor1", "editor1", "2001", "1a", "qrcode", "", "DVD", "Edição Colecionador Autografada", 1);
		bd.insertItem(tmpItem);
		tmpItem = new Item("Filme", "Senhor dos Aneis - As Duas Torres", "autor1", "editor1", "2002", "1a", "qrcode", "", "DVD", "Edição Colecionador Autografada",1);
		bd.insertItem(tmpItem);
		tmpItem = new Item("Filme", "Senhor dos Aneis - O Regresso do Rei", "autor1", "editor1", "2003", "1a", "qrcode", "", "DVD", "Edição Colecionador Autografada",1);
		bd.insertItem(tmpItem);
		tmpItem = new Item("Filme", "O Hobbit ou Lá e de Volta Outra Vez", "autor2", "editor2", "2010", "Pre-Release", "qrcode", "", "DVD", "Edição Pirata",1);
		bd.insertItem(tmpItem);
		tmpItem = new Item("Filme", "A Mascara de Zorro", "autor3", "editor3", "2004", "qrcode", "", "", "DVD", "Acção e Cat. Zeta Jones",1);
		bd.insertItem(tmpItem);
		tmpItem = new Item("Filme", "Pontes de Maddisson County", "autor3", "editor3", "2007", "", "", "", "DVD", "Granda Seca",1);
		bd.insertItem(tmpItem);
        
             
    }
    
    public void capturarCodigo()
    {
    	
    	
    	try {
	    	Intent scan = new Intent("com.google.zxing.client.android.SCAN"); 
	       // intent.putExtra("SCAN_MODE", "PRODUCT_MODE");
	        startActivityForResult(scan, 1);
    	} catch (ActivityNotFoundException anfex) {
            Log.e("capturarCodigo", "Não Encontrou o scanner", anfex);
        }      	
    	
    }
    
    
    public void listarItems()
    {
//    	Toast.makeText(this, "VAMOS LISTAR", Toast.LENGTH_SHORT).show();
    	mostraLista();
    }
    
    @Override
    public void onDestroy(){
    	super.onDestroy(); 
    	bd.close();
    }
    
    public void onActivityResult(int requestCode, int resultCode, Intent intent) 
    {
    	 if (requestCode == 0) {
    		 //APAGAR
    		 resultCode=0;
         	trataResultadoActivityLogin(resultCode,intent);
         }
        if (requestCode == 1) {
        	trataResultadoActivityScan(resultCode,intent);
        }
    } // onActivityResult (fim)
 
    
    public void trataResultadoActivityLogin(int resultCode, Intent intent){
    	if (resultCode==1){
    		Toast toast = Toast.makeText(this, "@string/loginError" , Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP, 25, 400);
            toast.show();
    	}
    }
    
    public void trataResultadoActivityScan(int resultCode, Intent intent){
        if (resultCode == RESULT_OK) {
            final String contents = intent.getStringExtra("SCAN_RESULT");
            String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
            // Handle successful scan
            Toast toast = Toast.makeText(this, "Content:" + contents + " Format:" + format , Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP, 25, 400);
            toast.show();
            
            if (format.contains("QR_CODE"))
            {
            	
            	 Cursor cqr = bd.getItem("qrcode", contents);
            	 if (cqr.getCount()>0)
            	 {
            		 // mostrar resultados
            	 }
            	// Gerar JSON para QRCODE
            }
            else//Os barcodes podem ter varios tipos de especificação e o formato identifica-os
            {
            	Cursor ccb = bd.getItem("barcode", contents);
            	if (ccb.getCount()>0)
	           	{
            		// mostrar resultados
	           	}
            	// Gerar JSON para BARCODE
            }
            
            // Enviar pedido de consulta de dados ao servidor
            
            // Recolher Resposta
            
            // Recolhe todos os items
            Cursor c = bd.getItems();
        	
        	FuzzySearch fz = new FuzzySearch();
            
        	// Fazer Busca no resultado pelos dados Nome Autor editor
        	
        	// TESTE FUZZYSEARCH
        	String str = "Irma Dadé - Anel de Sangue";
        	
            fz.testeArrayStrings(c, str);
            // FIM TESTE
            
            // Apresentar resultados se houver
            
        	//Toast.makeText(this, "VAMOS CAPTURAR", Toast.LENGTH_SHORT).show();
        	
        	c.close();
        	
            //pesquisa_livro(contents); substituido pela alert dialog
//              final CharSequence[] items = {"Inserir", "Pesquisar", "Voltar"};

//              AlertDialog.Builder builder = new AlertDialog.Builder(this);
//              builder.setTitle("Escolha a opção:"); //Inserir, Pesquisar ou Voltar
//              builder.setItems(items, new DialogInterface.OnClickListener() {
//                  public void onClick(DialogInterface dialog, int item) {
//                  	if (item == 0){
//                  		dialog.dismiss();
//                  		mostraAlertDialog(contents);
                		//guardaLivro(contents);
//                  		}
//                  	else if (item == 1)
                		//pesquisa_livro(contents);
//                  		pesquisa_objecto(contents);
//                  }
//              });
//              AlertDialog alert = builder.create();
//              alert.show();
            

            
            
        } else if (resultCode == RESULT_CANCELED) {
            // Handle cancel
            Toast toast = Toast.makeText(this, "Scan was Cancelled!", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP, 25, 400);
            toast.show();               
        }
    }// trataResultadoActivityScan (fim)
    
    
    
    

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
		
		TextView idItem = (TextView)findViewById(R.id.id_item);
		TextView type = (TextView)findViewById(R.id.type);
//		TextView idItem = (TextView)findViewById(R.id.id_item);
//		TextView idItem = (TextView)findViewById(R.id.id_item);
//		TextView idItem = (TextView)findViewById(R.id.id_item);

//		String from[] = new String[] { "_id", "tipo", "titulo",
//				"autor", "editor", "ano_pub", "edicao", "ext_tipo",
//				"obs_pess" };
//
//		int to[] = new int[] { R.id.id_item, R.id.type, R.id.title,
//				R.id.author, R.id.editor, R.id.year, R.id.edition,
//				R.id.ext_type, R.id.obs_pess };

//		Cursor cursor = bd.getItem("_id", Integer.toString(i));
		
		Cursor cursor = bd.getItemById(i);
		cursor.moveToFirst();
		
		idItem.setText(cursor.getString(cursor.getColumnIndex("_id")));

		type.setText(cursor.getString(cursor.getColumnIndex("tipo")));
		
		
//		SimpleCursorAdapter detail; 
//				
//		detail = new SimpleCursorAdapter(this, R.layout.detail, cursor, from, to);
		
		

	}
	

}