package com.example.uzhvorlesungen.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;

import com.devspark.sidenavigation.ISideNavigationCallback;
import com.devspark.sidenavigation.SideNavigationView;
import com.devspark.sidenavigation.SideNavigationView.Mode;
import com.example.uzhvorlesungen.R;

public class TimeTableActivity extends Activity  implements ISideNavigationCallback {
	
    public static final String EXTRA_TITLE = "com.devspark.sidenavigation.sample.extra.MTGOBJECT";
    public static final String EXTRA_RESOURCE_ID = "com.devspark.sidenavigation.sample.extra.RESOURCE_ID";
    public static final String EXTRA_MODE = "com.devspark.sidenavigation.sample.extra.MODE";
    
    private ImageView icon;
    private SideNavigationView sideNavigationView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_stundenplan);
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
            	invokeActivity(AndroidDashboardDesignActivity.class);//FIXME: here the SavedLEcturesActivity should be invoked
//                invokeActivity(getString(R.string.title2), R.drawable.ic_android2);
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
}