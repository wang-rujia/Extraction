package extraction;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Rework {
	private List<Integer> occurrenceNumber;
	private List<Double> progress;
	private List<Double> noReworkPossibility;
	private List<Task> reworkFrom;
	
	public Rework(){
		occurrenceNumber = new ArrayList<Integer>();
		progress = new ArrayList<Double>();
		noReworkPossibility = new ArrayList<Double>();
		reworkFrom = new ArrayList<Task>();
	}
	
	public void addNewValue(int a, double b, double c, Task d){
		occurrenceNumber.add(a);
		progress.add(b);
		noReworkPossibility.add(c);
		reworkFrom.add(d);
	}
	
	public Rework duplicate(){
		Rework dupli = new Rework();
		boolean ifExist;
		for(int i=0; i < this.occurrenceNumber.size();i++){
			ifExist = false;
			for(int j=0; j<dupli.getOccurrenceNumber().size();j++){
				if(this.occurrenceNumber.get(i)==dupli.getOccurrenceNumber().get(j) &&
					this.progress.get(i)>dupli.getProgress().get(j)-0.00001 && this.progress.get(i)<dupli.getProgress().get(j)+0.00001 &&
					this.reworkFrom.get(i).equals(dupli.getReworkFrom().get(j))) ifExist=true;
			}
			if(!ifExist)
				dupli.addNewValue(this.occurrenceNumber.get(i), this.progress.get(i), 
						this.noReworkPossibility.get(i), this.reworkFrom.get(i));
		}
		return dupli;
	}
	
	private List<Integer> getOccurrenceNumber(){
		return this.occurrenceNumber;
	}
	
	private List<Double> getProgress(){
		return this.progress;
	}
	
	private List<Task> getReworkFrom(){
		return this.reworkFrom;
	}
	
	public List<Double> getReworkP(int oc, double pro){
		List<Double> l = new ArrayList<Double>();
		for(int i=0;i<occurrenceNumber.size();i++){
			if(occurrenceNumber.get(i)==oc && progress.get(i)==pro) l.add(1-noReworkPossibility.get(i));
		}
		return l;
	}
	
	public Map<Double, Task> getReworkMap(int oc, double pro){
		Map<Double, Task> m = new LinkedHashMap<Double, Task>();
		double key=0;
		for(int i=0;i<occurrenceNumber.size();i++){
			if(occurrenceNumber.get(i)==oc && progress.get(i)==pro) {
				key += 1-noReworkPossibility.get(i);
				m.put(key, reworkFrom.get(i));
			}
		}
		return m;
	}
	
	public Task getReworkD(int oc, double pro){
		for(int i=0;i<occurrenceNumber.size();i++){
			if(occurrenceNumber.get(i)==oc && progress.get(i)==pro) return reworkFrom.get(i);
		}
		return null;
	}
	
	//<<<<<TEST FOR PRINT>>>>>
	public void print(){
		for(int i=0;i<occurrenceNumber.size();i++){
			System.out.println("  "+occurrenceNumber.get(i)+"  "+progress.get(i)+"  "
					+reworkFrom.get(i).getName()+"  "+noReworkPossibility.get(i));
		}
	}
	
}
