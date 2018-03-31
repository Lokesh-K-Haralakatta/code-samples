package com.loki.fma;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

class MonitorDirectory implements Runnable{
  //Static logger object for the class
  private static final Logger LOG = Logger.getLogger(MonitorDirectory.class.getName());
		
  private DirectoryOperations dOps = null;
  
  //Default values for threads sleep time in minutes
  private int moveThreadSleepTime = 1;
  private int archiveThreadSleepTime = 1;
  //Default value for secured directory threshold value in bytes
  private int secDirThreshold = 100;
  
  static {
	  DirectoryOperations.turnOffLogging();
  }
  
  public MonitorDirectory(String tPath, String sPath, String aPath) throws IOException {
			  dOps = new DirectoryOperations(tPath, sPath, aPath);
  }
			
  public MonitorDirectory(String tPath, String sPath, String aPath, int mtst, int atst, int sdt) 
  throws IOException {
	  dOps = new DirectoryOperations(tPath, sPath, aPath);
	  moveThreadSleepTime = mtst;
	  archiveThreadSleepTime = atst;
	  secDirThreshold = sdt;
  }
  
//Method to turn off logging
	public static void turnOffLogging() {
		LOG.setLevel(Level.OFF);
	}

  @Override
  public void run() {
	while(true) {  
	// move thread is running, then call method to move files
	if(Thread.currentThread().getName().equalsIgnoreCase("movethread")){
		LOG.info("Thread - move is running, calling moveFiles method");
		try {
			//Call moveFiles method to move files from tmp to secured directory
			dOps.moveFiles();
			//Dump details about moved files
			int movedFiles = dOps.getMovedFiles().size();
			System.out.println("Number of files Moved: " + movedFiles);
			if(movedFiles>0) {
				System.out.print("List of Moved Files: ");
				displayListItems(dOps.getMovedFiles());
			}
			//Make thread to sleep
			Thread.sleep(moveThreadSleepTime*60*1000);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//archive thread is running, then call archive method to archive low lmt file
	else if(Thread.currentThread().getName().equalsIgnoreCase("archivethread")){
		LOG.info("Thread - archive is running, calling archiveFiles method");
		//Call archive files to move files with low lmt to archived folder if any
		try {
			//Call archiveFiles method
			dOps.archiveFiles(secDirThreshold);
			//Dump details about the secured directory and archived files
			System.out.println("Size of securedDirectory = " + dOps.getSecDirSize());
			int filesCount = dOps.getArchivedFiles().size();
			System.out.println("Number of archived files = " + filesCount);
			if(filesCount > 0) {
				System.out.print("List of Archived Files: ");
				displayListItems(dOps.getArchivedFiles());
			}
			filesCount = dOps.getDeletedFiles().size();
			System.out.println("Number of deleted files = " + filesCount);
			if(filesCount > 0) {
				System.out.print("List of Deleted Files: ");
				displayListItems(dOps.getDeletedFiles());
			}
			//dOps.displayLMTMap();
			//Make thread to sleep
			Thread.sleep(archiveThreadSleepTime*60*1000);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	else {
		LOG.info("Some other thread has got the chance to run now, No Op");
	}
  }
 }
  
 //Method to display items from given list
 private void displayListItems(List<String> list) {
	 for(String file:list)
			System.out.print(file + " , ");
	 System.out.println("");
 }
}
