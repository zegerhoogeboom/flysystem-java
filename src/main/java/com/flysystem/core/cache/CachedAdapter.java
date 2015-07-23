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

package com.flysystem.core.cache;/*
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

import com.flysystem.core.Adapter;
import com.flysystem.core.Config;
import com.flysystem.core.FileMetadata;
import com.flysystem.core.Visibility;
import com.flysystem.core.exception.FileExistsException;
import com.flysystem.core.exception.FileNotFoundException;

import java.util.HashMap;
import java.util.List;

/**
 * @author Zeger Hoogeboom
 */
public class CachedAdapter implements Adapter
{

	private final Adapter adapter;
	private final Cache cache;
	private CacheCommands cacheCommands;

	public CachedAdapter(Adapter adapter, Cache cache)
	{
		this.adapter = adapter;
		this.cache = cache;
		this.cacheCommands = new CacheCommands(this);
	}

	public CachedAdapter(Adapter adapter, Cache cache, CacheCommands cacheCommands)
	{
		this.adapter = adapter;
		this.cache = cache;
		this.cacheCommands = cacheCommands;
	}

	public Adapter getAdapter()
	{
		return adapter;
	}

	public Cache getCache()
	{
		return cache;
	}

	public List<FileMetadata> listContents(String directory, boolean recursive)
	{
		if (cache.isComplete(directory, recursive)) {
			return cache.listContents(directory, recursive);
		}
		List<FileMetadata> result = adapter.listContents(directory, recursive);
		if (result != null) {
			cache.storeContents(directory, result, recursive);
		}
		return result;
	}

	public boolean has(String path)
	{
		return cacheCommands.get(path, cacheCommands.new HasCommand());
	}

	public String read(String path) throws FileNotFoundException
	{
		return cacheCommands.get(path, cacheCommands.new ReadCommand());
	}

	public FileMetadata getMetadata(String path)
	{
		return cacheCommands.get(path, cacheCommands.new GetMetadataCommand());
	}

	public Long getSize(String path)
	{
		return cacheCommands.get(path, cacheCommands.new GetSizeCommand());
	}

	public String getMimetype(String path)
	{
		return cacheCommands.get(path, cacheCommands.new GetMimetypeCommand());
	}

	public Long getTimestamp(String path)
	{
		return cacheCommands.get(path, cacheCommands.new GetTimestampCommand());
	}

	public Visibility getVisibility(String path)
	{
		return cacheCommands.get(path, cacheCommands.new GetVisibilityCommand());
	}

	public boolean write(String path, String contents, Config config)
	{
		boolean result = adapter.write(path, contents, config);
		if (result) {
			cache.updateObject(path, contents, true);
		}
		return result;
	}

	public boolean write(String path, String contents)
	{
		return write(path, contents, new Config());
	}

	public boolean update(String path, String contents)
	{
		return update(path, contents, new Config());
	}

	public boolean update(String path, String contents, Config config)
	{
		boolean result = adapter.update(path, contents, config);
		if (result) {
			cache.updateObject(path, contents, true);
		}
		return result;
	}

	public boolean rename(String from, String to) throws FileExistsException, FileNotFoundException
	{
		boolean result = adapter.rename(from, to);
		if (result) {
			cache.rename(from, to);
		}
		return result;
	}

	public boolean copy(String path, String newpath)
	{
		boolean result = adapter.copy(path, newpath);
		if (result) {
			cache.copy(path, newpath);
		}
		return result;
	}

	public boolean delete(String path)
	{
		boolean result = adapter.delete(path);
		if (result) {
			cache.delete(path);
		}
		return result;
	}

	public boolean deleteDir(String dirname)
	{
		boolean result = adapter.deleteDir(dirname);
		if (result) {
			cache.deleteDir(dirname);
		}
		return result;
	}

	public boolean createDir(final String dirname, Config config)
	{
		boolean result = adapter.createDir(dirname, config);
		if (result) {
			cache.updateObject(dirname, new HashMap<String, Object>()
			{{
					put("path", dirname);
					put("type", "dir");
				}}, true);
		}
		return result;
	}

	public boolean createDir(String dirname)
	{
		return createDir(dirname, new Config());
	}

	public boolean setVisibility(final String path, Visibility visibility)
	{
		boolean result = adapter.setVisibility(path, visibility);
		if (result) {
			cache.updateObject(path, visibility, true);
		}
		return result;
	}

}
