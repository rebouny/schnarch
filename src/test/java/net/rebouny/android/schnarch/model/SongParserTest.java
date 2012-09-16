/**
 * 
 */
package net.rebouny.android.schnarch.model;

import java.io.File;

import net.rebouny.android.schnarch.model.Song;
import net.rebouny.android.schnarch.model.SongParser;

import junit.framework.TestCase;

/**
 * @author rebouny
 *
 */
public class SongParserTest extends TestCase
{
	private File exampleFile;
	
	private static final String EXAMPLE_FILENAME = "example.song";
	
	@Override
	public void setUp() throws Exception
	{
		this.exampleFile = new File(getClass().getClassLoader().getResource(EXAMPLE_FILENAME).getFile());
	}
	
	public void testParseExampleSongTitle() throws Exception
	{
		final Song song = SongParser.parseTitle(this.exampleFile);
		
		assertNotNull(song);
		assertNotNull(song.getTitle());
		assertTrue(song.getTitle().length() > 0);
	}

	public void testParseExampleSong() throws Exception
	{
		final Song song = SongParser.parseTitle(this.exampleFile);
		
		SongParser.parseSections(song);
		
		assertTrue(song.getSectionCount() > 0);
		for (final String section: song.getSections())
			assertTrue(section.length() > 0);
		
		assertNotNull(song.getComment());
		assertTrue(song.getComment().length() > 0);
	}
}
