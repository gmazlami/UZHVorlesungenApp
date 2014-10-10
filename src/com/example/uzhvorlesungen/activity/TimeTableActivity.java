package com.example.uzhvorlesungen.activity;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import main.java.com.u1aryz.android.lib.newpopupmenu.PopupMenu;
import main.java.com.u1aryz.android.lib.newpopupmenu.PopupMenu.OnItemSelectedListener;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.devspark.sidenavigation.ISideNavigationCallback;
import com.devspark.sidenavigation.SideNavigationView;
import com.devspark.sidenavigation.SideNavigationView.Mode;
import com.example.uzhvorlesungen.R;
import com.example.uzhvorlesungen.data.GlobalAppData;
import com.example.uzhvorlesungen.database.LecturesDAO;
import com.example.uzhvorlesungen.model.BeginEndLocation;
import com.example.uzhvorlesungen.model.Lecture;

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
        
        
        LecturesDAO dao = new LecturesDAO(getApplicationContext());
        dao.openDataBase();
        List<Lecture> lectures = dao.getAllLectures(); 
        
        for(Lecture lecture : lectures){
        	HashMap<String, BeginEndLocation> belMap = lecture.getDayBeginEndTime();
        	for(String day : belMap.keySet()){
        		BeginEndLocation bel = belMap.get(day);
        		addTerminTextView(lecture.getTitle(), bel.locations.toString(), day, bel.begin, bel.end, lecture.getPoints(), lecture.getDocent());
        	}
        }
        
        
	}
	
	private void addTerminTextView(String title, String location, String day, String begin, String end, String points, String docent){
		float time = computeLectureLengthInHours(begin, end);
		RelativeLayout relativeLayoutForDay = null;
		if(day.equals("Mo")){
			relativeLayoutForDay = (RelativeLayout) findViewById(R.id.Monday);
		}else if(day.equals("Di")){
			relativeLayoutForDay = (RelativeLayout) findViewById(R.id.Tuesday);			
		}else if(day.equals("Mi")){
			relativeLayoutForDay = (RelativeLayout) findViewById(R.id.Wednesday);			
		}else if(day.equals("Do")){
			relativeLayoutForDay = (RelativeLayout) findViewById(R.id.Thursday);			
		}else if(day.equals("Fr")){
			relativeLayoutForDay = (RelativeLayout) findViewById(R.id.Friday);			
		}else{
			Toast.makeText(getApplicationContext(), "Fehler bei der Zusammenstellung des Stundenplans", Toast.LENGTH_LONG).show();
			return;
		}
		int marginTop = computeTopMargin(begin);

		//seperators
		View sep1 = new View(this);
		sep1.setBackgroundColor(Color.BLUE);
		RelativeLayout.LayoutParams paramsSep1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,2);
		paramsSep1.setMargins(0, marginTop-2, 0, 0);
		sep1.setLayoutParams(paramsSep1);
		relativeLayoutForDay.addView(sep1);

		//textview
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, (int)(time * 80));
		params.setMargins(0,marginTop, 0, 0);
		TextView v = new TextView(this);
		v.setText(computeCrop(title, (int)time));
		v.setBackgroundColor(Color.LTGRAY);
		v.setLayoutParams(params);
		relativeLayoutForDay.addView(v);
		
		StringBuilder sb = new StringBuilder();
		sb.append(title);
		sb.append("\n");
		sb.append("\n");
		sb.append(location);
		sb.append("\n");
		sb.append("\n");
		sb.append(points).append(" ECTS");
		sb.append("\n");
		sb.append("\n");
		sb.append(docent);
		final String info = sb.toString();
		v.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			     PopupMenu menu = new PopupMenu(TimeTableActivity.this);
			        menu.setHeaderTitle("Info");
			        menu.add(0, info);
			        menu.show(v);
			}
		});
		
	}
	
	private String computeCrop(String title, int time){
		int textLines = title.length()/20;
		int lines = time * 2;
		if(textLines >lines){
			return title.substring(0, (lines * 20) - 3) + "...";
		}else{
			return title;
		}
	}
	
	private int computeTopMargin(String beginTime){
		
		String[] a = beginTime.split(":");
		int beginHour = Integer.parseInt(a[0]);
		int beginMinute = Integer.parseInt(a[1]);
		
		float tmp = (float) 60 / beginMinute;
		float beginMinuteDecimal = 1.0f / tmp;
		float beginTimeDec = (float) beginHour + beginMinuteDecimal;
		
		float distanceFromMorningStart = (beginTimeDec*40) - (7*40);
		return convertDpToPx((int)distanceFromMorningStart);
	}
	
	private int convertDpToPx(int dp){
		float d = getApplicationContext().getResources().getDisplayMetrics().density;
		return (int)(dp * d); // margin in pixels
	}
	
	private float computeLectureLengthInHours(String begin, String end){
		//begin
		String[] a = begin.split(":");
		int beginHour = Integer.parseInt(a[0]);
		int beginMinute = Integer.parseInt(a[1]);
		
		float tmp = (float) 60 / beginMinute;
		float beginMinuteDecimal = 1.0f / tmp;
		float beginTimeDec = (float) beginHour + beginMinuteDecimal;
		
		//end
		String[] b = end.split(":");
		int endHour = Integer.parseInt(b[0]);
		int endMinute = Integer.parseInt(b[1]);
		
		tmp = (float) 60 / endMinute;
		float endMinuteDecimal = 1.0f / tmp;
		float endTimeDec = (float) endHour + endMinuteDecimal;
		
		//length of the lecture
		return (endTimeDec - beginTimeDec);
	}
	
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                sideNavigationView.toggleMenu();
                break;
            case R.id.action_share:

            	break;
            case R.id.action_save:
            	saveTimeTableBitMap();
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
                // all of the other activities on top of it will be closed and this
                // Intent will be delivered to the (now on top) old activity as a
                // new Intent.
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
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.actionbar, menu);
        return super.onCreateOptionsMenu(menu);
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
    
    private void saveTimeTableBitMap(){
    	Bitmap bitmap = loadBitmapFromView();
        saveBitmap(bitmap);
    }
    
    public void saveBitmap(Bitmap bitmap) {
        try {
        	FileOutputStream fos = openFileOutput(GlobalAppData.PRIVATE_FILE_NAME, MODE_WORLD_READABLE);
            bitmap.compress(CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
        	e.printStackTrace();
        } catch (IOException e) {
        	e.printStackTrace();
        }
    }
    
    private  Bitmap loadBitmapFromView() {
    	View v = findViewById(R.id.timetableView);
    	int width = v.getWidth();
    	int height = v.getHeight();
        Bitmap b = Bitmap.createBitmap(width , height, Bitmap.Config.ARGB_8888);                
        Canvas c = new Canvas(b);
        v.layout(0, 0, v.getWidth(), v.getHeight());
        v.draw(c);
        return b;
    }
}

