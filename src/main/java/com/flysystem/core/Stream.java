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

import java.io.OutputStream;
import java.util.Map;

/**
 * @author Zeger Hoogeboom
 */
public interface Stream
{

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
	 * Retrieves a read-stream for a path.
	 *
	 * @param path The path to the file.
	 * @return resource|false The path resource or false on failure.
	 * @throws FileNotFoundException
	 */
	String readStream(String path);

	/**
	 * Create a file or update if exists.
	 */
	boolean putStream(String path, OutputStream resource, Config config);
	boolean putStream(String path, OutputStream resource);
}
