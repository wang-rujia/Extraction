package extraction;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Delay {
	private List<Integer> occurrenceNumber;
	private List<Double> label;
	private List<Integer> additionalMH;
	
	public Delay(){
		occurrenceNumber = new ArrayList<Integer>();
		label = new ArrayList<Double>();
		additionalMH = new ArrayList<Integer>();
	}
	
	public void addNewValue(int a, double b, int c){
		occurrenceNumber.add(a);
		label.add(b);
		additionalMH.add(c);
	}
	
	public Map<Double, Integer> getDelayMap(int oc){
		Map<Double, Integer> m = new LinkedHashMap<Double, Integer>();
		for(int i=0; i<occurrenceNumber.size();i++){
			if(occurrenceNumber.get(i)==oc){
				m.put(label.get(i), additionalMH.get(i));
			}
		}
		return m;
	}
	
	//<<<<<TEST FOR PRINT>>>>>
	public void print(){
		for(int i=0; i<occurrenceNumber.size();i++){
			System.out.println("   "+occurrenceNumber.get(i)+"  "+label.get(i)+"  "+additionalMH.get(i));
		}
		System.out.println();
	}
}
