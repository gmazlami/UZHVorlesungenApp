package com.example.uzhvorlesungen.activity;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.devspark.sidenavigation.ISideNavigationCallback;
import com.devspark.sidenavigation.SideNavigationView;
import com.devspark.sidenavigation.SideNavigationView.Mode;
import com.example.uzhvorlesungen.R;
import com.example.uzhvorlesungen.activity.majorminor.PassedDataContainer;
import com.example.uzhvorlesungen.data.GlobalAppData;
import com.example.uzhvorlesungen.model.Lecture;
import com.google.gson.Gson;

public class SaveLecturesActivity extends Activity implements ISideNavigationCallback {

    public static final String EXTRA_TITLE = "com.devspark.sidenavigation.sample.extra.MTGOBJECT";
    public static final String EXTRA_RESOURCE_ID = "com.devspark.sidenavigation.sample.extra.RESOURCE_ID";
    public static final String EXTRA_MODE = "com.devspark.sidenavigation.sample.extra.MODE";
    public static final String EXTRA_SAVED_LECTURE = "com.gmazlami.uzh.vorlesungen.SAVED";
    
    private ImageView icon;
    private SideNavigationView sideNavigationView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_saved);
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

        getActionBar().setDisplayHomeAsUpEnabled(true);
        String string = "";
        
        try{
        	FileInputStream fis = openFileInput(GlobalAppData.PRIVATE_FILE_NAME);
        	BufferedReader br = new BufferedReader(new InputStreamReader(fis));
        	String current = null;
        	while((current = br.readLine())!=null){
        		string += current;
        	}
        	
        	
        }catch(IOException e){
        	e.printStackTrace();
        }
        
        Gson gson = new Gson();
        String[] array = string.split("_&_");
        for (int i = 0; i < array.length; i++) {
			System.out.println(array[i]);
		}
        final Lecture[] lectures = new Lecture[array.length];
        for (int i = 0; i < array.length; i++) {
			lectures[i] = gson.fromJson(array[i], Lecture.class);
		}
        
        String[] shortenedArray = new String[array.length -1];
        for (int i = 1; i < lectures.length; i++) {
        	shortenedArray[i-1] = lectures[i].getTitle();
		}
        
        ListView listview = (ListView) findViewById(R.id.savedList);
        listview.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.list_row_item, shortenedArray));
        listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String lectureTitle = ((TextView) view).getText().toString();
				Lecture lecture = searchLecture(lectureTitle, lectures);
				if(lecture==null){
					Toast.makeText(getApplicationContext(), "Fehler beim Laden der Vorlesung!", Toast.LENGTH_SHORT).show();
					return;
				}else{
					Intent intent = new Intent(getApplicationContext(), DetailsActivity.class);
					intent.putExtra(EXTRA_SAVED_LECTURE,true);
					PassedDataContainer.passedLecture = lecture;
					startActivity(intent);
				}
			}
        	
		});
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

            default:
                return;
        }
        finish();
    }
    
    @Override
    public void onBackPressed() {
        // hide menu if its shown
        if (sideNavigationView.isShown()) {
            sideNavigationView.hideMenu();
        } else {
        	sideNavigationView.showMenu();
//            super.onBackPressed();
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
    
    private Lecture searchLecture(String title, Lecture[] array){
    	for (int i = 1; i < array.length; i++) {
			if(array[i].getTitle().equals(title)){
				return array[i];
			}
		}
    	return null;
    }
}
