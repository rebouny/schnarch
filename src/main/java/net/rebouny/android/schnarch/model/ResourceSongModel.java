/**
 * 
 */
package net.rebouny.android.schnarch.model;

import java.util.ArrayList;
import java.util.List;

import net.rebouny.android.schnarch.exception.SongModelInitializationException;
import android.content.Context;

/**
 * @author rebouny
 *
 */
public class ResourceSongModel implements SongModel
{
	private List<Song> songs;
	
	public ResourceSongModel()
	{
		
	}
	
	@Override
	public void initialize(Context context) throws SongModelInitializationException
	{
		this.songs = createDefaultSongs();
	}

	/**
	 * @see net.rebouny.android.schnarch.model.SongModel#getTitles()
	 */
	@Override
	public List<String> getTitles()
	{
		final List<String> titles = new ArrayList<String>();
		for (final Song song: this.songs)
			titles.add(song.getTitle());
		return titles;
	}

	/**
	 * @see net.rebouny.android.schnarch.model.SongModel#getSongsSize()
	 */
	@Override
	public int getSongsSize()
	{
		return this.songs.size();
	}

	/**
	 * @see net.rebouny.android.schnarch.model.SongModel#getSong(int)
	 */
	@Override
	public Song getSong(int index)
	{
		return this.songs.get(index);
	}

	private static List<Song> createDefaultSongs()
	{
		final List<Song> songs = new ArrayList<Song>();
		
		final Song s1 = new Song("Song 1");
		final Song s2 = new Song("Song 2");
		final Song s3 = new Song("Song 3");
		
		s1.addSection("section 1");
		s2.addSection("section 1");
		s3.addSection("section 1");
		s3.addSection("section 2");

		songs.add(s1);
		songs.add(s2);
		songs.add(s3);
		
		return songs;
	}
}
