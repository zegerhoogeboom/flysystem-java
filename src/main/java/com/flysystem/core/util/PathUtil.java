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

package com.flysystem.core.util;

import com.flysystem.core.FileMetadata;
import com.flysystem.core.adapter.local.FileMetadataConverter;
import net.sf.jmimemagic.*;
import org.apache.commons.io.FilenameUtils;

import java.io.File;

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

	/**
	 * Get normalized pathinfo.
	 *
	 * @param path
	 *
	 * @return array pathinfo
	 */
	public static FileMetadata pathinfo(String path)
	{
		File file = new File(path);
		return new FileMetadataConverter().convert(file);
	}

	/**
	 * Normalize a dirname return value.
	 *
	 * @param dirname
	 *
	 * @return string normalized dirname
	 */
	public static String normalizeDirname(String dirname)
	{
		if (".".equals(dirname)) {
			return "";
		}
		return dirname;
	}

	/**
	 * Get a normalized dirname from a path.
	 *
	 * @param path
	 *
	 * @return string dirname
	 */
	public static String dirname(String path)
	{
		return normalizeDirname(new File(path).getParent());
	}

	public static String guessMimeType(String path)
	{
		MagicMatch match = null;
		try {
			match = Magic.getMagicMatch(new File(path), true);
		} catch (MagicParseException | MagicMatchNotFoundException | MagicException e) {
			e.printStackTrace();
		}
		return match != null ? match.getMimeType() : null;
	}
}
