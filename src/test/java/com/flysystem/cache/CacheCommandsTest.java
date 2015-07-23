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

import com.flysystem.core.cache.Cache;
import com.flysystem.core.cache.CacheCommands;
import com.flysystem.core.cache.CachedAdapter;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.*;

/**
 * @author Zeger Hoogeboom
 */
public class CacheCommandsTest
{
	CacheCommands commands;
	CachedAdapter mockedAdapter;
	Cache mockedCache;
	String file = "example.txt";
	String contents = "contents";
	CacheCommands.GetFromCacheCommand<Object> mockedCommand;

	@Before
	public void setUp() throws Exception
	{
		mockedCommand = mock(CacheCommands.GetFromCacheCommand.class);
		mockedCache = mock(Cache.class);
		mockedAdapter = mock(CachedAdapter.class);
		when(mockedAdapter.getCache()).thenReturn(mockedCache);
		commands = new CacheCommands(mockedAdapter);
	}

	@Test
	public void getsFromCache()
	{
		when(mockedCommand.fromCache(file)).thenReturn(contents);
		assertEquals(contents, commands.get(file, mockedCommand));
		verify(mockedCommand, times(0)).fromFilesystem(file);
	}

	@Test
	public void getsFromFilesystem()
	{
		when(mockedCommand.fromCache(file)).thenReturn(null);
		when(mockedCommand.fromFilesystem(file)).thenReturn(contents);

		assertEquals(contents, commands.get(file, mockedCommand));

		verify(mockedCommand, times(1)).fromFilesystem(file);
		verify(mockedCache, times(1)).updateObject(file, contents, true);
		verify(mockedCache, never()).storeMiss(file);
	}

	@Test
	public void failToGetFromFilesystem()
	{
		when(mockedCommand.fromCache(file)).thenReturn(null);
		when(mockedCommand.fromFilesystem(file)).thenReturn(null);

		assertEquals(null, commands.get(file, mockedCommand));

		verify(mockedCommand, times(1)).fromFilesystem(file);
		verify(mockedCache, times(1)).storeMiss(file);
		verify(mockedCache, never()).updateObject(file, contents, true);
	}
}
