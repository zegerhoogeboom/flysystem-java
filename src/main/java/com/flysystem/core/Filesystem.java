package com.flysystem.core;

/**
 * @author Zeger Hoogeboom
 */
public interface Filesystem extends Read, Write, Update
{
	/**
	 * Get a file/directory handler.
	 *
	 * @param path The path to the file.
	 * @param handler An optional existing handler to populate.
	 * @return Handler Either a file or directory handler.
	 */
	Handler get(String path, Handler handler);

	/**
	 * Register a plugin.
	 *
	 * @param plugin The plugin to register.
	 * @return this
	 */
	Filesystem addPlugin(Plugin plugin);
}
