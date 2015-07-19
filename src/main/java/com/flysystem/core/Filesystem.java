package com.flysystem.core;

/**
 * @author Zeger Hoogeboom
 */
public interface Filesystem extends Read, Write, Update
{
	/**
	 * Get a file/directory handler.
	 *
	 * @param string  String path    The path to the file.
	 * @param Handler handler An optional existing handler to populate.
	 * @return Handler Either a file or directory handler.
	 */
	public Handler get(String path, Handler handler);

	/**
	 * Register a plugin.
	 *
	 * @param plugin The plugin to register.
	 * @return this
	 */
	public Filesystem addPlugin(Plugin plugin);
}
