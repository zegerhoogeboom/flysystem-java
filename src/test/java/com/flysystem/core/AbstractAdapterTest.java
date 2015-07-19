package com.flysystem.core;

import com.flysystem.core.stubs.AbstractAdapterStub;
import com.flysystem.core.util.FlysystemTestUtil;
import com.google.common.base.Strings;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

/**
 * @author Zeger Hoogeboom
 */
public class AbstractAdapterTest
{
	AbstractAdapterStub stub;

	@Before
	public void setUp() throws Exception
	{
		stub = new AbstractAdapterStub();
	}

	@Test
	public void setPrefixSuccess()
	{
		stub.setPathPrefix(FlysystemTestUtil.getRoot());
		String prefixed = stub.applyPathPrefix("example.txt");
		assertEquals(prefixed, FlysystemTestUtil.getRoot() + "example.txt");
	}

	@Test
	public void setEmptyPrefixShouldBeNull()
	{
		stub.setPathPrefix("");
		assertEquals(null, stub.getPathPrefix());
	}

	@Test
	public void setNullPrefixShouldBeNull()
	{
		stub.setPathPrefix(null);
		assertEquals(null, stub.getPathPrefix());
	}

	@Test
	public void pathEndingWithPathPrefixShouldBeStripped()
	{
		stub.setPathPrefix(FlysystemTestUtil.getRoot()+ Strings.repeat(File.separator, 5));
		assertEquals(FlysystemTestUtil.getRoot(), stub.getPathPrefix());
	}

	@Test
	public void prefixSeparatorShouldBeStripped()
	{
		stub.setPathPrefix(FlysystemTestUtil.getRoot());
		String prefixed = stub.applyPathPrefix(Strings.repeat(File.separator, 4));
		assertEquals(FlysystemTestUtil.getRoot(), prefixed);
	}

	@Test
	public void removePathPrefix()
	{
		stub.setPathPrefix(FlysystemTestUtil.getRoot());
		String prefixed = stub.applyPathPrefix("test");
		String unprefixed = stub.removePathPrefix(prefixed);
		assertTrue(prefixed.startsWith(FlysystemTestUtil.getRoot()));
		assertFalse(unprefixed.startsWith(FlysystemTestUtil.getRoot()));
		assertTrue(prefixed.endsWith(unprefixed));
	}

	@Test
	public void removePrefixWithNullPrefix()
	{
		stub.setPathPrefix(null);
		String prefixed = stub.applyPathPrefix("test");
		String unprefixed = stub.removePathPrefix(prefixed);
		assertEquals(prefixed, unprefixed);
	}
}
