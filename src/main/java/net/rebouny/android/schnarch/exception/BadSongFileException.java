/**
 * 
 */
package net.rebouny.android.schnarch.exception;

import java.io.File;

/**
 * @author rebouny
 *
 */
public class BadSongFileException extends Exception
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final File file;
	
	public BadSongFileException(final File file)
	{
		this.file = file;
	}
	
	public File getFile()
	{
		return this.file;
	}
}
