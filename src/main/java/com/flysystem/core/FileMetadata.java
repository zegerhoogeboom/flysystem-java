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

import java.io.File;

/**
 * @author Zeger Hoogeboom
 */
public class FileMetadata
{
	String path;
	Long size;
	String visibility;
	String mimetype;
	Long timestamp;
	String type;

	public FileMetadata(String path)
	{
		this.path = path;
	}

	public FileMetadata(String path, Long size, String visibility, String mimetype, Long timestamp, String type)
	{
		this.path = path;
		this.size = size;
		this.visibility = visibility;
		this.mimetype = mimetype;
		this.timestamp = timestamp;
		this.type = type;
	}

	public String getPath()
	{
		return path;
	}

	public void setPath(String path)
	{
		this.path = path;
	}

	public Long getSize()
	{
		return size;
	}

	public void setSize(Long size)
	{
		this.size = size;
	}

	public String getVisibility()
	{
		return visibility;
	}

	public void setVisibility(String visibility)
	{
		this.visibility = visibility;
	}

	public String getMimetype()
	{
		return mimetype;
	}

	public void setMimetype(String mimetype)
	{
		this.mimetype = mimetype;
	}

	public Long getTimestamp()
	{
		return timestamp;
	}

	public void setTimestamp(Long timestamp)
	{
		this.timestamp = timestamp;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public File getFile()
	{
		return new File(path);
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (! (o instanceof FileMetadata)) return false;

		FileMetadata that = (FileMetadata) o;

		return ! (path != null ? ! path.equals(that.path) : that.path != null);

	}

	@Override
	public int hashCode()
	{
		return path != null ? path.hashCode() : 0;
	}
}
