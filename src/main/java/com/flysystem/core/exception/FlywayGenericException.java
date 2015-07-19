package com.flysystem.core.exception;

/**
 * @author Zeger Hoogeboom
 */
public class FlywayGenericException extends RuntimeException
{
	public FlywayGenericException(String message)
	{
		super(message);
	}

	public FlywayGenericException(Throwable cause)
	{
		super(cause);
	}
}
