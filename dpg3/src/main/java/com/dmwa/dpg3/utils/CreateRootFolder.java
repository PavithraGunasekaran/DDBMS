package com.dmwa.dpg3.utils;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

public class CreateRootFolder {

	public static final String PATH = "root";

	public static void createRootDirectory() {
		if (Files.isDirectory(Paths.get(PATH))) {
		} else {
			File dir = new File("root");
			dir.mkdir();
		}
	}
}
