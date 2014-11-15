package com.digiturtle.common;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Date;

import com.digiturtle.common.Logger.DefaultLogger;

public class FileLogger extends DefaultLogger {
	
	private PrintStream sysOut;
	
	public FileLogger(File file) {
		sysOut = System.out;
		try {
			PrintStream printStream = new PrintStream(file);
			System.setOut(printStream);
		} catch (FileNotFoundException e) {
			LoggingSystem.error("FileNotFoundException in FileLogger(File)", e);
		}
		{
			System.out.println("==================================================================================");
			System.out.println("        INITIALIZING LOGGING INTERFACE       " + new Date() + "       ");
			System.out.println("==================================================================================");
			System.out.println("");
		}
	}
	
	public void close() {
		System.setOut(sysOut);
	}

}
