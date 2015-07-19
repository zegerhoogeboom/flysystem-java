package com.flysystem.core.adapter.local;

import com.flysystem.core.exception.FileExistsException;
import com.flysystem.core.exception.FileNotFoundException;
import com.flysystem.core.exception.FlywayGenericException;
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
	 * Interface to manipulate something from source to target.
	 */
	public interface ManipulateFileCommand
	{
		void execute(File source, File target) throws FlywayGenericException;
	}

	public static class CopyFileCommand implements ManipulateFileCommand
	{
		public void execute(File source, File target)
		{
			try {
				FileUtils.copyFile(source, target);
			} catch (IOException e) {
				throw new FlywayGenericException(e);
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
				throw new FlywayGenericException(e);
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
