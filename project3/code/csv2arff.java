import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
 

public class csv2arff {
	private static int SIZE = 9835;
//	private static Instances data = null;
	public static int[][]  gro = new int[9835][169];
	public static HashMap<String , Integer> map = new HashMap<String , Integer>(); 
	public static String[] arr = new String[169]; 
//	ArrayList gro = new ArrayList();
	public static void readFromCSV() throws IOException{
		int value = 0;
		BufferedReader in = new BufferedReader(new FileReader("Groceries.csv"));
		String str0 = in.readLine();
		for(int i = 1; i < SIZE+1; i++) {
			String str = in.readLine();
			str = str.replaceAll("\"", "");
			str = str.replaceAll("\\{", "");
			str = str.replaceAll("\\}", "");
			String[] split = str.split(",");
			
			for(int j = 1; j < split.length; j++) {
				if(map.containsKey(split[j])) {
			//		System.out.println(map.get(split[j]) + " a");
					gro[i-1][map.get(split[j])] = 1;
				}
				else {
					map.put(split[j], value);
		//			System.out.println(map.get(split[j]) + " q");
					value++;
					gro[i-1][map.get(split[j])] = 1;
				}
			}
			
	/*		for(int j = 0; j < 169; j++) {
				if(j<split.length-1) {
			//		System.out.println(j);
					gro[i-1][j]=split[j+1];
				}
				else {
			//		System.out.println(j);
					gro[i-1][j]="0";
				}
				
			}*/
			
//			System.out.println(str+ split.length);
/*			for(int j = 0; j < 169; j++) {
				System.out.print(gro[i-1][j]+"~");
			}
			System.out.println();*/
		}
	}
	
	public static void writeToArff() throws IOException{
		FileWriter filewriter = new FileWriter("Groceries"+".arff");
		filewriter.write("@relation Groceries\n");
//		filewriter.write("@attribute item string\n");
		Iterator<String> iter = map.keySet().iterator();
		for(int i = 0; i < 169; i++) {
		//	filewriter.write("@attribute item" + i + " {0,1}\n");
	//		System.out.println(iter.next()+" " + i);
			String key = iter.next();
			int value = map.get(key);
			arr[value] = key;
			
		}
		
		for(int i = 0; i < 169; i++) {
			filewriter.write("@attribute '" + arr[i] + "' {0,1}\n");
		}
		filewriter.write("@data\n");
/*		Attribute att = new Attribute("item",(FastVector)null);
		FastVector attInfo = new FastVector();
		attInfo.addElement(att);
		Instances data = new Instances("item",attInfo,0);*/
	//	data.setClassIndex(data.numAttributes()-1);
//		filewriter.write(data.toString());
		for(int i = 0; i < SIZE; i++) {
		//	filewriter.write("{");
			for(int j = 0; j < 168; j++) {
				filewriter.write(gro[i][j] + ", ");
			}
			filewriter.write(gro[i][168] + "\n");
		//	filewriter.write("\n");
		}
		filewriter.flush();
	}
	

	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub
		
        
        readFromCSV();
        writeToArff();
       
        // save ARFF  
 /*       ArffSaver saver = new ArffSaver();  
        saver.setInstances(data);  
        saver.setFile(new File("Groceries.arff"));  
        //saver.setDestination(new File(args[1]));  
        saver.writeBatch();  */

	}

}
