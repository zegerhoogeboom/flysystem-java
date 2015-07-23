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

package com.flysystem.cache;

import com.flysystem.core.Adapter;
import com.flysystem.core.Config;
import com.flysystem.core.FileMetadata;
import com.flysystem.core.Visibility;
import com.flysystem.core.cache.Cache;
import com.flysystem.core.cache.CacheCommands;
import com.flysystem.core.cache.CachedAdapter;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

/**
 * @author Zeger Hoogeboom
 */
public class CachedAdapterTest
{
	CachedAdapter cachedAdapter;
	Cache mockedCache;
	Adapter mockedAdapter;
	CacheCommands mockedCommands;
	String path = "example.txt";
	String contents = "test";
	String to = "new.txt";

	@Before
	public void setUp() throws Exception
	{
		mockedAdapter = mock(Adapter.class);
		mockedCache = mock(Cache.class);
		mockedCommands = mock(CacheCommands.class);
		this.cachedAdapter = new CachedAdapter(mockedAdapter, mockedCache, mockedCommands);
	}

	@Test
	public void constructorTest()
	{
		CachedAdapter cachedAdapter = new CachedAdapter(mockedAdapter, mockedCache);
		assertThat(cachedAdapter.getAdapter(), CoreMatchers.instanceOf(Adapter.class));
		assertThat(cachedAdapter.getCache(), CoreMatchers.instanceOf(Cache.class));
	}

	@Test
	public void read()
	{
		when(mockedCommands.get(eq(path), Matchers.<CacheCommands.GetFromCacheCommand<Object>>any())).thenReturn(contents);
		assertEquals(contents, cachedAdapter.read(path));
	}

	@Test
	public void has()
	{
		when(mockedCommands.get(eq(path), Matchers.<CacheCommands.GetFromCacheCommand<Object>>any())).thenReturn(true);
		assertTrue(cachedAdapter.has(path));
	}

	@Test
	public void getMetadata()
	{
		FileMetadata metadata = new FileMetadata(path);
		when(mockedCommands.get(eq(path), Matchers.<CacheCommands.GetFromCacheCommand<Object>>any())).thenReturn(metadata);
		assertEquals(metadata, cachedAdapter.getMetadata(path));
	}

	@Test
	public void getSize()
	{
		when(mockedCommands.get(eq(path), Matchers.<CacheCommands.GetFromCacheCommand<Object>>any())).thenReturn(4l);
		assertEquals(4l, (long) cachedAdapter.getSize(path));
	}

	@Test
	public void getMimetype()
	{
		when(mockedCommands.get(eq(path), Matchers.<CacheCommands.GetFromCacheCommand<Object>>any())).thenReturn(contents);
		assertEquals(contents, cachedAdapter.getMimetype(path));
	}

	@Test
	public void getTimestamp()
	{
		when(mockedCommands.get(eq(path), Matchers.<CacheCommands.GetFromCacheCommand<Object>>any())).thenReturn(4l);
		assertEquals(4l, (long) cachedAdapter.getTimestamp(path));
	}

	@Test
	public void getVisibility()
	{
		when(mockedCommands.get(eq(path), Matchers.<CacheCommands.GetFromCacheCommand<Object>>any())).thenReturn(Visibility.PUBLIC);
		assertEquals(Visibility.PUBLIC, cachedAdapter.getVisibility(path));
	}

	@Test
	public void listContentsAlreadyCached()
	{
		List<FileMetadata> list = new ArrayList<>();
		when(mockedCache.isComplete(path, true)).thenReturn(true);
		when(mockedCache.listContents(path, true)).thenReturn(list);
		assertEquals(list, cachedAdapter.listContents(path, true));
		verify(mockedCache, times(1)).listContents(path, true);
	}

	@Test
	public void listContentsNotYetCached()
	{
		List<FileMetadata> list = new ArrayList<>();
		when(mockedCache.isComplete(path, true)).thenReturn(false);
		when(mockedCache.listContents(path, true)).thenReturn(list);
		assertEquals(list, cachedAdapter.listContents(path, true));
		verify(mockedCache, times(1)).storeContents(path, list, true);
	}

	@Test
	public void writeSuccess()
	{
		when(mockedAdapter.write(path, contents, new Config())).thenReturn(true);
		assertEquals(true, cachedAdapter.write(path, contents));
		verify(mockedCache, times(1)).updateObject(path, contents, true);
	}

	@Test
	public void writeFail()
	{
		when(mockedAdapter.write(path, contents)).thenReturn(false);
		assertEquals(false, cachedAdapter.write(path, contents));
		verify(mockedCache, times(0)).updateObject(path, contents, true);
	}

	@Test
	public void updateSuccess()
	{
		when(mockedAdapter.update(path, contents, new Config())).thenReturn(true);
		assertEquals(true, cachedAdapter.update(path, contents));
		verify(mockedCache, times(1)).updateObject(path, contents, true);
	}

	@Test
	public void updateFail()
	{
		when(mockedAdapter.update(path, contents)).thenReturn(false);
		assertEquals(false, cachedAdapter.update(path, contents));
		verify(mockedCache, times(0)).updateObject(path, contents, true);
	}

	@Test
	public void renameSuccess()
	{
		when(mockedAdapter.rename(path, to)).thenReturn(true);
		assertEquals(true, cachedAdapter.rename(path, to));
		verify(mockedCache, times(1)).rename(path, to);
	}

	@Test
	public void renameFail()
	{
		when(mockedAdapter.rename(path, to)).thenReturn(false);
		assertEquals(false, cachedAdapter.rename(path, to));
		verify(mockedCache, times(0)).rename(path, to);
	}

	@Test
	public void copySuccess()
	{
		when(mockedAdapter.copy(path, to)).thenReturn(true);
		assertEquals(true, cachedAdapter.copy(path, to));
		verify(mockedCache, times(1)).copy(path, to);
	}

	@Test
	public void copyFail()
	{
		when(mockedAdapter.copy(path, to)).thenReturn(false);
		assertEquals(false, cachedAdapter.copy(path, to));
		verify(mockedCache, times(0)).copy(path, to);
	}

	@Test
	public void deleteSuccess()
	{
		when(mockedAdapter.delete(path)).thenReturn(true);
		assertEquals(true, cachedAdapter.delete(path));
		verify(mockedCache, times(1)).delete(path);
	}

	@Test
	public void deleteFail()
	{
		when(mockedAdapter.delete(path)).thenReturn(false);
		assertEquals(false, cachedAdapter.delete(path));
		verify(mockedCache, times(0)).delete(path);
	}

	@Test
	public void deleteDirSuccess()
	{
		when(mockedAdapter.deleteDir(path)).thenReturn(true);
		assertEquals(true, cachedAdapter.deleteDir(path));
		verify(mockedCache, times(1)).deleteDir(path);
	}

	@Test
	public void deleteDirFail()
	{
		when(mockedAdapter.deleteDir(path)).thenReturn(false);
		assertEquals(false, cachedAdapter.deleteDir(path));
		verify(mockedCache, times(0)).deleteDir(path);
	}

	@Test
	public void createDirSuccess()
	{
		when(mockedAdapter.createDir(path, new Config())).thenReturn(true);
		assertEquals(true, cachedAdapter.createDir(path, new Config()));
		verify(mockedCache, times(1)).updateObject(path, new HashMap<String, Object>()
		{{
				put("path", path);
				put("type", "dir");
			}}, true);
	}

	@Test
	public void createDirFail()
	{
		when(mockedAdapter.createDir(path)).thenReturn(false);
		assertEquals(false, cachedAdapter.createDir(path));
		verify(mockedCache, times(0)).updateObject(path, new HashMap<String, Object>()
		{{
				put("path", path);
				put("type", "dir");
			}}, true);
	}

	@Test
	public void setVisibilitySuccess()
	{
		when(mockedAdapter.setVisibility(path, Visibility.PUBLIC)).thenReturn(true);
		assertEquals(true, cachedAdapter.setVisibility(path, Visibility.PUBLIC));
		verify(mockedCache, times(1)).updateObject(path, Visibility.PUBLIC, true);
	}

	@Test
	public void setVisibilityFail()
	{
		when(mockedAdapter.setVisibility(path, Visibility.PUBLIC)).thenReturn(false);
		assertEquals(false, cachedAdapter.setVisibility(path, Visibility.PUBLIC));
		verify(mockedCache, times(0)).updateObject(path, Visibility.PUBLIC, true);
	}
}
