package com.flysystem.core.stubs;

import com.flysystem.core.Config;
import com.flysystem.core.FileMetadata;
import com.flysystem.core.Filesystem;
import com.flysystem.core.Visibility;
import com.flysystem.core.adapter.AbstractAdapter;

import java.io.OutputStream;
import java.util.List;
import java.util.Map;

/**
 * @author Zeger Hoogeboom
 */
public class AbstractAdapterStub extends AbstractAdapter
{
	public boolean has(String path)
	{
		return false;
	}

	public String read(String path)
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

	public boolean update(String path, String contents, Config config)
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

	public void rename(String from, String to)
	{

	}

	public void copy(String path, String newpath)
	{

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
