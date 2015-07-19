package com.flysystem.core.exception;

/**
 * @author Zeger Hoogeboom
 */
public class FlysystemGenericException extends RuntimeException
{
	public FlysystemGenericException(String message)
	{
		super(message);
	}

	public FlysystemGenericException(Throwable cause)
	{
		super(cause);
	}
}
