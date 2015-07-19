package com.flysystem.core.exception;

/**
 * @author Zeger Hoogeboom
 */
public class DirectoryNotFoundException extends FlywayGenericException
{
	public DirectoryNotFoundException(String path)
	{
		super("Directory not found at path: " + path);
	}
}
