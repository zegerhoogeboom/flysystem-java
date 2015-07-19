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

/**
 * @author Zeger Hoogeboom
 */
abstract class Handler
{
	protected String path;
	protected Filesystem filesystem;

	public Handler(Filesystem filesystem, String path)
	{
		this.filesystem = filesystem;
		this.path = path;
	}

	/**
	 * Check whether the entree is a directory.
	 *
	 * @return bool
	 */
	public boolean isDir()
	{
		return this.getType().equals("dir");
	}

	/**
	 * Check whether the entree is a file.
	 *
	 * @return bool
	 */
	public boolean isFile()
	{
		return this.getType().equals("file");
	}

	/**
	 * Retrieve the entree type (file|dir).
	 *
	 * @return string file or dir
	 */
	public String getType()
	{
		FileMetadata metadata = this.filesystem.getMetadata(this.path);
		return (String) metadata.getType();
	}


	public void setPath(String path)
	{
		this.path = path;
	}

	public String getPath()
	{
		return path;
	}

	public Filesystem getFilesystem()
	{
		return filesystem;
	}

	public void setFilesystem(Filesystem filesystem)
	{
		this.filesystem = filesystem;
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (! (o instanceof Handler)) return false;

		Handler handler = (Handler) o;

		if (path != null ? ! path.equals(handler.path) : handler.path != null) return false;
		return ! (filesystem != null ? ! filesystem.equals(handler.filesystem) : handler.filesystem != null);

	}

	@Override
	public int hashCode()
	{
		int result = path != null ? path.hashCode() : 0;
		result = 31 * result + (filesystem != null ? filesystem.hashCode() : 0);
		return result;
	}
}