/*
 * Copyright (c) 2013-2015 Frank de Jonge
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is furnished
 * to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.flysystem.core;

import com.flysystem.core.exception.FileNotFoundException;

import java.util.List;

/**
 * @author Zeger Hoogeboom
 */
public interface Read
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
	 * List contents of a directory.
	 *
	 * @param directory The directory to list.
	 * @param recursive Whether to list recursively.
	 * @return A list of file metadata.
	 */
	List<FileMetadata> listContents(String directory, boolean recursive);

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
	Long getSize(String path);

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
	Long getTimestamp(String path);

	/**
	 * Get a file's visibility.
	 *
	 * @param path The path to the file.
	 * @return string|false The visibility (public|private) or false on failure.
	 * @throws FileNotFoundException
	 */
	Visibility getVisibility(String path);
}
