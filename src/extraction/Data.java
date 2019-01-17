package extraction;

import java.util.Random;

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
	private String reworkFromInLog;
	
	//extracted information
	private int occurrenceNumberInProject;
	private String reworkTask;
	private int progressWhenEnd10;
	
	//For recording next task
	public boolean recordedAsReworkDes;
	
	public Data(String id, String project, String task, String st, String et, String r, String rc, String dt,String rf){
		this.dataId=id;
		this.project = project;
		this.task=task;
		this.st=Integer.parseInt(st);
		this.et=Integer.parseInt(et);
		this.resource=r;
		this.resourceCapacity=Double.parseDouble(rc);
		this.dependentTask=dt;
		this.occurrenceNumberInProject=0;
		this.reworkTask="none";
		this.progressWhenEnd10=0;
		this.recordedAsReworkDes = false;
		this.reworkFromInLog = rf;
	}
	
	public void setReworkFromInLog(){
		String re[] = reworkFromInLog.split(";");
		if(re.length>1){
			double unit = (double)(1.0/re.length);
			Random rand = new Random();
			double p=rand.nextDouble();
			int count=0;
			for(double a=unit;a<=1;a+=unit){
				if(p<a) reworkFromInLog=re[count];
				count++;
			}
		}
	}
	
	
	public String getReworkFromInLog(){
		return reworkFromInLog;
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
		double d = (this.et-this.st) * this.resourceCapacity * 0.01;
		return d;
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
