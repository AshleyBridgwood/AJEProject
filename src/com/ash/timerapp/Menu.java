package com.ash.timerapp;

import android.R;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Menu extends ListActivity {
	
	String classes[] = {"Timer"};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setListAdapter(new ArrayAdapter<String>(Menu.this, android.R.layout.simple_list_item_1, classes));
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		String menuPos = classes[position];
		try{
			Class ourClass = Class.forName("com.ash.timerapp." + menuPos);
		Intent outIntent = new Intent(Menu.this, ourClass);
		startActivity(outIntent);
		} catch (ClassNotFoundException e){
			e.printStackTrace();
		}
	}

}
