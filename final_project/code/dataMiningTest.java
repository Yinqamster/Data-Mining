
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import weka.classifiers.Classifier;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.meta.Vote;
import weka.core.Instances;
import weka.core.SelectedTag;
import weka.core.Debug.Random;
import weka.core.converters.ArffLoader;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.NumericToNominal;




public class dataMiningTest {
	private static String project_name;
	private static int build_id;
	private static int author_id;
	private static String author_type;
	private static String author_site_admin;
	private static int committer_id;
	private static String committer_type;
	private static String committer_site_admin;
	private static int stats_total;
	private static int stats_additions;
	private static int stats_deletions;
	private static String files_filename;
	private static String files_status;
	private static int files_additions;
	private static int files_deletions;
	private static int files_changes;
	private static int num_commit;
	private static String build_result;
	private static int tests_num;
	private static HashMap<Integer, Integer> map = new HashMap<Integer ,Integer>();
	private static HashMap<Integer, Integer> testMap = new HashMap<Integer, Integer>();
	
	private static ArrayList<String> filelist= new ArrayList<String>();

	public static void readFromJson (String filename,String file)throws IOException{//, JSONException{
		String path = filename+"/" + file+".txt";

		BufferedReader in = new BufferedReader(new FileReader(path));
		String data = in.readLine(); 
		String out = filename+"/"+file+".arff";
		System.out.println(filename);
		FileWriter filewriter = new FileWriter(out);
		filewriter.write("@relation " + filename + "\n");
		filewriter.write("@attribute build_id numeric\n");
		filewriter.write("@attribute author_id numeric\n");
//		filewriter.write("@attribute author_type string\n");
//		filewriter.write("@attribute author_site_admin string\n");
		filewriter.write("@attribute committer_id numeric\n");
//		filewriter.write("@attribute committer_type string\n");
//		filewriter.write("@attribute committer_site_admin string\n");
		filewriter.write("@attribute stats_total numeric\n");
		filewriter.write("@attribute stats_additions numeric\n");
		filewriter.write("@attribute stats_deletions numeric\n");
//		filewriter.write("@attribute files_filename string\n");
		filewriter.write("@attribute files_status {added, removed,modified,renamed,none}\n");
		filewriter.write("@attribute files_additions numeric\n");
		filewriter.write("@attribute files_deletions numeric\n");
		filewriter.write("@attribute files_changes numeric\n");
		filewriter.write("@attribute num_commit numeric\n");
		filewriter.write("@attribute build_result {0, 1}\n");   //0 passed -1 errored 1 failed
		filewriter.write("@data\n");
		
		String str="";
		try {
			JSONArray jsonObjArray=new JSONArray(data);
			if(file.equals("test_set")) {
				tests_num+=jsonObjArray.length();
			}
			for (int i = 0; i < jsonObjArray.length(); i++) {
				str=jsonObjArray.getString(i);
				JSONObject jsonObj = (JSONObject) jsonObjArray.get(i);
				project_name=jsonObj.getString("project_name");
				build_id=jsonObj.getInt("build_id");
				if (file.equals("test_set")) {
					if (!testMap.containsKey(build_id)) {
						testMap.put(build_id, 1);
					}
				}
				build_result=jsonObj.getString("build_result");
				int res=-1;
				if (build_result.toString().equals("passed")) {
					res=0;
				}
				else if (build_result.toString().equals("failed")) {
					res=1;
				}
				else if (build_result.toString().equals("errored")) {
					res=-1;
					continue;
				}
				
				JSONArray commits=jsonObj.getJSONArray("commits");
				num_commit = commits.length();
				if(commits.length()==0) {
						author_id=-1;
						author_type="?";
						author_site_admin="?";
						committer_id=-1;
						committer_type="?";
						committer_site_admin="?";
						stats_total=-1;
						stats_additions=-1;
						stats_deletions=-1;
						files_filename="?";
						files_status="none";
						files_additions=-1;
						files_deletions=-1;
						files_changes=-1;
						if (file.equals("train_set")) {
							filewriter.write(build_id+", "+
									+author_id+", "
									+committer_id+", "
									+stats_total+", "+stats_additions+", "+stats_deletions+", "
									+files_status + ", " +files_additions+", "+files_deletions+", "+files_changes+", "
									+num_commit + ", "
									+res+"\n");
						}
						else if(file.equals("test_set")) {
							filewriter.write(build_id+", "+
									+author_id+", "
									+committer_id+", "
									+stats_total+", "+stats_additions+", "+stats_deletions+", "
									+files_status + ", " +files_additions+", "+files_deletions+", "+files_changes+", "
									+num_commit + ", "
									+ "?\n");
						}
					
				}
				for(int j = 0; j < commits.length(); j++) {
					if(commits.get(j).toString().equals("null") ||commits.get(j).toString().equals("{}")) {
						author_id=-1;
						author_type="?";
						author_site_admin="?";
						committer_id=-1;
						committer_type="?";
						committer_site_admin="?";
						stats_total=-1;
						stats_additions=-1;
						stats_deletions=-1;
						files_filename="?";
						files_status="none";
						files_additions=-1;
						files_deletions=-1;
						files_changes=-1;
						if (file.equals("train_set")) {
							filewriter.write(build_id+", "+
									+author_id+", "
									+committer_id+", "
									+stats_total+", "+stats_additions+", "+stats_deletions+", "
									+files_status + ", " +files_additions+", "+files_deletions+", "+files_changes+", "
									+num_commit + ", "
									+res+"\n");
						}
						else if(file.equals("test_set")) {
							filewriter.write(build_id+", "+
									+author_id+", "
									+committer_id+", "
									+stats_total+", "+stats_additions+", "+stats_deletions+", "
									+files_status + ", " +files_additions+", "+files_deletions+", "+files_changes+", "
									+num_commit + ", "
									+ "?\n");
						}
					}
					else {
						JSONObject commitsObj=(JSONObject) commits.get(j);
						if (!commitsObj.has("author") || commitsObj.get("author").toString().equals("null") || commitsObj.get("author").toString().equals("{}")) {
							author_id=-1;
							author_type="?";
							author_site_admin="?";
						}
						else {
							JSONObject authorObj=commitsObj.getJSONObject("author");
							author_id=authorObj.getInt("id");
							author_type=authorObj.getString("type");
							author_site_admin=authorObj.getString("site_admin");
						}
						
						if(!commitsObj.has("committer") || commitsObj.get("committer").toString().equals("null") || commitsObj.get("committer").toString().equals("{}")){
							committer_id=-1;
							committer_type="?";
							committer_site_admin="?";
						}
						else {
							JSONObject comitterObj=commitsObj.getJSONObject("committer");
							committer_id=comitterObj.getInt("id");
							committer_type=comitterObj.getString("type");
							committer_site_admin=comitterObj.getString("site_admin");
						}
		
						if(!commitsObj.has("stats") || commitsObj.get("stats").toString().equals("null") || commitsObj.get("stats").toString().equals("{}")) {
							stats_total=-1;
							stats_additions=-1;
							stats_deletions=-1;
						}
						else {
							JSONObject statsObj=commitsObj.getJSONObject("stats");
							stats_total=statsObj.getInt("total");
							stats_additions=statsObj.getInt("additions");
							stats_deletions=statsObj.getInt("deletions");
						}
						
						
						if(!commitsObj.has("files") || commitsObj.get("files").toString().equals("null") || commitsObj.get("files").toString().equals("{}")) {
							System.out.println("line:" + i);
							files_filename="?";
							files_status="none";
							files_additions=-1;
							files_deletions=-1;
							files_changes=-1;
							if (file.equals("train_set")) {
								filewriter.write(build_id+", "+
										+author_id+", "
										+committer_id+", "
										+stats_total+", "+stats_additions+", "+stats_deletions+", "
										+files_status + ", " +files_additions+", "+files_deletions+", "+files_changes+", "
										+num_commit + ", "
										+res+"\n");
							}
							else if(file.equals("test_set")) {
								filewriter.write(build_id+", "+
										+author_id+", "
										+committer_id+", "
										+stats_total+", "+stats_additions+", "+stats_deletions+", "
										+files_status + ", " +files_additions+", "+files_deletions+", "+files_changes+", "
										+num_commit + ", "
										+ "?\n");
							}
						}
						else {
							JSONArray files=commitsObj.getJSONArray("files");
							if(files.length()==0) {
								files_filename="?";
								files_status="none";
								files_additions=-1;
								files_deletions=-1;
								files_changes=-1;
								if (file.equals("train_set")) {
									filewriter.write(build_id+", "+
											+author_id+", "
											+committer_id+", "
											+stats_total+", "+stats_additions+", "+stats_deletions+", "
											+files_status + ", " +files_additions+", "+files_deletions+", "+files_changes+", "
											+num_commit + ", "
											+res+"\n");
								}
								else if(file.equals("test_set")) {
									filewriter.write(build_id+", "+
											+author_id+", "
											+committer_id+", "
											+stats_total+", "+stats_additions+", "+stats_deletions+", "
											+files_status + ", " +files_additions+", "+files_deletions+", "+files_changes+", "
											+num_commit + ", "
											+ "?\n");
								}
							}
							else {
								for(int k = 0; k < files.length(); k++) {
									JSONObject filesObj=(JSONObject) files.get(k);
									
									files_filename=filesObj.getString("filename");
									files_status=filesObj.getString("status");
									files_additions=filesObj.getInt("additions");
									files_deletions=filesObj.getInt("deletions");
									files_changes=filesObj.getInt("changes");
			
									if (file.equals("train_set")) {
										filewriter.write(build_id+", "+
												+author_id+", "
												+committer_id+", "
												+stats_total+", "+stats_additions+", "+stats_deletions+", "
												+files_status + ", "+files_additions+", "+files_deletions+", "+files_changes+", "
												+num_commit + ", "
												+res+"\n");
									}
									else if(file.equals("test_set")) {
										filewriter.write(build_id+", "+
												+author_id+", "
												+committer_id+", "
												+stats_total+", "+stats_additions+", "+stats_deletions+", "
												+files_status + ", " +files_additions+", "+files_deletions+", "+files_changes+", "
												+num_commit + ", "
												+ "?\n");
									}
								}
							}
						}
					}
				}
			}
			filewriter.flush();
		} catch(JSONException j) {
			j.printStackTrace();
			System.out.println(str);
		}
		data=null;
	}
	
	public static Classifier AdaBoost() throws ClassNotFoundException, IllegalAccessException, InstantiationException{
		Classifier cfs1 = null;
		Classifier cfs2 = null;
	    Classifier cfs3 = null;
	    Classifier[] cfsArray = new Classifier[3];
	    cfs1 = (Classifier)Class.forName("weka.classifiers.bayes.NaiveBayes").newInstance();
        cfs2 = (Classifier)Class.forName("weka.classifiers.trees.J48").newInstance();
        cfs3 = (Classifier)Class.forName("weka.classifiers.rules.ZeroR").newInstance();
        cfsArray[0] = cfs1;
        cfsArray[1] = cfs2;
        cfsArray[2] = cfs3;
        Vote ensemble = new Vote();
        SelectedTag tag1 = new SelectedTag(Vote.MAJORITY_VOTING_RULE, Vote.TAGS_RULES);

         ensemble.setCombinationRule(tag1);
         ensemble.setClassifiers(cfsArray);
         //设置随机数种子
         ensemble.setSeed(2);
         return ensemble;
	}
	
	
	public static void trainAndPredict(String filePath) throws IOException,Exception{
		NumericToNominal numToNom=new NumericToNominal();
		
//		Classifier m_classifier = new NaiveBayes();
//		Classifier m_classifier = (Classifier) Class.forName("weka.classifiers.functions.LibSVM").newInstance();
		MultilayerPerceptron m_classifier=new MultilayerPerceptron();
//		m_classifier.setHiddenLayers("t");
//		m_classifier.setLearningRate(0.1);
		m_classifier.normalizeAttributesTipText();
//		Classifier m_classifier = new LibSVM();
//		Classifier m_classifier = AdaBoost();
		System.out.println(filePath);
	    File inputFile = new File(filePath+"/train_set.arff");
	    
	    ArffLoader atf = new ArffLoader(); 
	    atf.setFile(inputFile);
	    Instances instancesTrain = atf.getDataSet(); // 读入训练文件    
	    instancesTrain.setClassIndex(instancesTrain.numAttributes()-1);


    	numToNom.setAttributeIndices("" + (instancesTrain.classIndex()+1));
    	numToNom.setInputFormat(instancesTrain);
    	Instances instancesT=Filter.useFilter(instancesTrain, numToNom);

	    
	    inputFile = new File(filePath+"/test_set.arff");
	    atf.setFile(inputFile);          
	    Instances instancesTest = atf.getDataSet(); // 读入测试文件
	    instancesTest.setClassIndex(instancesTrain.numAttributes()-1); 

	    numToNom.setAttributeIndices("" + (instancesTest.classIndex()+1));
    	numToNom.setInputFormat(instancesTest);
	    Instances instancesTe=Filter.useFilter(instancesTest, numToNom);
	    double sum = instancesTe.numInstances();//测试语料实例数
	
	    double[] build_id = instancesTest.attributeToDoubleArray(0);
	    m_classifier.buildClassifier(instancesT); //训练          
	    for (int i = 0; i < sum; i++) {
	    	int res = (int)m_classifier.classifyInstance(instancesTe.instance(i));
	    	int id = (int)build_id[i];
	    	if (map.containsKey(id)) {
	    		if (map.get(id)!= res) {
	    			map.put(id, res);
	    		}
	    	}
	    	else {
	    		map.put(id, res);
	    	}
	    }
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
	
	public static void write2csv() throws IOException{
		BufferedReader in = new BufferedReader(new FileReader("non_error_build_ids.csv"));
		FileWriter filewriter = new FileWriter("predict"+".csv");
		filewriter.write("Id" + "," + "Prediction" + "\n");
		String str0 = in.readLine();
		int count = 0;
		int count1=0;
		for(int i = 0; i < 168289; i++) {
			str0 = in.readLine();
			int id = Integer.parseInt(str0);
			if(map.containsKey(id)) {
				count++;
				filewriter.write(id + "," + map.get(id) + "\n");
			}
			else {
				filewriter.write(id + "," + 1 + "\n");
			}
		}
		filewriter.flush();
		System.out.println("count " + count);
		System.out.println("count1 " + count1);
	}
	
	public static void fold10Test(String filePath) throws IOException,Exception{
		NumericToNominal numToNom=new NumericToNominal();
		
//		Classifier m_classifier = new NaiveBayes();
//		Classifier m_classifier = (Classifier) Class.forName("weka.classifiers.functions.LibSVM").newInstance();
		MultilayerPerceptron m_classifier=new MultilayerPerceptron();
		m_classifier.setHiddenLayers("T");
		m_classifier.setLearningRate(0.1);
		m_classifier.normalizeAttributesTipText();
//		Classifier m_classifier = new LibSVM();
//		Classifier m_classifier = ensembleWay();
		System.out.println(filePath);
	    File inputFile = new File(filePath+"/train_set.arff");//训练语料文件
	    
	    ArffLoader atf = new ArffLoader(); 
	    atf.setFile(inputFile);
	    Instances instancesTrain = atf.getDataSet(); // 读入训练文件    
	    instancesTrain.setClassIndex(instancesTrain.numAttributes()-1);


    	numToNom.setAttributeIndices("" + (instancesTrain.classIndex()+1));
    	numToNom.setInputFormat(instancesTrain);
    	Instances instancesT=Filter.useFilter(instancesTrain, numToNom);
    	
    	instancesT.randomize(new Random());  
        double right=0.0f;
        int sum_sum=0;
         
        for (int i = 0; i < 10; i++) {
        	 Instances train = instancesT.trainCV(10, i);  
             Instances test = instancesT.testCV(10, i); 
             int sum = test.numInstances();
             sum_sum+=sum;
             m_classifier.buildClassifier(train);
             for(int j = 0; j < sum; j++) {
            	 if(m_classifier.classifyInstance(test.instance(j))==test.instance(j).classValue())
            		 right++;
             }
        }

        System.out.println(right/sum_sum);
    }
	
	public static void main(String[] args) throws IOException, JSONException,Exception{
		// TODO Auto-generated method stub
		
		String filePath="DM_course_project";
		tests_num=0;
		getFiles(filePath);
//		System.out.println("file_num:" + filelist.size());
		for (int i = 0; i < filelist.size(); i++) {
			readFromJson(filelist.get(i),"train_set");
			readFromJson(filelist.get(i),"test_set");
			trainAndPredict(filelist.get(i));
//			fold10Test(filelist.get(i));
		}
		
		write2csv();
		
		

	}

}
