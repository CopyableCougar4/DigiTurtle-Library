package com.digiturtle.common;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;

public interface Logger {

	public void info(String output);
	
	public void error(String output, Throwable throwable);
	
	public void warn(String output);
	
	public void debug(String output);
	
	public void close();
	
	public static class LoggingSystem {
		
		private static Logger logger;
		private static boolean inited = false;
		private static int logLevel = 1;	
		// 0 shows all, 1 hides debug, 2 hides debug/info, 3 hides debug/info/warn, 4 hides debug/info/warn/error
		
		public static void initialize() {
			if (inited) return;
			inited = true;
			setLogger(new DefaultLogger());
		}
		
		public static void info(String output) {
			initialize();
			if (logLevel > 1) return;
			logger.info(output);
		}
		
		public static void error(String output, Throwable throwable) {
			initialize();
			if (logLevel > 3) return;
			logger.error(output, throwable);
		}
		
		public static void warn(String output) {
			initialize();
			if (logLevel > 2) return;
			logger.warn(output);
		}
		
		public static void debug(String output) {
			initialize();
			if (logLevel > 0) return;
			logger.debug(output);
		}
		
		public static void setLevel(int level) {
			logLevel = level;
		}
		
		public static void setLogger(Logger logger) {
			initialize();
			LoggingSystem.logger = logger;
		}
		
		protected static String getStackTrace(final Throwable throwable) {
			final StringWriter sw = new StringWriter();
			final PrintWriter pw = new PrintWriter(sw, true);
			throwable.printStackTrace(pw);
			return sw.getBuffer().toString();
		}
		
		public static void close() {
			logger.close();
		}
		
	}
	
	public static class DefaultLogger implements Logger {

		@Override
		public void info(String output) {
			System.out.println("[Info] " + new Date() + ": " + output);
		}

		@Override
		public void error(String output, Throwable throwable) {
			System.out.println("[Error] " + new Date() + ": " + output);
			System.out.println("[Error] " + new Date() + ": " + LoggingSystem.getStackTrace(throwable));
		}

		@Override
		public void warn(String output) {
			System.out.println("[Warn] " + new Date() + ": " + output);
		}

		@Override
		public void debug(String output) {
			System.out.println("[Debug] " + new Date() + ": " + output);
		}
		
		public void close() {
			
		}
		
	}
	
}
