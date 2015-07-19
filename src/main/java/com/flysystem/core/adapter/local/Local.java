package com.flysystem.core.adapter.local;

import com.flysystem.core.Config;
import com.flysystem.core.FileMetadata;
import com.flysystem.core.Filesystem;
import com.flysystem.core.Visibility;
import com.flysystem.core.adapter.AbstractAdapter;
import com.flysystem.core.exception.DirectoryNotFoundException;
import com.flysystem.core.exception.FileNotFoundException;
import com.flysystem.core.exception.FlysystemGenericException;
import com.google.common.io.Files;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.List;

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
			throw new FlysystemGenericException(String.format("The root path %s is not readable.", realRoot.getAbsolutePath()));
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
		return new File(applyPathPrefix(path)).exists();
	}

	public String read(String path) throws FileNotFoundException
	{
		try {
			return Files.toString(getExistingFile(path), Charset.defaultCharset());
		} catch (IOException e) {
			throw new FlysystemGenericException(e);
		}
	}

	public String readStream(String path)
	{
		return null;
	}

	public List<com.flysystem.core.File> listContents(Filesystem filesystem, String directory, boolean recursive)
	{
		List<File> files = (List<File>) FileUtils.listFiles(getExistingFile(directory), null, recursive);
		return LocalFileConverter.doConvert(filesystem, files, getPathPrefix());
	}

	public List<com.flysystem.core.File> listContents(Filesystem filesystem, String directory)
	{
		return listContents(filesystem, directory, false);
	}

	public FileMetadata getMetadata(String path) throws FileNotFoundException
	{
		return new BasicFileAttributesConverter().convert(getExistingFile(path));
	}

	public long getSize(String path)
	{
		return getExistingFile(path).length();
	}

	public String getMimetype(String path)
	{
		return null;
	}

	public long getTimestamp(String path)
	{
		return getExistingFile(path).lastModified();
	}

	public String getVisibility(String path)
	{
		return null;
	}

	public boolean write(String path, String contents, Config config)
	{
		try {
			File file = new File(applyPathPrefix(path));
			setPermissions(file, config);
			FileUtils.write(file, contents);
			return true;
		} catch (IOException e) {
			throw new FlysystemGenericException(e);
		}
	}

	public boolean write(String path, String contents)
	{
   	    return write(path, contents, new Config());
	}

	public boolean writeStream(String path, OutputStream resource, Config config)
	{
		return false;
	}

	public boolean writeStream(String path, OutputStream resource)
	{
		return false;
	}

	public boolean update(String path, String contents)
	{
		return update(path, contents, new Config());
	}

	public boolean update(String path, String contents, Config config)
	{
		try {
			File existingFile = getExistingFile(path);
			setPermissions(existingFile, config);
			FileUtils.write(existingFile, contents);
			return true;
		} catch (IOException e) {
			throw new FlysystemGenericException(e);
		}
	}

	public boolean updateStream(String path, OutputStream resource, Config config)
	{
		return false;
	}

	public boolean updateStream(String path, OutputStream resource)
	{
		return false;
	}


	public void rename(String from, String to)
	{
		File source = new File(applyPathPrefix(from));
		File target = new File(applyPathPrefix(to));
		FileCommands.manipulate(this, source, target, new FileCommands.MoveFileCommand());
	}

	public void copy(String path, String newpath)
	{
		File source = new File(applyPathPrefix(path));
		File destination = new File(applyPathPrefix(newpath));
		FileCommands.manipulate(this, source, destination, new FileCommands.CopyFileCommand());
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
			throw new FlysystemGenericException(e);
		}
		return false;
	}

	public boolean createDir(String dirname, Config config)
	{
		File file = new File(applyPathPrefix(dirname));
		setPermissions(file, config);
		return file.mkdirs();
	}

	public boolean createDir(String dirname)
	{
		return createDir(dirname, new Config());
	}

	private void setPermissions(File file, Config config)
	{
		Visibility visibility = (Visibility) config.get("visibility", Visibility.PUBLIC);
		setVisibility(file.getPath(), visibility);
	}

	@Override
	public boolean setVisibility(String path, Visibility visibility)
	{
		File file = new File(applyPathPrefix(path));
		switch (visibility) {
			case PRIVATE: return setVisibilityPrivate(file);
			case PUBLIC: return setVisibilityPublic(file);
			default: return false;
		}
	}

	private boolean setVisibilityPrivate(File file)
	{
		boolean readable = file.setReadable(true, true);
		boolean executable = file.setExecutable(true, true);
		boolean writable = file.setWritable(true, true);
		return readable && executable && writable;
	}

	private boolean setVisibilityPublic(File file)
	{
		boolean readable = file.setReadable(true, false);
		boolean executable = file.setExecutable(true, false);
		boolean writable = file.setWritable(true, false);
		return readable && executable && writable;
	}

	private void validateIsDirectoryAndExists(File file) throws DirectoryNotFoundException
	{
		if (!file.exists() || !file.isDirectory()) throw new DirectoryNotFoundException(file.getPath());
	}

	private File getExistingFile(String path)
	{
		File file = new File(applyPathPrefix(path));
		if (!file.exists()) throw new FileNotFoundException(file.getPath());
		return file;
	}

}
