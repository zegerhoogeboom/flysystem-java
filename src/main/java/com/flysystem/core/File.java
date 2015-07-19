package com.flysystem.core;

import com.flysystem.core.exception.FileNotFoundException;
import com.google.common.base.Optional;

import java.io.OutputStream;

/**
 * @author Zeger Hoogeboom
 */
public class File extends Handler
{

	public File(Filesystem filesystem, String path)
	{
		super(filesystem, path);
	}

	public File(Filesystem filesystem, java.io.File file)
	{
		super(filesystem, file.getPath());
	}

	/**
	 * Check whether the file exists.
	 *
	 * @return bool
	 */
	public boolean exists()
	{
		return this.filesystem.has(this.path);
	}

	/**
	 * Read the file.
	 *
	 * @return string file contents
	 */
	public String read() throws FileNotFoundException
	{
		return this.filesystem.read(this.path);
	}

	/**
	 * Read the file as a stream.
	 *
	 * @return resource file stream
	 */
	public String readStream()
	{
		return this.filesystem.readStream(this.path);
	}

	/**
	 * Write the new file.
	 *
	 * @return bool success boolean
	 */
	public boolean write(String content)
	{
		return this.filesystem.write(this.path, content);
	}

	/**
	 * Write the new file using a stream.
	 *
	 * @param resource resource
	 *
	 * @return bool success boolean
	 */
	public boolean writeStream(OutputStream resource)
	{
		return this.filesystem.writeStream(this.path, resource);
	}

	/**
	 * Update the file contents.
	 * @return bool success boolean
	 */
	public boolean update(String content)
	{
		return this.filesystem.update(this.path, content);
	}

	/**
	 * Update the file contents with a stream.
	 *
	 * @param resource resource
	 *
	 * @return bool success boolean
	 */
	public boolean updateStream(OutputStream resource)
	{
		return this.filesystem.updateStream(this.path, resource);
	}

	/**
	 * Create the file or update if exists.
	 *
	 * @return bool success boolean
	 */
	public boolean put(String content)
	{
		return this.filesystem.put(this.path, content);
	}

	/**
	 * Create the file or update if exists using a stream.
	 *
	 * @param resource resource
	 *
	 * @return bool success boolean
	 */
	public boolean putStream(OutputStream resource)
	{
		return this.filesystem.putStream(this.path, resource);
	}

	/**
	 * Rename the file.
	 *
	 * @return bool success boolean
	 */
	public boolean rename(String newpath)
	{
		if (this.filesystem.rename(this.path, newpath)) {
			this.path = newpath;
			return true;
		}

		return false;
	}

	/**
	 * Copy the file.
	 *
	 * @return File|false new file or false
	 */
	public Optional<File> copy(String newpath)
	{
		if (this.filesystem.copy(this.path, newpath)) {
			return Optional.of(new File(this.filesystem, newpath));
		}

		return Optional.absent();
	}

	/**
	 * Get the file's timestamp.
	 *
	 * @return int unix timestamp
	 */
	public long getTimestamp()
	{
		return this.filesystem.getTimestamp(this.path);
	}

	/**
	 * Get the file's mimetype.
	 *
	 * @return string mimetime
	 */
	public String getMimetype()
	{
		return this.filesystem.getMimetype(this.path);
	}

	/**
	 * Get the file's visibility.
	 *
	 * @return string visibility
	 */
	public String getVisibility()
	{
		return this.filesystem.getVisibility(this.path);
	}

	/**
	 * Get the file's metadata.
	 *
	 * @return array
	 */
	public FileMetadata getMetadata()
	{
		return this.filesystem.getMetadata(this.path);
	}

	/**
	 * Get the file size.
	 *
	 * @return int file size
	 */
	public long getSize()
	{
		return this.filesystem.getSize(this.path);
	}

	/**
	 * Delete the file.
	 *
	 * @return bool success boolean
	 */
	public boolean delete()
	{
		return this.filesystem.delete(this.path);
	}

}
