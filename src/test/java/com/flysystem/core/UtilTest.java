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
import org.junit.Test;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;

/**
 * @author Zeger Hoogeboom
 */
public class UtilTest
{

	@Test
	public void testNormalizePath()
	{
		Map<String, String> data = new HashMap<String, String>() {{
			put("/dirname/", "dirname");
			put("dirname/..", "");
			put("dirname/../", "");
			put("dirname./", "dirname.");
			put("dirname/./", "dirname");
			put("dirname/.", "dirname");
			put("./dir/../././", "");
			put("00004869/files/other/10-75..stl", String.format("00004869%sfiles%sother%s10-75..stl", File.separator, File.separator, File.separator));
			put("/dirname//subdir///subsubdir", String.format("dirname%ssubdir%ssubsubdir", File.separator, File.separator));
			put("\\dirname\\\\subdir\\\\\\subsubdir", String.format("dirname%ssubdir%ssubsubdir", File.separator, File.separator));
			put("\\\\some\\shared\\\\drive", String.format("some%sshared%sdrive", File.separator, File.separator));
			put("C:\\dirname\\\\subdir\\\\\\subsubdir", String.format("C:%sdirname%ssubdir%ssubsubdir", File.separator, File.separator, File.separator));
//			put("C:\\\\dirname\\subdir\\\\\\\\subsubdir", "C:\\dirname\\subdir\\subsubdir"); //fixme!
		}};
		for (Map.Entry<String, String> entry : data.entrySet()) {
			String result = PathUtil.normalizePath(entry.getKey());
			assertEquals(entry.getValue(), result);
		}
	}

	@Test
	public void testMimetypeGuess()
	{
		String mimeType = PathUtil.guessMimeType(FlysystemTestUtil.getRoot() + "example.txt");
		assertEquals("text/plain", mimeType);
	}
}
