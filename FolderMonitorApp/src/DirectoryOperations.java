package com.loki.fma;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.FileTime;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

class DirectoryOperations {
	//Static logger object for the class
	private static final Logger LOG = Logger.getLogger(DirectoryOperations.class.getName());
	
	// Member to hold latest directory size for secured folder
	int dSize = 0;
	
	// Member to hold the minimum last modified time stamp
	FileTime lmt = null;
	
	// Map to cache files based on modification time
	HashMap<FileTime, Path> lmtMap = null;
	
	// Members to store details about directories
	private Path tempDirPath = null;
	private Path secDirPath = null;
	private Path arcDirPath = null;
	
	//Member to store list of moved files
	List<String> movedFiles = null;
	
	//Member to store list of deleted files
	List<String> deletedFiles = null;
	
	//Member to store list of archived files
	List<String> archivedFiles = null;
	
	public DirectoryOperations(String tPath, String sPath, String aPath) throws IOException{
		//Get required path instances
		tempDirPath = Paths.get(tPath);
		secDirPath = Paths.get(sPath);
		arcDirPath = Paths.get(aPath);
		
		//Validate the given temporary directory path
		if(Files.notExists(tempDirPath)) {
			LOG.warning("Given temporary directory not exists, creating one...");
			try {
				Set<PosixFilePermission> perms = PosixFilePermissions.fromString("rwxr-x---");
				FileAttribute<Set<PosixFilePermission>> attr = PosixFilePermissions.asFileAttribute(perms);
				Files.createDirectory(tempDirPath,attr);
				LOG.info("Temporary directory creation successful at " + tempDirPath);
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
		else {
			if(Files.isWritable(tempDirPath))
				LOG.info("Given temp directory is writeable...");
			else {
				throw new IOException("Temp Directory - " + tPath + "is not readable");
			}
		}
			
		
		//Validate the given secured directory path
		if(Files.notExists(secDirPath)) {
			LOG.warning("Given secured directory not exists, creating one...");
			try {
				Set<PosixFilePermission> perms = PosixFilePermissions.fromString("rwxr-x---");
				FileAttribute<Set<PosixFilePermission>> attr = PosixFilePermissions.asFileAttribute(perms);
				Files.createDirectory(secDirPath,attr);
				LOG.info("Secured directory creation successful at " + secDirPath);
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
		else {
			if(Files.exists(secDirPath) && Files.isWritable(secDirPath)) {
				LOG.info("Given secured directory is writeable...");
				
			}
			else {
				throw new IOException("Secured Directory - " + sPath + "is not writeable");
			}
		}
		
		//Validate the given archive directory path
		if(Files.notExists(arcDirPath)) {
			LOG.warning("Given archive directory not exists, creating one...");
			try {
				Set<PosixFilePermission> perms = PosixFilePermissions.fromString("rwxr-x---");
				FileAttribute<Set<PosixFilePermission>> attr = PosixFilePermissions.asFileAttribute(perms);
				Files.createDirectory(arcDirPath,attr);
				LOG.info("Archive directory creation successful at " + arcDirPath);
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
		else {
			if(Files.exists(arcDirPath) && Files.isWritable(arcDirPath)) {
				LOG.info("Given archive directory is writeable...");
			}
			else {
				throw new IOException("Archive Directory - " + aPath + "is not writeable");
			}
		}
	}
	
	//Method to turn off logging
	public static void turnOffLogging() {
		LOG.setLevel(Level.OFF);
	}
	
	//Method to set logging level
	public static void setLogLevel(Level l) {
		LOG.setLevel(l);
	}
	
	//Getter Method to return size of secured directory
	public int getSecDirSize() {
		return dSize;
	}
	
	//Getter Method to return lowest modification time in milliseconds
	public Long getLMT() {
		if(lmt != null)
			return lmt.toMillis();
		else
			return 0l;
	}
	
	//Method to update the latest statistics from secured directory path
	private synchronized void updateSecDirStats() {
		if(Files.exists(secDirPath)) {
			//List to cache deleted files if any
			if(deletedFiles == null)
				deletedFiles = new ArrayList<String>();
			else
				deletedFiles.clear();
			
			//Check and delete files with .bat or .sh extensions if any
			try (DirectoryStream<Path> stream =
				     Files.newDirectoryStream(secDirPath, "*.{bat,sh}")) {
				for (Path filePath: stream) {
				        LOG.info("Deleting file - " + filePath.toString()+ "from " + secDirPath.toString());
				        Files.delete(filePath);
				        deletedFiles.add(filePath.getFileName().toString());
				    }
				LOG.info("Number of files deleted - " + deletedFiles.size());
			} catch (IOException e) {
				    e.printStackTrace();
			}
			
			//Collect the files and get file attributes to update the statistics
			try (DirectoryStream<Path> stream = Files.newDirectoryStream(secDirPath)) {
				//Initialize dSize to 0 and lmt to null
		    	dSize = 0;
		    	lmt = null;
		    	//Set lmtMap
		    	if(lmtMap == null)
		        	lmtMap = new HashMap<FileTime, Path>();
		    	else
		    		lmtMap.clear();
		    	
		    	for (Path filePath: stream) {			    	
				        LOG.info("Collecting stats for file - " + filePath.toString());
				        BasicFileAttributes attr = Files.readAttributes(filePath, BasicFileAttributes.class);
				        Long curSize = attr.size();
				        FileTime curLMT = attr.lastModifiedTime();
				        LOG.info("File Size in bytes = " + curSize);
				        LOG.info("Last Modification time = " + curLMT.toString());
				        
				        //Add file size to directory size
				        dSize += curSize;
				        
				        //Check for lowest LMT and update
				        if(lmt == null)
				        	lmt = curLMT;
				        else 
				        	if(curLMT.compareTo(lmt)<0)
				        		lmt = curLMT;
				        
				        //Log new lmt for debug
				        LOG.info("New LMT - " + lmt.toMillis());
				        
				        //Add Last Modification Time and File to LMT Map
				        if(lmtMap.containsKey(curLMT)) {
				        	LOG.severe("lmtMap already contains the object with key - " + curLMT.toMillis());
				        	}
				        else { 
				        	lmtMap.put(curLMT, filePath);
					        LOG.info("lmtMap does not contain key " + curLMT.toMillis() + " added key , " +
					        								" filePath " + filePath.toString());
				        }
				    }
		    } catch (IOException e) {
				    e.printStackTrace();
			}
		}
	}
	
	//Method to display lmtMap contents for debug purpose
	public void displayLMTMap() {
		if(lmtMap == null)
			System.out.println("LMT Map is Null");
		else if(lmtMap.size() == 0)
			System.out.println("LMT Map is Empty");
		else {
			int lmtCount = lmtMap.size();
			System.out.println("LMT Map contains " + lmtCount + " number of entries...");
			for(FileTime lmt : lmtMap.keySet()) {
				System.out.println(lmt.toMillis() + " ==>  " + lmtMap.get(lmt));
			}
		}
	}
	
	//Method to move files from temp directory to secured directory
	public synchronized void moveFiles() {
		if(movedFiles == null)
			movedFiles = new ArrayList<String>();
		else
			movedFiles.clear();
		
		try (DirectoryStream<Path> stream = Files.newDirectoryStream(tempDirPath)) {
			LOG.info("Moving files from  - " + tempDirPath + " to " + secDirPath + " if any exist");
			for (Path filePath: stream) {
		    	if(Files.isDirectory(filePath))
	        		LOG.warning("Can not move " + filePath.toString() + ", Is a Directory");
		    	else {
		    		String fileToMove = filePath.getFileName().toString();
		    		String targetFile = secDirPath + "/" + fileToMove;
		    		Path targetPath = Paths.get(targetFile);
		    		LOG.info("Target file path to move - " + targetPath.toString());
		        	Files.move(filePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
		        	LOG.info("Moved file - " + fileToMove + " to " + secDirPath);
		        	movedFiles.add(fileToMove);
		        }
		    }
			LOG.info("Number of files moved to " + secDirPath + " : " + movedFiles.size());
			
	} catch (Exception e) {
		    e.printStackTrace();
		}
	}
	
	//Method to get file with lowest LMT value
	private synchronized Path getFileWithLowestLMT() {
		if(lmtMap == null || lmtMap.size() == 0)
			return null;
		else {
			Path filePath = lmtMap.get(lmt);
			//Remove lmt filePath from lmtMap
			lmtMap.remove(lmt);
			//Find next lmt value from lmtMap
			lmt = null;
			for(FileTime ft : lmtMap.keySet())
				if(lmt == null)
					lmt = ft;
				else if(ft.compareTo(lmt)<0)
					lmt = ft;
				else
					continue;
			
			//Return file with lmt
			LOG.info("File with low LMT value - " + filePath.toString());
			return filePath;
		}
	}
	
	//Method to archive file with lowest lmt value from secDirPath to arcDirPath
	public synchronized void archiveFiles(int dtSize) {
		//List to cache archived files
		if(archivedFiles == null)
			archivedFiles = new ArrayList<String>();
		else
			archivedFiles.clear();
	
		//Call updateSecDirStats() to cache latest changes to secured directory
	    updateSecDirStats();
		
	    //Check to archive files if the secured directory size crosses the given threshold
		while(lmtMap.size()>0 && this.getSecDirSize() >= dtSize) {
			try {
				Path filePath = getFileWithLowestLMT();
				if(Files.isDirectory(filePath))
					LOG.warning("Can not move " + filePath.toString() + " , Is a firectory");
				else {
					//Prepare file to archive
					String fileToArchive = filePath.getFileName().toString();
					Long ftaSize = Files.size(filePath);
					String targetFile = arcDirPath + "/" + fileToArchive;
					Path targetPath = Paths.get(targetFile);
					LOG.info("Target file path to archive - " + targetPath.toString());
					//Move the file to archive directory
					Files.move(filePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
					LOG.info("Archived file - " + filePath.toString() + " to " + arcDirPath);
					//Update the archived file details
					archivedFiles.add(fileToArchive);
					dSize -= ftaSize;
				}
		
			}
		catch(Exception e) {
			e.printStackTrace();
		}
	  }
	}
	
	//Method to return list of moved files
	public List<String> getMovedFiles(){
		return movedFiles;
	}
		
	//Method to return list of deleted files
	public List<String> getDeletedFiles(){
		return deletedFiles;
	}
	
	//Method to return list of archived files
	public List<String> getArchivedFiles(){
		return archivedFiles;
	}
}

