package extraction;

public class ReworkCalc {

	private int occurrenceNumber;
	private int progressWhenEnd10;
	private String next;
	
	public ReworkCalc(int occurrenceNumber, int progress10, String next){
		this.occurrenceNumber=occurrenceNumber;
		this.progressWhenEnd10=progress10;
		this.next=next;
	}
	
	public int getOccurrenceNumber(){
		return this.occurrenceNumber;
	}
	
	public int getProgressWhenEnd10(){
		return this.progressWhenEnd10;
	}
	
	public String getNext(){
		return this.next;
	}
	
}
