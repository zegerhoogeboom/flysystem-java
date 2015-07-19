package com.flysystem.core.util;

import org.apache.commons.io.FilenameUtils;

/**
 * @author Zeger Hoogeboom
 */
public class PathUtil
{
	/**
	 * Normalize path.
	 *
	 * @return string
	 */
	public static String normalizePath(String path)
	{
		path = path.replaceAll("^[^[/\\\\]]*[/\\\\]", ""); //replace all / and \ at the start of the string
		return FilenameUtils.normalizeNoEndSeparator(path);
	}
}
