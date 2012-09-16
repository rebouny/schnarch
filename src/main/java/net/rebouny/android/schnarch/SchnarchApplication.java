/**
 * 
 */
package net.rebouny.android.schnarch;

import net.rebouny.android.schnarch.model.ResourceSongModel;
import net.rebouny.android.schnarch.model.SDCardSongModel;
import net.rebouny.android.schnarch.model.SongModel;
import android.app.Application;

/**
 * Provides application wide data storage that can be shared among activities.
 * 
 * TODO possibility to change the font-size
 *      we could use some song-tools configuration (stored in here) and force redraw the view
 * TODO create model factory and move out of application, probably song tools
 * 
 * @author Martin Schuh (development@rebouny.net)
 */
public class SchnarchApplication extends Application
{
	/** main data model class containing scanned and parsed songs */
	private SongModel model;
	/** application mode, demo or not */
	private ApplicationMode mode;
	
	/**
	 * Application mode constants.
	 * 
	 * @author Martin Schuh (development@rebouny.net)
	 */
	public enum ApplicationMode
	{
		/** demo mode, used as fallback if no real model can be loaded */
		DEMO,
		/** load songs from sd storage */
		SDCARD
	}

	/**
	 * Default constructions, enters sdcard mode.
	 */
	public SchnarchApplication()
	{
		switchMode(ApplicationMode.SDCARD);
	}
	
	/**
	 * @return SongModel stored
	 */
	public SongModel getModel()
	{
		return this.model;
	}
	
	/**
	 * @return ApplicationMode of application
	 */
	public ApplicationMode getApplicationMode()
	{
		return this.mode;
	}
	
	/**
	 * Switches mode of application wide data model.
	 * 
	 * @param  mode to switch to
	 * @return newly set model
	 */
	public SongModel switchMode(final ApplicationMode mode)
	{
		// skip unnecessary changes
		if (this.mode == mode) return this.model;
		
		this.mode = mode;
		
		switch (mode)
		{
		case SDCARD:
			this.model = new SDCardSongModel();
			break;
		case DEMO:
		default:
			this.model = new ResourceSongModel();
			break;
		}
		
		return this.model;
	}
}
