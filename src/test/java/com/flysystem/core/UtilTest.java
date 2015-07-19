package com.flysystem.core;

import com.flysystem.core.util.PathUtil;
import org.junit.Test;

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
			put("00004869/files/other/10-75..stl", "00004869\\files\\other\\10-75..stl");
			put("/dirname//subdir///subsubdir", "dirname\\subdir\\subsubdir");
			put("\\dirname\\\\subdir\\\\\\subsubdir", "dirname\\subdir\\subsubdir");
			put("\\\\some\\shared\\\\drive", "some\\shared\\drive");
			put("C:\\dirname\\\\subdir\\\\\\subsubdir", "C:\\dirname\\subdir\\subsubdir");
//			put("C:\\\\dirname\\subdir\\\\\\\\subsubdir", "C:\\dirname\\subdir\\subsubdir"); //fixme!
		}};
		for (Map.Entry<String, String> entry : data.entrySet()) {
			String result = PathUtil.normalizePath(entry.getKey());
			assertEquals(entry.getValue(), result);
		}
	}
}
