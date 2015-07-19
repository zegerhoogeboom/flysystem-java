package com.flysystem.core;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

/**
 * @author Zeger Hoogeboom
 */
public class FilesystemTest
{

	Filesystem filesystem;
	Config config;
	Adapter mockedAdapter;

	@Before
	public void setUp() throws Exception
	{
		mockedAdapter = mock(Adapter.class);
		config = spy(new Config());
		filesystem = new FilesystemImpl(mockedAdapter, new Config());
	}

	@Test
	public void testHas()
	{
		when(mockedAdapter.has("example.txt")).thenReturn(true);
		assertTrue(filesystem.has("example.txt"));
	}

	@Test
	public void testWrite()
	{
		String path = "path.txt";
		String contents = "contents";
		when(mockedAdapter.has(path)).thenReturn(false);
		when(mockedAdapter.write(path, contents, config)).thenReturn(true);
		assertTrue(filesystem.write(path, contents));
	}
}
