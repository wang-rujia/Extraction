package extraction;

public class Run {
	public static void main(String[] args) {

		String fileName1 = "case2_input.xml";
		String fileName2 = "log.txt";

		final ReadInput readInput = new ReadInput();
		readInput.read("InputFiles/"+fileName1);

		final ReadData readData = new ReadData();
		readData.read("InputFiles/"+fileName2);
		
		Extract extract = new Extract(readData.dataList, readInput.getTaskList());
		//extraction.extractResource();
		extract.extractTask();
		extract.extractUncertainty();
	}
	
}
