package com.flysystem.core.exception;

/**
 * @author Zeger Hoogeboom
 */
public class FileExistsException extends FlysystemGenericException
{
	public FileExistsException(String path)
	{
		super("File already exists at path: " + path);
	}
}
