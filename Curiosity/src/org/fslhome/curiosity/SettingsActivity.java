package org.fslhome.curiosity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

public class SettingsActivity extends ActionBarActivity {


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }
    
    private void exemplo (){
    
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setTitle("About Us");
    	builder.setMessage("Developed By Thibatt Smith and Mateus Saunier");
    	
    	
    	
    	
    	
    }

}
