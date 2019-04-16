import java.io.File;


public class Report {
	
	private String fileName =null;
	private String filePath =null;
	private String parentFolder = null;
	private File   reportFile = null;
	private long   age = 0;
	private long   size=0;
	private String format =null;
	
	public Report(String fileName){
		setFileName(fileName);
		if(this.reportFile.exists()){
			setFilePath();
			setFileName();
			setParentFolder();
			setSize();
			setAge();
			setFormat();			
		}
	}
	public Report(File reportFile){
		setFileName(reportFile.getAbsolutePath());
		if(this.reportFile.exists()){
			setFilePath();
			setFileName();
			setParentFolder();
			setSize();
			setAge();
			setFormat();			
		}
	}
	
    private void setFileName(String fileName){
    	try {
    		File tempFile = new File(fileName.replace("\\","/"));
    		if(tempFile.exists())this.reportFile =tempFile;
		}
	catch (Exception e)
		{
			e.printStackTrace();
		}
    }
    private void setFilePath(){
    	this.filePath = this.reportFile.getAbsolutePath();  	
    }
    private void setFileName(){
    	this.fileName  = this.reportFile.getName();
    }
    private void setParentFolder(){
    	this.parentFolder=this.reportFile.getParentFile().getAbsolutePath();
    }
    private void setSize(){
    	this.size= this.reportFile.length();
    }
    private void setAge(){
    	long currentTime = System.currentTimeMillis();
    	long sourceFileAge = this.reportFile.lastModified();
    	long timeDiff = currentTime  - sourceFileAge;
		long  numOfDays = timeDiff/(24*60*60*1000);
    	this.age=numOfDays ;
		this.age=this.age==0?1:this.age;
    }
    private void setFormat(){
	
    	String extension = this.fileName.substring(this.fileName.lastIndexOf('.')+1);
		
    	if(extension.equalsIgnoreCase("csv")){
    		this.format ="CSV";
    	}else if(extension.equalsIgnoreCase("pdf")){
    		this.format ="PDF";
    	}else if(extension.equalsIgnoreCase("txt")){
    		this.format ="TEXT";
    	}else if(extension.equalsIgnoreCase("xls") || extension.equalsIgnoreCase("xlsx")){
    		this.format ="EXCEL";
    	}else{
			this.format = "UNSUPPORTED";
		}
    }
    public File getFile(){
		return this.reportFile;
	}
    public String getName(){
    	return this.fileName;    	
    } 
    public String getPath(){
        return this.filePath;
    }
	public boolean exists(){
	 return this.reportFile.exists();
	}
    public String getParentFolder(){
    	return this.parentFolder;
    }
    public long getAge(){
    	return this.age;	
    } 
    public String getFormat(){
    	return this.format;
    }
    public long getSize(){
    	return this.size;    	
    }
    public boolean delete(){
    	boolean deleted =false;
    	deleted = getFile().delete();
    	return deleted;
    	
    }
}
