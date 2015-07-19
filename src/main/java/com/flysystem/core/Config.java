package com.flysystem.core;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Zeger Hoogeboom
 */
public class Config
{
	protected Map<String, Object> settings = new HashMap<String, Object>();
	protected Config fallback;

	public Config(Map<String, Object> settings)
	{
		this.settings = settings;
	}

	public Config()
	{
		this.settings = new HashMap<>();
	}

	public static Config withVisibility(final Visibility visibility)
	{
		return new Config(new HashMap<String, Object>() {{
			put("visibility", visibility);
		}});
	}

	public Object get(String key, Object defaultValue)
	{
		if (! this.settings.containsKey(key)) {
			return getDefault(key, defaultValue);
		}
		return this.settings.get(key);
	}

	public boolean has(String key)
	{
		return this.settings.containsKey(key);
	}

	protected Object getDefault(String key, Object defaultValue)
	{
		if (this.fallback == null) {
			return defaultValue;
		}
		return this.fallback.get(key, defaultValue);
	}

	public Config set(String key, Object value)
	{
		this.settings.put(key, value);
		return this;
	}

	public Config setFallback(Config fallback)
	{
		this.fallback = fallback;
		return this;
	}


}
