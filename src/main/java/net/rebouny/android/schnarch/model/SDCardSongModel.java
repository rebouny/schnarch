/**
 * 
 */
package net.rebouny.android.schnarch.model;

import java.io.File;
import java.util.List;

import net.rebouny.android.schnarch.exception.NoSongsDirectoryException;
import net.rebouny.android.schnarch.exception.NoStorageAvailableException;
import net.rebouny.android.schnarch.exception.SongModelInitializationException;
import net.rebouny.android.schnarch.tools.StorageTools;

import android.content.Context;

/**
 * @author rebouny
 *
 */
public class SDCardSongModel implements SongModel
{
	private SongParser parser;
	private File songsDirectory;
	
	public SDCardSongModel() 
	{

	}

	@Override
	public void initialize(final Context context) throws SongModelInitializationException
	{
		try
		{
			this.songsDirectory = StorageTools.getApplicationStorageDirectory(context);
			this.parser = new SongParser(this.songsDirectory);
		}
		catch (NoStorageAvailableException e)
		{
			throw new SongModelInitializationException();
		}
		catch (NoSongsDirectoryException e)
		{
			throw new SongModelInitializationException();
		}		
	}
	
	@Override
	public List<String> getTitles()
	{
		return this.parser.getSongTitles();
	}
	
	@Override
	public int getSongsSize()
	{
		return this.parser.getSongsSize();
	}
	
	@Override
	public Song getSong(final int index)
	{
		return this.parser.getSongAt(index);
	}
}
