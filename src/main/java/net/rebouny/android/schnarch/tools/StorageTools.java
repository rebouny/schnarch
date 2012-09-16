/**
 * 
 */
package net.rebouny.android.schnarch.tools;

import java.io.File;

import net.rebouny.android.schnarch.exception.NoSongsDirectoryException;
import net.rebouny.android.schnarch.exception.NoStorageAvailableException;

import android.content.Context;
import android.os.Environment;

/**
 * Provides access to external storage for reading song-format.
 * 
 * @author Martin Schuh (development@rebouny.net)
 */
public class StorageTools
{
	/** sub-dir constant too store song files at*/
	private static final String SONG_DIRECTORY_NAME = "songs";
	
	/**
	 * @return whether sdcard is available or not
	 */
	public static boolean isMediaAvailable()
	{
		final String state = Environment.getExternalStorageState();

		return (Environment.MEDIA_MOUNTED.equals(state)
				|| Environment.MEDIA_MOUNTED_READ_ONLY.equals(state));
	}
	
	/**
	 * Returns file pointing to storage directory of application.
	 * 
	 * @param  context to look at
	 * @return directory to get songs from
	 * 
	 * @throws NoStorageAvailableException if storage directory is not available
	 * @throws NoSongsDirectoryException if songs directory cannot be created 
	 */
	public static File getApplicationStorageDirectory(final Context context)
        throws NoStorageAvailableException, NoSongsDirectoryException
	{
		if (!isMediaAvailable()) throw new NoStorageAvailableException();
		
		// getExternalFilesDir is an api 8 function
		// TODO look up an alternative so this is running on my galaxy
		final File externalStorageRoot = context.getExternalFilesDir(null);
		final File songsDirectory = new File(externalStorageRoot + File.separator + SONG_DIRECTORY_NAME);
		
		boolean songsDirExists = songsDirectory.exists();
		if (! songsDirExists) songsDirExists = songsDirectory.mkdirs();
		
		if (! songsDirExists) throw new NoSongsDirectoryException();
		
		return songsDirectory;
	}
}
