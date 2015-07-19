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

import com.flysystem.core.adapter.local.Local;
import com.flysystem.core.exception.DirectoryNotFoundException;
import com.flysystem.core.exception.FileExistsException;
import com.flysystem.core.exception.FileNotFoundException;
import com.flysystem.core.util.FlysystemTestUtil;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * @author Zeger Hoogeboom
 */
public class LocalAdapterTest
{

	Local adapter;
	String example;

	@Before
	public void setUp() throws Exception
	{
		adapter = new Local(FlysystemTestUtil.getRoot(), 2, 2);
		example = "example.txt";
	}

	@Test
	public void exampleFileExists()
	{
		assertTrue(adapter.has(example));
	}

	@Test
	public void canReadExampleFileContents() throws IOException
	{
	    assertTrue(adapter.has(example));
		assertEquals(adapter.read(example), "test");
	}

	@Test
	public void canWriteReadAndDeleteDir()
	{
		assertFalse(adapter.has("temp"));

		this.adapter.createDir("temp");
		assertTrue(adapter.has("temp"));

		adapter.deleteDir("temp");
		assertFalse(adapter.has("temp"));
	}

	@Test(expected = DirectoryNotFoundException.class)
	public void deleteNonExistingDir()
	{
		adapter.deleteDir("nonexisting");
	}

	@Test(expected = DirectoryNotFoundException.class)
	public void deleteDirThatIsActuallyAFile()
	{
		adapter.deleteDir(example);
	}

	@Test
	public void canWriteReadAndDeleteFile()
	{
		assertFalse(adapter.has("temp.txt"));
		adapter.write("temp.txt", "temp");
		assertTrue(adapter.has("temp.txt"));
		adapter.delete("temp.txt");
		assertFalse(adapter.has("temp.txt"));
	}

	@Test
	public void copy()
	{
		assertFalse(adapter.has("newexample.txt"));
		adapter.copy(example, "newexample.txt");
		assertTrue(adapter.has("newexample.txt"));
		adapter.delete("newexample.txt");
	}

	@Test
	public void copyShouldCreateDirectoryIfNotExists()
	{
		adapter.copy(example, "newdirectory/newexample.txt");
		assertTrue(adapter.has("newdirectory/newexample.txt"));
		adapter.deleteDir("newdirectory");
	}

	@Test(expected = FileNotFoundException.class)
	public void copyNonExistingFile()
	{
		adapter.copy("nonexisting.txt", "somethingelse.txt");
	}

	@Test
	public void listContents()
	{
		List<FileMetadata> files = adapter.listContents("");
		assertThat(files, CoreMatchers.hasItem(new FileMetadata(example)));
	}

	@Test
	public void metadataShouldBeFilled()
	{
		FileMetadata metadata = adapter.getMetadata(example);
		assertEquals(FlysystemTestUtil.getRoot() + example, metadata.getPath());
		assertEquals("file", metadata.getType());
		assertEquals(4l, (long) metadata.getSize()); //file contains the string "test"
	}

	@Test(expected = FileNotFoundException.class)
	public void getMetadataOfNonExistingFile()
	{
		adapter.getMetadata("nonexisting.txt");
	}

	@Test
	public void renameShouldSucceed()
	{
		assertFalse(adapter.has("renamed.txt"));
		adapter.write("temp.txt", "temp");
		adapter.rename("temp.txt", "renamed.txt");
		assertTrue(adapter.has("renamed.txt"));
		adapter.delete("renamed.txt");
	}

	@Test(expected = FileExistsException.class)
	public void renameToExistingFile()
	{
		adapter.write("temp1.txt", "temp1");
		adapter.write("temp2.txt", "temp2");

		try {
			adapter.rename("temp1.txt", "temp2.txt");
		} catch (FileExistsException e) {
			adapter.delete("temp1.txt");
			adapter.delete("temp2.txt");
			throw e;
		}
	}

	@Test
	public void renameToNonExistingDirectory()
	{
		adapter.write("temp.txt", "temp");
		adapter.rename("temp.txt", "newdirectory/temp.txt");
		assertTrue(adapter.has("newdirectory/temp.txt"));
		adapter.delete("temp.txt");
		adapter.deleteDir("newdirectory");
	}

	@Test(expected = FileNotFoundException.class)
	public void renameNonExistingFile()
	{
		adapter.rename("nonexisting.txt", "stillnonexisting.txt");
	}

	@Test
	public void updateContents()
	{
		adapter.write("temp.txt", "foo");
		adapter.update("temp.txt", "bar");
		assertEquals("bar", adapter.read("temp.txt"));
		adapter.delete("temp.txt");
	}

	@Test(expected = FileNotFoundException.class)
	public void updateContentsOfNonExistingFile()
	{
		adapter.update("nonexisting.txt", "bar");
	}
}
