package com.flysystem.core.adapter.local;

/**
 * @author Zeger Hoogeboom
 */
public class Permissions
{
	public static class File {
		public static int publicFile = 0744;
		public static int privateFile = 0700;
	}
	public static class Dir {
		public static int publicDir = 0755;
		public static int privateDir = 0700;
	}
}
