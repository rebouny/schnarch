/**
 * 
 */
package net.rebouny.android.schnarch.model;

import java.util.List;

import android.content.Context;

import net.rebouny.android.schnarch.exception.SongModelInitializationException;

/**
 * Song model interface.
 * 
 * @author Martin Schuh (development@rebouny.net)
 */
public interface SongModel
{
	/**
	 * Initialize the model, need context to query android environment.
	 * 
	 * @param  context to query
	 * @throws SongModelInitializationException on initialization failures
	 */
	public void initialize(final Context context) throws SongModelInitializationException;
	
	/**
	 * @return List of Strings titles of all songs to display
	 */
	public List<String> getTitles();
	
	/**
	 * @return amount of scanned songs
	 */
	public int getSongsSize();
	
	/**
	 * Returns a song by index.
	 * 
	 * @param  index to look for
	 * @return queried song, <code>null</code> if none is found
	 */
	public Song getSong(final int index);
}
