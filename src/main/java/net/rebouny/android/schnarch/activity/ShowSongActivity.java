/**
 * 
 */
package net.rebouny.android.schnarch.activity;

import net.rebouny.android.schnarch.R;
import net.rebouny.android.schnarch.SchnarchApplication;
import net.rebouny.android.schnarch.model.Song;
import net.rebouny.android.schnarch.model.SongModel;
import net.rebouny.android.schnarch.tools.SongTools;
import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

/**
 * Displays a single song.
 * 
 * @author Martin Schuh (development@rebouny.net)
 */
public class ShowSongActivity extends Activity
{
	/** currently used model, storing is not necessary */
	SongModel model;
	/** currently used song */
	Song song;
	
	/**
	 * Initialization
	 * 
	 * @param Bundle to initialize from (not null if we're restoring a previous state).
	 */
	public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
        final SchnarchApplication appState = ((SchnarchApplication) getApplicationContext());
        this.model = appState.getModel();
        
        // get "calling parameter"
        Bundle bundle = getIntent().getExtras();
        int index = bundle.getInt("index");
        
        this.song = this.model.getSong(index);
        
        setContentView(R.layout.show_song_layout);
        
        TextView view = (TextView) findViewById(R.id.show_song_textview);
        // TODO set this dynamically
        view.setTextSize(16); 
        view.setText(Html.fromHtml(SongTools.convertToHtml(this.song)));
    }
}
