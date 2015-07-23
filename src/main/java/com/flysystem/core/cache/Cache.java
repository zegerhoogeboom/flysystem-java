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

package com.flysystem.core.cache;/*
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

import com.flysystem.core.FileMetadata;
import com.flysystem.core.Read;

import java.util.List;

/**
 * @author Zeger Hoogeboom
 */
public interface Cache extends Read
{
	boolean isComplete(String dirname, boolean recursive);

	/**
	 * Set a directory to completely listed.
	 *
	 * @param dirname
	 * @param recursive
	 */
	void setComplete(String dirname, boolean recursive);

	/**
	 * Store the contents of a directory.
	 *
	 * @param directory
	 * @param contents
	 * @param recursive
	 */
	void storeContents(String directory, List<FileMetadata> contents, boolean recursive);

	/**
	 * Flush the cache.
	 */
	void flush();

	/**
	 * Autosave trigger.
	 */
	void autosave();

	/**
	 * Store the cache.
	 */
	void save();

	/**
	 * Load the cache.
	 */
	void load();

	/**
	 * Rename a file.
	 *
	 * @param from
	 * @param to
	 */
	void rename(String from, String to);

	/**
	 * Copy a file.
	 *
	 * @param from
	 * @param to
	 */
	void copy(String from, String to);

	/**
	 * Delete an object from cache.
	 *
	 * @param path object path
	 */
	void delete(String path);

	/**
	 * Delete all objects from from a directory.
	 *
	 * @param dirname directory path
	 */
	void deleteDir(String dirname);

	/**
	 * Update the metadata for an object.
	 *
	 * @param path     object path
	 * @param object   object metadata
	 * @param autoSave whether to trigger the autosave routine
	 */
	void updateObject(String path, Object object, boolean autoSave);

	/**
	 * Update the metadata for an object.
	 *
	 * @param path   object path
	 * @param object object metadata
	 */
	void updateObject(String path, Object object);

	/**
	 * Store object hit miss.
	 *
	 * @param path
	 */
	void storeMiss(String path);
}
