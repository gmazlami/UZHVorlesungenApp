package com.example.uzhvorlesungen.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.devspark.sidenavigation.ISideNavigationCallback;
import com.devspark.sidenavigation.SideNavigationView;
import com.devspark.sidenavigation.SideNavigationView.Mode;
import com.example.uzhvorlesungen.R;
import com.example.uzhvorlesungen.activity.majorminor.PassedDataContainer;
import com.example.uzhvorlesungen.database.LecturesDAO;
import com.example.uzhvorlesungen.model.Lecture;

public class SaveLecturesActivity extends Activity implements ISideNavigationCallback {

    public static final String EXTRA_TITLE = "com.devspark.sidenavigation.sample.extra.MTGOBJECT";
    public static final String EXTRA_RESOURCE_ID = "com.devspark.sidenavigation.sample.extra.RESOURCE_ID";
    public static final String EXTRA_MODE = "com.devspark.sidenavigation.sample.extra.MODE";
    public static final String EXTRA_SAVED_LECTURE = "com.gmazlami.uzh.vorlesungen.SAVED";
    
    private ImageView icon;
    private SideNavigationView sideNavigationView;
	private Lecture[] lectureArray;
	private LecturesDAO dao;

	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setTitle(getString(R.string.title_stored));
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
        
        dao = new LecturesDAO(getApplicationContext());
        dao.openDataBase();
        final List<Lecture> lectures = dao.getAllLectures();
        dao.closeDataBase();
        
        lectureArray = new Lecture[lectures.size()];
        lectureArray = lectures.toArray(lectureArray);
        
        ArrayList<String> titlesList = new ArrayList<String>();
        for(Lecture l : lectures){
        	titlesList.add(l.getTitle());
        }
        
        ListView listview = (ListView) findViewById(R.id.savedList);
        listview.setAdapter(new SavedLecturesAdapter(getApplicationContext(), R.layout.saved_list_row_item,  titlesList));
        listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String  lectureTitle = lectures.get(position).getTitle();
				Lecture lecture = searchLecture(lectureTitle, SaveLecturesActivity.this.lectureArray);
				if(lecture==null){
					Toast.makeText(getApplicationContext(), getString(R.string.error_loading_lecture), Toast.LENGTH_SHORT).show();
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
    
    private Lecture searchLecture(String title, Lecture[] array){
    	for (int i = 0; i < array.length; i++) {
			if(array[i].getTitle().equals(title)){
				System.out.println(array[i].getTitle());
				return array[i];
			}
		}
    	return null;
    }
    
    public class SavedLecturesAdapter extends ArrayAdapter<String> {

    	private List<String> lectures;
    	public SavedLecturesAdapter(Context context,int textViewResourceId, List<String> objects) {
    		super(context, textViewResourceId, objects);
    		this.lectures = objects;
    		
    	}
    	
    	@Override
    	public View getView(int position, View convertView, ViewGroup parent){
    	    if( convertView == null ){
    	    	convertView = LayoutInflater.from(getContext()).inflate(R.layout.saved_list_row_item, parent, false);
    	        Button deleteBtn = (Button) convertView.findViewById(R.id.deleteButton);
    	        TextView lectureTextView = (TextView) convertView.findViewById(R.id.savedLectureTextView);
    	        final String title = lectures.get(position);
    	        lectureTextView.setText(title);		
    	        deleteBtn.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						dao.openDataBase();
						dao.deleteLecture(title);
						dao.closeDataBase();
						remove(title);
						notifyDataSetChanged();
						
					}
				});
    	    }else{
    	        Button deleteBtn = (Button) convertView.findViewById(R.id.deleteButton);
    	        TextView lectureTextView = (TextView) convertView.findViewById(R.id.savedLectureTextView);
    	        final String title = lectures.get(position);
    	        lectureTextView.setText(title);		
    	        deleteBtn.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						dao.openDataBase();
						dao.deleteLecture(title);
						dao.closeDataBase();
						remove(title);
						notifyDataSetChanged();
						
					}
				});
    	    }
    	    return convertView;
    	}

    }
}
