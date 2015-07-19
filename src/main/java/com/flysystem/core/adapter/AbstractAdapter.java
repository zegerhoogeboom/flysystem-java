package com.flysystem.core.adapter;

import com.flysystem.core.Adapter;
import com.google.common.base.Strings;
import org.apache.commons.lang3.StringUtils;

import java.io.File;

/**
 * @author Zeger Hoogeboom
 */
public abstract class AbstractAdapter implements Adapter
{
	protected String pathPrefix;

	public void setPathPrefix(String prefix)
	{
		boolean isEmpty = Strings.isNullOrEmpty(prefix);
		if (! isEmpty) {
			prefix = StringUtils.stripEnd(prefix, File.separator) + File.separator;
		}
		this.pathPrefix = isEmpty ? null : prefix;
	}

	public String getPathPrefix()
	{
		return pathPrefix;
	}

	public String applyPathPrefix(String path)
	{
		path = StringUtils.stripStart(path, "\\/");

		if (path.length() == 0) {
			return getPathPrefix() != null ? getPathPrefix() : "";
		}

		path = getPathPrefix() + path;

		return path;
	}
}
