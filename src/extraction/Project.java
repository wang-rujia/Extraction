package extraction;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Project {
	private List<Data> dataList;
	private List<Task> taskList;
	private String name;
	
	public Project(String name, Data newData){
		this.dataList = new ArrayList<Data>();
		this.dataList.add(newData);
		this.taskList = new ArrayList<Task>();
		this.name=name;
	}
	
	public void addData(Data d){
		this.dataList.add(d);
	}
	
	public void addData(String id, String project, String task, String st, String et, String r, String rc, String dt, String dtype, int oc){
		Data d=new Data(id,project,task,st,et,r,rc,dt,dtype);
		d.setOccurrenceNumberInProject(oc);
		this.dataList.add(d);
	}
	
	public void removeDataById(String id){
		int a=-1;
		for(Data d: dataList){
			if(d.getId().equals(id)){
				a=dataList.indexOf(d);
				break;
			}
		}
		dataList.remove(a);
	}
	
	private Task getTaskByName(String name){
		for(Task t: this.taskList){
			if(t.getName().equals(name)){
				return t;
			}
		}
		return null;
	}
	
//SEPERATE REWORK FOR FINISH-START DEPENDENCY TYPE (Project.taskList)
	public boolean seperateByRework(){
		for(Data d1: dataList){
			Task t1 = this.getTaskByName(d1.getTaskName());
			for(Data d2: dataList){	
				Task t2 = this.getTaskByName(d2.getTaskName());
				if(t1.ifFSTask(t2) && 
						d2.getST()<d1.getET() && 
						d1.getOccurrenceNumberInProject()<d2.getOccurrenceNumberInProject()){
					if(d2.getST()>d1.getST() && d2.getET()<d1.getET()){
						this.addData(UUID.randomUUID().toString(),this.getName(),d1.getTaskName(),
								String.valueOf(d2.getET()+1),String.valueOf(d1.getET()),
								d1.getResourceName(),String.valueOf(d1.getResourceCapacity()),
								d1.getDependentTask(),d1.getDependencyType(), d1.getOccurrenceNumberInProject()+1);
						d1.setET(d2.getST()-1);
						return true;
					}else if(d2.getST()<d1.getST()){
						d1.setST(d2.getET()+1);
						return true;
					}else if(d2.getET()>d1.getET()){
						d1.setET(d2.getST()-1);
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public void countOccurrenceNumber(){
		//sort by d.ST
		dataList.sort((d1,d2) -> {
			int st1 = d1.getST();
			int st2 = d2.getST();
			return Integer.compare(st1, st2);
		});
		//count occurrence number
		int tcount;
		int[] count = new int[taskList.size()];
		for(Data d : dataList){
			tcount=0;
			for(Task t: taskList){
				tcount++;
				if(d.getTaskName().equals(t.getName())){
					count[tcount-1]++;
					d.setOccurrenceNumberInProject(count[tcount-1]);
					break;
				}
			}
		}
	}
	
	public void recordNextTask(){
		//sort data by start time
		dataList.sort((d1,d2) -> {
			int st1 = d1.getST();
			int st2 = d2.getST();
			return Integer.compare(st1, st2);
		});
		//record next task
		for(Data d : dataList){
			if(dataList.indexOf(d)<dataList.size()-1) {
				d.setNextTask(dataList.get(dataList.indexOf(d)+1).getTaskName());
			}
		}
		
	}
	
	
	public String getName(){
		return this.name;
	}
	
	public List<Data> getDataList(){
		return this.dataList;
	}
	
	public List<Task> getTaskList(){
		return this.taskList;
	}
	
	public void setTaskList(List<Task> t){
		this.taskList=t;
	}
	
}
