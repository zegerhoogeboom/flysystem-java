package com.flysystem.core;

/**
 * @author Zeger Hoogeboom
 */
public interface Plugin
{
	String getMethod();  //fixme different implementation
	Filesystem setFilesystem(Filesystem filesystem);
}
