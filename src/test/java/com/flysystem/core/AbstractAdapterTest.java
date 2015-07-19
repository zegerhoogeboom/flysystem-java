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
