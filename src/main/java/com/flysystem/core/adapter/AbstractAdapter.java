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

package com.flysystem.core.adapter;

import com.flysystem.core.Adapter;
import com.google.common.base.Strings;
import org.apache.commons.lang3.StringUtils;

import java.io.File;

/**
 * @author Zeger Hoogeboom
 */
public abstract class AbstractAdapter implements Adapter
{
	protected String pathPrefix;

	public void setPathPrefix(String prefix)
	{
		boolean isEmpty = Strings.isNullOrEmpty(prefix);
		if (! isEmpty) {
			prefix = StringUtils.stripEnd(prefix, File.separator) + File.separator;
		}
		this.pathPrefix = isEmpty ? null : prefix;
	}

	public String getPathPrefix()
	{
		return pathPrefix;
	}

	public String applyPathPrefix(String path)
	{
		path = StringUtils.stripStart(path, "\\/");
		if (path.length() == 0) {
			return getPathPrefix() != null ? getPathPrefix() : "";
		}
		return getPathPrefix() + path;
	}

	/**
	 * Remove a path prefix.
	 *
	 * @return string path without the prefix
	 */
	public String removePathPrefix(String path)
	{
		if (getPathPrefix() == null) {
			return path;
		}

		return path.substring(pathPrefix.length());
	}
}
