
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.BayesNet;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.LibSVM;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.meta.Vote;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.RandomForest;
import weka.core.Instances;
import weka.core.SelectedTag;
import weka.core.Debug.Random;
import weka.core.converters.ArffLoader;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.NumericToNominal;
import weka.filters.unsupervised.attribute.StringToNominal;

//import net.sf.json.JSONArray;
//import net.sf.json.JSONObject;



public class sumTest {
	private static String project_name;
	private static int build_id;
	private static int comment_count;
	private static String author_name;
	private static int author_id;
	private static String author_type;
	private static String author_site_admin;
	private static String committer_name;
	private static int committer_id;
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
	private static HashMap<Integer, Integer> map = new HashMap<Integer ,Integer>();
	private static HashMap<Integer, Integer> testMap = new HashMap<Integer, Integer>();
	
	private static ArrayList<String> filelist= new ArrayList<String>();

	public static void readFromJson (String filename,String file, FileWriter filewriter)throws IOException{//, JSONException{
		String path = filename+"/" + file+".txt";

		BufferedReader in = new BufferedReader(new FileReader(path));
		String data = in.readLine(); 
//		String out = filename+"/"+file+".arff";
		System.out.println(filename);
		
		String str="";
		try {
			JSONArray jsonObjArray=new JSONArray(data);
	//		System.out.println(jsonObjArray.length());
			if(file.equals("test_set")) {
				tests_num+=jsonObjArray.length();
			}
			sumOfIns=0;
			if (file.equals("train_set")) {
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
//				System.out.println(jsonObjArray.length() + " " + sumOfIns);
				rate = (double)rate/sumOfIns;
			}
//			author_id=-1;
//			author_type="?";
//			author_site_admin="?";
//			committer_id=-1;
//			committer_type="?";
//			committer_site_admin="?";
//			stats_total=0;
//			stats_additions=0;
//			stats_deletions=0;
//			files_filename="?";
//			files_status="none";
//			files_additions=0;
//			files_deletions=0;
//			files_changes=0;
//			num_commit=0;
//			num_file=0;
			for (int i = 0; i < jsonObjArray.length(); i++) {
				project_name="";
				author_id=-1;
				author_type="?";
				author_site_admin="?";
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
//				System.out.println(i);
				str=jsonObjArray.getString(i);
				JSONObject jsonObj = (JSONObject) jsonObjArray.get(i);
				project_name=jsonObj.getString("project_name");
				build_id=jsonObj.getInt("build_id");
			
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
//						if (file.equals("train_set")) {
//							filewriter.write(rate + ", " + build_id+", "+
//									+author_id+", "
//									+committer_id+", "
//									+stats_total+", "+stats_additions+", "+stats_deletions+", "
//									+files_status + ", " +files_additions+", "+files_deletions+", "+files_changes+", "
//									+res+"\n");
//						}
//						else if(file.equals("test_set")) {
//							filewriter.write(rate + ", " + build_id+", "+
//									+author_id+", "
//									+committer_id+", "
//									+stats_total+", "+stats_additions+", "+stats_deletions+", "
//									+files_status + ", " +files_additions+", "+files_deletions+", "+files_changes+", " 
//									+ "?\n");
//						}
					
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
//						if (file.equals("train_set")) {
//							filewriter.write(rate + ", " + build_id+", "+
//									+author_id+", "
//									+committer_id+", "
//									+stats_total+", "+stats_additions+", "+stats_deletions+", "
//									+files_status + ", " +files_additions+", "+files_deletions+", "+files_changes+", "
//									+res+"\n");
//						}
//						else if(file.equals("test_set")) {
//							filewriter.write(rate + ", " + build_id+", "+
//									+author_id+", "
//									+committer_id+", "
//									+stats_total+", "+stats_additions+", "+stats_deletions+", "
//									+files_status + ", " +files_additions+", "+files_deletions+", "+files_changes+", " 
//									+ "?\n");
//						}
					}
					else {
						JSONObject commitsObj=(JSONObject) commits.get(j);
						
						if (!commitsObj.has("commit") || commitsObj.get("commit").toString().equals("null") || commitsObj.get("commit").toString().equals("{}")) {
							author_name="";
							committer_name="";
							message="";
							comment_count=0;
						}
						else {
							JSONObject commitObj=(JSONObject) commitsObj.get("commit");
							JSONObject authorObj=commitObj.getJSONObject("author");
							author_name=authorObj.getString("name");
							JSONObject committerObj=commitObj.getJSONObject("committer");
							committer_name=committerObj.getString("name");
							message=commitObj.getString("message");
							comment_count=commitObj.getInt("comment_count");
						}
		//				System.out.println(commitsObj.get("author").toString().equals("{}"));
						if (!commitsObj.has("author") || commitsObj.get("author").toString().equals("null") || commitsObj.get("author").toString().equals("{}")) {
							author_id=-1;
							author_type="";
							author_site_admin="";
						}
						else {
							JSONObject authorObj=commitsObj.getJSONObject("author");
		//					System.out.println("00");
							author_id=authorObj.getInt("id");
		//					System.out.println("11");
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
		//					System.out.println("22");
							committer_type=comitterObj.getString("type");
							committer_site_admin=comitterObj.getString("site_admin");
						}
		//				System.out.println(commitsObj.has("stats"));
		
						if(!commitsObj.has("stats") || commitsObj.get("stats").toString().equals("null") || commitsObj.get("stats").toString().equals("{}")) {
		//					System.out.println("11");
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
//							if (file.equals("train_set")) {
//								filewriter.write(rate + ", " + build_id+", "+
//										+author_id+", "
//										+committer_id+", "
//										+stats_total+", "+stats_additions+", "+stats_deletions+", "
//										+files_status + ", " +files_additions+", "+files_deletions+", "+files_changes+", "
//										+res+"\n");
//							}
//							else if(file.equals("test_set")) {
//								filewriter.write(rate + ", " + build_id+", "+
//										+author_id+", "
//										+committer_id+", "
//										+stats_total+", "+stats_additions+", "+stats_deletions+", "
//										+files_status + ", " +files_additions+", "+files_deletions+", "+files_changes+", " 
//										+ "?\n");
//							}
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
//								if (file.equals("train_set")) {
//									filewriter.write(rate + ", " + build_id+", "+
//											+author_id+", "
//											+committer_id+", "
//											+stats_total+", "+stats_additions+", "+stats_deletions+", "
//											+files_status + ", " +files_additions+", "+files_deletions+", "+files_changes+", "
//											+res+"\n");
//								}
//								else if(file.equals("test_set")) {
//									filewriter.write(rate + ", " + build_id+", "+
//											+author_id+", "
//											+committer_id+", "
//											+stats_total+", "+stats_additions+", "+stats_deletions+", "
//											+files_status + ", " +files_additions+", "+files_deletions+", "+files_changes+", " 
//											+ "?\n");
//								}
							}
							else {
								for(int k = 0; k < files.length(); k++) {
									JSONObject filesObj=(JSONObject) files.get(k);
									
									files_filename=filesObj.getString("filename");
									files_status=filesObj.getString("status");
									files_additions+=filesObj.getInt("additions");
									files_deletions+=filesObj.getInt("deletions");
									files_changes+=filesObj.getInt("changes");
			
		//							System.out.println(project_name+", "+build_id+", "+build_result+", "
		//									+author_id+", "+author_type+", "+author_site_admin+", "
		//									+committer_id+", "+committer_type+", "+committer_site_admin+", "
		//									+stats_total+", "+stats_additions+", "+stats_deletions+", "
		//									+files_filename+", "+files_status+", "+files_additions+", "+files_deletions+", "+files_changes);
//									if (file.equals("train_set")) {
//										filewriter.write(rate + ", " +  build_id+", "+
//												+author_id+", "
//												+committer_id+", "
//												+stats_total+", "+stats_additions+", "+stats_deletions+", "
//												+files_status + ", "+files_additions+", "+files_deletions+", "+files_changes+", "
//												+res+"\n");
//									}
//									else if(file.equals("test_set")) {
//										filewriter.write(rate + ", " + build_id+", "+
//												+author_id+", "
//												+committer_id+", "
//												+stats_total+", "+stats_additions+", "+stats_deletions+", "
//												+files_status + ", " +files_additions+", "+files_deletions+", "+files_changes+", " 
//												+ "?\n");
//									}
								}
							}
						}
					}
				}
				if (file.equals("train_set")) {
					filewriter.write(rate + ", " + build_id + ", "
//							+ author_name +", "
//							+committer_name+", "
							+ author_id +", "
							+committer_id+", "
							+stats_total+", "+stats_additions+", "+stats_deletions+", "
							+files_status + ", "+files_additions+", "+files_deletions+", "+files_changes+", "
							+num_commit + ", "
							+res+"\n");
				}
				else if(file.equals("test_set")) {
					filewriter.write(rate + ", " + build_id + ", "
//							+author_name+", "
//							+committer_name+", "
							+author_id+", "
							+committer_id+", "
							+stats_total+", "+stats_additions+", "+stats_deletions+", "
							+files_status + ", " +files_additions+", "+files_deletions+", "+files_changes+", " 
							+num_commit + ", "
							+ "?\n");
				}
			}
			filewriter.flush();
		} catch(JSONException j) {
			j.printStackTrace();
			System.out.println(str);
		}
		data=null;
//		System.out.println(jsonObjArray.length());
	}
	
	public static Classifier ensembleWay() throws ClassNotFoundException, IllegalAccessException, InstantiationException{
		Classifier cfs1 = null;
		Classifier cfs2 = null;
	    Classifier cfs3 = null;
	    Classifier[] cfsArray = new Classifier[3];
	    cfs1 = (Classifier)Class.forName("weka.classifiers.bayes.NaiveBayes").newInstance();
        //决策树算法，是我们常听说的C45的weka版本，不过在我看代码的过程中发现有一些与原始算法有点区别的地方。
        //即在原始的C45算法中，我们规定没有一个属性节点在被使用（即被作为一个分裂节点以后，他将被从属性集合中去除掉）。
        //但是在J48中没有这样做，它依然在下次分裂点前，使用全部的属性集合来探测一个合适的分裂点。这样做好不好？
        cfs2 = (Classifier)Class.forName("weka.classifiers.trees.J48").newInstance();
        //什么东东，不知道做什么用，平常很少用。本想要用LibSVM的，但是由于要加载一些包，比较麻烦。
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
	
	
	public static void trainAndPredict() throws IOException,Exception{
		NumericToNominal numToNom=new NumericToNominal();
		StringToNominal strToNom;
//		Classifier m_classifier = new NaiveBayes();
//		Classifier m_classifier = new J48();
		Classifier m_classifier = new BayesNet();
//		RandomForest m_classifier = new RandomForest();
//		Classifier m_classifier = (Classifier) Class.forName("weka.classifiers.functions.LibSVM").newInstance();
//		MultilayerPerceptron m_classifier=new MultilayerPerceptron();
//		m_classifier.setHiddenLayers("t");
//		m_classifier.setLearningRate(0.1);
//		m_classifier.normalizeAttributesTipText();
//		m_classifier.s
//		Classifier m_classifier = new LibSVM();
//		Classifier m_classifier = ensembleWay();
//		System.out.println(filePath);
	    File inputFile = new File("train.arff");//训练语料文件
	    
	    ArffLoader atf = new ArffLoader(); 
	    atf.setFile(inputFile);
	    Instances instancesTrain = atf.getDataSet(); // 读入训练文件    
	    instancesTrain.setClassIndex(instancesTrain.numAttributes()-1);


    	numToNom.setAttributeIndices("" + (instancesTrain.classIndex()+1));
//    	System.out.println("" + (instancesTrain.classIndex()+1));
    	numToNom.setInputFormat(instancesTrain);
    	Instances instancesT=Filter.useFilter(instancesTrain, numToNom);
    	
    	Instances instancesT1 = instancesT;
    	for (int index = 0; index < instancesT.numAttributes(); index ++) {
    		if (instancesT.attribute(index).isString()){
    			System.out.println("11111");
    			strToNom = new StringToNominal();
    			strToNom.setInputFormat(instancesTrain);
    			instancesT1 = Filter.useFilter(instancesT, strToNom);
    		}
    	}
	    
	    inputFile = new File("test.arff");//测试语料文件
	    atf.setFile(inputFile);          
	    Instances instancesTest = atf.getDataSet(); // 读入测试文件
	    instancesTest.setClassIndex(instancesTrain.numAttributes()-1); //设置分类属性所在行号（第一行为0号），instancesTest.numAttributes()可以取得属性总数

	    numToNom.setAttributeIndices("" + (instancesTest.classIndex()+1));
    	numToNom.setInputFormat(instancesTest);
	    Instances instancesTe=Filter.useFilter(instancesTest, numToNom);
	   
	    Instances instancesTe1 = instancesTe;
	    for (int index = 0; index < instancesTe.numAttributes(); index ++) {
    		if (instancesTe.attribute(index).isString()){
    			strToNom = new StringToNominal();
    			 strToNom.setInputFormat(instancesTest);
    			instancesTe1 = Filter.useFilter(instancesTe, strToNom);
    		}
    	}
	    
	    int sum = instancesTe1.numInstances();//测试语料实例数
	    int right = 0;
//	    instancesT.setClassIndex(10);
	
	    double[] build_id = instancesTest.attributeToDoubleArray(1);
//	    System.out.println(sum==build_id.length);
	    m_classifier.buildClassifier(instancesT1); //训练       
	    for (int i = 0; i < sum; i++) {
	    	int res = (int)m_classifier.classifyInstance(instancesTe1.instance(i));
//	    	res = res - 1;
	    	int id = (int)build_id[i];
//	    	System.out.println("id = " + id);
	    	map.put(id, res);
//	    	if (map.containsKey(id)) {
//	    		if (map.get(id)!= res) {
////	    			map.remove(id);
//	    			map.put(id, res);
//	    		}
//	    	}
//	    	else {
//	    		map.put(id, res);
//	    	}
	    }
    }
	
	public static void getFiles(String filePath) {
		File root = new File(filePath);
		File[] files = root.listFiles();
		for (File file:files) {
			if(file.isDirectory()) {
				filelist.add(file.getAbsolutePath());
			}
//			System.out.println(file.getPath());
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
//			System.out.println(i + " " + str0);
			int id = Integer.parseInt(str0);
			if(map.containsKey(id)) {
				count++;
				if(map.get(id)==-1) {
					count1++;
					filewriter.write(id + "," + 0 + "\n");
				}
				else {
					filewriter.write(id + "," + map.get(id) + "\n");
//					filewriter.write(id + "," + 0 + "\n");
				}
			}
			else {
//				System.out.print(id + " ");
				filewriter.write(id + "," + 0 + "\n");
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
//    	System.out.println("" + (instancesTrain.classIndex()+1));
    	numToNom.setInputFormat(instancesTrain);
    	Instances instancesT=Filter.useFilter(instancesTrain, numToNom);
    	
//    	m_classifier.buildClassifier(instancesT);
//    	Evaluation eval = new Evaluation(instancesT);
//        eval.crossValidateModel(m_classifier, instancesT, 5, new Random(1));
//        System.out.println(eval.errorRate());
    	
    	instancesT.randomize(new Random());  
//        if (instancesT.classAttribute().isNominal()) {  
//        	 instancesT.stratify(10);  
//        }
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
//	    instancesT.setClassIndex(10);
	
//	    System.out.println(sum==build_id.length);
    }

	public static void write2arff() throws IOException, JSONException,Exception{
		String filePath="DM_course_project";
		String out_train = "train.arff";
		String out_test = "test.arff";
		FileWriter filewriter_train = new FileWriter(out_train);
		FileWriter filewriter_test = new FileWriter(out_test);
		filewriter_train.write("@relation " + "train_data" + "\n");
		filewriter_train.write("@attribute rate numeric\n");
//		filewriter_train.write("@attribute project_name string\n");
		filewriter_train.write("@attribute build_id numeric\n");
//		filewriter_train.write("@attribute author_name string\n");
//		filewriter_train.write("@attribute committer_name string\n");
		filewriter_train.write("@attribute author_id numeric\n");
		filewriter_train.write("@attribute committer_id numeric\n");
		filewriter_train.write("@attribute stats_total numeric\n");
		filewriter_train.write("@attribute stats_additions numeric\n");
		filewriter_train.write("@attribute stats_deletions numeric\n");
		filewriter_train.write("@attribute files_status {added, removed,modified,renamed,none}\n");
		filewriter_train.write("@attribute files_additions numeric\n");
		filewriter_train.write("@attribute files_deletions numeric\n");
		filewriter_train.write("@attribute files_changes numeric\n");
		filewriter_train.write("@attribute num_commit numeric\n");
//		filewriter_train.write("@attribute num_file numeric\n");
		filewriter_train.write("@attribute build_result {0, 1}\n");   //0 passed  1 failed
		filewriter_train.write("@data\n");
		
		filewriter_test.write("@relation " + "test_data" + "\n");
		filewriter_test.write("@attribute rate numeric\n");
//		filewriter_test.write("@attribute project_name String\n");
		filewriter_test.write("@attribute build_id numeric\n");
//		filewriter_test.write("@attribute author_name String\n");
//		filewriter_test.write("@attribute committer_name String\n");
		filewriter_test.write("@attribute author_id numeric\n");
		filewriter_test.write("@attribute committer_id numeric\n");
		filewriter_test.write("@attribute stats_total numeric\n");
		filewriter_test.write("@attribute stats_additions numeric\n");
		filewriter_test.write("@attribute stats_deletions numeric\n");
		filewriter_test.write("@attribute files_status {added, removed,modified,renamed,none}\n");
		filewriter_test.write("@attribute files_additions numeric\n");
		filewriter_test.write("@attribute files_deletions numeric\n");
		filewriter_test.write("@attribute files_changes numeric\n");
		filewriter_test.write("@attribute num_commit numeric\n");
//		filewriter_test.write("@attribute num_file numeric\n");
		filewriter_test.write("@attribute build_result {0, 1}\n");   //0 passed -1 errored 1 failed
		filewriter_test.write("@data\n");
		tests_num=0;
		getFiles(filePath);
		System.out.println("file_num:" + filelist.size());
//		for (int i = 0; i < filelist.size(); i++) {
			for (int i = 0; i < 2; i++) {
			readFromJson(filelist.get(i),"train_set",filewriter_train);
			readFromJson(filelist.get(i),"test_set",filewriter_test);
		}
	}
	public static void main(String[] args) throws IOException, JSONException,Exception{
		// TODO Auto-generated method stub
		
//		String filePath="DM_course_project";
//		String out_train = "train.arff";
//		String out_test = "test.arff";
//		FileWriter filewriter_train = new FileWriter(out_train);
//		FileWriter filewriter_test = new FileWriter(out_test);
//		filewriter_train.write("@relation " + "train_data" + "\n");
//		filewriter_train.write("@attribute rate numeric\n");
//		filewriter_train.write("@attribute build_id numeric\n");
//		filewriter_train.write("@attribute author_id numeric\n");
//		filewriter_train.write("@attribute committer_id numeric\n");
//		filewriter_train.write("@attribute stats_total numeric\n");
//		filewriter_train.write("@attribute stats_additions numeric\n");
//		filewriter_train.write("@attribute stats_deletions numeric\n");
//		filewriter_train.write("@attribute files_status {added, removed,modified,renamed,none}\n");
//		filewriter_train.write("@attribute files_additions numeric\n");
//		filewriter_train.write("@attribute files_deletions numeric\n");
//		filewriter_train.write("@attribute files_changes numeric\n");
//		filewriter_train.write("@attribute build_result {-1, 0, 1}\n");   //0 passed -1 errored 1 failed
//		filewriter_train.write("@data\n");
//		
//		filewriter_test.write("@relation " + "train_data" + "\n");
//		filewriter_test.write("@attribute rate numeric\n");
//		filewriter_test.write("@attribute build_id numeric\n");
//		filewriter_test.write("@attribute author_id numeric\n");
//		filewriter_test.write("@attribute committer_id numeric\n");
//		filewriter_test.write("@attribute stats_total numeric\n");
//		filewriter_test.write("@attribute stats_additions numeric\n");
//		filewriter_test.write("@attribute stats_deletions numeric\n");
//		filewriter_test.write("@attribute files_status {added, removed,modified,renamed,none}\n");
//		filewriter_test.write("@attribute files_additions numeric\n");
//		filewriter_test.write("@attribute files_deletions numeric\n");
//		filewriter_test.write("@attribute files_changes numeric\n");
//		filewriter_test.write("@attribute build_result {-1, 0, 1}\n");   //0 passed -1 errored 1 failed
//		filewriter_test.write("@data\n");
//		tests_num=0;
//		getFiles(filePath);
//		System.out.println("file_num:" + filelist.size());
//		for (int i = 0; i < filelist.size(); i++) {
////			for (int i = 0; i < 2; i++) {
//			readFromJson(filelist.get(i),"train_set",filewriter_train);
//			readFromJson(filelist.get(i),"test_set",filewriter_test);
////			trainAndPredict(filelist.get(i));
//			
////			fold10Test(filelist.get(i));
//		}
		write2arff();
		trainAndPredict();
		write2csv();
//		System.out.println("map size:" + map.size());
//		System.out.println("tests_num:" + tests_num);
//		System.out.println("test map size:" + testMap.size());
//		readFromJson("D:/eclipse_workspace/DataMiningFinal/DM_course_project/orientation_orientation","train_set");
//		readFromJson("D:/eclipse_workspace/DataMiningFinal/DM_course_project/orientation_orientation","test_set");
		
		
		

	}

}
