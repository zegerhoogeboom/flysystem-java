package com.flysystem.core;

import java.io.OutputStream;
import java.util.Map;

/**
 * @author Zeger Hoogeboom
 */
interface Update
{
	/**
	 * Create a file or update if exists.
	 * @return bool True on success, false on failure.
	 */
	boolean put(String path, String contents, Map<String, Object> config);
	boolean put(String path, String contents);

	/**
	 * Create a file or update if exists.
	 */
	boolean putStream(String path, OutputStream resource, Map<String, Object> config);
	boolean putStream(String path, OutputStream resource);

	/**
	 * Read and delete a file.
	 */
	String readAndDelete(String path);
}
