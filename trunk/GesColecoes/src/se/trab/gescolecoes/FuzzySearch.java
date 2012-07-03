package se.trab.gescolecoes;

import uk.ac.shef.wit.simmetrics.similaritymetrics.AbstractStringMetric;
import uk.ac.shef.wit.simmetrics.similaritymetrics.MongeElkan;
import uk.ac.shef.wit.simmetrics.similaritymetrics.SmithWatermanGotoh;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.util.Log;

public class FuzzySearch {

	 public float[] MybatchCompareSetAVG(Cursor set, String comparatorNome,String comparatorAutores)
	    {
	    	float[] results = null;
	    	if(set.moveToFirst()) {
	    		results = new float[set.getCount()];
	    		
	    		AbstractStringMetric metric1 = new MongeElkan(); 

	    		AbstractStringMetric metric2 = new SmithWatermanGotoh (); 
	    		
	    		for (int strNum = 0; strNum < set.getCount(); strNum++)
	    		{
	    			float resTitulo;
	    			String sTitulo = set.getString(2);
	    			float resultMongeElkan = metric1.getSimilarity(sTitulo.toUpperCase(), comparatorNome.toUpperCase());
	    			
	    			float SmithWatermanGotoh = metric2.getSimilarity(sTitulo.toUpperCase(), comparatorNome.toUpperCase());
	    			
	    			resTitulo = (resultMongeElkan + SmithWatermanGotoh)/2;
	    			//Log.i("MybatchCompareSetAVG", "|T|" + sTitulo + "|" + "=" + resTitulo + "-> Média");
	    			
	    			float resAutores;
	    			String sAutores = set.getString(3);
	    			float resultMongeElkanAut = metric1.getSimilarity(sAutores.toUpperCase(), comparatorAutores.toUpperCase());
	    			
	    			float SmithWatermanGotohAut = metric2.getSimilarity(sAutores.toUpperCase(), comparatorAutores.toUpperCase());
	    			
	    			resAutores = (resultMongeElkanAut + SmithWatermanGotohAut)/2;
	    			//Log.i("MybatchCompareSetAVG", "|A|" + sAutores + "|" + "=" + resAutores + "-> Média");
	    			
	    			results[strNum] = (resTitulo + resAutores)/2;
	    			
	    			set.moveToNext();
	    		}
	    	}
	    	//set.close();
	    	return results;
	    }
	    

	    
	    public Cursor GetMatchCursor(Cursor ListaItems, String comparatorTitulo,String comparatorAutores){
	    	
	    	MatrixCursor matchCursor=null;


	        float[] result = MybatchCompareSetAVG(ListaItems, comparatorTitulo.toUpperCase(),comparatorAutores.toUpperCase());
	        //outputs the results
	        ListaItems.requery();
	        if(ListaItems.moveToFirst()) {
	        	int colsLista =ListaItems.getColumnCount();
	        	String[] Cols = new String[colsLista+1];
	        	
	        	for (int c=0;c<colsLista;c++)
	        	{
	        		Cols[c]=ListaItems.getColumnName(c);
	        	}
	        	Cols[colsLista] = "PercentMatch";
	        	
	        	int contMatchIncluidos=0;
	        	for (int c=0; c<result.length;c++)
	        		if (result[c]>=0.60)
	        			contMatchIncluidos++;
	        	
	        	matchCursor = new MatrixCursor(Cols,contMatchIncluidos); 
	        	
	        	
		        for(int i=0;i<result.length;i++)
		        {
		        	if (result[i]>=0.60)
		        	{
			        	Object[] rowValues= new Object[colsLista+1];
			        	
			        	for (int c=0;c<colsLista;c++)
			        	{
			        		if (c!=7)
			        			rowValues[c]=ListaItems.getString(c);
			        	}
			        	rowValues[colsLista] = result[i];
			        	
			        	matchCursor.addRow(rowValues);	
			        	
			        	String sTitulo = ListaItems.getString(2);
			        	String sAutores = ListaItems.getString(3);
			        	if (result[i]>=0.60)
			        		Log.i("=>60", "|" + comparatorTitulo + "|" + sTitulo + "|" + sAutores + "=" + result[i] + "-> Média");
			        	else if (result[i]>=0.40)
			        		Log.i("=>40", "|" + comparatorTitulo + "|" + sTitulo + "=" + result[i] + "-> Média");
			        	else if (result[i]>=0.20)
			        		Log.i("=>20", "|" + comparatorTitulo + "|" + sTitulo + "=" + result[i] + "-> Média");
			        	else if (result[i]>=0.0)
			        		Log.i("=>0", "|" + comparatorTitulo + "|" + sTitulo + "=" + result[i] + "-> Média");
			        }
			        	
			        ListaItems.moveToNext();
			        
	        	}
	        }
	        ListaItems.close();
	        return matchCursor;
	        
	    }
}
