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

import com.flysystem.core.util.FlysystemTestUtil;
import com.flysystem.core.util.PathUtil;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;

import static junit.framework.TestCase.assertEquals;

/**
 * @author Zeger Hoogeboom
 */
@RunWith(DataProviderRunner.class)
public class UtilTest
{

	@DataProvider
	public static Object[][] dataProviderNormalizePath() {
		return new Object[][] {
				{"/dirname/", "dirname"},
				{"dirname/..", ""},
				{"dirname/../", ""},
				{"dirname./", "dirname."},
				{"dirname/./", "dirname"},
				{"dirname/.", "dirname"},
				{"./dir/../././", ""},
				{"00004869/files/other/10-75..stl", String.format("00004869%sfiles%sother%s10-75..stl", File.separator, File.separator, File.separator)},
				{"/dirname//subdir///subsubdir", String.format("dirname%ssubdir%ssubsubdir", File.separator, File.separator)},
				{"\\dirname\\\\subdir\\\\\\subsubdir", String.format("dirname%ssubdir%ssubsubdir", File.separator, File.separator)},
				{"\\\\some\\shared\\\\drive", String.format("some%sshared%sdrive", File.separator, File.separator)},
				{"C:\\dirname\\\\subdir\\\\\\subsubdir", String.format("C:%sdirname%ssubdir%ssubsubdir", File.separator, File.separator, File.separator)}
//				{"C:\\\\dirname\\subdir\\\\\\\\subsubdir", "C:\\dirname\\subdir\\subsubdir"} //fixme!
		};
	}

	@Test
	@UseDataProvider("dataProviderNormalizePath")
	public void testNormalizePath(String original, String expected)
	{
		String result = PathUtil.normalizePath(original);
		assertEquals(expected, result);
	}

	@Test
	public void testMimetypeGuess()
	{
		String mimeType = PathUtil.guessMimeType(FlysystemTestUtil.getRoot() + "example.txt");
		assertEquals("text/plain", mimeType);
	}
}
