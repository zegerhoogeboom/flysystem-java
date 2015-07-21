/*
 * Copyright (c) 2013-2015 Frank de Jonge
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is furnished
 * to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.flysystem.core;

import com.flysystem.core.exception.FileExistsException;
import com.flysystem.core.exception.FileNotFoundException;
import com.flysystem.core.exception.RootViolationException;
import com.flysystem.core.util.PathUtil;
import com.google.common.base.Strings;

import java.util.List;

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
		path = PathUtil.normalizePath(path);

		if (handler == null) {
			FileMetadata metadata = getMetadata(path);
			return metadata.isFile() ? new File(this, path) : new Directory(this, path);
		}

		handler.setPath(path);
		handler.setFilesystem(this);
		return handler;
	}

	public Handler get(String path)
	{
		return get(path, null);
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

	public List<FileMetadata> listContents(String directory, boolean recursive)
	{
		directory = PathUtil.normalizePath(directory);
		return adapter.listContents(directory, recursive);
	}

	public FileMetadata getMetadata(String path)
	{
		path = PathUtil.normalizePath(path);
		assertPresent(path);
		return adapter.getMetadata(path);
	}

	public Long getSize(String path)
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

	public Long getTimestamp(String path)
	{
		path = PathUtil.normalizePath(path);
		assertPresent(path);
		return adapter.getTimestamp(path);
	}

	public Visibility getVisibility(String path)
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

	public boolean rename(String from, String to)
	{
		from = PathUtil.normalizePath(from);
		to = PathUtil.normalizePath(to);
		assertPresent(from);
		assertAbsent(to);
		config = withConfigFallback(config);
		adapter.rename(from, to);
		return true;
	}

	public boolean copy(String path, String newpath)
	{
		path = PathUtil.normalizePath(path);
		newpath = PathUtil.normalizePath(newpath);
		assertPresent(path);
		assertAbsent(newpath);
		adapter.copy(path, newpath);
		return true;
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
		if (Strings.isNullOrEmpty(dirname)) {
			throw new RootViolationException("Root directories can not be deleted.");
		}
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
		if (config == null) {
			config = new Config();
		}
		return config.setFallback(this.config);
	}
}
