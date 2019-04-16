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

public class ReportRecycler{
	  public String reportsPath  = null;
	  public String deleteString   = null;
	  public Report   reportFile  = null;
	  public Set<Report> reportFileList = new HashSet<Report>();
	  public File outputFile =  null;
	  public File reportDirectory;
      public FileWriter fileWriter =  null;
      public PrintWriter printWriter =  null;
	  public Report tempReport;
      public static  SimpleDateFormat dateFormat = new SimpleDateFormat("dd, MMM,yyyy HH:mm:ss");
      public static final String recyclerLog = "log\\reportrecycler.log";
	  public static  ArrayList<String> allowedFormats =  new ArrayList();
	  static final String  ERROR_MARGIN = "\t\t\t\t\t\t\t\t\t\t\t";
		public ReportRecycler(){
		
		}
		public ReportRecycler(String reportDir, String delStr){
			initLogFile();
			System.out.println("\nReports Folder: "+reportDir);
			System.out.println("\n File removal identifier File:"+delStr);
			printWriter.println("\nReports Folder: "+reportDir);
			printWriter.println("\n File removal identifier File:"+delStr);
			setReportsPath(reportDir);
			setDeleteString(delStr);
			if(!getDeleteString().isEmpty()){
				getAllowedFormats();
				readSourceDir();
				recycle();
			}else{
			     
				 printWriter.println("Please specify string identifier for deletion.");
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
	  public String getDeleteString(){
		return this.deleteString;
	  }
	  public void setDeleteString(String delStr){
			this.deleteString  = delStr;
	  }
 	  public void recycle(){
 		   Report tempReport = null;
 		   Report RptFile = null;
 		   Iterator<Report> reportItr = reportFileList.iterator();
 		   while(reportItr.hasNext()){
 			  RptFile = reportItr.next();
 			  if(RptFile.exists()){
 				try{
 				  if( RptFile.delete()){
					printWriter.println(RptFile.getPath()+" has been successfully removed from the reports folder.");
					System.out.println(RptFile.getPath()+" has been successfully removed from the reports folder.");
 				  }else{
					printWriter.println(RptFile.getPath()+" could not be deleted. Please ensure that file is not in use.");
					System.out.println(RptFile.getPath()+" could not be deleted. Please ensure that file is not in use.");
				  }
 				} catch(Exception e){
					printWriter.println("Errors deleting log file: "+ getErrorMessage(e));
					printWriter.println( getErrorMessage(e));
					System.out.println( getErrorMessage(e));
 				}
 			  }

 		   }


 	   }
	   public void readDirectory(File reportDir){
			try{
				 File[] reportFiles =  reportDir.listFiles();
				 int size  =reportFiles.length;
				 for(int i=0; i< size; i++){
						 if(reportFiles[i].isFile()){
							   tempReport = new Report(reportFiles[i]);
							   System.out.println("Processing file \""+tempReport.getPath()+"\"");;
								if (allowedFormats.contains(tempReport.getFormat()) &&  tempReport.getName().toLowerCase().contains(this.deleteString.toLowerCase()) ){
								   this.reportFileList.add(tempReport);
								   System.out.println("Adding \""+tempReport.getPath()+"\" to list of files to be removed.");
								   printWriter.println("Adding \""+tempReport.getPath()+"\" to list of files to be removed.");
								  }
						 } else if(reportFiles[i].isDirectory()){
							 System.out.println("Now scanning \""+reportFiles[i].getAbsolutePath()+"\" for reports to be removed.");
							 printWriter.println("Now scanning \""+reportFiles[i].getAbsolutePath()+"\" for reports to be removed.");
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
 
 				 outputFile =  new File(recyclerLog);
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
	
 		System.out.println("Usage:  ReportRecycler -s 'C:\\temp' -d '20150101' OR  ReportRecycler -p" );
 		System.out.println("Where: ");
 		System.out.println("s: Source folder or report folder");
 		System.out.println("d: Identifier for files to be deleted");
 		System.out.println("p: prompt");
 		System.exit(0);

  }
		  
		  
		public static void main (String[]args){
		
			String source = "";
			String deleteStr = "";
			String prompt = "";


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
 						deleteStr =args[i+1].trim();
 						System.out.println("\nDelete identifier:"+ deleteStr );
 					}else 	if(args[i].equalsIgnoreCase("-p") || args[i].equalsIgnoreCase("/p") ){
 						try{ 
						source =null;
						deleteStr = null;
						BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
						
						while(source == null || source.isEmpty()){
							System.out.println("Please type the location of the reports folder below: ");
							source =in.readLine();
						}
						source =source.replace("\\","\\\\");
 						source =source.replace("\'","");
 						source =source.replace("\"","");
						while(deleteStr == null || deleteStr.isEmpty()){
							System.out.println("Please type the identifier for files to be deleted below: ");
							deleteStr =in.readLine();
						}
						
						}catch(Exception e){
							
							e.printStackTrace(); 
						}
						
					} else if(args[i].equalsIgnoreCase("-h") || args[i].equalsIgnoreCase("/h") ){
							   showUsage();
					}
 		   }
 					 if(source.isEmpty() && deleteStr.isEmpty()) {
 						showUsage();
 					  }else if(source.isEmpty() ){

 						 System.out.print("\nInvalid source folder specified");
 						 System.out.print("\nExiting...");
 						 System.exit(0);

 					 }else if(deleteStr.isEmpty()){
 						 System.out.print("\nNo identifier for file removal specified.");
 						 System.out.print("\nExiting...");
 						 System.exit(0);

 					 }
				}catch(Exception e){
						   e.printStackTrace();
						   showUsage();
				}
 	}

			
			new ReportRecycler( source,  deleteStr);
			System.out.println("\n\nReports clean up complete.\n Exiting....");

		
		
		
		}

}
