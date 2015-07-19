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

import com.flysystem.core.exception.FileExistsException;
import com.flysystem.core.exception.FileNotFoundException;
import com.flysystem.core.exception.RootViolationException;

import java.io.OutputStream;
import java.util.Map;

/**
 * @author Zeger Hoogeboom
 */
interface Write
{
	/**
	 * Write a new file.
	 *
	 * @param path     The path of the new file.
	 * @param contents The file contents.
	 * @param config   A configuration array.
	 * @return bool True on success, false on failure.
	 * @throws FileExistsException
	 */
	boolean write(String path, String contents, Config config);
	boolean write(String path, String contents);


	/**
	 * Write a new file using a stream.
	 *
	 * @param path     The path of the new file.
	 * @param resource resource The file handle.
	 * @param config   An optional configuration array.
	 * @return bool True on success, false on failure.
	 * @throws InvalidArgumentException If resource is not a file handle.
	 * @throws FileExistsException
	 */
	boolean writeStream(String path, OutputStream resource, Config config);
	boolean writeStream(String path, OutputStream resource);


	/**
	 * Update an existing file.
	 *
	 * @param path     The path of the existing file.
	 * @param contents The file contents.
	 * @return bool True on success, false on failure.
	 * @throws FileNotFoundException
	 */
	boolean update(String path, String contents);
	boolean update(String path, String contents, Config config);

	/**
	 * Update an existing file using a stream.
	 *
	 * @param path     The path of the existing file.
	 * @param resource resource The file handle.
	 * @param config   An optional configuration array.
	 * @return bool True on success, false on failure.
	 * @throws InvalidArgumentException If resource is not a file handle.
	 * @throws FileNotFoundException
	 */
	boolean updateStream(String path, OutputStream resource, Config config);
	boolean updateStream(String path, OutputStream resource);

	/**
	 * Rename a file.
	 *
	 * @param from    Path to the existing file.
	 * @param to The new path of the file.
	 * @return bool True on success, false on failure.
	 * @throws FileExistsException   Thrown if newpath exists.
	 * @throws FileNotFoundException Thrown if String path does not exist.
	 */
	boolean rename(String from, String to) throws FileExistsException, FileNotFoundException;

	/**
	 * Copy a file.
	 *
	 * @param path    Path to the existing file.
	 * @param newpath The new path of the file.
	 * @return bool True on success, false on failure.
	 * @throws FileExistsException   Thrown if newpath exists.
	 * @throws FileNotFoundException Thrown if String path does not exist.
	 */
	boolean copy(String path, String newpath);

	/**
	 * Delete a file.
	 *
	 * @param string String path
	 * @return bool True on success, false on failure.
	 * @throws FileNotFoundException
	 */
	boolean delete(String path);

	/**
	 * Delete a directory.
	 *
	 * @param dirname
	 * @return bool True on success, false on failure.
	 * @throws RootViolationException Thrown if dirname is empty.
	 */
	boolean deleteDir(String dirname);

	/**
	 * Create a directory.
	 *
	 * @param string dirname The name of the new directory.
	 * @param array  config  An optional configuration array.
	 * @param config
	 * @return bool True on success, false on failure.
	 */
	boolean createDir(String dirname, Config config);
	boolean createDir(String dirname);

	/**
	 * Set the visibility for a file.
	 *
	 * @param string String path       The path to the file.
	 * @param string visibility One of 'public' or 'private'.
	 * @return bool True on success, false on failure.
	 */
	boolean setVisibility(String path, Visibility visibility);

}
