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

package com.flysystem.core.adapter.local;

import com.flysystem.core.File;
import com.flysystem.core.Filesystem;
import com.flysystem.core.FilesystemFileWrapper;
import com.google.common.base.Converter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Zeger Hoogeboom
 */
public class LocalFileConverter extends Converter<FilesystemFileWrapper, com.flysystem.core.File>
{
	@Override
	protected com.flysystem.core.File doForward(FilesystemFileWrapper wrapper)
	{
		return new File(wrapper.getFilesystem(), wrapper.getFile());
	}

	@Override
	protected FilesystemFileWrapper doBackward(com.flysystem.core.File file)
	{
		return new FilesystemFileWrapper(file.getFilesystem(), new java.io.File(file.getPath()));
	}

	public static File doConvert(Filesystem filesystem, java.io.File file)
	{
		return new LocalFileConverter().convert(new FilesystemFileWrapper(filesystem, file));
	}

	public static List<File> doConvert(Filesystem filesystem, List<java.io.File> files, String pathPrefix)
	{
		LocalFileConverter localFileConverter = new LocalFileConverter();
		List<File> convertedFiles = new ArrayList<File>();
		for (java.io.File file : files) {
			file = new java.io.File(file.getPath().replace(pathPrefix, ""));
			convertedFiles.add(localFileConverter.convert(new FilesystemFileWrapper(filesystem, file)));
		}
		return convertedFiles;
	}
}
