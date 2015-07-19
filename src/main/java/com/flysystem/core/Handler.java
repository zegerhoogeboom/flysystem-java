package com.flysystem.core;

import java.util.Map;

/**
 * @author Zeger Hoogeboom
 */
abstract class Handler
{
	protected String path;
	protected Filesystem filesystem;

	public Handler(Filesystem filesystem, String path)
	{
		this.filesystem = filesystem;
		this.path = path;
	}

	/**
	 * Check whether the entree is a directory.
	 *
	 * @return bool
	 */
	public boolean isDir()
	{
		return this.getType().equals("dir");
	}

	/**
	 * Check whether the entree is a file.
	 *
	 * @return bool
	 */
	public boolean isFile()
	{
		return this.getType().equals("file");
	}

	/**
	 * Retrieve the entree type (file|dir).
	 *
	 * @return string file or dir
	 */
	public String getType()
	{
		Map<String, Object> metadata = this.filesystem.getMetadata(this.path);
		return (String) metadata.get("type");
	}


	public void setPath(String path)
	{
		this.path = path;
	}

	public String getPath()
	{
		return path;
	}

	public Filesystem getFilesystem()
	{
		return filesystem;
	}

	public void setFilesystem(Filesystem filesystem)
	{
		this.filesystem = filesystem;
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (! (o instanceof Handler)) return false;

		Handler handler = (Handler) o;

		if (path != null ? ! path.equals(handler.path) : handler.path != null) return false;
		return ! (filesystem != null ? ! filesystem.equals(handler.filesystem) : handler.filesystem != null);

	}

	@Override
	public int hashCode()
	{
		int result = path != null ? path.hashCode() : 0;
		result = 31 * result + (filesystem != null ? filesystem.hashCode() : 0);
		return result;
	}
}