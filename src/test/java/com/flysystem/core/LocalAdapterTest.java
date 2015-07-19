package com.flysystem.core;

import com.flysystem.core.adapter.local.Local;
import com.flysystem.core.exception.DirectoryNotFoundException;
import com.flysystem.core.exception.FileNotFoundException;
import com.flysystem.core.util.FlysystemTestUtil;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.*;

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
		List<File> files = adapter.listContents(null, "");
		assertThat(files, CoreMatchers.hasItem(new File(null, example)));
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

	@Test
	public void renameToExistingFile()
	{
		adapter.write("temp1.txt", "temp1");
		adapter.write("temp2.txt", "temp2");
		adapter.rename("temp1.txt", "temp2.txt");
		assertEquals("temp1", adapter.read("temp2.txt"));
		adapter.delete("temp1.txt");
		adapter.delete("temp2.txt");
	}

	@Test
	public void renameToNonExistingDirectory()
	{
		adapter.write("temp.txt", "temp");
		assertTrue(adapter.rename("temp.txt", "newdirectory/temp.txt"));
		assertTrue(adapter.has("newdirectory/temp.txt"));
		adapter.delete("temp.txt");
		adapter.deleteDir("newdirectory");
	}
}
