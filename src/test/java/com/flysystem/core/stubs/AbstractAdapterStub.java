package com.flysystem.core.stubs;

import com.flysystem.core.Config;
import com.flysystem.core.FileMetadata;
import com.flysystem.core.Visibility;
import com.flysystem.core.adapter.AbstractAdapter;

import java.io.OutputStream;
import java.util.List;

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

	public List<FileMetadata> listContents(String directory, boolean recursive)
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

	public Visibility getVisibility(String path)
	{
		return null;
	}

	public boolean write(String path, String contents, Config config)
	{
		return false;
	}

	public boolean write(String path, String contents)
	{
		return false;
	}

	public boolean writeStream(String path, OutputStream resource, Config config)
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

	public boolean updateStream(String path, OutputStream resource, Config config)
	{
		return false;
	}

	public boolean updateStream(String path, OutputStream resource)
	{
		return false;
	}

	public boolean rename(String from, String to)
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

	public boolean createDir(String dirname, Config config)
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
