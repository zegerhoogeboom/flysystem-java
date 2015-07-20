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

	public Object get(String key)
	{
		return get(key, null);
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

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (! (o instanceof Config)) return false;

		Config config = (Config) o;

		if (settings != null ? ! settings.equals(config.settings) : config.settings != null) return false;
		return ! (fallback != null ? ! fallback.equals(config.fallback) : config.fallback != null);

	}

	@Override
	public int hashCode()
	{
		int result = settings != null ? settings.hashCode() : 0;
		result = 31 * result + (fallback != null ? fallback.hashCode() : 0);
		return result;
	}
}
