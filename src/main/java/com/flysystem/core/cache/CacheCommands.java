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

package com.flysystem.core.cache;

import com.flysystem.core.Adapter;
import com.flysystem.core.FileMetadata;
import com.flysystem.core.Visibility;

/**
 * @author Zeger Hoogeboom
 */
public class CacheCommands
{

	private final Adapter adapter;
	private Cache cache;

	public CacheCommands(CachedAdapter cachedAdapter)
	{
		this.cache = cachedAdapter.getCache();
		this.adapter = cachedAdapter.getAdapter();
	}

	public interface GetFromCacheCommand<T>
	{
		T fromCache(String path);
		T fromFilesystem(String path);
	}

	class GetMetadataCommand implements GetFromCacheCommand<FileMetadata> {
		public FileMetadata fromCache(String path) {
			return cache.getMetadata(path);
		}
		public FileMetadata fromFilesystem(String path) {
			return adapter.getMetadata(path);
		}
	}

	class GetTimestampCommand implements GetFromCacheCommand<Long>
	{
		public Long fromCache(String path) {
			return cache.getTimestamp(path);
		}
		public Long fromFilesystem(String path) {
			return adapter.getTimestamp(path);
		}
	}

	class GetSizeCommand implements GetFromCacheCommand<Long>
	{
		public Long fromCache(String path) {
			return cache.getSize(path);
		}
		public Long fromFilesystem(String path) {
			return adapter.getSize(path);
		}
	}

	class GetVisibilityCommand implements GetFromCacheCommand<Visibility>
	{
		public Visibility fromCache(String path) {
			return cache.getVisibility(path);
		}
		public Visibility fromFilesystem(String path) {
			return adapter.getVisibility(path);
		}
	}

	class GetMimetypeCommand implements GetFromCacheCommand<String>
	{
		public String fromCache(String path) {
			return cache.getMimetype(path);
		}
		public String fromFilesystem(String path) {
			return adapter.getMimetype(path);
		}
	}

	class ReadCommand implements GetFromCacheCommand<String>
	{
		public String fromCache(String path) {
			return cache.read(path);
		}
		public String fromFilesystem(String path) {
			return adapter.read(path);
		}
	}

	class HasCommand implements GetFromCacheCommand<Boolean>
	{
		public Boolean fromCache(String path) {
			return cache.has(path);
		}
		public Boolean fromFilesystem(String path) {
			return adapter.has(path);
		}
	}

	public <T> T get(String path, GetFromCacheCommand<T> command)
	{
		T cached = command.fromCache(path);
		if (cached != null) return cached;

		T fetched = command.fromFilesystem(path);
		if (fetched != null) {
			cache.updateObject(path, fetched, true);
		} else {
			cache.storeMiss(path);
		}
		return fetched;
	}

}
