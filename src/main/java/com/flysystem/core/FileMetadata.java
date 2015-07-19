package com.flysystem.core;

/**
 * @author Zeger Hoogeboom
 */
public class FileMetadata
{
	String path;
	Long size;
	String visibility;
	String mimetype;
	Long timestamp;
	String type;

	public FileMetadata(String path, Long size, String visibility, String mimetype, Long timestamp, String type)
	{
		this.path = path;
		this.size = size;
		this.visibility = visibility;
		this.mimetype = mimetype;
		this.timestamp = timestamp;
		this.type = type;
	}

	public String getPath()
	{
		return path;
	}

	public void setPath(String path)
	{
		this.path = path;
	}

	public Long getSize()
	{
		return size;
	}

	public void setSize(Long size)
	{
		this.size = size;
	}

	public String getVisibility()
	{
		return visibility;
	}

	public void setVisibility(String visibility)
	{
		this.visibility = visibility;
	}

	public String getMimetype()
	{
		return mimetype;
	}

	public void setMimetype(String mimetype)
	{
		this.mimetype = mimetype;
	}

	public Long getTimestamp()
	{
		return timestamp;
	}

	public void setTimestamp(Long timestamp)
	{
		this.timestamp = timestamp;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}
}
