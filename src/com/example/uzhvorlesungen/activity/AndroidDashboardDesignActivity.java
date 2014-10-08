package com.example.uzhvorlesungen.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.devspark.sidenavigation.ISideNavigationCallback;
import com.devspark.sidenavigation.SideNavigationView;
import com.devspark.sidenavigation.SideNavigationView.Mode;
import com.example.uzhvorlesungen.R;
import com.example.uzhvorlesungen.activity.majorminor.PassedDataContainer;
import com.example.uzhvorlesungen.callbacks.FacultiesCallbackInterface;
import com.example.uzhvorlesungen.threading.ParsingFacultiesTitlesAsyncTask;

public class AndroidDashboardDesignActivity extends Activity implements FacultiesCallbackInterface, ISideNavigationCallback{
    
    private Map<String, List<String>> facultiesMap;
	private Map<String, String> titlesLinksMap;
	private ProgressDialog progress;
    public static final String EXTRA_TITLE = "com.devspark.sidenavigation.sample.extra.MTGOBJECT";
    public static final String EXTRA_RESOURCE_ID = "com.devspark.sidenavigation.sample.extra.RESOURCE_ID";
    public static final String EXTRA_MODE = "com.devspark.sidenavigation.sample.extra.MODE";
    private ImageView icon;
    private SideNavigationView sideNavigationView;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_layout);
        getActionBar().setTitle(getString(R.string.choose_faculty));
        getActionBar().setDisplayHomeAsUpEnabled(true);
        
        if(isInternetActive()){
        	progress = ProgressDialog.show(this, getString(R.string.gathering_data),getString(R.string.please_wait), true);
        	ParsingFacultiesTitlesAsyncTask task = new ParsingFacultiesTitlesAsyncTask(this);
        	task.execute();
        }
		
        icon = (ImageView) findViewById(android.R.id.icon);
        sideNavigationView = (SideNavigationView) findViewById(R.id.side_navigation_view);
        sideNavigationView.setMenuItems(R.menu.side_navigation_menu);
        sideNavigationView.setMenuClickCallback(this);

        if (getIntent().hasExtra(EXTRA_TITLE)) {
            String title = getIntent().getStringExtra(EXTRA_TITLE);
            int resId = getIntent().getIntExtra(EXTRA_RESOURCE_ID, 0);
            setTitle(title);
            icon.setImageResource(resId);
            sideNavigationView.setMode(getIntent().getIntExtra(EXTRA_MODE, 0) == 0 ? Mode.LEFT : Mode.RIGHT);
        }

        
        
        /**
         * Creating all buttons instances
         * */
        
        Button btnMNF = (Button) findViewById(R.id.btn_mnf);
        
        Button btnWWF = (Button) findViewById(R.id.btn_wwf);
        
        Button btnRWF = (Button) findViewById(R.id.btn_rwf);
        
        Button btnMedF = (Button) findViewById(R.id.btn_medf);

        Button btnPhF = (Button) findViewById(R.id.btn_phf);
        
        Button btnVetF = (Button) findViewById(R.id.btn_vetf);
        
        Button btnThF = (Button) findViewById(R.id.btn_thf);
        
        /**
         * Handling all button click events
         * */
        
        // Listening to News Feed button click
        btnMNF.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				if(isInternetActive()){
					String faculty = "Mathematisch-naturwissenschaftliche Fakult�t";
					startTitlesActivity(faculty);
				}else{
					Toast.makeText(getApplicationContext(), "Bitte aktiviere die Internetverbindung auf deinem Gerät", Toast.LENGTH_LONG).show();
				}
			}
		});
        
       // Listening Friends button click
        btnWWF.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				if(isInternetActive()){
					String faculty = "Wirtschaftswissenschaftliche Fakult�t";
					startTitlesActivity(faculty);
				}else{
					Toast.makeText(getApplicationContext(), "Bitte aktiviere die Internetverbindung auf deinem Gerät", Toast.LENGTH_LONG).show();
				}
			}
		});
        
        // Listening Messages button click
        btnRWF.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				if(isInternetActive()){
					String faculty = "Rechtswissenschaftliche Fakult�t";
					startTitlesActivity(faculty);
				}else{
					Toast.makeText(getApplicationContext(), "Bitte aktiviere die Internetverbindung auf deinem Gerät", Toast.LENGTH_LONG).show();
				}
			}
		});
        
        // Listening to Places button click
        btnMedF.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				if(isInternetActive()){
					String faculty = "Medizinische Fakult�t";
					startTitlesActivity(faculty);
				}else{
					Toast.makeText(getApplicationContext(), "Bitte aktiviere die Internetverbindung auf deinem Gerät", Toast.LENGTH_LONG).show();
				}
			}
		});
        
        // Listening to Events button click
        btnPhF.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				if(isInternetActive()){
					String faculty = "Philosophische Fakult�t";
					startTitlesActivity(faculty);
				}else{
					Toast.makeText(getApplicationContext(), "Bitte aktiviere die Internetverbindung auf deinem Gerät", Toast.LENGTH_LONG).show();
				}
			}
		});
        
        // Listening to Photos button click
        btnVetF.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				if(isInternetActive()){
					String faculty = "Vetsuisse-Fakult�t";
					startTitlesActivity(faculty);
				}else{
					Toast.makeText(getApplicationContext(), "Bitte aktiviere die Internetverbindung auf deinem Gerät", Toast.LENGTH_LONG).show();
				}
			}
		});
        
        btnThF.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				if(isInternetActive()){
					String faculty = "Theologische Fakult�t";
					startTitlesActivity(faculty);
				}else{
					Toast.makeText(getApplicationContext(), "Bitte aktiviere die Internetverbindung auf deinem Gerät", Toast.LENGTH_LONG).show();
				}
			}
		});
    }

	@Override
	public void onTaskCompleted(ArrayList<String> faculties,
			Map<String, List<String>> map,
			Map<String, String> titlesMap) {
		
		facultiesMap = map;
		titlesLinksMap = titlesMap;
		progress.dismiss();
	}
	
	private void startTitlesActivity(String faculty){
		Intent intent = new Intent(getApplicationContext(),
				TitlesActivity.class);
		List<String> passedList = facultiesMap.get(faculty);
		List<String> linksList = new ArrayList<String>();
		
		for(int i =0; i < passedList.size(); i++){
			String title = passedList.get(i);
			linksList.add(titlesLinksMap.get(title));
		}
		
		
		PassedDataContainer.passedTitles = passedList;
		PassedDataContainer.passedTitlesLinks = linksList;
		startActivity(intent);
	}
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        if (sideNavigationView.getMode() == Mode.RIGHT) {
            menu.findItem(R.id.mode_right).setChecked(true);
        } else {
            menu.findItem(R.id.mode_left).setChecked(true);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                sideNavigationView.toggleMenu();
                break;
            case R.id.mode_left:
                item.setChecked(true);
                sideNavigationView.setMode(Mode.LEFT);
                break;
            case R.id.mode_right:
                item.setChecked(true);
                sideNavigationView.setMode(Mode.RIGHT);
                break;

            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public void onSideNavigationItemClick(int itemId) {
        switch (itemId) {
            case R.id.side_navigation_menu_item1:
            	invokeActivity(AndroidDashboardDesignActivity.class);
                break;

            case R.id.side_navigation_menu_item2:
                Intent intent = new Intent(this, SaveLecturesActivity.class);
                intent.putExtra(EXTRA_MODE, sideNavigationView.getMode() == Mode.LEFT ? 0 : 1);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                startActivity(intent);
                // no animation of transition
                overridePendingTransition(0, 0);
                break;
                
            case R.id.side_navigation_menu_item3:
                Intent timeTableIntent = new Intent(this, TimeTableActivity.class);
                timeTableIntent.putExtra(EXTRA_MODE, sideNavigationView.getMode() == Mode.LEFT ? 0 : 1);
                timeTableIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                startActivity(timeTableIntent);
                // no animation of transition
                overridePendingTransition(0, 0);
                break;
            default:
                return;
        }
        finish();
    }

    @Override
    public void onBackPressed() {
        // hide menu if it shown
        if (sideNavigationView.isShown()) {
            sideNavigationView.hideMenu();
        } else {
            super.onBackPressed();
        }
    }
    
    /**
     * Start activity from SideNavigation.
     * 
     * @param title title of Activity
     * @param resId resource if of background image
     */
    private void invokeActivity(Class<AndroidDashboardDesignActivity> class1) {
        Intent intent = new Intent(this, class1);
        intent.putExtra(EXTRA_MODE, sideNavigationView.getMode() == Mode.LEFT ? 0 : 1);

        // all of the other activities on top of it will be closed and this
        // Intent will be delivered to the (now on top) old activity as a
        // new Intent.
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(intent);
        // no animation of transition
        overridePendingTransition(0, 0);
    }
    
    private boolean isInternetActive(){
    	ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
    	NetworkInfo activeNetworkInfo = manager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    
}