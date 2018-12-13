package extraction;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReadData {
	public List<Data> dataList;
	
	public ReadData(){
		dataList = new ArrayList<Data>();
	}
	
	public void read(String path){
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File(path)));
			String line = br.readLine();
			while(line!=null){
				String[] lineElement = line.split(",");
				this.dataList.add(new Data(lineElement[0],lineElement[1],lineElement[2],lineElement[3],lineElement[4],lineElement[5],lineElement[6],
						lineElement[7]));
				line=br.readLine();
			}
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
