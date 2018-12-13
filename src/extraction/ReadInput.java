package extraction;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ReadInput {
	private List<Task> taskList;
	
	public ReadInput(){
		taskList = new ArrayList<Task>();
	}
	
	public void read(String path){
		Document xml = null;
		try{
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			xml = builder.parse(new FileInputStream(new File(path)));
			
			NodeList nodeList = xml.getElementsByTagName("TaskNode");
			for(int i=0;i<nodeList.getLength();i++){
				Task task = new Task();
				Node node = nodeList.item(i);
				NodeList tags = node.getChildNodes();
				for(int j=0;j<tags.getLength();j++){
					Node tag = tags.item(j);
					if(!tag.getNodeName().equals(null) && !tag.getNodeName().equals("#text")){
						String tagName = tag.getNodeName();
						String value = tag.getTextContent();
						if(tagName.equals("Project")) task.setProject(value);
						else if(tagName.equals("Name")) task.setName(value);
					}
				}
				this.taskList.add(task);
			}	

			nodeList = xml.getElementsByTagName("Dependency");			
			Node node = nodeList.item(0);
			NodeList tags = node.getChildNodes();
			for(int j=0;j<tags.getLength();j++){
				Node tag = tags.item(j);
				if(!tag.getNodeName().equals(null) && !tag.getNodeName().equals("#text")){
					String tagName = tag.getNodeName();
					if(tagName.equals("TaskDependency")) {
						NamedNodeMap attrs = tag.getAttributes();
						String subject = attrs.getNamedItem("subject").getNodeValue();
						//String type = attrs.getNamedItem("type").getNodeValue();
						String name = attrs.getNamedItem("name").getNodeValue();
						for(Task t1:this.taskList){
							if(t1.getName().equals(name))
								this.taskList.stream()
									.filter(t2 -> t2.getName().equals(subject))
									.forEach(t2 -> t2.addInputTask(t1));
						}
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public List<Task> getTaskList(){
		return this.taskList;
	}
	
}
