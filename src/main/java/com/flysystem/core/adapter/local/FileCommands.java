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

package com.flysystem.core.adapter.local;

import com.flysystem.core.exception.FileExistsException;
import com.flysystem.core.exception.FileNotFoundException;
import com.flysystem.core.exception.FlysystemGenericException;
import com.google.common.io.Files;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * @author Zeger Hoogeboom
 */
public class FileCommands
{
	/**
	 * Interface to manipulate something from a source to target file.
	 */
	public interface ManipulateFileCommand
	{
		void execute(File source, File target) throws FlysystemGenericException;
	}

	public static class CopyFileCommand implements ManipulateFileCommand
	{
		public void execute(File source, File target)
		{
			try {
				FileUtils.copyFile(source, target);
			} catch (IOException e) {
				throw new FlysystemGenericException(e);
			}
		}
	}

	public static class MoveFileCommand implements ManipulateFileCommand
	{
		public void execute(File source, File target)
		{
			try {
				Files.move(source, target);
			} catch (IOException e) {
				throw new FlysystemGenericException(e);
			}
		}
	}

	/**
	 *
	 * @param local The local filesystem to check existence of files against.
	 * @param source Must exists. Throws FileNotFoundException otherwise.
	 * @param target Must not exist. Throws FileExistsException otherwise.
	 * @param command The command to be executed.
	 */
	public static void manipulate(Local local, File source, File target, ManipulateFileCommand command)
	{
		validateIsFileAndExists(source);
		if (target.exists()) throw new FileExistsException(target.getPath());
		local.ensureDirectory(target);
		command.execute(source, target);
	}

	private static void validateIsFileAndExists(File file) throws FileNotFoundException
	{
		if (!file.exists() || !file.isFile()) throw new FileNotFoundException(file.getPath());
	}
}
