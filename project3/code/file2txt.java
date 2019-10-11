import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class file2txt {
	public static String[]  res = new String[12000];
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
					 String str="";
					 line = in.readLine();
					 while(!line.equals("**EOF**")) {
						 if(line.startsWith("<")&&line.endsWith(">")) {
							 if(line.indexOf(">")-line.indexOf("<")<=3){
								 line = in.readLine();
								 continue;
							 }
						 }
						 if(str.equals("")) {
							 str = str + line;
						 }
						 else {
							 str = str + ", " + line;
						 }
//						 if(!(line.startsWith("<")&&line.endsWith(">"))) {
//							 if(!(line.charAt(2)=='>' || line.charAt(3)=='>')) {
								 
					//			 List<String> a1 = new ArrayList<String>();
//							 }
//						 }
						 
						 line = in.readLine();
					 }
					 if(!str.equals("")) {
						 res[lineNum]=str;
						 lineNum++;
					 }
					 line = in.readLine();
				 }
			 }
//			 String st0 = in.readLine();
//			 System.out.println(st0);
		 }
//		 System.out.println(map.size());
//		 Iterator<String> iter = map.keySet().iterator();
		 for(int i = 0; i < lineNum; i++) {
//			 for(int j = 0; j < 2358; j++) {
//				 System.out.print(res[i][j]);
//			 }
			 System.out.println(res[i]);
		 }
	 }
	 
		public static void write2txt() throws IOException {
			FileWriter filewriter = new FileWriter("UNIX_usage"+".txt");
			for(int i = 0; i < lineNum; i++) {
				filewriter.write(res[i]+"\n");
			}
		}

	public static void main(String[] args) throws IOException{
		// TODO Auto-generated method stub
		readFromFile();
		write2txt();
	}

}
