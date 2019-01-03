package extraction;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Extract {
	private List<Data> dataList;
	private List<Task> taskList;
	//private List<Resource> resourceList;
	private List<Project> projectList;
	
	public Extract(List<Data> dataList, List<Task> taskListFromInput){
		this.dataList = dataList;
		this.taskList = taskListFromInput;
		//this.resourceList = new ArrayList<Resource>();
		this.projectList = new ArrayList<Project>();
	}
	
//GET RESOURCE
//	public void extractResource(){
//		int flag1; //resource does not exist
//		int flag2; //resource's skill does not exit
//		for(Data d: dataList){
//			flag1=0;
//			for(Resource r:resourceList){
//				if(r.getName().equals(d.getResourceName())){
//					flag1=1; //resource exists
//					flag2=0;
//					for(String t:r.getTaskList()){
//						if(d.getTaskName().equals(t)){
//							flag2=1; //resource skill exists
//							break;
//						}
//					}
//					if(flag2==0) r.addSkill(d.getTaskName(),d.getResourceCapacity());
//					break;
//				}
//			}
//			if(flag1==0) resourceList.add(new Resource(d.getResourceName(),d.getTaskName(),d.getResourceCapacity()));
//		}
//	}
	
//1 PRE-PROCESSING
	public void extractTask(){
		for(Data d: dataList){
			d.setReworkFromInLog();
			int flag=0;
			for(Project p: projectList){
				if(d.getProjectName().equals(p.getName())){
					flag=1;
					p.addData(d);
					break;
				}
			}
			if(flag==0) this.projectList.add(new Project(d.getProjectName(),d));
		}
		for(Project p: projectList){
			p.setTaskList(this.taskList);
//			p.countOccurrenceNumber();
			p.combineInterruptData();
// SEPERATE DATA: loop until no change happens
//			boolean ifChanged;
//			while(true){
//				p.countOccurrenceNumber();
//				ifChanged = p.seperateByRework();
//				if(!ifChanged) break;
//			}
//			p.recordReworkTask();
		}
		keepConsistency();
	}

//2 EXTRACT UNCERTAINTY
	public void extractUncertainty(){
	//2.1 MINIMAL MAN-HOUR
		for(int i=1;i<100;i++) calMWAInEachOccurrence(i);
	//2.2 DELAY
		for(Task t: this.taskList){
			//storage map<occurrence number, list of ceil(additional work amount)>
			Map<Integer,List<Integer>> additionalWorkAmountMap = new HashMap<Integer,List<Integer>>();
			for(Data d: this.dataList){
				if(d.getTaskName().equals(t.getName())){
					int additionalWorkAmount = (int)Math.ceil(d.getWorkAmount()-t.getMinimumWorkAmount(d.getOccurrenceNumberInProject()));
					if(additionalWorkAmount<0) additionalWorkAmount=0;
					if(additionalWorkAmountMap.containsKey(d.getOccurrenceNumberInProject())){
						additionalWorkAmountMap.get(d.getOccurrenceNumberInProject()).add(additionalWorkAmount);
					}else{
						List<Integer> tmp = new ArrayList<Integer>();
						tmp.addAll(Arrays.asList(additionalWorkAmount));
						additionalWorkAmountMap.put(d.getOccurrenceNumberInProject(),tmp);
					}
				}
			}
			//calculation
			int i=1; //occurrence number
			Delay delay = new Delay();
			while(additionalWorkAmountMap.containsKey(i)){
				List<Integer> delayList = additionalWorkAmountMap.get(i);//list of additional work amount in occurrence time i
				Collections.sort(delayList);
				double label = 1;
				double tmp = delayList.get(delayList.size()-1)+1;
				for(int j= delayList.size()-1;j>=0;j--){
					if(delayList.get(j)<tmp) delay.addNewValue(i, label, delayList.get(j));
					label-=(double)1/delayList.size();
					tmp=delayList.get(j);
				}
				i++;
			}
			t.setDelay(delay);
		}
		 //<<<<<PRINT FOR TEST>>>>>
		System.out.println("Defualt");
		for(Task t: this.taskList){
			System.out.println(t.getName());
			for(int i : t.getMinimumWorkAmountMap().keySet()){
				System.out.println("   "+i+"  "+t.getMinimumWorkAmount(i));
			}
		}
		System.out.println("Delay");
		for(Task t: this.taskList){
			System.out.println(t.getName());
			Delay test = t.getDelay();
			test.print();
		}
		
	//2.3 REWORK
		for(Data d: this.dataList) {
			if(this.getTaskByName(d.getTaskName()).getMinimumWorkAmount(d.getOccurrenceNumberInProject()) > 0){
				int pwe10 = (int)Math.floor(d.getWorkAmount()/this.getTaskByName(d.getTaskName()).getMinimumWorkAmount(d.getOccurrenceNumberInProject())*10);
				d.setProgressWhenEnd10(pwe10);
			}else{
				d.setProgressWhenEnd10(-10);
			}
			
		}
		for(Task t : this.taskList){
			//storage: occurrence time, progress, reworkDes (including "none")
			List<ReworkCalc> reworkCalcList = new ArrayList<ReworkCalc>();
			for(Data d : this.dataList){
				if(d.getTaskName().equals(t.getName())){
					reworkCalcList.add(new ReworkCalc(d.getOccurrenceNumberInProject()
							,d.getProgressWhenEnd10(), d.getReworkFromInLog()));
				}
			}
			//calculation
			int i=1;
			Rework rework = new Rework();
			boolean ifContinue=true; //if occurrence number exist
			boolean ifProgress=true; //if there is a log of progress>0
			while(ifContinue){ //i. in every occurrence time
				ifContinue=false;
				for(ReworkCalc r1: reworkCalcList){
					if(r1.getOccurrenceNumber()==i){
						Map<Integer, Integer> OverProgressCount = new HashMap<Integer, Integer>();
						for(int p=-10;p<=200;p++){ //ii. count number of > every progress(0~20)
							OverProgressCount.put(p, 0);
							ifProgress = false;
							for(ReworkCalc r2: reworkCalcList){
								if(r2.getProgressWhenEnd10()>0) ifProgress=true;
								if(r2.getOccurrenceNumber()==i && r2.getProgressWhenEnd10()>=p){
									OverProgressCount.put(p, OverProgressCount.get(p)+1);
								}
							}
							if(!ifProgress) OverProgressCount.put(-1, -1);
						}
						
						for(ReworkCalc r2: reworkCalcList){
							String tmpNext=r2.getNext();
							if(t.ifDependentTask(tmpNext)){
								int tmpPwe10=r2.getProgressWhenEnd10();
								int count=0;
								for(ReworkCalc r3: reworkCalcList){
									if(r3.getOccurrenceNumber()==i && r3.getNext().equals(tmpNext) 
											&& r3.getProgressWhenEnd10()==tmpPwe10) count++;
								}
								if(count>0){
									if(!OverProgressCount.containsKey(tmpPwe10)){
										System.out.println("Exceptional progress:" + tmpPwe10);
									}else{
										if(tmpPwe10<0){
											rework.addNewValue(i, (double)tmpPwe10/10, 1.0 , this.getTaskByName(tmpNext));
										}else{
											rework.addNewValue(i, (double)tmpPwe10/10, (double)count/OverProgressCount.get(tmpPwe10), this.getTaskByName(tmpNext));
										}
									}
								}
							}
						}
						ifContinue=true;
						i++;
						break;
					}
				}
			}
			t.setRework(rework.duplicate());
		}
		//<<<<<PRINT FOR TEST>>>>>
		System.out.println("Rework");
		for(Task t: this.taskList){
			System.out.println(t.getName());
			Rework test = t.getRework();
			test.print();
		}
		
	}
	
	private Task getTaskByName(String name){
		for(Task t: this.taskList){
			if(t.getName().equals(name)) return t;
		}
		return null;
	}
	
	private void keepConsistency(){
		this.dataList.clear();
		for(Project p: projectList){
			for(Data d : p.getDataList()) this.dataList.add(d);
		}
		List<Task> duplicateList = new ArrayList<Task>();
		int fDupli;
		for(Task t: this.taskList){
			fDupli=0;
			for(Task td: duplicateList) if(td.getName().equals(t.getName())) fDupli=1;
			if(fDupli==0) duplicateList.add(t);
		}
		this.taskList=duplicateList;
	}
	
	private void calMWAInEachOccurrence(int i){
		boolean[] ifInit = new boolean[taskList.size()];//ifInit[] <---> taskList
		Arrays.fill(ifInit, false);
		double wa=0.0;
		for(Data d: this.dataList){
			Task t = this.getTaskByName(d.getTaskName());
			wa = d.getWorkAmount(); 
			if(d.getOccurrenceNumberInProject()==i && !ifInit[taskList.indexOf(t)] && d.getReworkFromInLog().equals("None") && wa>0){
				t.addMinimumWorkAmount(i,wa);
				ifInit[taskList.indexOf(t)]=true;
			}else if(d.getOccurrenceNumberInProject()==i && d.getReworkFromInLog().equals("None")){
				if(t.getMinimumWorkAmount(i) > wa && wa>0) t.addMinimumWorkAmount(i,wa);	
			}
		}
	}
	
	public void saveAsFile(String filePath){
		try{
			PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(filePath)));
			pw.println("<?xml version=\"1.0\"?>");
			pw.println("<!--Extracted Result-->");
			
			pw.println("<NodeElementList>");
			for(Task t: this.taskList){
				pw.println("	<TaskNode>");
				
				pw.println("		<Name>"+t.getName()+"</Name>");
				
				for(int i : t.getMinimumWorkAmountMap().keySet()){
					pw.println("		<MinimumWorkAmount>"+i+","+t.getMinimumWorkAmount(i)+"</MinimumWorkAmount>");
				}
				
				Delay d = t.getDelay();
				for(int i=1; i<=d.getMaxOccurrenceNumber();i++){
					Map<Double, Integer> map = d.getDelayMap(i);
					double p = 1.0;
					int previousKey = 0;
					for(Double delayKey: map.keySet()){
						p = p-delayKey;
						if(previousKey!=0)
							pw.println("		<Delay>"+i+","+p+","+previousKey+"</Delay>");
						previousKey = map.get(delayKey);
						p = delayKey;
					}
				}
				
				Rework r = t.getRework();
				
				for(int i=0;i<r.getOccurrenceNumberList().size();i++){
					pw.println("		<Rework>"+r.getOccurrenceNumberList().get(i)+","+r.getProgressList().get(i)+","
							+r.getNoReworkPossibilityList().get(i)+","+r.getReworkFromList().get(i).getName()+"</Rework>");
				}
				
				pw.println("	</TaskNode>");
			}
			pw.println("</NodeElementList>");
			
			pw.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	
	public List<Task> getTaskList(){
		return this.taskList;
	}
}
