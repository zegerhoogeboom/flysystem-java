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

package com.flysystem.cache.stubs;

import com.flysystem.core.MetadataWrapper;
import com.flysystem.core.adapter.local.FileMetadataConverter;
import com.flysystem.core.cache.storage.AbstractCache;
import com.google.common.cache.Cache;

/**
 * @author Zeger Hoogeboom
 */
public class AbstractCacheStub extends AbstractCache
{
	public AbstractCacheStub(boolean autosave, Cache<String, MetadataWrapper> cache, Cache<String, Object> complete, FileMetadataConverter converter)
	{
		super(autosave, cache, complete, converter);
	}

	public AbstractCacheStub(boolean autosave)
	{
		super(autosave);
	}

	@Override
	public void save()
	{

	}

	@Override
	public void load()
	{

	}
}
