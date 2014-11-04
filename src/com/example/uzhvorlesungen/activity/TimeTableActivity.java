package com.example.uzhvorlesungen.activity;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import main.java.com.u1aryz.android.lib.newpopupmenu.PopupMenu;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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
	int index = 0;
	
    private ImageView icon;
    private SideNavigationView sideNavigationView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setTitle(getString(R.string.title_timetable));
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
        
        if(lectures == null || lectures.size() == 0){
        	AlertDialog.Builder builder = new AlertDialog.Builder(this);
        	builder.setMessage(R.string.alert_nolectures_timetable)
        	       .setCancelable(false)
        	       .setPositiveButton("OK", new DialogInterface.OnClickListener() {
        	           public void onClick(DialogInterface dialog, int id) {
        	                //do things
        	           }
        	       });
        	AlertDialog alert = builder.create();
        	alert.show();
        }else{
        	for(Lecture lecture : lectures){
        		HashMap<String, BeginEndLocation> belMap = lecture.getDayBeginEndTime();
        		for(String day : belMap.keySet()){
        			BeginEndLocation bel = belMap.get(day);
        			if(bel.begin != null && bel.end != null && !bel.begin.equals("") && !bel.end.equals("")){
        				addTerminTextView(lecture.getTitle(), bel.locations.toString(), day, bel.begin, bel.end, lecture.getPoints(), lecture.getDocent());
        			}
        		}
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
			Toast.makeText(getApplicationContext(), getString(R.string.error_loading_timetable), Toast.LENGTH_LONG).show();
			return;
		}
		int marginTop = computeTopMargin(begin);

		float d = getApplicationContext().getResources().getDisplayMetrics().density;

		//textview
		int[] colors = {Color.LTGRAY, Color.MAGENTA, Color.GREEN, Color.CYAN, Color.YELLOW, Color.RED, Color.GRAY};
		int color = -1;
		if(index >= colors.length){
			index = 0;
			color = colors[index];
		}else{
			color = colors[index];
			index++;
		}
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, (int)(time * 40 * d));
		params.setMargins(0,marginTop, 0, 0);
		TextView v = new TextView(this);
		v.setText(computeCrop(title, (int)time));
		v.setBackgroundColor(color);
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
		final String dayString = day;
		final String beginString = begin;
		final String endString = end;
		v.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			     PopupMenu menu = new PopupMenu(TimeTableActivity.this);
			        menu.setHeaderTitle(dayString + " " + beginString + " - " + endString);
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
            	try{
            		shareTimeTable();
            	}catch(FileNotFoundException e){
            		e.printStackTrace(); //TODO: better exception/error handling
            		Toast.makeText(this, getString(R.string.error_loading_timetable), Toast.LENGTH_LONG).show();
            	}
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
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }
    
    private void shareTimeTable() throws FileNotFoundException{
    	FileOutputStream fos = new FileOutputStream(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) +"/timetable.jpg" );
        loadBitmapFromView().compress(CompressFormat.JPEG, 100, fos);
        String path = "file://" + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) +"/timetable.jpg";
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(path));
        shareIntent.setType("image/jpeg");
        startActivity(Intent.createChooser(shareIntent, getString(R.string.intent_send_timetable)));
    }
    
    private void saveTimeTableBitMap(){
    	Bitmap bitmap = loadBitmapFromView();
        saveBitmap(bitmap);
    }
    
    @SuppressLint("WorldReadableFiles") 
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

