package com.flysystem.core;

import com.flysystem.core.exception.FileNotFoundException;

import java.io.*;
import java.util.List;
import java.util.Map;

/**
 * @author Zeger Hoogeboom
 */
public class FilesystemImpl implements Filesystem
{

	private Adapter adapter;

	public FilesystemImpl(Adapter adapter)
	{
		this.adapter = adapter;
	}

	public Handler get(String path, Handler handler)
	{
		return null;
	}

	public Filesystem addPlugin(Plugin plugin)
	{
		return null;
	}

	public boolean has(String path)
	{
		return false;
	}

	public String read(String path) throws FileNotFoundException
	{
		return null;
	}

	public String readStream(String path)
	{
		return null;
	}

	public List<com.flysystem.core.File> listContents(Filesystem filesystem, String directory, boolean recursive)
	{
		return null;
	}

	public FileMetadata getMetadata(String path)
	{
		return null;
	}

	public long getSize(String path)
	{
		return 0;
	}

	public String getMimetype(String path)
	{
		return null;
	}

	public long getTimestamp(String path)
	{
		return 0;
	}

	public String getVisibility(String path)
	{
		return null;
	}

	public boolean put(String path, String contents, Map<String, Object> config)
	{
		return false;
	}

	public boolean put(String path, String contents)
	{
		return false;
	}

	public boolean putStream(String path, OutputStream resource, Map<String, Object> config)
	{
		return false;
	}

	public boolean putStream(String path, OutputStream resource)
	{
		return false;
	}

	public String readAndDelete(String path)
	{
		return null;
	}

	public boolean write(String path, String contents, Map<String, Object> config)
	{
		return false;
	}

	public boolean write(String path, String contents)
	{
		return false;
	}

	public boolean writeStream(String path, OutputStream resource, Map<String, Object> config)
	{
		return false;
	}

	public boolean writeStream(String path, OutputStream resource)
	{
		return false;
	}

	public boolean update(String path, String contents, Map<String, Object> config)
	{
		return false;
	}

	public boolean update(String path, String contents)
	{
		return false;
	}

	public boolean updateStream(String path, OutputStream resource, Map<String, Object> config)
	{
		return false;
	}

	public boolean updateStream(String path, OutputStream resource)
	{
		return false;
	}

	public boolean rename(String path, String newpath)
	{
		return false;
	}

	public boolean copy(String path, String newpath)
	{
		return false;
	}

	public boolean delete(String path)
	{
		return false;
	}

	public boolean deleteDir(String dirname)
	{
		return false;
	}

	public boolean createDir(String dirname, Map<String, Object> config)
	{
		return false;
	}

	public boolean createDir(String dirname)
	{
		return false;
	}

	public boolean setVisibility(String path, Visibility visibility)
	{
		return false;
	}
}
