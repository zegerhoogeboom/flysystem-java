package com.flysystem.core;

import com.flysystem.core.exception.FileNotFoundException;

import java.util.List;

/**
 * @author Zeger Hoogeboom
 */
interface Read
{
	/**
	 * Check whether a file exists.
	 * @param path
	 * @return
	 */
	boolean has(String path);

	/**
	 * Read a file.
	 *
	 * @param path The path to the file.
	 * @return The file contents or false on failure.
	 * @throws FileNotFoundException
	 */
	String read(String path) throws FileNotFoundException;

	/**
	 * Retrieves a read-stream for a path.
	 *
	 * @param path The path to the file.
	 * @return resource|false The path resource or false on failure.
	 * @throws FileNotFoundException
	 */
	String readStream(String path);

	/**
	 * List contents of a directory.
	 *
	 * @param directory The directory to list.
	 * @param recursive Whether to list recursively.
	 * @param filesystem
	 * @return A list of file metadata.
	 */
	List<File> listContents(Filesystem filesystem, String directory, boolean recursive);

	/**
	 * Get a file's metadata.
	 *
	 * @param path The path to the file.
	 * @return array|false The file metadata or false on failure.
	 * @throws FileNotFoundException
	 */
	FileMetadata getMetadata(String path);

	/**
	 * Get a file's size.
	 *
	 * @param path The path to the file.
	 * @return int|false The file size or false on failure.
	 */
	long getSize(String path);

	/**
	 * Get a file's mime-type.
	 *
	 * @param path The path to the file.
	 * @return string|false The file mime-type or false on failure.
	 * @throws FileNotFoundException
	 */
	String getMimetype(String path);

	/**
	 * Get a file's timestamp.
	 *
	 * @param path The path to the file.
	 * @return string|false The timestamp or false on failure.
	 * @throws FileNotFoundException
	 */
	long getTimestamp(String path);

	/**
	 * Get a file's visibility.
	 *
	 * @param path The path to the file.
	 * @return string|false The visibility (public|private) or false on failure.
	 * @throws FileNotFoundException
	 */
	String getVisibility(String path);
}
