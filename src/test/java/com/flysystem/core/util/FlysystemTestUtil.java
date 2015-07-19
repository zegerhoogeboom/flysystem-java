package com.flysystem.core.util;

import java.io.File;

/**
 * @author Zeger Hoogeboom
 */
public class FlysystemTestUtil
{
	public static String getRoot()
	{
		return System.getProperty("user.dir") + String.format("%ssrc%stest%sfiles%s", File.separator, File.separator, File.separator, File.separator);
	}
}
