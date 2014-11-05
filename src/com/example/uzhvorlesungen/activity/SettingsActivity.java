package com.example.uzhvorlesungen.activity;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.MenuItem;
import android.widget.ImageView;

import com.devspark.sidenavigation.ISideNavigationCallback;
import com.devspark.sidenavigation.SideNavigationView;
import com.devspark.sidenavigation.SideNavigationView.Mode;
import com.example.uzhvorlesungen.R;

public class SettingsActivity extends PreferenceActivity implements ISideNavigationCallback{

    public static final String EXTRA_TITLE = "com.devspark.sidenavigation.sample.extra.MTGOBJECT";
    public static final String EXTRA_RESOURCE_ID = "com.devspark.sidenavigation.sample.extra.RESOURCE_ID";
    public static final String EXTRA_MODE = "com.devspark.sidenavigation.sample.extra.MODE";
	
    private ImageView icon;
    private SideNavigationView sideNavigationView;
    
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setTitle(getString(R.string.title_stored));
        setContentView(R.layout.activity_settings);
        addPreferencesFromResource(R.xml.settings);
        
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
            	Intent vintent = new Intent(this, TimeTableActivity.class);
            	vintent.putExtra(EXTRA_MODE, sideNavigationView.getMode() == Mode.LEFT ? 0 : 1);
            	vintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            	startActivity(vintent);
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
        // hide menu if its shown
        if (sideNavigationView.isShown()) {
            sideNavigationView.hideMenu();
        } else {
        	super.onBackPressed();
        }
    }
    
    private void invokeActivity(Class<AndroidDashboardDesignActivity> class1) {
        Intent intent = new Intent(this, class1);
        intent.putExtra(EXTRA_MODE, sideNavigationView.getMode() == Mode.LEFT ? 0 : 1);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }
	
}
