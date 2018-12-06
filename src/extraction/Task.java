package extraction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Task {

	private String name;
	private String project;
	private Map<Integer,Double> minimumWorkAmount;
	private List<Task> FSTaskList;
	private Rework rework;
	private Delay delay;
	
	public Task(){
		this.name = "";
		this.FSTaskList = new ArrayList<Task>();
		this.minimumWorkAmount = new HashMap<Integer,Double>();
	}
	
	public Task(String n) {
		this.name = n;
		this.FSTaskList = new ArrayList<Task>();
		this.minimumWorkAmount = new HashMap<Integer,Double>();
	}
	

	
	public void addDependency(String dtype, Task t){
		if(dtype.equals("FS")) this.FSTaskList.add(t);
	}
	
	public boolean ifFSTask(Task t1){
		if(this.FSTaskList.isEmpty()) return false;
		for(Task t2: this.FSTaskList){
			if(t1.getName().equals(t2.getName())){
				return true;
			}
		}
		return false;
	}
	
	public String getProject(){
		return this.project;
	}
	
	public String getName(){
		return this.name;
	}
	
	public double getMinimumWorkAmount(int i){
		if(minimumWorkAmount.size()<i){
			for(int j=i; j>0; j--){
				if(minimumWorkAmount.size()>=j){
					System.out.println("cannot find correct minimum work amount, using mwa("+j+")");
					return this.minimumWorkAmount.get(j);
				}
			}
			System.out.println("cannot find correct minimum work amount, using 0");
			return 0;
		}else{
			return this.minimumWorkAmount.get(i);
		}
	}
	
	public Delay getDelay(){
		return this.delay;
	}
	
	public Rework getRework(){
		return this.rework;
	}
	
	public List<Task> getFSTaskList(){
		return this.FSTaskList;
	}
	
	public Map<Integer, Double> getMinimumWorkAmountMap(){
		return this.minimumWorkAmount;
	}
	
	public void setFSTaskList(List<Task> newList){
		this.FSTaskList=newList;
	}
	
	public void setDelay(Delay d){
		this.delay=d;
	}
	
	public void setRework(Rework r){
		this.rework=r;
	}
	
	public void setProject(String p){
		this.project=p;
	}
	
	public void setName(String n){
		this.name=n;
	}
	
	public void setMinimumWorkAmountMap(Map<Integer,Double> m){
		this.minimumWorkAmount=m;
	}
	
	public void addMinimumWorkAmount(int i, double wm){
		this.minimumWorkAmount.put(i, wm);
	}
	
	public boolean ifDependentTask(String task){
		if(this.getName().equals("C") && task.equals("A1")){
			System.out.println("checking");
		}
		for(Task t: this.FSTaskList){
			if(t.getName().equals(task)){
				return true;
			}else{
				if(t.ifDependentTask(task)==false){
					continue;
				}else{
					return true;
				}
			}
		}
		return false;
	}
	
}
