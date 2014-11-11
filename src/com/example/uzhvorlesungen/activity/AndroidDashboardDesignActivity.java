package com.example.uzhvorlesungen.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import com.example.uzhvorlesungen.data.GlobalAppData;
import com.example.uzhvorlesungen.threading.ParsingFacultiesTitlesAsyncTask;

public class AndroidDashboardDesignActivity extends Activity implements FacultiesCallbackInterface, ISideNavigationCallback{
    
	
	//string keys for the passed extras, used by sidenav library
	public static final String EXTRA_TITLE = "com.devspark.sidenavigation.sample.extra.MTGOBJECT";
	public static final String EXTRA_RESOURCE_ID = "com.devspark.sidenavigation.sample.extra.RESOURCE_ID";
	public static final String EXTRA_MODE = "com.devspark.sidenavigation.sample.extra.MODE";

	//String key for sharedpref section used for firstrun

	//title strings for the labels of the dashboard buttons
	private static final String MNF = "Mathematisch-naturwissenschaftliche Fakultät";
	private static final String WWF = "Wirtschaftswissenschaftliche Fakultät";
	private static final String PHF = "Philosophische Fakultät";
	private static final String RWF = "Rechtswissenschaftliche Fakultät";
	private static final String THF = "Theologische Fakultät";
	private static final String MEDF = "Medizinische Fakultät";
	private static final String VETF = "Vetsuisse-Fakultät";
	
	//local data containers
	private Map<String, List<String>> facultiesMap; //maps a faculty to a list of titles
	private Map<String, String> titlesLinksMap; // maps a title to the link with the title's content
	
	//UI variables
	private ProgressDialog progress; //progress dialog displayed when the asynctask loads data
    private ImageView icon;
    private SideNavigationView sideNavigationView;
    
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_layout);
        getActionBar().setTitle(getString(R.string.choose_faculty));
        getActionBar().setDisplayHomeAsUpEnabled(true);
        
        PassedDataContainer.bscMscMed = false; //initialize/reset the bscMscFlag, because here we start a new lecture lookup
        
        
        //only download data if device has activated network access
        if(isInternetActive()){
        	//disable the UI and show a progress popup while executing asynctask
        	progress = ProgressDialog.show(this, getString(R.string.gathering_data),getString(R.string.please_wait), true);
        	
        	//get reference to the preferences of this applications through android preference API 
	        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(AndroidDashboardDesignActivity.this);
	        
	        //get the semester to use when gathering data, defined in sharedpreferences and set it globally
	        GlobalAppData.SEMESTER_PREFERENCE = sharedPref.getString("pref_semester", "HS14");
	        
	        //start gathering data asynchronously
        	ParsingFacultiesTitlesAsyncTask task = new ParsingFacultiesTitlesAsyncTask(this);
        	task.execute();
        }
		
        //initialize side navigation
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
         * Creating all button instances
         * */
        
        Button btnMNF = (Button) findViewById(R.id.btn_mnf);
        
        Button btnWWF = (Button) findViewById(R.id.btn_wwf);
        
        Button btnRWF = (Button) findViewById(R.id.btn_rwf);
        
        Button btnMedF = (Button) findViewById(R.id.btn_medf);

        Button btnPhF = (Button) findViewById(R.id.btn_phf);
        
        Button btnVetF = (Button) findViewById(R.id.btn_vetf);
        
        Button btnThF = (Button) findViewById(R.id.btn_thf);
        
        Button btnSearch = (Button) findViewById(R.id.btn_search);
        
        /**
         * Handling all button click events
         * */
        
        // Listening to MNF button click
        btnMNF.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				if(isInternetActive()){
					String faculty = MNF;
					startTitlesActivity(faculty);
				}else{
					Toast.makeText(getApplicationContext(), getString(R.string.activate_internet), Toast.LENGTH_LONG).show();
				}
			}
		});
        
       // Listening WWF button click
        btnWWF.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				if(isInternetActive()){
					String faculty = WWF;
					startTitlesActivity(faculty);
				}else{
					Toast.makeText(getApplicationContext(), getString(R.string.activate_internet), Toast.LENGTH_LONG).show();
				}
			}
		});
        
        // Listening RWF button click
        btnRWF.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				if(isInternetActive()){
					String faculty = RWF;
					startTitlesActivity(faculty);
				}else{
					Toast.makeText(getApplicationContext(), getString(R.string.activate_internet), Toast.LENGTH_LONG).show();
				}
			}
		});
        
        // Listening to MedF button click
        btnMedF.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				if(isInternetActive()){
					String faculty = MEDF;
					startTitlesActivity(faculty);
				}else{
					Toast.makeText(getApplicationContext(), getString(R.string.activate_internet), Toast.LENGTH_LONG).show();
				}
			}
		});
        
        // Listening to PhF button click
        btnPhF.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				if(isInternetActive()){
					String faculty = PHF;
					startTitlesActivity(faculty);
				}else{
					Toast.makeText(getApplicationContext(), getString(R.string.activate_internet), Toast.LENGTH_LONG).show();
				}
			}
		});
        
        // Listening to VetF button click
        btnVetF.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				if(isInternetActive()){
					String faculty = VETF;
					startTitlesActivity(faculty);
				}else{
					Toast.makeText(getApplicationContext(), getString(R.string.activate_internet), Toast.LENGTH_LONG).show();
				}
			}
		});
        
        // Listening to ThF button click
        btnThF.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				if(isInternetActive()){
					String faculty = THF;
					startTitlesActivity(faculty);
				}else{
					Toast.makeText(getApplicationContext(), getString(R.string.activate_internet), Toast.LENGTH_LONG).show();
				}
			}
		});
        
        // Listening to Search button click
        btnSearch.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				if(isInternetActive()){
					Intent intent = new Intent(getApplicationContext(), FreeSearchActivity.class);
					intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY); // Adds the FLAG_ACTIVITY_NO_HISTORY flag
					startActivity(intent);
				}else{
					Toast.makeText(getApplicationContext(), getString(R.string.activate_internet), Toast.LENGTH_LONG).show();
				}
			}
		});
    }

	/**
	 * callback method which is executed when the AsyncTask started in this activity finishes.
	 */
	@Override
	public void onTaskCompleted(ArrayList<String> faculties,
			Map<String, List<String>> map,
			Map<String, String> titlesMap) {
		
		facultiesMap = map; //maps faculties to a list of titles available in that faculty
		titlesLinksMap = titlesMap; //maps each title to its URL/link
		progress.dismiss(); // closes the progressbar popup
		
	}
	
	private void startTitlesActivity(String faculty){
		//create intent to launch next activity
		Intent intent = new Intent(getApplicationContext(),TitlesActivity.class);
		
		//find the clicked faculty in the map, and get its list of titles
		List<String> passedList = facultiesMap.get(faculty);
		
		//this list of Strings/URLS will contain all links for the titles we found in the code above
		List<String> linksList = new ArrayList<String>();
		
		//populate the list of URLS with the links associated to the titles
		for(int i =0; i < passedList.size(); i++){
			String title = passedList.get(i);
			linksList.add(titlesLinksMap.get(title));
		}
		
		//put the data in the global static container, in order to retrieve later in the next activity
		// (this is done this way, because passing complex datastructures (collections) is not efficient through extras)
		PassedDataContainer.passedTitles = passedList;
		PassedDataContainer.passedTitlesLinks = linksList;
		
		//eventually start the new intent
		startActivity(intent);
	}
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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
                overridePendingTransition(0, 0);
                break;
                
            case R.id.side_navigation_menu_item3:
                Intent timeTableIntent = new Intent(this, TimeTableActivity.class);
                timeTableIntent.putExtra(EXTRA_MODE, sideNavigationView.getMode() == Mode.LEFT ? 0 : 1);
                timeTableIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(timeTableIntent);
                overridePendingTransition(0, 0);
                break;
                
            case R.id.side_navigation_menu_item4:
                Intent settingsIntent = new Intent(this, SettingsActivity.class);
                settingsIntent.putExtra(EXTRA_MODE, sideNavigationView.getMode() == Mode.LEFT ? 0 : 1);
                settingsIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(settingsIntent);
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

    private void invokeActivity(Class<AndroidDashboardDesignActivity> class1) {
    	//TODO: refactor this helper method. It doesn't make sense with this parameter etc.
        Intent intent = new Intent(this, class1);
        intent.putExtra(EXTRA_MODE, sideNavigationView.getMode() == Mode.LEFT ? 0 : 1);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }
    
    /**
     * Helper method to check if the device internet is activated at all
     * Note: this does not check wether the connection is slow or times out, only if internet is 
     * activated at all at device level
     * @return true if internet is activated, false otherwise
     */
    private boolean isInternetActive(){
    	ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
    	NetworkInfo activeNetworkInfo = manager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    
}