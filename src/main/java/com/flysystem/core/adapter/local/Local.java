package com.flysystem.core.adapter.local;

import com.flysystem.core.FileMetadata;
import com.flysystem.core.Filesystem;
import com.flysystem.core.Visibility;
import com.flysystem.core.adapter.AbstractAdapter;
import com.flysystem.core.exception.DirectoryNotFoundException;
import com.flysystem.core.exception.FileNotFoundException;
import com.flysystem.core.exception.FlywayGenericException;
import com.google.common.io.Files;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

/**
 * @author Zeger Hoogeboom
 */
public class Local extends AbstractAdapter
{
	private static int SKIP_LINKS = 0001;
	private static int DISALLOW_LINKS = 0002;
	protected String pathSeparator = "/";
	protected int writeFlags;
	private int linkHandling;

	public Local(String root, int writeFlags, int linkHandling)
	{
		File realRoot = this.ensureDirectory(root);

		if (! realRoot.isDirectory() || ! realRoot.canRead()) {
			throw new FlywayGenericException(String.format("The root path %s is not readable.", realRoot.getAbsolutePath()));
		}

		try {
			this.setPathPrefix(realRoot.getCanonicalPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.writeFlags = writeFlags;
		this.linkHandling = linkHandling;
	}

	public Local(String root)
	{
		this(root, 2, 2);
	}

	/**
	 * Ensure the root directory exists.
	 *
	 * @return string real path to root
	 */
	protected File ensureDirectory(String root)
	{
		File file = new File(root);
		return ensureDirectory(file);
	}

	protected File ensureDirectory(File file)
	{
		if (!file.isDirectory()) {
			new File(file.getParent()).mkdirs();
		}

		return file;
	}

	public boolean has(String path)
	{
		String location = this.applyPathPrefix(path);
		return new File(location).exists();
	}

	public String read(String path) throws FileNotFoundException
	{
		String location = this.applyPathPrefix(path);
		try {
			return Files.toString(new File(location), Charset.defaultCharset());
		} catch (IOException e) {
			throw new FileNotFoundException(location);
		}
	}

	public String readStream(String path)
	{
		return null;
	}

	public List<com.flysystem.core.File> listContents(Filesystem filesystem, String directory, boolean recursive)
	{
		List<File> files = (List<File>) FileUtils.listFiles(new File(applyPathPrefix(directory)), null, recursive);
		return LocalFileConverter.doConvert(filesystem, files, getPathPrefix());
	}

	public List<com.flysystem.core.File> listContents(Filesystem filesystem, String directory)
	{
		return listContents(filesystem, directory, false);
	}

	public FileMetadata getMetadata(String path) throws FileNotFoundException
	{
		File file = new File(applyPathPrefix(path));
		if (!file.exists()) throw new FileNotFoundException(file.getPath());
		return new BasicFileAttributesConverter().convert(file);
	}

	public long getSize(String path)
	{
		File file = new File(path);
		if (!file.isFile()) throw new FileNotFoundException(path);
		return file.length();
	}

	public String getMimetype(String path)
	{
		return null;
	}

	public long getTimestamp(String path)
	{
		return new File(path).lastModified();
	}

	public String getVisibility(String path)
	{
		return null;
	}

	public boolean write(String path, String contents, Map<String, Object> config)
	{
		return false;
	}

	public boolean write(String path, String contents)
	{
		File file = new File(applyPathPrefix(path));
		try {
			FileUtils.write(file, contents);
			return true;
		} catch (IOException e) {
			throw new FlywayGenericException(e);
		}
	}

	public boolean writeStream(String path, OutputStream resource, Map<String, Object> config)
	{
		return false;
	}

	public boolean writeStream(String path, OutputStream resource)
	{
		return false;
	}

	public boolean update(String path, String contents, Map<String, Object> config)
	{
		return false;
	}

	public boolean update(String path, String contents)
	{
		return false;
	}

	public boolean updateStream(String path, OutputStream resource, Map<String, Object> config)
	{
		return false;
	}

	public boolean updateStream(String path, OutputStream resource)
	{
		return false;
	}

	public boolean rename(String from, String to)
	{
		File target = new File(applyPathPrefix(to));
		ensureDirectory(target);
		try {
			Files.move(new File(applyPathPrefix(from)), target);
			return true;
		} catch (IOException e) {
			throw new FlywayGenericException(e);
		}
	}

	public boolean copy(String path, String newpath)
	{
		File source = new File(this.applyPathPrefix(path));
		validateIsFileAndExists(source);
		File destination = new File(this.applyPathPrefix(newpath));
		this.ensureDirectory(destination);
		try {
			FileUtils.copyFile(source, destination);
			return true;
		} catch (IOException e) {
			throw new FlywayGenericException(e);
		}
	}

	public boolean delete(String path)
	{
		File file = new File(applyPathPrefix(path));
		return file.isFile() && file.delete();
	}

	public boolean deleteDir(String dirname)
	{
		File directory = new File(applyPathPrefix(dirname));
		validateIsDirectoryAndExists(directory);
		try {
			FileUtils.deleteDirectory(directory);
		} catch (IOException e) {
			throw new FlywayGenericException(e);
		}
		return false;
	}

	public boolean createDir(String dirname, Map<String, Object> config)
	{
		return false;
	}

	public boolean createDir(String dirname)
	{
		return new File(applyPathPrefix(dirname)).mkdirs();
	}

	public boolean setVisibility(String path, Visibility visibility)
	{
		switch (visibility) {
			case PRIVATE: return setVisibilityPrivate(path);
			case PUBLIC: return setVisibilityPublic(path);
			default: return false;
		}
	}

	private boolean setVisibilityPrivate(String path)
	{
		File file = new File(path);
		boolean readable = file.setReadable(true, true);
		boolean executable = file.setExecutable(true, true);
		boolean writable = file.setWritable(true, true);
		return readable && executable && writable;
	}

	private boolean setVisibilityPublic(String path)
	{
		File file = new File(path);
		boolean readable = file.setReadable(true, false);
		boolean executable = file.setExecutable(true, false);
		boolean writable = file.setWritable(true, false);
		return readable && executable && writable;
	}

	private void validateIsDirectoryAndExists(File file) throws DirectoryNotFoundException
	{
		if (!file.exists() || !file.isDirectory()) throw new DirectoryNotFoundException(file.getPath());
	}

	private void validateIsFileAndExists(File file) throws FileNotFoundException
	{
		if (!file.exists() || !file.isFile()) throw new FileNotFoundException(file.getPath());
	}
}
