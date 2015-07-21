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

import com.flysystem.core.FileMetadata;
import com.flysystem.core.exception.FileNotFoundException;
import com.flysystem.core.util.PathUtil;
import com.google.common.base.Converter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Zeger Hoogeboom
 */
public class FileMetadataConverter extends Converter<File, FileMetadata>
{

	@Override
	protected FileMetadata doForward(File file) throws FileNotFoundException
	{
		String type = file.isFile() ? "file" : file.isDirectory() ? "directory" : null;
		return new FileMetadata(file.getPath(), file.length(), null, PathUtil.guessMimeType(file.getPath()), file.lastModified(), type);   //FIXME: add visibility
	}

	@Override
	protected File doBackward(FileMetadata fileMetadata)
	{
		return new File(fileMetadata.getPath());
	}

	public static List<FileMetadata> doConvert(List<java.io.File> files, String pathPrefix)
	{
		FileMetadataConverter fileMetadataConverter = new FileMetadataConverter();
		List<FileMetadata> convertedFiles = new ArrayList<>();
		for (File file : files) {
			FileMetadata converted = fileMetadataConverter.convert(file);
			converted.setPath(converted.getPath().replace(pathPrefix, ""));
			convertedFiles.add(converted);
		}
		return convertedFiles;
	}
}
