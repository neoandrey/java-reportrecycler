import java.util.Scanner;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.HashMap;
import java.util.Map;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.ArrayList;

public class ReportCopy{
	  public String reportsPath  = null;
	  public String folderString   = null;
	  public  String copyString   = null;
	  public Report   reportFile  = null;
	  public Set<Report> reportFileList = new HashSet<Report>();
	  public File outputFile =  null;
	  public File destinationFolder  = null;
	  public File reportDirectory;
      public FileWriter fileWriter =  null;
      public PrintWriter printWriter =  null;
	  public Report tempReport;
      public static  SimpleDateFormat dateFormat = new SimpleDateFormat("dd, MMM,yyyy HH:mm:ss");
      public static final String startCopyrLog = "log\\ReportCopy.log";
	  public static  ArrayList<String> allowedFormats =  new ArrayList();
	  static final String  ERROR_MARGIN = "\t\t\t\t\t\t\t\t\t\t\t";
		public ReportCopy(){
		
		}
		public ReportCopy(String reportDir, String folderStr, String copyStr){
			initLogFile();
			System.out.println("\nReports Folder: "+reportDir);
			System.out.println("\n File copy identifier File:"+folderStr);
			System.out.println("\n Copy string:"+copyString);
			printWriter.println("\nReports Folder: "+reportDir);
			printWriter.println("\n File copy identifier File:"+folderStr);
			setReportsPath(reportDir);
			setCopyFolder(folderStr);
			setCopyString(copyStr);
			if(!getDestinationFolderPath().isEmpty()){
				getAllowedFormats();
				readSourceDir();
				startCopy();
			}else{
			     
				 printWriter.println("Please specify location where file are to be copied.");
				 System.out.println("Please specify string identifier for deletion.");
				 System.out.println("Exiting...");
				 printWriter.println("Exiting...");
			}
			close();
		}
		
		public void getAllowedFormats(){
			allowedFormats.add("CSV");
			allowedFormats.add("PDF");
			allowedFormats.add("EXCEL");
			allowedFormats.add("TEXT");
		}

		 public  String getErrorMessage(Exception e){
    	
			StringBuilder message= new StringBuilder();
			message.append(e.getMessage()).append("\n"+ERROR_MARGIN);
			StackTraceElement[] errorStack = e.getStackTrace();
			for (int i = 0; i < errorStack.length; i++){
			   message.append(errorStack[i].toString()).append("\n"+ERROR_MARGIN);
			}
			String errorMessage = message.toString();
			message.delete(0, message.length());
			return errorMessage;
		}

		  public String getThrownMessage(Throwable e){
			  
			StringBuilder message= new StringBuilder();
			StackTraceElement[] errorStack = e.getStackTrace();
			for (int i = 0; i < errorStack.length; i++){
			   message.append(errorStack[i].toString()).append("\n");
			}
			String errorMessage = message.toString();
			message.delete(0, message.length());
			return errorMessage;
			
		}
			
	   public void readSourceDir(){
 	     try{
 	    	 File reportDirectory  = new File(getReportsPath());
 	    	 if(reportDirectory.exists()){
					readDirectory(reportDirectory);
 	    	 }else{
 	    		 printWriter.println("Source directory does not exist.");
 	    		 printWriter.println("Please provide a valid source directory and rerun application.");
 	    		 printWriter.println("Exiting...");
				 System.out.println("Source directory does not exist.");
 	    		 System.out.println("Please provide a valid source directory and rerun application.");
 	    		 System.out.println("Exiting...");
 	    		 close(); 
 	    	 }
 	      } catch (Exception e){

 	    	 printWriter.println("Error reading files in source directory: "+ getErrorMessage(e));
			 printWriter.println( getErrorMessage(e));
			  System.out.println("Error reading files in source directory: "+ getErrorMessage(e));
			  System.out.println( getErrorMessage(e));
			 System.out.println( getErrorMessage(e));
 	     }
 	  
 	  }
	  public String getReportsPath(){
			return this.reportsPath;
	  
	  }
	  public void setReportsPath(String reportsDir){
			this.reportsPath = reportsDir;
	  }  
	  public String getDestinationFolderPath(){
		return this.destinationFolder.getAbsolutePath();
	  }
	  public void setCopyString(String copyStr){
		  this.copyString = copyStr;
		  
	  }
	  public String getCopyString(){
		  return this.copyString;
		  
	  }
	  public void setCopyFolder(String folderStr){
	  
	  try{
		  File tmpFile =   new File(folderStr);
		if(tmpFile.exists()){
			destinationFolder = tmpFile; 
		}else{
			System.out.println("Destination folder does not exist or access is denied");
			System.exit(0);
		}
	  }catch(Exception e){
		e.printStackTrace();
	  
	  }
			
	  }
 	  public void startCopy(){
 		   Report tempReport = null;
 		   Report RptFile = null;
 		   Iterator<Report> reportItr = reportFileList.iterator();
 		   while(reportItr.hasNext()){
 			  RptFile = reportItr.next();
 			  if(RptFile.exists()){
 				try{
 				  if(copyReport(RptFile)){
					printWriter.println(RptFile.getPath()+" has been successfully copied to: "+destinationFolder.getAbsolutePath());
					System.out.println(RptFile.getPath()+" has been successfully copied to: "+destinationFolder.getAbsolutePath());
 				  }else{
					printWriter.println(RptFile.getPath()+" could not be copied. Please ensure that file is not in use.");
					System.out.println(RptFile.getPath()+" could not be copied. Please ensure that file is not in use.");
				  }
 				} catch(Exception e){
					printWriter.println("Errors deleting log file: "+ getErrorMessage(e));
					printWriter.println( getErrorMessage(e));
					System.out.println( getErrorMessage(e));
 				}
 			  }

 		   }


 	   }
	   
	   public boolean copyReport(Report rptFile ){
		    boolean hasCopied = false;
			String reportPath = rptFile.getPath().toLowerCase();
			int bankReportsInd = reportPath.indexOf("\\",reportPath.indexOf("bank"));
			int lastBackSlashIndex = reportPath.lastIndexOf("\\");
			String repFolder = reportPath.substring((bankReportsInd+1),lastBackSlashIndex )+"\\"+rptFile.getName();
			String targetPath ="\""+destinationFolder.getAbsolutePath()+"\\"+repFolder+"\"";
			String copyCommand = "xcopy /J /y \""+rptFile.getPath()+"\"  "+targetPath;
		   try{
			   System.out.print("Running command: "+copyCommand);
			final Process p = Runtime.getRuntime().exec(copyCommand);
  
				new Thread(new Runnable() {
					public void run() {
					 BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
					 String line = null; 

					 try {
						while ((line = input.readLine()) != null)
							System.out.println(line);
					 } catch (IOException e) {
							e.printStackTrace();
					 }
					}
				}).start();
				hasCopied =true;
				p.waitFor();
				} catch(Exception e){
					e.printStackTrace();
					hasCopied =false;
				}
				return hasCopied;
	   
	   }
	   public void readDirectory(File reportDir){
			try{
				 File[] reportFiles =  reportDir.listFiles();
				 int size  =reportFiles.length;
				 for(int i=0; i< size; i++){
						 if(reportFiles[i].isFile()){
							   tempReport = new Report(reportFiles[i]);
							   System.out.println("Processing file \""+tempReport.getPath()+"\"");;
								if (allowedFormats.contains(tempReport.getFormat()) &&  tempReport.getName().toLowerCase().contains(getCopyString().toLowerCase()) ){
								   this.reportFileList.add(tempReport);
								   System.out.println("Adding \""+tempReport.getPath()+"\" to list of files to be copied.");
								   printWriter.println("Adding \""+tempReport.getPath()+"\" to list of files to be copied.");
								  }
						 } else if(reportFiles[i].isDirectory()){
							 System.out.println("Now scanning \""+reportFiles[i].getAbsolutePath()+"\" for reports to be copied.");
							 printWriter.println("Now scanning \""+reportFiles[i].getAbsolutePath()+"\" for reports to be copied.");
							 this.readDirectory(reportFiles[i]);
						 }
					 }
			} catch (Exception e){
				 printWriter.println("Error reading file: "+ getErrorMessage(e));
				 printWriter.println( getErrorMessage(e));
				 System.out.println( getErrorMessage(e));
			}
		  

	 	  }
		  
		  	public void initLogFile(){
 
 	         try{
 
 				 outputFile =  new File(startCopyrLog);
 			 if(outputFile.exists()) outputFile.delete();
 				 outputFile.createNewFile();
 	             fileWriter =  new FileWriter(outputFile);
 	             printWriter =  new PrintWriter(fileWriter);
 
 	          }catch(Exception e){
					printWriter.println("Error creating log file: "+ getErrorMessage(e));
					System.out.println("Error creating log file: "+ getErrorMessage(e));
					printWriter.println( getErrorMessage(e));
					System.out.println( getErrorMessage(e));
 			  }
 
 		}
 	    
 		public void closeLogFile(){
 
 	         try{
 				  outputFile = null;
 				  printWriter.close();
 				  fileWriter.close();
 				  System.gc();
				  
 				 }catch(Exception e){
				 
					printWriter.println("Error closing log file: "+ getErrorMessage(e));
					System.out.println("Error closing log file: "+ getErrorMessage(e));
					printWriter.println( getErrorMessage(e));
					System.out.println( getErrorMessage(e)); 
					
 				}

 
 	}
 	  
   
  public void close(){
  
 	 closeLogFile();
 	 System.exit(0);
	 
  }
  
   	public static void showUsage(){
	
 		System.out.println("Usage:  ReportCopy -s 'C:\\temp' -d '20150101' OR  ReportCopy -p" );
 		System.out.println("Where: ");
 		System.out.println("s: Source folder or report folder");
 		System.out.println("d: location where files are to be copied");
 		System.out.println("p: prompt");
 		System.exit(0);

  }
		  
		  
		public static void main (String[]args){
		
			String source = "";
			String folderStr = "";
			String prompt = "";
			String copyStr = "";


			if(args.length==0){
				 System.out.print("Please provide a source folder to process.");
				 System.out.print("Exiting...");
				 System.exit(0);

			}else{

 		int argsCount = args.length;
 		try{
 				for(int i=0; i< argsCount; i++){
 					args[i] = args[i].trim();
 					if(args[i].equalsIgnoreCase("-s")|| args[i].equalsIgnoreCase("/s")){
 						source =args[i+1].trim();
 						source =source.replace("\\","\\\\");
 						source =source.replace("\'","");
 						source =source.replace("\"","");
 						System.out.println("\nSource Directory: "+source );
 					}else 	if(args[i].equalsIgnoreCase("-d") || args[i].equalsIgnoreCase("/d") ){
 						folderStr =args[i+1].trim();
 						System.out.println("\nCopy destination:"+ folderStr );
 					}else 	if(args[i].equalsIgnoreCase("-p") || args[i].equalsIgnoreCase("/p") ){
 						try{ 
						source =null;
						folderStr = null;
						BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
						
						while(source == null || source.isEmpty()){
							System.out.println("Please type the location of the reports folder below: ");
							source =in.readLine();
						}
						source =source.replace("\\","\\\\");
 						source =source.replace("\'","");
 						source =source.replace("\"","");
						while(folderStr == null || folderStr.isEmpty()){
							System.out.println("Please type the location  where files are to be Copied below: ");
							folderStr =in.readLine();
						}
				    	folderStr =folderStr.replace("\\","\\\\");
 						folderStr =folderStr.replace("\'","");
 						folderStr =folderStr.replace("\"","");
						
						while(copyStr == null || copyStr.isEmpty()){
							System.out.println("Please type the identifier for files to be copied below: ");
							copyStr =in.readLine();
						}
						
						
						}catch(Exception e){
							
							e.printStackTrace(); 
						}
						
					} else if(args[i].equalsIgnoreCase("-h") || args[i].equalsIgnoreCase("/h") ){
							   showUsage();
					}
 		   }
 					 if(source.isEmpty() && folderStr.isEmpty()) {
 						showUsage();
 					  }else if(source.isEmpty() ){

 						 System.out.print("\nInvalid source folder specified");
 						 System.out.print("\nExiting...");
 						 System.exit(0);

 					 }else if(folderStr.isEmpty()){
 						 System.out.print("\nNo folder for file copy specified.");
 						 System.out.print("\nExiting...");
 						 System.exit(0);

 					 }
				}catch(Exception e){
						   e.printStackTrace();
						   showUsage();
				}
 	}

			
			new ReportCopy( source,  folderStr, copyStr);
			System.out.println("\n\nReports copy complete.\n Exiting....");
		
		}

}
