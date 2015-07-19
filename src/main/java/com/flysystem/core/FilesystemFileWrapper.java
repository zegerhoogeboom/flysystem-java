package com.flysystem.core;

import java.io.File;

/**
 * @author Zeger Hoogeboom
 */
public class FilesystemFileWrapper
{
	private Filesystem filesystem;
	private File file;

	public FilesystemFileWrapper(Filesystem filesystem, File file)
	{
		this.filesystem = filesystem;
		this.file = file;
	}

	public Filesystem getFilesystem()
	{
		return filesystem;
	}

	public void setFilesystem(Filesystem filesystem)
	{
		this.filesystem = filesystem;
	}

	public File getFile()
	{
		return file;
	}

	public void setFile(File file)
	{
		this.file = file;
	}
}
