package extraction;

public class Data {
	
	//data content
	private String dataId;
	private String project;
	private String task;
	private int st;
	private int et;
	private String resource;
	private double resourceCapacity;
	private String dependentTask;
	private String dependencyType; //FF,FS,SS,SF,NONE
	
	//extracted information
	private int occurrenceNumberInProject;
	private String reworkTask;
	private int progressWhenEnd10;
	
	public Data(String id, String project, String task, String st, String et, String r, String rc, String dt, String dtype){
		this.dataId=id;
		this.project = project;
		this.task=task;
		this.st=Integer.parseInt(st);
		this.et=Integer.parseInt(et);
		this.resource=r;
		this.resourceCapacity=Double.parseDouble(rc);
		this.dependentTask=dt;
		this.dependencyType=dtype;
		this.occurrenceNumberInProject=0;
		this.reworkTask="none";
		this.progressWhenEnd10=0;
	}
	
	public String getId(){
		return this.dataId;
	}
	
	public String getResourceName(){
		return this.resource;
	}
	
	public double getResourceCapacity(){
		return this.resourceCapacity;
	}
	
	public String getTaskName(){
		return this.task;
	}
	
	public double getWorkAmount(){
		double d = (this.et-this.st+1) * this.resourceCapacity;
		return d;
	}
	
	public String getDependencyType(){
		return this.dependencyType;
	}
	
	public String getDependentTask(){
		return this.dependentTask;
	}
	
	public int getST(){
		return this.st;
	}
	
	public int getET(){
		return this.et;
	}
	
	public String getProjectName(){
		return this.project;
	}
	
	public int getOccurrenceNumberInProject(){
		return this.occurrenceNumberInProject;
	}
	
	public int getProgressWhenEnd10(){
		return this.progressWhenEnd10;
	}
	
	public String getReworkTask(){
		return this.reworkTask;
	}
	
	public void setOccurrenceNumberInProject(int o){
		this.occurrenceNumberInProject=o;
	}
	
	public void setET(int et){
		this.et=et;
	}
	
	public void setST(int st){
		this.st=st;
	}
	
	public void setProgressWhenEnd10(int d){
		this.progressWhenEnd10=d;
	}
	
	public void setReworkTask(String t){
		this.reworkTask=t;
	}
	
}
