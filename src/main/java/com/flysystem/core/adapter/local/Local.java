package com.flysystem.core.adapter.local;

import com.flysystem.core.Filesystem;
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
//			throw new InvalidArgumentException(String.format("The root path %s is not readable.", realRoot.getAbsolutePath()));
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
		this.writeFlags = 2;
		this.linkHandling = 2;
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

	public Map<String, Object> getMetadata(String path)
	{
		return null;
	}

	public int getSize(String path)
	{
		return 0;
	}

	public String getMimetype(String path)
	{
		return null;
	}

	public int getTimestamp(String path)
	{
		return 0;
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

	public boolean rename(String path, String newpath)
	{
		return false;
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

	public boolean setVisibility(String path, String visibility)
	{
		return false;
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
