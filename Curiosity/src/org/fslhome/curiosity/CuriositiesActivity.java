package org.fslhome.curiosity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class CuriositiesActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_curiosities);
		
		// Get the data sent from the homepage
		Intent myIntent = getIntent();
		String currentCuriosity = myIntent.getStringExtra("button_clicked");
		TextView tv = (TextView) findViewById(R.id.current_curiosity_place);
		tv.setText(currentCuriosity);
	}
}
