/**
 * 
 */
package net.rebouny.android.schnarch.model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author rebouny
 *
 */
public class Song
{
	private String title;
	private List<String> sections = new ArrayList<String>();
	private String comment;
	
	public enum SongReadState
	{
		INIT,
		TITLE,
		FULL,
		ERROR
	};
	
	private SongReadState state = SongReadState.INIT;
	
	/** song file reference */
	private final File file;
	
	public Song(final File file)
	{
		this.file = file;
	}
	
	public Song(final String title)
	{
		this.file = null;
		this.title = title;
		this.state = SongReadState.TITLE;
	}
	
	public void setTitle(final String title)
	{
		this.title = title;
	}
	
	public String getTitle()
	{
		return this.title;
	}
	
	public void setComment(final String comment)
	{
		this.comment = comment;
	}
	
	public String getComment()
	{
		return this.comment;
	}
	
	public void addSection(final String section)
	{
		this.sections.add(section);
	}
	
	public String[] getSections()
	{
		return this.sections.toArray(new String[0]);
	}
	
	public String getSection(final int index)
	{
		if (index >= sections.size()) return null;
		
		return this.sections.get(index);
	}
	
	public int getSectionCount()
	{
		return this.sections.size();
	}
	
	public SongReadState getState()
	{
		return this.state;
	}
	
	public void setState(final SongReadState state)
	{
		this.state = state;
	}
	
	public File getFile()
	{
		return this.file;
	}
	
	public void clearSong()
	{
		if (this.state == SongReadState.FULL)
		{
			this.state = SongReadState.TITLE;
			this.sections.clear();
		}
	}
}
