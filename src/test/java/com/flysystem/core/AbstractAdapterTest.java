package com.flysystem.core;

import com.flysystem.core.stubs.AbstractAdapterStub;
import com.flysystem.core.util.FlysystemTestUtil;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

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
		stub.setPathPrefix(FlysystemTestUtil.getRoot()+"/////");
		assertEquals(FlysystemTestUtil.getRoot(), stub.getPathPrefix());
	}

	@Test
	public void prefixSeparatorShouldBeStripped()
	{
		stub.setPathPrefix(FlysystemTestUtil.getRoot());
		String prefixed = stub.applyPathPrefix("////");
		assertEquals(FlysystemTestUtil.getRoot(), prefixed);
	}
}
