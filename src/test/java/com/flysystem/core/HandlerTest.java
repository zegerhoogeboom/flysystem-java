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

import org.junit.Before;
import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Zeger Hoogeboom
 */
public class HandlerTest
{

	Filesystem filesystem;
	Config config;
	Adapter mockedAdapter;
	String path;
	String dirPath;
	String contents;
	File file;
	Directory directory;

	@Before
	public void setUp() throws Exception
	{
		mockedAdapter = mock(Adapter.class);
		config = PowerMockito.spy(new Config());
		filesystem = mock(FilesystemImpl.class);
		path = "path.txt";
		contents = "contents";
		dirPath = "path";
		file = new File(filesystem, path);
		directory = new Directory(filesystem, dirPath);
	}

	@Test
	public void fileRead()
	{
		when(filesystem.read(path)).thenReturn(contents);
		assertEquals(contents, file.read());
	}

	@Test
	public void fileDelete()
	{
		when(filesystem.delete(path)).thenReturn(true);
		assertTrue(file.delete());
	}

	@Test
	public void fileUpdate()
	{
		when(filesystem.update(path, contents)).thenReturn(true);
		assertTrue(file.update(contents));
	}

	@Test
	public void fileIsFile()
	{
		FileMetadata fileMetadata = new FileMetadata(path);
		fileMetadata.setType("file");
		when(filesystem.getMetadata(path)).thenReturn(fileMetadata);
		assertTrue(file.isFile());
		assertFalse(file.isDir());
	}

	@Test
	public void fileGetPath()
	{
		file.setPath("path.txt");
		assertEquals("path.txt", file.getPath());
	}

	@Test
	public void dirDelete()
	{
		when(filesystem.deleteDir(dirPath)).thenReturn(true);
		assertTrue(directory.delete());
	}

	@Test
	public void dirListContents()
	{
		List<FileMetadata> expected = new ArrayList<>();
		expected.add(new FileMetadata(path));

		when(filesystem.listContents(dirPath, true)).thenReturn(expected);

		List<FileMetadata> output = directory.getContents(true);
		assertEquals(expected, output);
	}

	@Test
	public void getFilesystem()
	{
		assertEquals(filesystem, directory.getFilesystem());
	}

}
