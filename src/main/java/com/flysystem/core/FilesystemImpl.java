package com.flysystem.core;

import com.flysystem.core.exception.FileExistsException;
import com.flysystem.core.exception.FileNotFoundException;
import com.flysystem.core.util.PathUtil;

import java.io.OutputStream;
import java.util.List;
import java.util.Map;

/**
 * @author Zeger Hoogeboom
 */
public class FilesystemImpl implements Filesystem
{

	private Adapter adapter;
	private Config config;

	public FilesystemImpl(Adapter adapter, Config config)
	{
		this.adapter = adapter;
		this.config = config;
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
		path = PathUtil.normalizePath(path);
		return adapter.has(path);
	}

	public String read(String path) throws FileNotFoundException
	{
		path = PathUtil.normalizePath(path);
		assertPresent(path);
		return adapter.read(path);
	}

	public String readStream(String path)
	{
		return null;
	}

	public List<com.flysystem.core.File> listContents(Filesystem filesystem, String directory, boolean recursive)
	{
		directory = PathUtil.normalizePath(directory);
		assertPresent(directory);
		return adapter.listContents(filesystem, directory, recursive);
	}

	public FileMetadata getMetadata(String path)
	{
		path = PathUtil.normalizePath(path);
		assertPresent(path);
		return adapter.getMetadata(path);
	}

	public long getSize(String path)
	{
		path = PathUtil.normalizePath(path);
		assertPresent(path);
		return adapter.getSize(path);
	}

	public String getMimetype(String path)
	{
		path = PathUtil.normalizePath(path);
		assertPresent(path);
		return adapter.getMimetype(path);
	}

	public long getTimestamp(String path)
	{
		path = PathUtil.normalizePath(path);
		assertPresent(path);
		return adapter.getTimestamp(path);
	}

	public String getVisibility(String path)
	{
		path = PathUtil.normalizePath(path);
		assertPresent(path);
		return adapter.getVisibility(path);
	}

	public boolean put(String path, String contents, Config config)
	{
		path = PathUtil.normalizePath(path);
		config = withConfigFallback(config);
		if (has(path)) return adapter.update(path, contents, config);
		return adapter.write(path, contents, config);
	}

	public boolean put(String path, String contents)
	{
		return put(path, contents, new Config());
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
		path = PathUtil.normalizePath(path);
		assertPresent(path);
		String contents = read(path);
		delete(path);
		return contents;
	}

	public boolean write(String path, String contents, Config config)
	{
		path = PathUtil.normalizePath(path);
		assertAbsent(path);
		config = withConfigFallback(config);
		return adapter.write(path, contents, config);
	}

	public boolean write(String path, String contents)
	{
		return write(path, contents, new Config());
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
		path = PathUtil.normalizePath(path);
		assertPresent(path);
		config = withConfigFallback(config);
		return adapter.update(path, contents, config);
	}

	public boolean update(String path, String contents)
	{
		return update(path, contents, new Config());
	}

	public boolean updateStream(String path, OutputStream resource, Config config)
	{
		return false;
	}

	public boolean updateStream(String path, OutputStream resource)
	{
		return false;
	}

	public void rename(String from, String to)
	{
		from = PathUtil.normalizePath(from);
		to = PathUtil.normalizePath(to);
		assertPresent(from);
		assertAbsent(to);
		config = withConfigFallback(config);
		adapter.rename(from, to);
	}

	public void copy(String path, String newpath)
	{
		path = PathUtil.normalizePath(path);
		newpath = PathUtil.normalizePath(newpath);
		assertPresent(path);
		assertAbsent(newpath);
		adapter.copy(path, newpath);
	}

	public boolean delete(String path)
	{
		path = PathUtil.normalizePath(path);
		assertPresent(path);
		return adapter.delete(path);
	}

	public boolean deleteDir(String dirname)
	{
		dirname = PathUtil.normalizePath(dirname);
		assertPresent(dirname);
		return adapter.deleteDir(dirname);
	}

	public boolean createDir(String dirname, Config config)
	{
		dirname = PathUtil.normalizePath(dirname);
		assertAbsent(dirname);
		return adapter.createDir(dirname, config);
	}

	public boolean createDir(String dirname)

	{
		return createDir(dirname, new Config());
	}

	public boolean setVisibility(String path, Visibility visibility)
	{
		path = PathUtil.normalizePath(path);
		assertPresent(path);
		return adapter.setVisibility(path, visibility);
	}

	private void assertPresent(String path)
	{
		if (!has(path)) throw new FileNotFoundException(path);
	}

	private void assertAbsent(String path)
	{
		if (has(path)) throw new FileExistsException(path);
	}

	protected Config withConfigFallback(Config config)
	{
		return config.setFallback(this.config);
	}
}
