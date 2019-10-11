import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import weka.classifiers.Classifier;
import weka.classifiers.bayes.BayesNet;
import weka.core.Instances;
import weka.core.converters.ArffLoader;
import weka.filters.Filter;
import weka.filters.supervised.attribute.Discretize;
import weka.filters.supervised.instance.Resample;
import weka.filters.unsupervised.attribute.NumericToNominal;

class author {
	public String author_name;
	public int author_passed;
	public int author_failed;
	public int author_sum;
	public double acc;
	author(String name, int p, int f, int s, double a) {
		author_name = name;
		author_passed = p;
		author_failed = f;
		author_sum = s;
		acc = a;
	}
}
class committer {
	public String committer_name;
	public int committer_passed;
	public int committer_failed;
	public int committer_sum;
	public double acc;
	committer(String name, int p, int f, int s, double a) {
		committer_name = name;
		committer_passed = p;
		committer_failed = f;
		committer_sum = s;
		acc = a;
	}
}



public class dataMining {
	private static String project_name;
	private static int project_id;
	private static int build_id;
	private static int comment_count;
	private static String author_name;
	private static int author_id;
	private static double author_acc;
	private static String author_type;
	private static String author_site_admin;
	private static String committer_name;
	private static int committer_id;
	private static double committer_acc;
	private static String committer_type;
	private static String committer_site_admin;
	private static String message;
	private static int stats_total;
	private static int stats_additions;
	private static int stats_deletions;
	private static String files_filename;
	private static String files_status;
	private static int files_additions;
	private static int files_deletions;
	private static int files_changes;
	private static double rate;
	private static int num_commit;
	private static int num_file;
	private static String build_result;
	private static int tests_num;
	private static int sumOfIns;
	private static double averageAuthorAcc;
	private static double averageCommitterAcc;
	private static HashMap<String, author> trainAuthor = new HashMap<String, author>();
	private static HashMap<String, committer> trainCommitter = new HashMap<String, committer>();
	private static ArrayList<Integer> nonErrorId = new ArrayList<Integer>();
	private static ArrayList<String> filelist= new ArrayList<String>();
	
	dataMining() {
		project_id = -1;
	}

	public static void getOthers(String filename) throws IOException,JSONException{  //trainAuthor, trainCommitter
		String path = filename+"/train_set.txt";
		BufferedReader in = new BufferedReader(new FileReader(path));
		String data = in.readLine(); 
//		System.out.println(filename);
		
		JSONArray jsonObjArray=new JSONArray(data);
			for (int i = 0; i < jsonObjArray.length(); i++) {
					
				JSONObject jsonObj = (JSONObject) jsonObjArray.get(i);
				build_result=jsonObj.getString("build_result");
				
				JSONArray commits=jsonObj.getJSONArray("commits");
				if(commits.length()!=0) {
					for(int j = 0; j < commits.length(); j++) {
						if(!(commits.get(j).toString().equals("null") ||commits.get(j).toString().equals("{}"))){
							JSONObject commitsObj=(JSONObject) commits.get(j);
							
							if (!commitsObj.has("commit") || commitsObj.get("commit").toString().equals("null") || commitsObj.get("commit").toString().equals("{}")) {
							}
							else {
								JSONObject commitObj=(JSONObject) commitsObj.get("commit");
								JSONObject authorObj=commitObj.getJSONObject("author");
								author_name=authorObj.getString("name");
								if (!trainAuthor.containsKey(author_name)) {
									author a = new author(author_name, 0, 0, 0, 0);
									trainAuthor.put(author_name, a);
								}
								trainAuthor.get(author_name).author_sum++;
								if (build_result.toString().equals("failed")) {
									trainAuthor.get(author_name).author_failed++;
								}
								else if(build_result.toString().equals("passed")) {
									trainAuthor.get(author_name).author_passed++;
								}
								JSONObject committerObj=commitObj.getJSONObject("committer");
								committer_name=committerObj.getString("name");
								if (!trainCommitter.containsKey(committer_name)) {
									committer c = new committer(committer_name, 0, 0, 0, 0);
									trainCommitter.put(committer_name, c);
								}
								trainCommitter.get(committer_name).committer_sum++;
								if (build_result.toString().equals("failed")) {
									trainCommitter.get(committer_name).committer_failed++;
								}
								else if(build_result.toString().equals("passed")) {
									trainCommitter.get(committer_name).committer_passed++;
								}
							}
						}
						
					}
				}
			}
			
	}
	
	public static void readFromJsonTrain (String filename, FileWriter filewriter)throws IOException{//, JSONException{
		String path = filename+"/train_set.txt";

		BufferedReader in = new BufferedReader(new FileReader(path));
		String data = in.readLine(); 
//		System.out.println(filename);
		project_id++;
		try {
			JSONArray jsonObjArray=new JSONArray(data);
			sumOfIns=0;
			rate = 0;
			
			for (int i = 0; i < jsonObjArray.length(); i++) {
					
				JSONObject jsonObj = (JSONObject) jsonObjArray.get(i);
				build_result=jsonObj.getString("build_result");
				if (build_result.toString().equals("failed")) {
					rate++;
				}
				if(!build_result.toString().equals("errored")) {
					sumOfIns++;
				}
			}
			rate = (double)rate/sumOfIns;
			
			
			for (int i = 0; i < jsonObjArray.length(); i++) {
				project_name="";
				author_name="";
				author_id=-1;
				author_type="?";
				author_site_admin="?";
				committer_name="";
				committer_id=-1;
				committer_type="?";
				committer_site_admin="?";
				stats_total=0;
				stats_additions=0;
				stats_deletions=0;
				files_filename="?";
				files_status="none";
				files_additions=0;
				files_deletions=0;
				files_changes=0;
				num_commit=0;
				num_file=0;
				JSONObject jsonObj = (JSONObject) jsonObjArray.get(i);
				project_name=jsonObj.getString("project_name");
				build_id=jsonObj.getInt("build_id");
			
				build_result=jsonObj.getString("build_result");
				if(build_result.toString().equals("errored")) {
					continue;
				}
				
				
				JSONArray commits=jsonObj.getJSONArray("commits");
				num_commit=commits.length();
				if(commits.length()==0) {
					author_name="";
					author_id=-1;
					author_type="";
					author_site_admin="";
					committer_name="";
					committer_id=-1;
					committer_type="";
					committer_site_admin="";
					stats_total=0;
					stats_additions=0;
					stats_deletions=0;
					files_filename="?";
					files_status="none";
					files_additions=0;
					files_deletions=0;
					files_changes=0;
					num_commit=0;
					num_file=0;
				}

				for(int j = 0; j < commits.length(); j++) {
					if(commits.get(j).toString().equals("null") ||commits.get(j).toString().equals("{}")) {
						author_name="";
						author_id=-1;
						author_type="";
						author_site_admin="";
						committer_name="";
						committer_id=-1;
						committer_type="";
						committer_site_admin="";
						stats_total+=0;
						stats_additions+=0;
						stats_deletions+=0;
						files_filename="?";
						files_status="none";
						files_additions+=0;
						files_deletions+=0;
						files_changes+=0;

					}
					else {
						JSONObject commitsObj=(JSONObject) commits.get(j);
						
						if (!commitsObj.has("commit") || commitsObj.get("commit").toString().equals("null") || commitsObj.get("commit").toString().equals("{}")) {
							author_name="";
							committer_name="";
							message="";
						}
						else {
							JSONObject commitObj=(JSONObject) commitsObj.get("commit");
							JSONObject authorObj=commitObj.getJSONObject("author");
							author_name=authorObj.getString("name");
							JSONObject committerObj=commitObj.getJSONObject("committer");
							committer_name=committerObj.getString("name");
							message=commitObj.getString("message");
						}
						if (!commitsObj.has("author") || commitsObj.get("author").toString().equals("null") || commitsObj.get("author").toString().equals("{}")) {
							author_id=-1;
							author_type="";
							author_site_admin="";
						}
						else {
							JSONObject authorObj=commitsObj.getJSONObject("author");
							author_id=authorObj.getInt("id");
							author_type=authorObj.getString("type");
							author_site_admin=authorObj.getString("site_admin");
						}
						
						if(!commitsObj.has("committer") || commitsObj.get("committer").toString().equals("null") || commitsObj.get("committer").toString().equals("{}")){
							committer_id=-1;
							committer_type="";
							committer_site_admin="";
						}
						else {
							JSONObject comitterObj=commitsObj.getJSONObject("committer");
							committer_id=comitterObj.getInt("id");
							committer_type=comitterObj.getString("type");
							committer_site_admin=comitterObj.getString("site_admin");
						}
		
						if(!commitsObj.has("stats") || commitsObj.get("stats").toString().equals("null") || commitsObj.get("stats").toString().equals("{}")) {
							stats_total+=0;
							stats_additions+=0;
							stats_deletions+=0;
						}
						else {
							JSONObject statsObj=commitsObj.getJSONObject("stats");
							stats_total+=statsObj.getInt("total");
							stats_additions+=statsObj.getInt("additions");
							stats_deletions+=statsObj.getInt("deletions");
						}
						
						
						if(!commitsObj.has("files") || commitsObj.get("files").toString().equals("null") || commitsObj.get("files").toString().equals("{}")) {
							System.out.println("line:" + i);
							files_filename="";
							files_status="none";
							files_additions+=0;
							files_deletions+=0;
							files_changes+=0;
//							
						}
						else {
							
							JSONArray files=commitsObj.getJSONArray("files");
							num_file+=files.length();
							if(files.length()==0) {
								files_filename="";
								files_status="none";
								files_additions+=0;
								files_deletions+=0;
								files_changes+=0;
//								
							}
							else {
								for(int k = 0; k < files.length(); k++) {
									JSONObject filesObj=(JSONObject) files.get(k);
									
									files_filename=filesObj.getString("filename");
									files_status=filesObj.getString("status");
									files_additions+=filesObj.getInt("additions");
									files_deletions+=filesObj.getInt("deletions");
									files_changes+=filesObj.getInt("changes");
			
								}
							}
						}
					}
				}
				if(trainAuthor.containsKey(author_name)) {
					author_acc=trainAuthor.get(author_name).acc;
				}
				else {
					System.out.println("error no author");
				}
				if(trainCommitter.containsKey(committer_name)) {
					committer_acc=trainCommitter.get(committer_name).acc;
				}
				else {
					System.out.println("error no committer");
				}
				filewriter.write(rate + ", " +  project_id+", "
						+ author_acc + ", "
						+ committer_acc + ","
//						+ author_name +", "
//						+committer_name+", "
//						+ author_id +", "
//						+committer_id+","
//						+stats_total+", "+stats_additions+", "+stats_deletions+", "
//						+files_status + ", "+files_additions+", "+files_deletions+", "+files_changes+", "
						+num_commit + ", "
						+build_result+"\n");
			}
			filewriter.flush();
		} catch(JSONException j) {
			j.printStackTrace();
		}
		data=null;
	}
	
	public static void readFromJsonTest(String filename, FileWriter filewriter) throws IOException{
		String path = filename+"/test_set.txt";

		BufferedReader in = new BufferedReader(new FileReader(path));
		String data = in.readLine(); 
		System.out.println(filename);
		try {
			JSONArray jsonObjArray=new JSONArray(data);
			
			for (int i = 0; i < jsonObjArray.length(); i++) {
				project_name="";
				author_name="";
				author_id=-1;
				author_type="?";
				author_site_admin="?";
				committer_name="";
				committer_id=-1;
				committer_type="?";
				committer_site_admin="?";
				stats_total=0;
				stats_additions=0;
				stats_deletions=0;
				files_filename="?";
				files_status="none";
				files_additions=0;
				files_deletions=0;
				files_changes=0;
				num_commit=0;
				num_file=0;
				JSONObject jsonObj = (JSONObject) jsonObjArray.get(i);
				project_name=jsonObj.getString("project_name");
				build_id=jsonObj.getInt("build_id");
				if (!nonErrorId.contains(build_id)) {
					continue;
				}
			
				JSONArray commits=jsonObj.getJSONArray("commits");
				num_commit=commits.length();
				if(commits.length()==0) {
					author_name="";
					author_id=-1;
					author_type="";
					author_site_admin="";
					committer_name="";
					committer_id=-1;
					committer_type="";
					committer_site_admin="";
					stats_total=0;
					stats_additions=0;
					stats_deletions=0;
					files_filename="";
					files_status="none";
					files_additions=0;
					files_deletions=0;
					files_changes=0;
					num_commit=0;
					num_file=0;
				}

				for(int j = 0; j < commits.length(); j++) {
					if(commits.get(j).toString().equals("null") ||commits.get(j).toString().equals("{}")) {
						author_name="";
						author_id=-1;
						author_type="";
						author_site_admin="";
						committer_name="";
						committer_id=-1;
						committer_type="";
						committer_site_admin="";
						stats_total+=0;
						stats_additions+=0;
						stats_deletions+=0;
						files_filename="?";
						files_status="none";
						files_additions+=0;
						files_deletions+=0;
						files_changes+=0;

					}
					else {
						JSONObject commitsObj=(JSONObject) commits.get(j);
						
						if (!commitsObj.has("commit") || commitsObj.get("commit").toString().equals("null") || commitsObj.get("commit").toString().equals("{}")) {
							author_name="";
							committer_name="";
							message="";
						}
						else {
							JSONObject commitObj=(JSONObject) commitsObj.get("commit");
							JSONObject authorObj=commitObj.getJSONObject("author");
							author_name=authorObj.getString("name");
							JSONObject committerObj=commitObj.getJSONObject("committer");
							committer_name=committerObj.getString("name");
							message=commitObj.getString("message");
						}
						if (!commitsObj.has("author") || commitsObj.get("author").toString().equals("null") || commitsObj.get("author").toString().equals("{}")) {
							author_id=-1;
							author_type="";
							author_site_admin="";
						}
						else {
							JSONObject authorObj=commitsObj.getJSONObject("author");
							author_id=authorObj.getInt("id");
							author_type=authorObj.getString("type");
							author_site_admin=authorObj.getString("site_admin");
						}
						
						if(!commitsObj.has("committer") || commitsObj.get("committer").toString().equals("null") || commitsObj.get("committer").toString().equals("{}")){
							committer_id=-1;
							committer_type="";
							committer_site_admin="";
						}
						else {
							JSONObject comitterObj=commitsObj.getJSONObject("committer");
							committer_id=comitterObj.getInt("id");
							committer_type=comitterObj.getString("type");
							committer_site_admin=comitterObj.getString("site_admin");
						}
		
						if(!commitsObj.has("stats") || commitsObj.get("stats").toString().equals("null") || commitsObj.get("stats").toString().equals("{}")) {
							stats_total+=0;
							stats_additions+=0;
							stats_deletions+=0;
						}
						else {
							JSONObject statsObj=commitsObj.getJSONObject("stats");
							stats_total+=statsObj.getInt("total");
							stats_additions+=statsObj.getInt("additions");
							stats_deletions+=statsObj.getInt("deletions");
						}
						
						
						if(!commitsObj.has("files") || commitsObj.get("files").toString().equals("null") || commitsObj.get("files").toString().equals("{}")) {
							System.out.println("line:" + i);
							files_filename="";
							files_status="none";
							files_additions+=0;
							files_deletions+=0;
							files_changes+=0;
//							
						}
						else {
							
							JSONArray files=commitsObj.getJSONArray("files");
							num_file+=files.length();
							if(files.length()==0) {
								files_filename="";
								files_status="none";
								files_additions+=0;
								files_deletions+=0;
								files_changes+=0;
//								
							}
							else {
								for(int k = 0; k < files.length(); k++) {
									JSONObject filesObj=(JSONObject) files.get(k);
									
									files_filename=filesObj.getString("filename");
									files_status=filesObj.getString("status");
									files_additions+=filesObj.getInt("additions");
									files_deletions+=filesObj.getInt("deletions");
									files_changes+=filesObj.getInt("changes");
			
								}
							}
						}
					}
				}
				if(trainAuthor.containsKey(author_name)) {
					author_acc=trainAuthor.get(author_name).acc;
				}
				else {
					author_acc=averageAuthorAcc;
				}
				if(trainCommitter.containsKey(committer_name)) {
					committer_acc=trainCommitter.get(committer_name).acc;
				}
				else {
					committer_acc=averageCommitterAcc;
				}
				filewriter.write(rate + ", " +  project_id+", "
						+ author_acc + ", "
						+ committer_acc + ","
//						+ author_name +", "
//						+committer_name+", "
//						+ author_id +", "
//						+committer_id+","
//						+stats_total+", "+stats_additions+", "+stats_deletions+", "
//						+files_status + ", "+files_additions+", "+files_deletions+", "+files_changes+", "
						+num_commit + ", "
						+"?"+"\n");
//				}
			}
			filewriter.flush();
		} catch(JSONException j) {
			j.printStackTrace();
		}
		data=null;
	}
	

	//涉及采样
	public static void trainAndPredictSample() throws IOException,Exception{
		Discretize numToNominal = new Discretize();
		Classifier m_classifier = new BayesNet();
	    File inputFile = new File("train.arff");//训练语料文件
	    ArffLoader atf = new ArffLoader(); 
	    atf.setFile(inputFile);
	    Instances instancesTrain = atf.getDataSet(); // 读入训练文件    
	    instancesTrain.setClassIndex(instancesTrain.numAttributes()-1);
	    numToNominal.setAttributeIndices("" + (instancesTrain.classIndex()+1));
	    numToNominal.setInputFormat(instancesTrain);
	    numToNominal.setMakeBinary(true);
    	Instances instancesT=Filter.useFilter(instancesTrain, numToNominal);
    	
	    inputFile = new File("test.arff");//测试语料文件
	    atf.setFile(inputFile);          
	    Instances instancesTest = atf.getDataSet(); // 读入测试文件
	    instancesTest.setClassIndex(instancesTrain.numAttributes()-1); //设置分类属性所在行号（第一行为0号），instancesTest.numAttributes()可以取得属性总数
	    
	    
	    int sum = instancesTest.numInstances();//测试语料实例数
	    System.out.println("sum = :" + sum);
	    
	    int passed=0, failed=0;
	    for(int i = 0; i < instancesTrain.numInstances(); i++) {
	    	if(instancesTrain.instance(i).classValue()==0) {
	    		passed++;
	    	}
	    	else if(instancesTrain.instance(i).classValue()==1) {
	    		failed++;
	    	}
	    	else {
	    		System.out.println("error");
	    	}
	    }
	    System.out.println("passed failed : " + passed + " " + failed);
	    
	    ArrayList<Double> resList = new ArrayList<Double>();
	    for (int i = 0; i < 10; i++) {
	    	Resample r = new Resample();
	    	r.setInputFormat(instancesTrain);
//	   	 	r.setNoReplacement(false);
//	  		r.setInvertSelection(false);
	    	r.setBiasToUniformClass(1.0);
	    	r.setRandomSeed(i);
	    	r.setSampleSizePercent(80);
	    	Instances insTrain = Filter.useFilter(instancesTrain, r);
	    	
	    	m_classifier.buildClassifier(insTrain);
	    
	    	for (int j = 0; j < sum; j++) {
	    		double res = m_classifier.classifyInstance(instancesTest.instance(j));
	    		if (resList.size() <sum) {
	    			resList.add(res);
	    		}
	    		else {
	    			res+=resList.get(j);
	    			resList.set(j, res);
	    		}
	    	}
	    	passed=0; failed=0;
	    	for(int j = 0; j < insTrain.numInstances(); j++) {
	    		if(insTrain.instance(j).classValue()==0) {
	    			passed++;
	    		}
	    		else if(insTrain.instance(j).classValue()==1) {
	    			failed++;
	    		}
	    	}
	    	System.out.println("passed failed : " + passed + " " + failed);
	    }
	    System.out.println("resList size:" + resList.size());
	    for (int i = 0; i < resList.size(); i++) {
	    	resList.set(i, (double)resList.get(i)/10);
	    }
	    
	    passed=0;failed=0;
	    FileWriter filewriter = new FileWriter("predict"+".csv");
		filewriter.write("Id" + "," + "Prediction" + "\n");
	    for (int i = 0; i < resList.size(); i++) {
	    	int out;
	    	if (resList.get(i) > 0.5) {
	    		out=1;
	    		failed++;
	    	}
	    	else {
	    		out=0;
	    		passed++;
	    	}
	    	filewriter.write(nonErrorId.get(i) + "," + out + "\n");
	    }
	    filewriter.flush();
	    System.out.println("passed failed:" + passed +" " +  failed);
    }
	
	//没有涉及采样
	public static void trainAndPredict() throws IOException,Exception{
		NumericToNominal numToNom=new NumericToNominal();
		Discretize numToNominal = new Discretize();
		Classifier m_classifier = new BayesNet();
	    File inputFile = new File("train.arff");//训练语料文件
	    
	    ArffLoader atf = new ArffLoader(); 
	    atf.setFile(inputFile);
	    Instances instancesTrain = atf.getDataSet(); // 读入训练文件    
	    instancesTrain.setClassIndex(instancesTrain.numAttributes()-1);
	    

	    System.out.println("" + (instancesTrain.classIndex()+1));
	    numToNominal.setAttributeIndices("" + (instancesTrain.classIndex()+1));
	    numToNominal.setInputFormat(instancesTrain);
	    numToNominal.setMakeBinary(true);
    	Instances instancesT=Filter.useFilter(instancesTrain, numToNominal);
    	
	    inputFile = new File("test.arff");//测试语料文件
	    atf.setFile(inputFile);          
	    Instances instancesTest = atf.getDataSet(); // 读入测试文件
	    instancesTest.setClassIndex(instancesTrain.numAttributes()-1); //设置分类属性所在行号（第一行为0号），instancesTest.numAttributes()可以取得属性总数
	    
	    int sum = instancesTest.numInstances();//测试语料实例数
	    System.out.println("sum = :" + sum);

	    m_classifier.buildClassifier(instancesT); //训练     
	    FileWriter filewriter = new FileWriter("predict"+".csv");
		filewriter.write("Id" + "," + "Prediction" + "\n");
		int passed=0, failed=0;
	    for (int i = 0; i < sum; i++) {
	    	int res = (int)m_classifier.classifyInstance(instancesTest.instance(i));
	    	filewriter.write(nonErrorId.get(i) + "," + res + "\n");
	    	
	    	if(res==1) {
	    		failed++;
	    	}
	    	else {
	    		passed++;
	    	}
	    }
	    filewriter.flush();
	   
	    System.out.println("pass fail:" + passed + " " + failed );
	   
    }
	
	public static void getFiles(String filePath) {
		File root = new File(filePath);
		File[] files = root.listFiles();
		for (File file:files) {
			if(file.isDirectory()) {
				filelist.add(file.getAbsolutePath());
			}
		}
	}
	

	public static void write2arff() throws IOException, JSONException,Exception{
		String filePath="DM_course_project";
		String out_train = "train.arff";
		String out_test = "test.arff";
		FileWriter filewriter_train = new FileWriter(out_train);
		FileWriter filewriter_test = new FileWriter(out_test);
		filewriter_train.write("@relation " + "train_data" + "\n");
		filewriter_train.write("@attribute rate numeric\n");
		filewriter_train.write("@attribute project_id {1");
		for(int i = 2; i <= 532; i++) {
			filewriter_train.write(", " + i);
		}
		filewriter_train.write("}\n");
//		filewriter_train.write("@attribute build_id numeric\n");
		filewriter_train.write("@attribute author_acc numeric\n");
		filewriter_train.write("@attribute committer_acc numeric\n");      
//		filewriter_train.write("@attribute author_id numeric\n");//correct string!!!!!!!!!
//		filewriter_train.write("@attribute committer_id numeric\n");
//		filewriter_train.write("@attribute stats_total numeric\n");
//		filewriter_train.write("@attribute stats_additions numeric\n");
//		filewriter_train.write("@attribute stats_deletions numeric\n");
//		filewriter_train.write("@attribute files_status {added, removed, modified, renamed, none}\n");
//		filewriter_train.write("@attribute files_additions numeric\n");
//		filewriter_train.write("@attribute files_deletions numeric\n");
//		filewriter_train.write("@attribute files_changes numeric\n");
		filewriter_train.write("@attribute num_commit numeric\n");
//		filewriter_train.write("@attribute num_file numeric\n");
		filewriter_train.write("@attribute build_result {passed, failed}\n");   //0 passed  1 failed
		filewriter_train.write("@data\n");
		
		filewriter_test.write("@relation " + "test_data" + "\n");
		filewriter_test.write("@attribute rate numeric\n");
		filewriter_test.write("@attribute project_id {1");
		for(int i = 2; i <= 532; i++) {
			filewriter_test.write(", " + i);
		}
		filewriter_test.write("}\n");
//		filewriter_test.write("@attribute build_id numeric\n");
		filewriter_test.write("@attribute author_acc numeric\n");
		filewriter_test.write("@attribute committer_acc numeric\n");
//		filewriter_test.write("@attribute author_id numeric\n");
//		filewriter_test.write("@attribute committer_id numeric\n");
//		filewriter_test.write("@attribute stats_total numeric\n");
//		filewriter_test.write("@attribute stats_additions numeric\n");
//		filewriter_test.write("@attribute stats_deletions numeric\n");
//		filewriter_test.write("@attribute files_status {added, removed, modified, renamed, none}\n");
//		filewriter_test.write("@attribute files_additions numeric\n");
//		filewriter_test.write("@attribute files_deletions numeric\n");
//		filewriter_test.write("@attribute files_changes numeric\n");
		filewriter_test.write("@attribute num_commit numeric\n");
//		filewriter_test.write("@attribute num_file numeric\n");
		filewriter_test.write("@attribute build_result {passed, failed}\n");   //0 passed -1 errored 1 failed
		filewriter_test.write("@data\n");
		tests_num=0;
		getFiles(filePath);
		System.out.println("file_num:" + filelist.size());
		for (int i = 0; i < filelist.size(); i++) {
//			for (int i = 0; i < 2; i++) {
			getOthers(filelist.get(i));
		}
		System.out.println(trainAuthor.size());
		
		int sum = 0;
		int num = 0;
		Iterator<Entry<String, author>> iteAuthor = trainAuthor.entrySet().iterator();  
		while(iteAuthor.hasNext()) {
			 Entry<String, author> entry = iteAuthor.next(); 
			 author value = (author)entry.getValue();
			 value.acc = (double)value.author_passed/value.author_sum;
			 entry.setValue(value);
			 num++;
			 sum+=value.acc;
		}
		averageAuthorAcc=(double)sum/num;
		sum=0;
		num=0;
		Iterator<Entry<String, committer>> iteCommitter = trainCommitter.entrySet().iterator();  
		while(iteCommitter.hasNext()) {
			 Entry<String, committer> entry = iteCommitter.next(); 
			 committer value = (committer)entry.getValue();
			 value.acc = (double)value.committer_passed/value.committer_sum;
			 entry.setValue(value);
			 num++;
			 sum+=value.acc;
		}
		averageCommitterAcc=(double)sum/num;
		System.out.println("average:" + averageAuthorAcc + ", " + averageCommitterAcc);
			
			
		for (int i = 0; i < filelist.size(); i++) {
//			for (int i = 0; i < 2; i++) {
			readFromJsonTrain(filelist.get(i),filewriter_train);
			readFromJsonTest(filelist.get(i),filewriter_test);
		}
	}
	public static void main(String[] args) throws IOException, JSONException,Exception{
		// TODO Auto-generated method stub
		BufferedReader in = new BufferedReader(new FileReader("non_error_build_ids.csv"));
		String str0 = in.readLine();
		for(int i = 0; i < 168289; i++) {
			str0 = in.readLine();
			int id = Integer.parseInt(str0);
			nonErrorId.add(id);
		}	

		write2arff();
//		trainAndPredict();
		trainAndPredictSample();
		
		System.out.println("finished");
		

	}

}
