package com.flysystem.core.exception;

/**
 * @author Zeger Hoogeboom
 */
public class FileNotFoundException extends FlywayGenericException
{
	public FileNotFoundException(String path)
	{
		super("File not found at path: " + path);
	}
}
