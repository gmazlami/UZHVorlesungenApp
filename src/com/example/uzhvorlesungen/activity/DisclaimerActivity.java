package com.example.uzhvorlesungen.activity;

import com.example.uzhvorlesungen.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class DisclaimerActivity extends Activity {

	private static final String KEY_MPREFS = "ch.gmazlami.uzhvorlesungen.firstrun";
	private SharedPreferences mPrefs;

	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disclaimer);
        getActionBar().setTitle(getString(R.string.title_disclaimer));
        mPrefs = getApplicationContext().getSharedPreferences(KEY_MPREFS, MODE_PRIVATE);

        if(getFirstRun()){
        	setRunned();
            Button declineButton = (Button) findViewById(R.id.declineDisclaimerButton);
            declineButton.setOnClickListener(new OnClickListener() {
    			
    			@Override
    			public void onClick(View v) {
    				new AlertDialog.Builder(DisclaimerActivity.this)
    			    .setTitle("Delete entry")
    			    .setMessage("Are you sure you want to delete this entry?")
    			    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
    			        public void onClick(DialogInterface dialog, int which) { 
    			            finish();
    			        }
    			     })
    			    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
    			        public void onClick(DialogInterface dialog, int which) { 
    			            // do nothing
    			        }
    			     })
    			    .setIcon(android.R.drawable.ic_dialog_alert)
    			     .show();
    			}
    		});
        }else{
        	Intent dashboardIntent = new Intent(getApplicationContext(), AndroidDashboardDesignActivity.class);
        	startActivity(dashboardIntent);
        	finish();
        }
        
	}
	
	
    /**
     * Helper method which checks wether this is the first run of the application since installation
     * @return true if this is the first run, false otherwise
     */
	private boolean getFirstRun() {
		return mPrefs.getBoolean("firstRun", true);
	}
	
	/**
	 * Helper method to set the flag if the first run of the application has happened
	 */
	private void setRunned() {
		SharedPreferences.Editor edit = mPrefs.edit();
		edit.putBoolean("firstRun", false);
		edit.commit();
	}
	
}
