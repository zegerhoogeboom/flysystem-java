package com.flysystem.core.adapter.local;

import com.flysystem.core.FileMetadata;
import com.flysystem.core.exception.FileNotFoundException;
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
		return new FileMetadata(file.getPath(), file.length(), null, null, file.lastModified(), type);   //FIXME: add mimetype and visibility
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
			file = new File(file.getPath().replace(pathPrefix, ""));
			convertedFiles.add(fileMetadataConverter.convert(file));
		}
		return convertedFiles;
	}
}
