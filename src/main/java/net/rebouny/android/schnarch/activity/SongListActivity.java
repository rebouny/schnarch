/**
 * 
 */
package net.rebouny.android.schnarch.activity;

import java.util.List;

import net.rebouny.android.schnarch.SchnarchApplication;
import net.rebouny.android.schnarch.SchnarchApplication.ApplicationMode;
import net.rebouny.android.schnarch.exception.SongModelInitializationException;
import net.rebouny.android.schnarch.model.SongModel;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * Displays all stored songs.
 * 
 * @author Martin Schuh (development@rebouny.net)
 */
public class SongListActivity extends ListActivity implements OnItemClickListener
{
	/** adapter of list */
	private ListAdapter adapter;
	/** currently used model */
	private SongModel model;
	
	/**
	 * Initialization
	 * 
	 * @param Bundle to initialize from (not null if we're restoring a previous state).
	 */
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
        SchnarchApplication appState = ((SchnarchApplication) getApplicationContext());

        this.model = appState.getModel();
        try
        {
			this.model.initialize(this);
		}
        catch (SongModelInitializationException e)
        {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        List<String> titles = this.model.getTitles();
        
        // switch to DEMO mode if no files can be read
        if (titles.size() == 0)
        {
        	this.model = appState.switchMode(ApplicationMode.DEMO);
        	titles = this.model.getTitles();
        }
        
        this.adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, titles);
        setListAdapter(this.adapter);

        AnimationSet set = new AnimationSet(true);

        Animation animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setDuration(50);
        set.addAnimation(animation);

        animation = new TranslateAnimation(
            Animation.RELATIVE_TO_SELF, 0.0f,Animation.RELATIVE_TO_SELF, 0.0f,
            Animation.RELATIVE_TO_SELF, -1.0f,Animation.RELATIVE_TO_SELF, 0.0f
        );
        animation.setDuration(100);
        set.addAnimation(animation);

        final LayoutAnimationController controller = new LayoutAnimationController(set, 0.5f);
        final ListView listView = getListView();        
        listView.setLayoutAnimation(controller);
        listView.setOnItemClickListener((OnItemClickListener) this);
    }

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int index, long id)
	{
		//final Song selectedSong = model.getSong(index);
		
		final Intent intent = new Intent();
		final Bundle bundle = new Bundle();

		bundle.putInt("index", index);

		intent.setClass(this, ShowSongActivity.class);
		intent.putExtras(bundle);
		startActivity(intent);
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
	}
}