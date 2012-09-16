/**
 * 
 */
package net.rebouny.android.schnarch.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
//import java.io.FilenameFilter;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import net.rebouny.android.schnarch.exception.BadSongFileException;
import net.rebouny.android.schnarch.exception.BadTokenParserException;
import net.rebouny.android.schnarch.model.Song.SongReadState;

/**
 * Parses songs in two steps. First is the title parser to get a nice listing,
 * second phase is to get real file content on demand.
 * 
 * TODO put parser error to song so we can show this to the user
 * 
 * @author Martin Schuh (development@rebouny.net)
 */
public class SongParser
{
	/**
	 * Different song tag (token) types.
	 * 
	 * @author Martin Schuh (development@rebouny.net)
	 */
	private enum SongTokenType
	{
		/** title of a song */
		title,
		/** a single verse */
		section,
		/** optional commentary */
		comment,
		/** unknown token, ignore */
		unknown,
		/** indicates end of song file */
		end;
		
		/**
		 * Create a token from string.
		 * 
		 * @param  s to parse
		 * @return parsed token, never <code>null</code>
		 */
		public static SongTokenType parseType(final String s)
		{
			if (s == null || s.trim().length() == 0)
				return end;
			try
			{
				return valueOf(s);
			}
			catch (IllegalArgumentException e)
			{
				return unknown;
			}
		}
	}
	
	private static class SongToken
	{
		public SongTokenType token;
		public String content;
	}
	
	//private static final String SONG_FILE_EXTENSION = ".song";
	
	private final File songDirectory;
	
	private final List<Song> songs = new ArrayList<Song>();
	
	public SongParser(final File songDirectory)
	{
		this.songDirectory = songDirectory;
	}
	
	public int getSongsSize()
	{
		return this.songs.size();
	}
	
	public Song getSongAt(final int index) 
	{
		final Song song = this.songs.get(index);
		
		try
		{
			if (song.getState() != SongReadState.FULL) parseSections(song);
		}
		catch (IOException e)
		{
			song.setState(SongReadState.ERROR);
			e.printStackTrace();
		}
		return song;
	}
	
	public List<String> getSongTitles()
	{
		final List<String> titles = new LinkedList<String>();

		if (songs.size() == 0)
		{
			// fetch titles
			
			final File[] files = this.songDirectory.listFiles(/*new FilenameFilter()
					{

						@Override
						public boolean accept(final File file, final String filter)
						{
							return file.getName().endsWith(SONG_FILE_EXTENSION);
						}
					}*/);
			
			for (final File file: files)
			{
				try
				{
					// parse song file for titles
					final Song song = parseTitle(file);
					
					this.songs.add(song);
				}
				catch (BadSongFileException e)
				{
					continue;
				}
			}
		}
		
		for (final Song song: this.songs)
			titles.add(song.getTitle());
		
		return titles;
	}

	static Song parseTitle(final File file) throws BadSongFileException
	{
		final Song song = new Song(file);
		
		try
		{
			final Reader f = new BufferedReader(new FileReader(file));
			try
			{
				final SongToken token = parseNextToken(f);
				switch (token.token)
				{
				case title:
					song.setTitle(token.content);
					song.setState(SongReadState.TITLE);
					break;
				case end:
					throw new BadSongFileException(file); // eof
				default:
					// continue
				}
			}
			finally
			{
				f.close();
			}
		}
		catch (BadTokenParserException e)
		{
			e.printStackTrace();
			throw new BadSongFileException(file);
		}
		catch (IOException e)
		{
			e.printStackTrace();
			throw new BadSongFileException(file);
		}

		return song;
	}
	
	public static void parseSections(final Song song) throws IOException
	{
		// open song.file, read sections
		
		final Reader f = new BufferedReader(new FileReader(song.getFile()));
		SongToken token;
		try
		{
			while (SongTokenType.end != (token = parseNextToken(f)).token)
			{
				switch (token.token)
				{
				case title:
					song.setTitle(token.content);
					break;
				case section:
					song.addSection(token.content);
				case comment:
					song.setComment(token.content);
				case end:
					break;
				default:
					// continue
				}
			}
			
			song.setState(SongReadState.FULL);
		}
		catch (BadTokenParserException e)
		{
			song.setState(SongReadState.ERROR);
			e.printStackTrace();
		}
		finally
		{
			f.close();
		}
	}
	
	private enum ParserState
	{
		init,
		key_begin_start,
		key_begin_content,
		key_begin_end,
		key_end_start,
		key_end_content,
		key_end_end,
		content
	}
	
	// example [abc]content[/abc]
	private static SongToken parseNextToken(final Reader in) throws IOException, BadTokenParserException
	{
		int cb;
		char c;
		ParserState state = ParserState.init;
		SongTokenType type = null;
		
		StringBuilder s = new StringBuilder();
		StringBuilder content = new StringBuilder();
		
		final SongToken token = new SongToken();
		
		boolean done = false;
		
		while (!done && (-1 < (cb = in.read())))
		{
			c = (char) cb;
			
			switch (c)
			{
			case '[': // begin token
				if (state == ParserState.init)
					state = ParserState.key_begin_start;
				else if (state == ParserState.content)
					state = ParserState.key_end_start;
				else
					throw new BadTokenParserException();
				break;
			case '/': // signal end token
				if (state == ParserState.key_end_start)
					state = ParserState.key_end_content;
				else
					throw new BadTokenParserException();
				break;
			case ']': // end token
				if (state == ParserState.key_begin_content)
				{
					state = ParserState.content;
					if (type != null) throw new BadTokenParserException();
					type = SongTokenType.parseType(s.toString());
					s.setLength(0);
				}
				else if (state == ParserState.key_end_content)
				{
					// get token type from buffer
					final SongTokenType localType = SongTokenType.parseType(s.toString());
					s.setLength(0);
					if (type == null || !localType.equals(type))
						throw new BadTokenParserException();
					token.token = type;
					token.content = content.toString();
					content.setLength(0);
					done = true;
				}
				else
					throw new BadTokenParserException();
				break;
			default: // content
				if (state == ParserState.content)
					content.append(c);
				else if (state == ParserState.key_begin_start)
				{
					state = ParserState.key_begin_content;
					s.append(c);
				}
				else if (state == ParserState.key_begin_content || state == ParserState.key_end_content)
					s.append(c);
				// else ignore character, is between
				break;
			}
		}
		
		if (!done)
		{
			if (type == null) token.token = SongTokenType.end;
			else throw new BadTokenParserException();
		}
		
		return token;
	}
}
