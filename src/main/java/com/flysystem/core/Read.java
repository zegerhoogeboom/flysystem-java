package com.flysystem.core;

import com.flysystem.core.exception.FileNotFoundException;
import com.google.common.base.Optional;

import java.io.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.Map;

/**
 * @author Zeger Hoogeboom
 */
interface Read
{
	/**
	 * Check whether a file exists.
	 *
	 * @param string String path
	 * @return bool
	 */
	boolean has(String path);

	/**
	 * Read a file.
	 *
	 * @param string String path The path to the file.
	 * @return string|false The file contents or false on failure.
	 * @throws FileNotFoundException
	 */
	String read(String path) throws FileNotFoundException;

	/**
	 * Retrieves a read-stream for a path.
	 *
	 * @param string String path The path to the file.
	 * @return resource|false The path resource or false on failure.
	 * @throws FileNotFoundException
	 */
	String readStream(String path);

	/**
	 * List contents of a directory.
	 *
	 * @param  String directory The directory to list.
	 * @param bool   recursive Whether to list recursively.
	 * @param filesystem
	 * @return array A list of file metadata.
	 */
	List<File> listContents(Filesystem filesystem, String directory, boolean recursive);

	/**
	 * Get a file's metadata.
	 *
	 * @param string String path The path to the file.
	 * @return array|false The file metadata or false on failure.
	 * @throws FileNotFoundException
	 */
	FileMetadata getMetadata(String path);

	/**
	 * Get a file's size.
	 *
	 * @param string String path The path to the file.
	 * @return int|false The file size or false on failure.
	 */
	long getSize(String path);

	/**
	 * Get a file's mime-type.
	 *
	 * @param string String path The path to the file.
	 * @return string|false The file mime-type or false on failure.
	 * @throws FileNotFoundException
	 */
	String getMimetype(String path);

	/**
	 * Get a file's timestamp.
	 *
	 * @param string String path The path to the file.
	 * @return string|false The timestamp or false on failure.
	 * @throws FileNotFoundException
	 */
	long getTimestamp(String path);

	/**
	 * Get a file's visibility.
	 *
	 * @param string String path The path to the file.
	 * @return string|false The visibility (public|private) or false on failure.
	 * @throws FileNotFoundException
	 */
	String getVisibility(String path);
}
