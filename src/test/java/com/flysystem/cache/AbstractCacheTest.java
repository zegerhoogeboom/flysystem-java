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

import com.flysystem.cache.stubs.AbstractCacheStub;
import com.flysystem.core.FileMetadata;
import com.flysystem.core.MetadataWrapper;
import com.flysystem.core.Visibility;
import com.flysystem.core.adapter.local.FileMetadataConverter;
import com.flysystem.core.cache.storage.AbstractCache;
import com.google.common.cache.Cache;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * @author Zeger Hoogeboom
 */
@RunWith(DataProviderRunner.class)
public class AbstractCacheTest
{
	AbstractCache cache;
	Cache<String, MetadataWrapper> cacheMock;
	Cache<String, Object> completedMock;
	FileMetadataConverter converterMock;
	String file = "example.txt";
	static String dir = "test";
	String contents = "contents";
	FileMetadata fileMetadata;
	MetadataWrapper wrapper;


	@Before
	public void setUp() throws Exception
	{
		cacheMock = mock(Cache.class);
		completedMock = mock(Cache.class);
		converterMock = mock(FileMetadataConverter.class);
		cache = new AbstractCacheStub(true,
				cacheMock,
				completedMock,
				converterMock);
		//doesn't have to be mocked since it's a data structure
		fileMetadata = new FileMetadata(file, 8L, Visibility.PUBLIC, "text/plain", 0L, "file");
		wrapper = new MetadataWrapper(fileMetadata, contents);
	}

	@Test
	public void testConstructor()
	{
		AbstractCacheStub cache = new AbstractCacheStub(true);
		assertTrue(cache.isAutosave());
		cache = new AbstractCacheStub(false);
		assertFalse(cache.isAutosave());
		new AbstractCacheStub(true, cacheMock, completedMock, converterMock); //assert doesn't throw exception
	}

    @Test
	public void updateObject()
    {
	    when(converterMock.fromPath(file, contents)).thenReturn(wrapper);
	    cache.updateObject(file, contents);
	    verify(converterMock).fromPath(file, contents);
    }

	@Test
	public void updateObjectWithAutoSave()
	{
		when(converterMock.fromPath(file, contents)).thenReturn(wrapper);
		cache.updateObject(file, contents, true);
		verify(converterMock).fromPath(file, contents);
	}

	@Test
	public void dirListingNotComplete()
	{
		when(completedMock.getIfPresent(dir)).thenReturn(null);
		assertFalse(cache.isComplete(dir, true));
		assertFalse(cache.isComplete(dir, false));
	}

	@Test
	public void dirListingCompleteNonRecursive()
	{
		when(completedMock.getIfPresent(dir)).thenReturn(true);
		assertTrue(cache.isComplete(dir, false));
		assertFalse(cache.isComplete(dir, true));
	}

	@Test
	public void dirListingCompleteRecursive()
	{
		when(completedMock.getIfPresent(dir)).thenReturn("recursive");
		assertTrue(cache.isComplete(dir, true));
		assertTrue(cache.isComplete(dir, false));
	}

	@Test
	public void setDirListingComplete()
	{
		cache.setComplete(dir, true);
		cache.setComplete(dir, false);
		verify(completedMock).put(dir, true);
		verify(completedMock).put(dir, "recursive");
	}

	@Test
	public void storeContents()
	{
		FileMetadata metadata = this.fileMetadata;
		metadata.setPath(dir);
		MetadataWrapper wrapper = new MetadataWrapper(metadata, dir);

		when(converterMock.fromPath(dir, metadata)).thenReturn(wrapper);
		cache.storeContents(dir, Arrays.asList(metadata), false);

		verify(completedMock).put(dir, true);
		verify(cacheMock).put(dir, wrapper);
	}

	@Test
	public void flush()
	{
		cache.flush();
		verify(cacheMock).invalidateAll();
	}

	@Test
	public void rename()
	{
		String newFile = "newfile.txt";
		when(cacheMock.getIfPresent(file)).thenReturn(wrapper);
		when(converterMock.fromPath(newFile, wrapper.getObject())).thenReturn(wrapper);
		cache.rename(file, newFile);
		verify(cacheMock).invalidate(file);
		verify(cacheMock).put(newFile, wrapper);
	}

	@Test
	public void renameSourceNotFound()
	{
		when(cacheMock.getIfPresent(file)).thenReturn(null);
		cache.rename(file, "new.txt");
		verify(cacheMock, never()).invalidate(any());
		verify(cacheMock, never()).put(Matchers.<String>any(), Matchers.<MetadataWrapper>any());
	}

	@Test
	public void copySourceNotFound()
	{
		when(cacheMock.getIfPresent(file)).thenReturn(null);
		cache.copy(file, "new.txt");
		verify(cacheMock, times(1)).getIfPresent(any());
	}

	@Test
	public void copy()
	{
		String newFile = "new.txt";
		when(cacheMock.getIfPresent(file)).thenReturn(wrapper);
		when(converterMock.fromPath(newFile, wrapper.getObject())).thenReturn(wrapper);
		cache.copy(file, newFile);
		verify(cacheMock).put(newFile, wrapper);
	}

	@Test
	public void delete()
	{
		MetadataWrapper expected = this.wrapper;
		expected.setObject(false);
		when(converterMock.fromPath(file, false)).thenReturn(expected);
		cache.delete(file);
		verify(cacheMock).put(file, expected);
	}

	@DataProvider
	public static Object[][] dataProviderDirectoryEntrySet() {
		return new Object[][] {
				{
					new HashSet<Map.Entry<String, MetadataWrapper>>() {{
						add(new AbstractMap.SimpleEntry<>(dir, new MetadataWrapper(new FileMetadata(dir), "something")));
					}}
				}
		};
	}

	@Test
	@UseDataProvider("dataProviderDirectoryEntrySet")
	public void deleteDirWasCached(HashSet<Map.Entry<String, MetadataWrapper>> entries)
	{
		when(cacheMock.asMap()).thenReturn(mock(ConcurrentMap.class));
		when(cacheMock.asMap().entrySet()).thenReturn(entries);
		when(completedMock.getIfPresent(dir)).thenReturn(wrapper);

		cache.deleteDir(dir);
		verify(cacheMock).invalidate(dir);
		verify(completedMock).invalidate(dir);
	}

	@Test
	@UseDataProvider("dataProviderDirectoryEntrySet")
	public void deleteDirNotCached(HashSet<Map.Entry<String, MetadataWrapper>> entries)
	{
		when(cacheMock.asMap()).thenReturn(mock(ConcurrentMap.class));
		when(cacheMock.asMap().entrySet()).thenReturn(entries);
		when(completedMock.getIfPresent(dir)).thenReturn(null);

		cache.deleteDir(dir);
		verify(cacheMock).invalidate(dir);
		verify(completedMock, never()).invalidate(dir);
	}

	@Test
	public void read()
	{
		when(cacheMock.getIfPresent(file)).thenReturn(wrapper);
		assertEquals(wrapper.getObject(), cache.read(file));
	}

	@Test
	public void readNull()
	{
		when(cacheMock.getIfPresent(file)).thenReturn(null);
		assertNull(cache.read(file));
	}

	@Test
	public void getMetadata()
	{
		when(cacheMock.getIfPresent(file)).thenReturn(wrapper);
		assertEquals(wrapper.getFileMetadata(), cache.getMetadata(file));
	}

	@Test
	public void getMetadataNull()
	{
		when(cacheMock.getIfPresent(file)).thenReturn(null);
		assertNull(cache.getMetadata(file));
	}

	@Test
	public void getSize()
	{
		when(cacheMock.getIfPresent(file)).thenReturn(wrapper);
		assertEquals(wrapper.getFileMetadata().getSize(), cache.getSize(file));
	}

	@Test
	public void getSizeNull()
	{
		when(cacheMock.getIfPresent(file)).thenReturn(null);
		assertNull(cache.getSize(file));
	}

	@Test
	public void getMimetype()
	{
		when(cacheMock.getIfPresent(file)).thenReturn(wrapper);
		assertEquals(wrapper.getFileMetadata().getMimetype(), cache.getMimetype(file));
	}

	@Test
	public void getMimetypeNull()
	{
		when(cacheMock.getIfPresent(file)).thenReturn(null);
		assertNull(cache.getMimetype(file));
	}

	@Test
	public void getTimeStamp()
	{
		when(cacheMock.getIfPresent(file)).thenReturn(wrapper);
		assertEquals(wrapper.getFileMetadata().getTimestamp(), cache.getTimestamp(file));
	}

	@Test
	public void getTimeStampNull()
	{
		when(cacheMock.getIfPresent(file)).thenReturn(null);
		assertNull(cache.getTimestamp(file));
	}

	@Test
	public void getVisibility()
	{
		when(cacheMock.getIfPresent(file)).thenReturn(wrapper);
		assertEquals(wrapper.getFileMetadata().getVisibility(), cache.getVisibility(file));
	}

	@Test
	public void getVisibilityNull()
	{
		when(cacheMock.getIfPresent(file)).thenReturn(null);
		assertNull(cache.getVisibility(file));
	}

	@Test
	public void setAutosave()
	{
		AbstractCache cache = this.cache;
		cache.setAutosave(true);
		assertTrue(cache.isAutosave());
		cache.setAutosave(false);
		assertFalse(cache.isAutosave());
	}
}
