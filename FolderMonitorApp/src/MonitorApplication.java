package com.loki.fma;

class MonitorApplication {
	
	{
		MonitorDirectory.turnOffLogging();
	}

	public static void main(String[] args) {
		try {
			
			MonitorDirectory mdObj = new MonitorDirectory("tmp","secured","archived");
			
			Thread t1 = new Thread(mdObj);
			Thread t2 = new Thread(mdObj);
			
			t1.setName("movethread");
			t2.setName("archivethread");
			
			t1.start();
			t2.start();
			
			for(;;) {}
			
			/*DirectoryOperations dOps = new DirectoryOperations("tmp","secured","archived");
			
			dOps.moveFiles();
			
			dOps.archiveFiles(100);
			
			System.out.println("Size of securedDirectory = " + dOps.getSecDirSize());
			System.out.println("Number of archived files = " + dOps.getArchivedFiles().size());
			System.out.println("Nuber of deleted files = " + dOps.getDeletedFiles().size());
			dOps.displayLMTMap();
			
			dOps.archiveFiles(70);
			
			System.out.println("Size of securedDirectory = " + dOps.getSecDirSize());
			System.out.println("Number of archived files = " + dOps.getArchivedFiles().size());
			System.out.println("Nuber of deleted files = " + dOps.getDeletedFiles().size());
			dOps.displayLMTMap(); */
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
	}

}
