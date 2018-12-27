package extraction;

public class Run {
	public static void main(String[] args) {

		String fileName1 = "case2_input.xml";
		String fileName2 = "log_case2_1228.txt";

		final ReadInput readInput = new ReadInput();
		readInput.read("InputFiles/"+fileName1);

		final ReadData readData = new ReadData();
		readData.read("InputFiles/"+fileName2);
		
		Extract extract = new Extract(readData.dataList, readInput.getTaskList());
		//extraction.extractResource();
		extract.extractTask();
		extract.extractUncertainty();
		extract.saveAsFile("result1228.xml");
	}
	
}
