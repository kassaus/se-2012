package se.trab.gescolecoes;

import uk.ac.shef.wit.simmetrics.similaritymetrics.AbstractStringMetric;

import uk.ac.shef.wit.simmetrics.similaritymetrics.Levenshtein;

import uk.ac.shef.wit.simmetrics.similaritymetrics.MongeElkan;

import uk.ac.shef.wit.simmetrics.similaritymetrics.SmithWatermanGotoh;

import android.database.Cursor;
import android.util.Log;

public class FuzzySearch {

	 public float[] MybatchCompareSetAVG(Cursor set, String comparatorNome)
	    {
	    	float[] results = null;
	    	if(set.moveToFirst()) {
	    		results = new float[set.getCount()];
	    		
	    		AbstractStringMetric metric1 = new MongeElkan(); 

	    		AbstractStringMetric metric2 = new SmithWatermanGotoh (); 
	    		
	    		for (int strNum = 0; strNum < set.getCount(); strNum++)
	    		{
	    			String sTitulo = set.getString(2);
	    			float resultMongeElkan = metric1.getSimilarity(sTitulo.toUpperCase(), comparatorNome.toUpperCase());
	    			//float resultJaroWinkler = metric6.getSimilarity(sTitulo.toUpperCase(), comparatorNome.toUpperCase());
	    			float SmithWatermanGotoh = metric2.getSimilarity(sTitulo.toUpperCase(), comparatorNome.toUpperCase());
	    			//results[strNum] = ((resultMongeElkan * 2)+(resultEuclideanDistance)+(resultSmithWatermanGotoh*2))/5;
	    			results[strNum] = (resultMongeElkan + SmithWatermanGotoh)/2;
	    			set.moveToNext();
	    		}
	    	}
	    	//set.close();
	    	return results;
	    }
	    

	    
	    public void testeArrayStrings(Cursor ListaItems, String comparator){
	    	
	       


	        float[] result = MybatchCompareSetAVG(ListaItems, comparator.toUpperCase());
	        //outputs the results
	        ListaItems.requery();
	        if(ListaItems.moveToFirst()) {
		        for(int i=0;i<result.length;i++)
		        {
		        	String sTitulo = ListaItems.getString(2);
		        	if (result[i]>=0.60)
		        		Log.i("=>60", "|" + comparator + "|" + sTitulo + "=" + result[i] + "-> Média");
		        	else if (result[i]>=0.40)
		        		Log.i("=>40", "|" + comparator + "|" + sTitulo + "=" + result[i] + "-> Média");
		        	else if (result[i]>=0.20)
		        		Log.i("=>20", "|" + comparator + "|" + sTitulo + "=" + result[i] + "-> Média");
		        	else if (result[i]>=0.0)
		        		Log.i("=>0", "|" + comparator + "|" + sTitulo + "=" + result[i] + "-> Média");
		        	ListaItems.moveToNext();
		        }
	        }
	        ListaItems.close();
	        
	    }
}
