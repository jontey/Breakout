package com.jonathantey.breakout;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class database {
	public ArrayList<ArrayList<String>> data;
    private Context context;
	private String db_name;
	private File db_file;
	
	
	public database(Context v, String file_name){
		db_name = file_name; //Save file name
        context = v;
		data = new ArrayList<ArrayList<String>>(10);
        for(int i=0; i<10; i++){
            ArrayList<String> db_line = new ArrayList<String>(2);
            db_line.add(0, "");
            db_line.add(1, "999999999");
            data.add(db_line);
        }
		readDatabase();
	}
	
	/****************
	* Database Format
	* ---------------
	* 0: Name
	* 1: Score (Time)
	****************/
	public void readDatabase(){
		try {
            InputStream instream = context.openFileInput(db_name);
            InputStreamReader inputreader = new InputStreamReader(instream);
            BufferedReader br = new BufferedReader(inputreader);
			String line = br.readLine();
			data.clear(); //Reset all info
			while (line != null) {
				String[] split = line.split("\t");
				ArrayList<String> db_line = new ArrayList<String>(2);
				db_line.add(0, "");
				db_line.add(1, "");
				for(int i = 0; i < split.length; i++){
					if(split[i].length() < 1){
						split[i] = "";
					}
					db_line.set(i, split[i]);
				}
				data.add(db_line); //Add new line
				line = br.readLine(); //Read next line
			}
			
		} catch (Exception e){
			saveDatabase();
			e.printStackTrace();
		}
	}
	
	public void saveDatabase(){
		try {
            FileOutputStream outputWriter = context.openFileOutput(db_name, Context.MODE_PRIVATE);

            //BufferedWriter outputWriter = null;
			for (int i = 0; i < data.size(); i++) { //Iterate each line
				for(int j = 0; j < (data.get(i)).size()-1; j++){
					outputWriter.write(((data.get(i)).get(j)+"\t").getBytes());
				}
				outputWriter.write(((data.get(i)).get((data.get(i)).size()-1)).getBytes()); //Last line without tab
				outputWriter.write(("\n").getBytes());
			}
			outputWriter.close();
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public void sortData(int index, boolean asc){
		final int i = index;
        final boolean a = asc;
		Collections.sort(data, new Comparator<ArrayList<String>>() {
			@Override
			public int compare(ArrayList<String> o1, ArrayList<String> o2) {
				if (a) {
					return o1.get(i).compareTo(o2.get(i));
				} else {
					return o2.get(i).compareTo(o1.get(i));
				}
			}
		});
	}
	
	public ArrayList<Integer> findData(int index, String s){
		ArrayList<Integer> temp_list = new ArrayList<Integer>();
		for(int i=0; i<data.size(); i++){
			if(data.get(i).get(index).equals(s)){
				temp_list.add(i);
			}
		}
		return temp_list;
	}

    /**
     * Custom method to add score and keep 10 best scores
     */
    public void addScore(ArrayList<String> input){
        data.add(input);
        sortData(1, true); //Sort ascending order
        trim(10); //Keep first 10 scores. Delete the rest
    }

    public void edit(ArrayList<String> input, int index){
        data.set(index, input);
    }

    public void add(ArrayList<String> input){
        data.add(input);
    }

    public void trim(int maxSize){
        while(data.size() > maxSize){
            data.remove(data.size()-1);
        }
    }

    public ArrayList<String> delete(int index){
        ArrayList<String> r = data.remove(index);
        saveDatabase();
        return r;
    }

	public void reset(){
		data = new ArrayList<ArrayList<String>>(10);
		for(int i=0; i<10; i++){
			ArrayList<String> db_line = new ArrayList<String>(2);
			db_line.add(0, "");
			db_line.add(1, "999999999");
			data.add(db_line);
		}
		saveDatabase();
	}
}