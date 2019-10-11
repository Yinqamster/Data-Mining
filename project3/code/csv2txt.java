import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class csv2txt {
	private static int SIZE = 9835;
//	private static Instances data = null;
	public static String[]  gro = new String[9835];
	public static HashMap<String , Integer> map = new HashMap<String , Integer>(); 
//	ArrayList gro = new ArrayList();
	public static void readFromCSV() throws IOException{
		int value = 0;
		BufferedReader in = new BufferedReader(new FileReader("Groceries.csv"));
		String str0 = in.readLine();
		for(int i = 1; i < SIZE+1; i++) {
			String str = in.readLine();
			str = str.replaceAll("\"", "");
		//	str = str.replaceAll("\\{", "");
			str = str.replaceAll("\\}", "");
			String[] split = str.split("\\{");
			
	/*		for(int j = 1; j < split.length; j++) {
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
			}*/
			
		//	for(int j = 0; j < 169; j++) {
				gro[i-1]=split[1];
				
		//	}
//			System.out.println(gro[i-1] + " " + i);
//			System.out.println(str+ split.length);
/*			for(int j = 0; j < 169; j++) {
				System.out.print(gro[i-1][j]+"~");
			}
			System.out.println();*/
		}
	}
	
	public static void write2txt() throws IOException {
		FileWriter filewriter = new FileWriter("Groceries"+".txt");
		for(int i = 0; i < SIZE; i++) {
			filewriter.write(gro[i]+"\n");
		}
	}
	public static void main(String args[]) throws IOException {
		readFromCSV();
		write2txt();
	}
}
