package com.flysystem.core;

/**
 * @author Zeger Hoogeboom
 */
public interface Plugin
{
	public String getMethod();
	Filesystem setFilesystem(Filesystem filesystem);
}
