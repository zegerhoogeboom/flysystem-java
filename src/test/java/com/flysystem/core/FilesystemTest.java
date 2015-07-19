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

import com.flysystem.core.adapter.local.FileCommands;
import com.flysystem.core.exception.FileExistsException;
import com.flysystem.core.exception.FileNotFoundException;
import com.flysystem.core.exception.FlysystemGenericException;
import com.flysystem.core.exception.RootViolationException;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * @author Zeger Hoogeboom
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest( FileCommands.class )
public class FilesystemTest
{

	Filesystem filesystem;
	Config config;
	Adapter mockedAdapter;
	String path;
	String contents;

	@Before
	public void setUp() throws Exception
	{
		mockedAdapter = PowerMockito.mock(Adapter.class);
		config = PowerMockito.spy(new Config());
		filesystem = new FilesystemImpl(mockedAdapter, null);
		path = "path.txt";
		contents = "contents";
	}

	@Test
	public void testHas()
	{
		when(mockedAdapter.has(path)).thenReturn(true);
		assertTrue(filesystem.has(path));
	}

	@Test
	public void testWrite()
	{
		when(mockedAdapter.has(path)).thenReturn(false);
		when(mockedAdapter.write(path, contents, config)).thenReturn(true);
		assertTrue(filesystem.write(path, contents, config));
	}

	@Test
	public void testUpdate()
	{
		when(mockedAdapter.has(path)).thenReturn(true);
		when(mockedAdapter.update(path, contents, config)).thenReturn(true);
		assertTrue(filesystem.update(path, contents, config));
	}

	@Test
	public void testPutNew()
	{
		when(mockedAdapter.has(path)).thenReturn(false);
		when(mockedAdapter.write(path, contents, config)).thenReturn(true);
		assertTrue(filesystem.put(path, contents, config));
	}

	@Test
	public void testPutUpdate()
	{
		when(mockedAdapter.has(path)).thenReturn(true);
		when(mockedAdapter.update(path, contents, config)).thenReturn(true);
		assertTrue(filesystem.put(path, contents, config));
	}

	@Test
	public void testReadAndDelete()
	{
		when(mockedAdapter.has(path)).thenReturn(true);
		when(mockedAdapter.read(path)).thenReturn(contents);
		when(mockedAdapter.delete(path)).thenReturn(true);
		String output = filesystem.readAndDelete(path);
		assertEquals(contents, output);
	}

	@Test(expected = FlysystemGenericException.class)
	public void testReadAndDeleteFailedRead()
	{
		when(mockedAdapter.has(path)).thenReturn(true);
		when(mockedAdapter.read(path)).thenThrow(new FlysystemGenericException(""));
		filesystem.readAndDelete(path);
	}

	@Test
	public void testRead()
	{
		when(mockedAdapter.has(path)).thenReturn(true);
		when(mockedAdapter.read(path)).thenReturn(contents);
		assertEquals(filesystem.read(path), contents);
	}

	@Test
	public void testRename() throws Exception
	{
		String oldFile = "old.txt";
		String newFile = "new.txt";

		when(mockedAdapter.has(Matchers.<String>any())).thenReturn(true, false);
		when(mockedAdapter.rename(oldFile, newFile)).thenReturn(true);

		assertTrue(filesystem.rename(oldFile, newFile));
	}

	@Test
	public void testCopy()
	{
		String oldFile = "old.txt";
		String newFile = "new.txt";

		when(mockedAdapter.has(Matchers.<String>any())).thenReturn(true, false);
		when(mockedAdapter.copy(oldFile, newFile)).thenReturn(true);

		assertTrue(filesystem.copy(oldFile, newFile));
	}

	@Test(expected = RootViolationException.class)
	public void testDeleteDirRootViolation()
	{
		filesystem.deleteDir("");
	}

	@Test
	public void testDeleteDir()
	{
		when(mockedAdapter.deleteDir("dirname")).thenReturn(true);
		assertTrue(filesystem.deleteDir("dirname"));
	}

	@Test
	public void testCreateDir()
	{
		when(mockedAdapter.createDir("dirname", new Config())).thenReturn(true);
		assertTrue(filesystem.createDir("dirname"));
	}

	@Test(expected = FileExistsException.class)
	public void testAssertPresentThrowsException()
	{
		when(mockedAdapter.has(path)).thenReturn(true);
		filesystem.write(path, contents);
	}

	@Test(expected = FileNotFoundException.class)
	public void testAssertAbsentThrowsException()
	{
		when(mockedAdapter.has(path)).thenReturn(false);
		filesystem.read(path);
	}

	@Test
	public void testSetVisibility()
	{
		when(mockedAdapter.has(path)).thenReturn(true);
		when(mockedAdapter.setVisibility(path, Visibility.PUBLIC)).thenReturn(true);
		assertTrue(filesystem.setVisibility(path, Visibility.PUBLIC));
	}

	@Test
	public void testSetVisibilityFail()
	{
		when(mockedAdapter.has(path)).thenReturn(true);
		when(mockedAdapter.setVisibility(path, Visibility.PUBLIC)).thenReturn(false);
		assertFalse(filesystem.setVisibility(path, Visibility.PUBLIC));
	}

	@Test
	public void getVisibility()
	{
		when(mockedAdapter.has(path)).thenReturn(true);
		when(mockedAdapter.getVisibility(path)).thenReturn(Visibility.PUBLIC);
		assertEquals(Visibility.PUBLIC, filesystem.getVisibility(path));
	}

	@Test
	public void getSize()
	{
		when(mockedAdapter.has(path)).thenReturn(true);
		when(mockedAdapter.getSize(path)).thenReturn(4l);
		assertEquals(4l, filesystem.getSize(path));
	}

	@Test
	public void getMetadata()
	{
		when(mockedAdapter.has(path)).thenReturn(true);
		when(mockedAdapter.getMetadata(path)).thenReturn(new FileMetadata(path));
		assertEquals(new FileMetadata(path), filesystem.getMetadata(path));
	}

	@Test
	@Ignore
	public void testGetFile()
	{
//		when(mockedAdapter.has(path)).thenReturn(true);
//		filesystem.get(path);
//		$this->prophecy->getMetadata($path)->willReturn([
//			'path' => $path,
//			'type' => 'file',
//		]);
//
//		$output = $this->filesystem->get($path);
//		$this->assertInstanceOf('League\Flysystem\File', $output);
	}

	@Test
	public void testListContents()
	{
		List<FileMetadata> raw = new ArrayList<FileMetadata>() {{ //todo double check with original test.
			add(new FileMetadata("valid/file.txt"));
			add(new FileMetadata("valid/a-valid-file.txt"));
		}};

		when(mockedAdapter.listContents("valid", false)).thenReturn(raw);
		assertEquals(raw, filesystem.listContents("valid", false));
	}

}
