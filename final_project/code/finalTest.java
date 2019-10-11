//
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileReader;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.Map;
//import java.util.Map.Entry;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import weka.classifiers.Classifier;
//import weka.classifiers.Evaluation;
//import weka.classifiers.bayes.BayesNet;
//import weka.classifiers.bayes.NaiveBayes;
//import weka.classifiers.functions.LibSVM;
//import weka.classifiers.functions.MultilayerPerceptron;
//import weka.classifiers.meta.Vote;
//import weka.classifiers.trees.J48;
//import weka.classifiers.trees.RandomForest;
//import weka.core.Instances;
//import weka.core.SelectedTag;
//import weka.core.Debug.Random;
//import weka.core.converters.ArffLoader;
//import weka.filters.Filter;
//import weka.filters.supervised.attribute.Discretize;
//import weka.filters.supervised.instance.Resample;
//import weka.filters.unsupervised.attribute.NumericToNominal;
//import weka.filters.unsupervised.attribute.StringToNominal;
//
////import net.sf.json.JSONArray;
////import net.sf.json.JSONObject;
//
//class author {
//	public String author_name;
//	public int author_passed;
//	public int author_failed;
//	public int author_sum;
//	public double acc;
//	author(String name, int p, int f, int s, double a) {
//		author_name = name;
//		author_passed = p;
//		author_failed = f;
//		author_sum = s;
//		acc = a;
//	}
//}
//class committer {
//	public String committer_name;
//	public int committer_passed;
//	public int committer_failed;
//	public int committer_sum;
//	public double acc;
//	committer(String name, int p, int f, int s, double a) {
//		committer_name = name;
//		committer_passed = p;
//		committer_failed = f;
//		committer_sum = s;
//		acc = a;
//	}
//}
//
//
//
//public class finalTest {
//	private static String project_name;
//	private static int project_id;
//	private static int build_id;
//	private static int comment_count;
//	private static String author_name;
//	private static int author_id;
//	private static double author_acc;
//	private static String author_type;
//	private static String author_site_admin;
//	private static String committer_name;
//	private static int committer_id;
//	private static double committer_acc;
//	private static String committer_type;
//	private static String committer_site_admin;
//	private static String message;
//	private static int stats_total;
//	private static int stats_additions;
//	private static int stats_deletions;
//	private static String files_filename;
//	private static String files_status;
//	private static int files_additions;
//	private static int files_deletions;
//	private static int files_changes;
//	private static double rate;
//	private static int num_commit;
//	private static int num_file;
//	private static String build_result;
//	private static int tests_num;
//	private static int sumOfIns;
//	private static double averageAuthorAcc;
//	private static double averageCommitterAcc;
////	private static HashMap<Integer, Integer> map = new HashMap<Integer ,Integer>();
////	private static HashMap<Integer, Integer> testMap = new HashMap<Integer, Integer>();
//	private static HashMap<String, author> trainAuthor = new HashMap<String, author>();
//	private static HashMap<String, committer> trainCommitter = new HashMap<String, committer>();
////	private static HashMap<String, project> trainProject = new HashMap<String, project>();
//	private static ArrayList<Integer> nonErrorId = new ArrayList<Integer>();
//	private static ArrayList<String> filelist= new ArrayList<String>();
//	
//	finalTest() {
//		project_id = -1;
//	}
//
//	public static void getOthers(String filename) throws IOException,JSONException{  //trainAuthor, trainCommitter
//		String path = filename+"/train_set.txt";
//		BufferedReader in = new BufferedReader(new FileReader(path));
//		String data = in.readLine(); 
//		System.out.println(filename);
//		
//		JSONArray jsonObjArray=new JSONArray(data);
//			for (int i = 0; i < jsonObjArray.length(); i++) {
//					
//				JSONObject jsonObj = (JSONObject) jsonObjArray.get(i);
//				build_result=jsonObj.getString("build_result");
//				
//				JSONArray commits=jsonObj.getJSONArray("commits");
//				if(commits.length()!=0) {
//					for(int j = 0; j < commits.length(); j++) {
//						if(!(commits.get(j).toString().equals("null") ||commits.get(j).toString().equals("{}"))){
//							JSONObject commitsObj=(JSONObject) commits.get(j);
//							
//							if (!commitsObj.has("commit") || commitsObj.get("commit").toString().equals("null") || commitsObj.get("commit").toString().equals("{}")) {
//							}
//							else {
//								JSONObject commitObj=(JSONObject) commitsObj.get("commit");
//								JSONObject authorObj=commitObj.getJSONObject("author");
//								author_name=authorObj.getString("name");
//								author_name = author_name.replaceAll("[^a-zA-Z0-9_]", "");
//								if (!trainAuthor.containsKey(author_name)) {
//									author a = new author(author_name, 0, 0, 0, 0);
//									trainAuthor.put(author_name, a);
//								}
//								trainAuthor.get(author_name).author_sum++;
//								if (build_result.toString().equals("failed")) {
//									trainAuthor.get(author_name).author_failed++;
//								}
//								else if(build_result.toString().equals("passed")) {
//									trainAuthor.get(author_name).author_passed++;
//								}
//								JSONObject committerObj=commitObj.getJSONObject("committer");
//								committer_name=committerObj.getString("name");
//								committer_name = committer_name.replaceAll("[^a-zA-Z0-9_]", "");
//								if (!trainCommitter.containsKey(committer_name)) {
//									committer c = new committer(committer_name, 0, 0, 0, 0);
//									trainCommitter.put(committer_name, c);
//								}
//								trainCommitter.get(committer_name).committer_sum++;
//								if (build_result.toString().equals("failed")) {
//									trainCommitter.get(committer_name).committer_failed++;
//								}
//								else if(build_result.toString().equals("passed")) {
//									trainCommitter.get(committer_name).committer_passed++;
//								}
//							}
//						}
//						
//					}
//				}
//			}
//			
//	}
//	
//	public static void readFromJsonTrain (String filename, FileWriter filewriter)throws IOException{//, JSONException{
//		String path = filename+"/train_set.txt";
//
//		BufferedReader in = new BufferedReader(new FileReader(path));
//		String data = in.readLine(); 
//		System.out.println(filename);
//		project_id++;
//		try {
//			JSONArray jsonObjArray=new JSONArray(data);
//			sumOfIns=0;
//			rate = 0;
//			
//			for (int i = 0; i < jsonObjArray.length(); i++) {
//					
//				JSONObject jsonObj = (JSONObject) jsonObjArray.get(i);
//				build_result=jsonObj.getString("build_result");
//				if (build_result.toString().equals("failed")) {
//					rate++;
//				}
//				if(!build_result.toString().equals("errored")) {
//					sumOfIns++;
//				}
//			}
//			rate = (double)rate/sumOfIns;
//			
//			
//			for (int i = 0; i < jsonObjArray.length(); i++) {
//				project_name="";
//				author_name="";
//				author_id=-1;
//				author_type="?";
//				author_site_admin="?";
//				committer_name="";
//				committer_id=-1;
//				committer_type="?";
//				committer_site_admin="?";
//				stats_total=0;
//				stats_additions=0;
//				stats_deletions=0;
//				files_filename="?";
//				files_status="none";
//				files_additions=0;
//				files_deletions=0;
//				files_changes=0;
//				num_commit=0;
//				num_file=0;
////				System.out.println(i);
//				JSONObject jsonObj = (JSONObject) jsonObjArray.get(i);
//				project_name=jsonObj.getString("project_name");
//				project_name = project_name.replaceAll("[^a-zA-Z0-9_]", "");
////				project_name = project_name.replace('\'', ' ');
////				project_name = project_name.replace(',', ' ');
////				project_name = project_name.replace('\"', ' ');
//				build_id=jsonObj.getInt("build_id");
//			
//				build_result=jsonObj.getString("build_result");
//				if(build_result.toString().equals("errored")) {
//					continue;
//				}
//				
//				
//				JSONArray commits=jsonObj.getJSONArray("commits");
//				num_commit=commits.length();
//				if(commits.length()==0) {
//					author_name="";
//					author_id=-1;
//					author_type="";
//					author_site_admin="";
//					committer_name="";
//					committer_id=-1;
//					committer_type="";
//					committer_site_admin="";
//					stats_total=0;
//					stats_additions=0;
//					stats_deletions=0;
//					files_filename="?";
//					files_status="none";
//					files_additions=0;
//					files_deletions=0;
//					files_changes=0;
//					num_commit=0;
//					num_file=0;
//				}
//
//				for(int j = 0; j < commits.length(); j++) {
//					if(commits.get(j).toString().equals("null") ||commits.get(j).toString().equals("{}")) {
//						author_name="";
//						author_id=-1;
//						author_type="";
//						author_site_admin="";
//						committer_name="";
//						committer_id=-1;
//						committer_type="";
//						committer_site_admin="";
//						stats_total+=0;
//						stats_additions+=0;
//						stats_deletions+=0;
//						files_filename="?";
//						files_status="none";
//						files_additions+=0;
//						files_deletions+=0;
//						files_changes+=0;
//
//					}
//					else {
//						JSONObject commitsObj=(JSONObject) commits.get(j);
//						
//						if (!commitsObj.has("commit") || commitsObj.get("commit").toString().equals("null") || commitsObj.get("commit").toString().equals("{}")) {
//							author_name="";
//							committer_name="";
//							message="";
//						}
//						else {
//							JSONObject commitObj=(JSONObject) commitsObj.get("commit");
//							JSONObject authorObj=commitObj.getJSONObject("author");
//							author_name=authorObj.getString("name");
//							author_name = author_name.replaceAll("[^a-zA-Z0-9_]", "");
////							author_name = author_name.replace('\'', ' ');
////							author_name = author_name.replace(',', ' ');
////							author_name = author_name.replace('\"', ' ');
//							JSONObject committerObj=commitObj.getJSONObject("committer");
//							committer_name=committerObj.getString("name");
//							committer_name = committer_name.replaceAll("[^a-zA-Z0-9_]", "");
////							committer_name = committer_name.replace('\'', ' ');
////							committer_name = committer_name.replace(',', ' ');
////							committer_name = committer_name.replace('\"', ' ');
//							message=commitObj.getString("message");
//						}
//		//				System.out.println(commitsObj.get("author").toString().equals("{}"));
//						if (!commitsObj.has("author") || commitsObj.get("author").toString().equals("null") || commitsObj.get("author").toString().equals("{}")) {
//							author_id=-1;
//							author_type="";
//							author_site_admin="";
//						}
//						else {
//							JSONObject authorObj=commitsObj.getJSONObject("author");
//		//					System.out.println("00");
//							author_id=authorObj.getInt("id");
//		//					System.out.println("11");
//							author_type=authorObj.getString("type");
//							author_site_admin=authorObj.getString("site_admin");
//						}
//						
//						if(!commitsObj.has("committer") || commitsObj.get("committer").toString().equals("null") || commitsObj.get("committer").toString().equals("{}")){
//							committer_id=-1;
//							committer_type="";
//							committer_site_admin="";
//						}
//						else {
//							JSONObject comitterObj=commitsObj.getJSONObject("committer");
//							committer_id=comitterObj.getInt("id");
//		//					System.out.println("22");
//							committer_type=comitterObj.getString("type");
//							committer_site_admin=comitterObj.getString("site_admin");
//						}
//		//				System.out.println(commitsObj.has("stats"));
//		
//						if(!commitsObj.has("stats") || commitsObj.get("stats").toString().equals("null") || commitsObj.get("stats").toString().equals("{}")) {
//		//					System.out.println("11");
//							stats_total+=0;
//							stats_additions+=0;
//							stats_deletions+=0;
//						}
//						else {
//							JSONObject statsObj=commitsObj.getJSONObject("stats");
//							stats_total+=statsObj.getInt("total");
//							stats_additions+=statsObj.getInt("additions");
//							stats_deletions+=statsObj.getInt("deletions");
//						}
//						
//						
//						if(!commitsObj.has("files") || commitsObj.get("files").toString().equals("null") || commitsObj.get("files").toString().equals("{}")) {
//							System.out.println("line:" + i);
//							files_filename="";
//							files_status="none";
//							files_additions+=0;
//							files_deletions+=0;
//							files_changes+=0;
////							
//						}
//						else {
//							
//							JSONArray files=commitsObj.getJSONArray("files");
//							num_file+=files.length();
//							if(files.length()==0) {
//								files_filename="";
//								files_status="none";
//								files_additions+=0;
//								files_deletions+=0;
//								files_changes+=0;
////								
//							}
//							else {
//								for(int k = 0; k < files.length(); k++) {
//									JSONObject filesObj=(JSONObject) files.get(k);
//									
//									files_filename=filesObj.getString("filename");
//									files_status=filesObj.getString("status");
//									files_additions+=filesObj.getInt("additions");
//									files_deletions+=filesObj.getInt("deletions");
//									files_changes+=filesObj.getInt("changes");
//			
//								}
//							}
//						}
//					}
//				}
////				if (file.equals("train_set")) {
//				if(trainAuthor.containsKey(author_name)) {
//					author_acc=trainAuthor.get(author_name).acc;
//				}
//				else {
//					System.out.println("error no author");
//				}
//				if(trainCommitter.containsKey(committer_name)) {
//					committer_acc=trainCommitter.get(committer_name).acc;
//				}
//				else {
//					System.out.println("error no committer");
//				}
//				filewriter.write(project_name + "," + rate + ", "
//						+ author_acc + ", "
//					    + committer_acc + ","
//						+build_result+"\n");
//			}
//			filewriter.flush();
//		} catch(JSONException j) {
//			j.printStackTrace();
////			System.out.println(str);
//		}
//		data=null;
////		System.out.println(jsonObjArray.length());
//	}
//	
//	public static void readFromJsonTest(String filename, FileWriter filewriter) throws IOException{
//		String path = filename+"/test_set.txt";
//
//		BufferedReader in = new BufferedReader(new FileReader(path));
//		String data = in.readLine(); 
//		System.out.println(filename);
////		int count = 0;
//		try {
//			JSONArray jsonObjArray=new JSONArray(data);
//			
//			for (int i = 0; i < jsonObjArray.length(); i++) {
//				project_name="";
//				author_name="";
//				author_id=-1;
//				author_type="?";
//				author_site_admin="?";
//				committer_name="";
//				committer_id=-1;
//				committer_type="?";
//				committer_site_admin="?";
//				stats_total=0;
//				stats_additions=0;
//				stats_deletions=0;
//				files_filename="?";
//				files_status="none";
//				files_additions=0;
//				files_deletions=0;
//				files_changes=0;
//				num_commit=0;
//				num_file=0;
////				System.out.println(i);
//				JSONObject jsonObj = (JSONObject) jsonObjArray.get(i);
//				project_name=jsonObj.getString("project_name");
//				project_name = project_name.replaceAll("[^a-zA-Z0-9_]", "");
////				project_name = project_name.replace('\'', ' ');
////				project_name = project_name.replace(',', ' ');
////				project_name = project_name.replace('\"', ' ');
//				build_id=jsonObj.getInt("build_id");
//				if (!nonErrorId.contains(build_id)) {
////					count++;
//					continue;
//				}
//			
//				JSONArray commits=jsonObj.getJSONArray("commits");
//				num_commit=commits.length();
//				if(commits.length()==0) {
//					author_name="";
//					author_id=-1;
//					author_type="";
//					author_site_admin="";
//					committer_name="";
//					committer_id=-1;
//					committer_type="";
//					committer_site_admin="";
//					stats_total=0;
//					stats_additions=0;
//					stats_deletions=0;
//					files_filename="";
//					files_status="none";
//					files_additions=0;
//					files_deletions=0;
//					files_changes=0;
//					num_commit=0;
//					num_file=0;
//				}
//
//				for(int j = 0; j < commits.length(); j++) {
//					if(commits.get(j).toString().equals("null") ||commits.get(j).toString().equals("{}")) {
//						author_name="";
//						author_id=-1;
//						author_type="";
//						author_site_admin="";
//						committer_name="";
//						committer_id=-1;
//						committer_type="";
//						committer_site_admin="";
//						stats_total+=0;
//						stats_additions+=0;
//						stats_deletions+=0;
//						files_filename="?";
//						files_status="none";
//						files_additions+=0;
//						files_deletions+=0;
//						files_changes+=0;
//
//					}
//					else {
//						JSONObject commitsObj=(JSONObject) commits.get(j);
//						
//						if (!commitsObj.has("commit") || commitsObj.get("commit").toString().equals("null") || commitsObj.get("commit").toString().equals("{}")) {
//							author_name="";
//							committer_name="";
//							message="";
//						}
//						else {
//							JSONObject commitObj=(JSONObject) commitsObj.get("commit");
//							JSONObject authorObj=commitObj.getJSONObject("author");
//							author_name=authorObj.getString("name");
//							author_name = author_name.replaceAll("[^a-zA-Z0-9_]", "");
////							author_name.toString();
////							System.out.println(author_name.length());
////							author_name = author_name.replace('\'', ' ');
////							author_name = author_name.replace(',', ' ');
////							author_name = author_name.replace('\"', ' ');
////							System.out.println(author_name);
//							JSONObject committerObj=commitObj.getJSONObject("committer");
//							committer_name=committerObj.getString("name");
//							committer_name = committer_name.replaceAll("[^a-zA-Z0-9_]", "");
////							committer_name = committer_name.replace(',', ' ');
////							committer_name = committer_name.replace('\'', ' ');
////							committer_name = committer_name.replace('\"', ' ');
//							message=commitObj.getString("message");
//						}
//		//				System.out.println(commitsObj.get("author").toString().equals("{}"));
//						if (!commitsObj.has("author") || commitsObj.get("author").toString().equals("null") || commitsObj.get("author").toString().equals("{}")) {
//							author_id=-1;
//							author_type="";
//							author_site_admin="";
//						}
//						else {
//							JSONObject authorObj=commitsObj.getJSONObject("author");
//		//					System.out.println("00");
//							author_id=authorObj.getInt("id");
//		//					System.out.println("11");
//							author_type=authorObj.getString("type");
//							author_site_admin=authorObj.getString("site_admin");
//						}
//						
//						if(!commitsObj.has("committer") || commitsObj.get("committer").toString().equals("null") || commitsObj.get("committer").toString().equals("{}")){
//							committer_id=-1;
//							committer_type="";
//							committer_site_admin="";
//						}
//						else {
//							JSONObject comitterObj=commitsObj.getJSONObject("committer");
//							committer_id=comitterObj.getInt("id");
//		//					System.out.println("22");
//							committer_type=comitterObj.getString("type");
//							committer_site_admin=comitterObj.getString("site_admin");
//						}
//		//				System.out.println(commitsObj.has("stats"));
//		
//						if(!commitsObj.has("stats") || commitsObj.get("stats").toString().equals("null") || commitsObj.get("stats").toString().equals("{}")) {
//		//					System.out.println("11");
//							stats_total+=0;
//							stats_additions+=0;
//							stats_deletions+=0;
//						}
//						else {
//							JSONObject statsObj=commitsObj.getJSONObject("stats");
//							stats_total+=statsObj.getInt("total");
//							stats_additions+=statsObj.getInt("additions");
//							stats_deletions+=statsObj.getInt("deletions");
//						}
//						
//						
//						if(!commitsObj.has("files") || commitsObj.get("files").toString().equals("null") || commitsObj.get("files").toString().equals("{}")) {
//							System.out.println("line:" + i);
//							files_filename="";
//							files_status="none";
//							files_additions+=0;
//							files_deletions+=0;
//							files_changes+=0;
////							
//						}
//						else {
//							
//							JSONArray files=commitsObj.getJSONArray("files");
//							num_file+=files.length();
//							if(files.length()==0) {
//								files_filename="";
//								files_status="none";
//								files_additions+=0;
//								files_deletions+=0;
//								files_changes+=0;
////								
//							}
//							else {
//								for(int k = 0; k < files.length(); k++) {
//									JSONObject filesObj=(JSONObject) files.get(k);
//									
//									files_filename=filesObj.getString("filename");
//									files_status=filesObj.getString("status");
//									files_additions+=filesObj.getInt("additions");
//									files_deletions+=filesObj.getInt("deletions");
//									files_changes+=filesObj.getInt("changes");
//			
//								}
//							}
//						}
//					}
//				}
////				System.out.println(count);
////				if (file.equals("train_set")) {
//				if(trainAuthor.containsKey(author_name)) {
//					author_acc=trainAuthor.get(author_name).acc;
//				}
//				else {
//					author_acc=averageAuthorAcc;
////					System.out.println("error no author");
//				}
//				if(trainCommitter.containsKey(committer_name)) {
//					committer_acc=trainCommitter.get(committer_name).acc;
//				}
//				else {
//					committer_acc=averageCommitterAcc;
////					System.out.println("error no committer");
//				}
//				filewriter.write(project_name + "," + rate + ", "
//						+ author_acc + ", "
//						+ committer_acc + ","
//						+"?"+"\n");;
////				}
//			}
//			filewriter.flush();
//		} catch(JSONException j) {
//			j.printStackTrace();
////			System.out.println(str);
//		}
//		data=null;
////		System.out.println(jsonObjArray.length());
//	}
//	
//
//
//	
//	public static void getFiles(String filePath) {
//		File root = new File(filePath);
//		File[] files = root.listFiles();
//		for (File file:files) {
//			if(file.isDirectory()) {
//				filelist.add(file.getAbsolutePath());
//			}
////			System.out.println(file.getPath());
//		}
//	}
//	
//
//	public static void write2csv() throws IOException, JSONException,Exception{
//		String filePath="DM_course_project";
//		String out_train = "train.csv";
//		String out_test = "test.csv";
//		FileWriter filewriter_train = new FileWriter(out_train);
//		FileWriter filewriter_test = new FileWriter(out_test);
//		
//		filewriter_train.write("project_name , rate , author_acc , committer_acc , build_result\n");
//		filewriter_test.write("project_name , rate , author_acc , committer_acc , build_result\n");
//		
//		tests_num=0;
//		getFiles(filePath);
//		System.out.println("file_num:" + filelist.size());
//		for (int i = 0; i < filelist.size(); i++) {
////			for (int i = 9; i < 10; i++) {
//			getOthers(filelist.get(i));
//		}
//		System.out.println(trainAuthor.size());
//		
//		int sum = 0;
//		int num = 0;
//		Iterator<Entry<String, author>> iteAuthor = trainAuthor.entrySet().iterator();  
//		while(iteAuthor.hasNext()) {
//			 Entry<String, author> entry = iteAuthor.next(); 
//			 author value = (author)entry.getValue();
//			 value.acc = (double)value.author_passed/value.author_sum;
//			 entry.setValue(value);
//			 num++;
//			 sum+=value.acc;
//		}
//		averageAuthorAcc=(double)sum/num;
//		sum=0;
//		num=0;
//		Iterator<Entry<String, committer>> iteCommitter = trainCommitter.entrySet().iterator();  
//		while(iteCommitter.hasNext()) {
//			 Entry<String, committer> entry = iteCommitter.next(); 
//			 committer value = (committer)entry.getValue();
//			 value.acc = (double)value.committer_passed/value.committer_sum;
//			 entry.setValue(value);
//			 num++;
//			 sum+=value.acc;
//		}
//		averageCommitterAcc=(double)sum/num;
//		System.out.println("average:" + averageAuthorAcc + ", " + averageCommitterAcc);
//			//get non error id list
//			
//			
//			
//		for (int i = 0; i < filelist.size(); i++) {
////			for (int i = 9; i < 10; i++) {
//			readFromJsonTrain(filelist.get(i),filewriter_train);
//			readFromJsonTest(filelist.get(i),filewriter_test);
//		}
//	}
//	public static void getResult() throws IOException{
//		BufferedReader inID = new BufferedReader(new FileReader("non_error_build_ids.csv"));
//		BufferedReader inRes = new BufferedReader(new FileReader("result.csv"));
//		FileWriter filewriter_res = new FileWriter("predict.csv");
//		filewriter_res.write("Id,Prediction\n");
//		
//		String str0 = inID.readLine();
//		String str1 = inRes.readLine();
//		for(int i = 0; i < 168289; i++) {
//			str0 = inID.readLine();
//			str1 = inRes.readLine();
//			String[] rr = str1.split(",");
//			int res = -1;
//			if(rr[4].equals("failed")){
//				res = 1;
//			}
//			else if(rr[4].equals("passed")) {
//				res=0;
//			}
//			int id = Integer.parseInt(str0);
//			filewriter_res.write(id + "," + res + "\n");
//		}	
//		filewriter_res.flush();
//	}
//	public static void main(String[] args) throws IOException, JSONException,Exception{
//		// TODO Auto-generated method stub
////		BufferedReader in = new BufferedReader(new FileReader("non_error_build_ids.csv"));
////		String str0 = in.readLine();
////		for(int i = 0; i < 168289; i++) {
////			str0 = in.readLine();
////			int id = Integer.parseInt(str0);
////			nonErrorId.add(id);
////		}	
////
////		write2csv();
//	
//		getResult();
//		System.out.println("finished");
//		
//
//	}
//
//}
