/**
 * 
 */
package net.rebouny.android.schnarch.tools;

import net.rebouny.android.schnarch.model.Song;
import net.rebouny.android.schnarch.model.Song.SongReadState;

/**
 * Namespace containing some functions preparing songs for displaying.
 * 
 * @author Martin Schuh (development@rebouny.net)
 */
public class SongTools
{
	public static String convertToHtml(final Song song)
	{
		if (song.getState() == SongReadState.ERROR)
			return getErrorDocument(song);
		
		final StringBuffer sb = new StringBuffer();
		
		sb.append("<h2>" + song.getTitle() +"</h2>");
		for (final String s: song.getSections())
			sb.append("<p>" + replaceLineFeeds(s.trim()) + "</p>");
		
		return sb.toString();
	}	
	
	private static String getErrorDocument(final Song song)
	{
		return "<h2>cannot read song</h2>" 
	            + "<p>filename: "
				+ song.getFile().getAbsolutePath()
				+ "</p>";
	}
	
	private static String replaceLineFeeds(final String s)
	{
		return s.replaceAll("\r?\n", "<br/>");
	}
}
