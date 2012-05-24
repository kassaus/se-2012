package se.trab.search;

import se.trab.search.DBAdapter;
import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import uk.ac.shef.wit.simmetrics.similaritymetrics.*;

public class FuzzySearch extends Activity {
	
	DBAdapter bd;
	
	Button capturar;
	Button listar;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        capturar = (Button)findViewById(R.id.capturar);
        listar = (Button)findViewById(R.id.listar);
        
        bd = new DBAdapter(this);
        bd.open();
     
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
        
		/*bd.deleteAllItems();
        bd.insertItem("Filme", "Senhor dos Aneis - A Irmandade do Anel", "autor1", "editor1", "2001", "1a", "", "", "DVD", "Edição Colecionador Autografada");
        bd.insertItem("Filme", "Senhor dos Aneis - As Duas Torres", "autor1", "editor1", "2002", "1a", "", "", "DVD", "Edição Colecionador Autografada");
        bd.insertItem("Filme", "Senhor dos Aneis - O Regresso do Rei", "autor1", "editor1", "2003", "1a", "", "", "DVD", "Edição Colecionador Autografada");
        bd.insertItem("Filme", "O Hobbit ou Lá e de Volta Outra Vez", "autor2", "editor2", "2010", "Pre-Release", "", "", "DVD", "Edição Pirata");
        bd.insertItem("Filme", "A Mascara de Zorro", "autor3", "editor3", "2004", "", "", "", "DVD", "Acção e Cat. Zeta Jones");
        bd.insertItem("Filme", "Pontes de Maddisson County", "autor3", "editor3", "2007", "", "", "", "DVD", "Granda Seca");
        */
		
        //Constroi lista de items
        Cursor c = bd.getItems();
        
        testeArrayStrings(c);
        
             
    }
    
    public void capturarCodigo()
    {
    	Toast.makeText(this, "VAMOS CAPTURAR", Toast.LENGTH_SHORT).show();
    }
    
    
    public void listarItems()
    {
    	Toast.makeText(this, "VAMOS LISTAR", Toast.LENGTH_SHORT).show();
    }
    
    public float[] MybatchCompareSetAVG(Cursor set, String comparatorNome)
    {
    	float[] results = null;
    	if(set.moveToFirst()) {
    		results = new float[set.getCount()];

    		AbstractStringMetric metric1 = new Levenshtein(); 

    		AbstractStringMetric metric2 = new CosineSimilarity(); 

    		AbstractStringMetric metric3 = new EuclideanDistance(); 

    		AbstractStringMetric metric4 = new MongeElkan(); 
    		AbstractStringMetric metric5 = new DiceSimilarity(); 
    		AbstractStringMetric metric6 = new JaroWinkler(); 
    		AbstractStringMetric metric7 = new Jaro(); 
    		AbstractStringMetric metric8 = new MatchingCoefficient(); //pode ser utilizado para retirar os que não têm Match
    		AbstractStringMetric metric9 = new NeedlemanWunch (); 

    		AbstractStringMetric metric10 = new OverlapCoefficient (); 

    		AbstractStringMetric metric11 = new QGramsDistance(); 
    		AbstractStringMetric metric12 = new SmithWatermanGotoh (); 
    		AbstractStringMetric metric13 = new SmithWatermanGotohWindowedAffine (); 
    		AbstractStringMetric metric14 = new Soundex();



    		for (int strNum = 0; strNum < set.getCount(); strNum++)
    		{
    			String sTitulo = set.getString(2);
    			float resultMongeElkan = metric4.getSimilarity(sTitulo.toUpperCase(), comparatorNome.toUpperCase());
    			//float resultJaroWinkler = metric6.getSimilarity(sTitulo.toUpperCase(), comparatorNome.toUpperCase());
    			float SmithWatermanGotoh = metric12.getSimilarity(sTitulo.toUpperCase(), comparatorNome.toUpperCase());
    			//results[strNum] = ((resultMongeElkan * 2)+(resultEuclideanDistance)+(resultSmithWatermanGotoh*2))/5;
    			results[strNum] = (resultMongeElkan + SmithWatermanGotoh)/2;
    			set.moveToNext();
    		}
    	}
    	//set.close();
    	return results;
    }
    

    
    public void testeArrayStrings(Cursor ListaItems){
    	// String str1 = "Magali";
//    	String[] lista;
        String str = "Irma Dadé - Anel de Sangue";
    	//String str = "o Senhor dos Aneis - O Regresso do Rei";
//        String str1 = "Senhor dos Aneis - A Irmandade do Anel";
//        String str2 = "Senhor dos Aneis - As Duas Torres";
//        String str3 = "Senhor dos Aneis - O Regresso do Rei";
//        String str4 = "O Hobbit ou Lá e de Volta Outra Vez";
//        String str5 = "O SENHOR DOS ANAIS - A Irmandade da Anilha";
//        String str6 = "Socia estou entesadissimo";

        float[] result = MybatchCompareSetAVG(ListaItems, str.toUpperCase());
        //outputs the results
        ListaItems.requery();
        if(ListaItems.moveToFirst()) {
	        for(int i=0;i<result.length;i++)
	        {
	        	String sTitulo = ListaItems.getString(2);
	        	if (result[i]>=0.60)
	        		Log.i("=>60", "|" + str + "|" + sTitulo + "=" + result[i] + "-> Média");
	        	else if (result[i]>=0.40)
	        		Log.i("=>40", "|" + str + "|" + sTitulo + "=" + result[i] + "-> Média");
	        	else if (result[i]>=0.20)
	        		Log.i("=>20", "|" + str + "|" + sTitulo + "=" + result[i] + "-> Média");
	        	else if (result[i]>=0.0)
	        		Log.i("=>0", "|" + str + "|" + sTitulo + "=" + result[i] + "-> Média");
	        	ListaItems.moveToNext();
	        }
        }
        ListaItems.close();
        
        
        
        //result = MybatchCompareSet(lista, str,new JaroWinkler());
        //outputs the results
        //for(int i=0;i<result.length;i++)
        //{
        //	Log.i("Teste", "|" + str + "|" + lista[i] + "=" + result[i] + "-> JaroWinkler");
        //}
        
        /*result = MybatchCompareSet(lista, str,new MongeElkan());
        //outputs the results
        for(int i=0;i<result.length;i++)
        {
        	Log.i("Teste", "|" + str + "|" + lista[i] + "=" + result[i] + "-> MongeEl");
        }
        
        result = MybatchCompareSet(lista, str,new SmithWatermanGotoh());
        //outputs the results
        for(int i=0;i<result.length;i++)
        {
        	Log.i("Teste", "|" + str + "|" + lista[i] + "=" + result[i] + "-> SmithWatermanGotoh");
        }
        */
    }
    public void teste2(){
    	// String str1 = "Magali";
        String str = "Testicário, António";
        String str1 = "António Testicário";
        String str2 = "António Pedro Froes";
        String str3 = "Pedro Armando Froes, António Magalhães Testicário, Paulo Pedro Pereira";
        String str4 = "Pedro Armando Froes, Paulo Magalhães Pereira";
        String str5 = "António Magalhães";
        String str6 = "Zé da Boneca";
        
//      AbstractStringMetric metric = new CosineSimilarity(); //As diferenças de acentuação têm mt impacto
//      AbstractStringMetric metric = new EuclideanDistance(); //As diferenças de acentuação têm mt impacto
      AbstractStringMetric metric = new MongeElkan(); //Parece me que é este
//       AbstractStringMetric metric = new Levenshtein(); //Analisa a % da string que corresponde logo não server
        
      //this single line performs the similarity test
        int result = (int) (metric.getUnNormalisedSimilarity(str, str1) * 100);
        //metric.batchCompareSet(arg0, arg1)

        //outputs the results
        Log.i("Teste", "|" + str + "|" + str1 + "=" + result + "<-" + metric.getShortDescriptionString() );
        
        result = (int) (metric.getUnNormalisedSimilarity(str, str2) * 100);

        //outputs the results
        Log.i("Teste", "|" + str + "|" + str2+ "=" + result + "<-" + metric.getShortDescriptionString() );
        
        result = (int) (metric.getUnNormalisedSimilarity(str, str3) * 100);

        //outputs the results
        Log.i("Teste", "|" + str + "|" + str3 + "=" + result + "<-" + metric.getShortDescriptionString() );
        
        result = (int) (metric.getUnNormalisedSimilarity(str, str4) * 100);

        //outputs the results
        Log.i("Teste", "|" + str + "|" + str4 + "=" + result + "<-" + metric.getShortDescriptionString() );
        
        result = (int) (metric.getUnNormalisedSimilarity(str, str5) * 100);

        //outputs the results
        Log.i("Teste", "|" + str + "|" + str5 + "=" + result + "<-" + metric.getShortDescriptionString() );
        
        result = (int) (metric.getUnNormalisedSimilarity(str, str6) * 100);

        //outputs the results
        Log.i("Teste", "|" + str + "|" + str6 + "=" + result + "<-" + metric.getShortDescriptionString() );
        
    }
    
    public void teste1(){
    	// String str1 = "Magali";
        String str = "Pedro Armando Froes, António Magalhães Testicário, Paulo Pedro Pereira";
        String str1 = "António Testicário";
        String str2 = "António Pedro Froes";
        String str3 = "Antonio Testicario";
        String str4 = "Antonio Magalhaes Testicario";
        String str5 = "Magalhaes testicareo";
        
//      AbstractStringMetric metric = new CosineSimilarity(); //As diferenças de acentuação têm mt impacto
//      AbstractStringMetric metric = new EuclideanDistance(); //As diferenças de acentuação têm mt impacto
//      AbstractStringMetric metric = new MongeElkan(); //Demasiado aberta, o resultado da str2 é superior ao da str3
        AbstractStringMetric metric = new Levenshtein();
        
      //this single line performs the similarity test
        int result = (int) (metric.getSimilarity(str, str1) * 100);
        //metric.batchCompareSet(arg0, arg1)

        //outputs the results
        Log.i("Teste", "|" + str + "|" + str1 + "=" + result + "<-" + metric.getShortDescriptionString() );
        
        result = (int) (metric.getSimilarity(str, str2) * 100);

        //outputs the results
        Log.i("Teste", "|" + str + "|" + str2+ "=" + result + "<-" + metric.getShortDescriptionString() );
        
        result = (int) (metric.getSimilarity(str, str3) * 100);

        //outputs the results
        Log.i("Teste", "|" + str + "|" + str3 + "=" + result + "<-" + metric.getShortDescriptionString() );
        
        result = (int) (metric.getSimilarity(str, str4) * 100);

        //outputs the results
        Log.i("Teste", "|" + str + "|" + str4 + "=" + result + "<-" + metric.getShortDescriptionString() );
        
        result = (int) (metric.getSimilarity(str, str5) * 100);

        //outputs the results
        Log.i("Teste", "|" + str + "|" + str5 + "=" + result + "<-" + metric.getShortDescriptionString() );
    }
}