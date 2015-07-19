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
