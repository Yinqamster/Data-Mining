import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instances;

public class file2arff {
	public static HashMap<String, Integer> map = new HashMap<String, Integer>();
	public static int[][]  res = new int[12000][2400];
//	public static List<List<Integer>> list = new ArrayList();
	public static int lineNum =0;
	
	
	 public static void readFromFile() throws IOException{
		 int value = 0;
	//	 int lineNum = 0;
		 for (int i = 0; i < 9; i++) {
			 String fileName = "USER" + i + "/sanitized_all.981115184025";
			 BufferedReader in = new BufferedReader(new FileReader(fileName));
			 String line = in.readLine();
			 
			 while(line !=null) {
				// System.out.println(line);
				 if(line.equals("**SOF**")) {
//					 count++;
					 line = in.readLine();
					 if(line.equals("**EOF**")) {
						 line = in.readLine();
						 continue;
					 }
					 while(!line.equals("**EOF**")) {
						 if(line.startsWith("<")&&line.endsWith(">")) {
							 if(line.indexOf(">")-line.indexOf("<")<=3){
								 line = in.readLine();
								 continue;
							 }
						 }
//						 if(!(line.startsWith("<")&&line.endsWith(">"))) {
//							 if(!(line.charAt(2)=='>' || line.charAt(3)=='>')) {
								 if(map.containsKey(line)) {
									 res[lineNum][map.get(line)] = 1;
								 }
								 else {
									 map.put(line, value);
									 value++;
									 res[lineNum][map.get(line)] = 1;
								 }
					//			 List<String> a1 = new ArrayList<String>();
//							 }
//						 }
						 
						 line = in.readLine();
					 }
					 lineNum++;
					 line = in.readLine();
				 }
			 }
//			 String st0 = in.readLine();
//			 System.out.println(st0);
		 }
		 System.out.println(map.size());
//		 Iterator<String> iter = map.keySet().iterator();
//		 for(int i = 0; i < 11113; i++) {
//			 for(int j = 0; j < 2358; j++) {
//				 System.out.print(res[i][j]);
//			 }
//			 System.out.println();
//		 }
	 }
	 public static Instances write2arff() throws IOException {
		 FastVector attInfo = new FastVector();
		 FastVector classes = new FastVector(2);
		 classes.addElement("0");
		 classes.addElement("1");
		 Iterator<String> iter = map.keySet().iterator();
		 String[] arr = new String[map.size()]; 
		for(int i = 0; i < map.size(); i++) {
			String key = iter.next();
			int value = map.get(key);
			arr[value] = key;
		}
			
//		System.out.println(arr.length);
        for(int i=0; i<map.size(); i++){
            Attribute att = new Attribute(arr[i],classes);
            
            attInfo.addElement(att);
        }
  //      System.out.println(attInfo.size());
        Instances instances = new Instances("UNIX_usage", attInfo, 0);
  //      instances.setClassIndex( instances.numAttributes() - 1);
        
        return instances;
	 }
	 public static void writeToArff() throws IOException{
		FileWriter filewriter = new FileWriter("UNIX_usage"+".arff");
		filewriter.write("@relation UNIX_usage\n");
		Iterator<String> iter = map.keySet().iterator();
		String[] arr = new String[map.size()]; 
		for(int i = 0; i < map.size(); i++) {
			String key = iter.next();
			int value = map.get(key);
			arr[value] = key;
		}
		
		for(int i = 0; i < map.size(); i++) {
			filewriter.write("@attribute '" + arr[i] + "' {0,1}\n");
		}
		filewriter.write("@data\n");
		
		for(int i = 0; i < lineNum; i++) {
			for(int j = 0; j < map.size()-1; j++) {
				filewriter.write(res[i][j] + ", ");
		//		System.out.println(i+" "+j);
			}
			filewriter.write(res[i][map.size()-1] + "\n");
	//		filewriter.write("\n");
		}
		filewriter.flush();
		 
	 }
	 
	 public static void main(String args[]) throws IOException{
		 readFromFile();
//		 writeToArff();
		 Instances ins = write2arff();
		 FileWriter filewriter = new FileWriter("UNIX_usage"+".arff");
	     filewriter.write(ins.toString());
	     for(int i = 0; i < lineNum; i++) {
				for(int j = 0; j < map.size()-1; j++) {
					filewriter.write(res[i][j] + ", ");
			//		System.out.println(i+" "+j);
				}
				filewriter.write(res[i][map.size()-1] + "\n");
		//		filewriter.write("\n");
			}
			filewriter.flush();
	 }
}