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

package com.flysystem.core.cache.storage;

import com.flysystem.core.FileMetadata;
import com.flysystem.core.MetadataWrapper;
import com.flysystem.core.Visibility;
import com.flysystem.core.adapter.local.FileMetadataConverter;
import com.flysystem.core.cache.Cache;
import com.flysystem.core.exception.FileNotFoundException;
import com.google.common.cache.CacheBuilder;

import java.util.List;
import java.util.Map;

/**
 * @author Zeger Hoogeboom
 */
public abstract class AbstractCache implements Cache
{

	protected boolean autosave = true;

	private com.google.common.cache.Cache<String, MetadataWrapper> cache;
	private com.google.common.cache.Cache<String, Object> complete;
	private FileMetadataConverter converter;
	private static final String recursive = "recursive";

	public AbstractCache(boolean autosave,
	                     com.google.common.cache.Cache<String, MetadataWrapper> cache,
	                     com.google.common.cache.Cache<String, Object> complete,
	                     FileMetadataConverter converter)
	{
		this.autosave = autosave;
		this.cache = cache;
		this.complete = complete;
		this.converter = converter;
	}

	public AbstractCache(boolean autosave)
	{
		this.autosave = autosave;
		this.cache = CacheBuilder.newBuilder().build();
		this.complete = CacheBuilder.newBuilder().build();
		converter = new FileMetadataConverter();
	}

	@Override
	protected void finalize() throws Throwable
	{
		super.finalize();
		if (!autosave) save();
	}


	public boolean isAutosave()
	{
		return autosave;
	}

	public void setAutosave(boolean autosave)
	{
		this.autosave = autosave;
	}

	@Override
	public void updateObject(String path, Object object)
	{
		/*if (! has(path)) {
			cache.put(path, PathUtil.pathinfo(path));
		}*/
		updateObject(path, object, false);
//		ensureParentDirectories(path); //fixme
	}

	@Override
	public void updateObject(String path, Object object, boolean autoSave)
	{
		cache.put(path, converter.fromPath(path, object));
		if (autoSave) save();
	}

	@Override
	public boolean isComplete(String dirname, boolean recursive)
	{
		if (complete.getIfPresent(dirname) == null) return false;
		if (recursive &&
				complete.getIfPresent(dirname) != null &&
				complete.getIfPresent(dirname) != AbstractCache.recursive) {
			return false;
		}
		return true;
	}

	@Override
	public void setComplete(String dirname, boolean recursive)
	{
		complete.put(dirname, recursive ? AbstractCache.recursive : true);
	}

	@Override
	public void storeContents(String directory, List<FileMetadata> contents, boolean recursive)
	{
		for (FileMetadata content : contents) {
			updateObject(content.getPath(), content);
		}
		setComplete(directory, recursive);
		autosave();
	}

	@Override
	public void flush()
	{
	  	cache.invalidateAll();
		autosave();
	}

	@Override
	public void autosave()
	{
	  	if (autosave) save();
	}

	@Override
	public void rename(String from, String to)
	{
		if (has(from)) {
			MetadataWrapper object = cache.getIfPresent(from);
			cache.invalidate(from);
			cache.put(to, converter.fromPath(to, object.getObject()));
			autosave();
		}
	}

	@Override
	public void copy(String from, String to)
	{
		if (has(from)) {
			MetadataWrapper object = cache.getIfPresent(from);
			updateObject(to, object.getObject(), true);
		}
	}

	@Override
	public void delete(String path)
	{
		storeMiss(path);
	}

	@Override
	public void deleteDir(String dirname)
	{
		for (Map.Entry<String, MetadataWrapper> entry : cache.asMap().entrySet()) {
		   if (entry.getKey().startsWith(dirname)) {
			   cache.invalidate(entry.getKey());
		   }
		}

		if (complete.getIfPresent(dirname) != null) {
			complete.invalidate(dirname);
		}

		autosave();
	}

	@Override
	public void storeMiss(String path)
	{
		cache.put(path, converter.fromPath(path, false));
		autosave();
	}

	@Override
	public boolean has(String path)
	{
		MetadataWrapper fromCache = cache.getIfPresent(path);
		return  fromCache != null &&
				fromCache.getObject() != null &&
				fromCache.getObject() != false;
	}

	@Override
	public String read(String path) throws FileNotFoundException
	{
		MetadataWrapper present = cache.getIfPresent(path);
		if (present == null) return null;
		return (String) present.getObject();
	}

	@Override
	public List<FileMetadata> listContents(String directory, boolean recursive)
	{
		//todo implement
		return null;
	}

	@Override
	public FileMetadata getMetadata(String path)
	{
		MetadataWrapper present = cache.getIfPresent(path);
		if (present == null) return null;
		return present.getFileMetadata();
	}

	@Override
	public Long getSize(String path)
	{
		MetadataWrapper present = cache.getIfPresent(path);
		if (present == null) return null;
		return present.getFileMetadata().getSize();
	}

	@Override
	public String getMimetype(String path)
	{
		MetadataWrapper present = cache.getIfPresent(path);
		if (present == null) return null;
		return present.getFileMetadata().getMimetype();
	}

	@Override
	public Long getTimestamp(String path)
	{
		MetadataWrapper present = cache.getIfPresent(path);
		if (present == null) return null;
		return present.getFileMetadata().getTimestamp();
	}

	@Override
	public Visibility getVisibility(String path)
	{
		MetadataWrapper present = cache.getIfPresent(path);
		if (present == null) return null;
		return present.getFileMetadata().getVisibility();
	}
}
