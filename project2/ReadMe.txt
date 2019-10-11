提交文件结构及功能如下：

code
    apriori.m
    trans2matrix.m
    findRules.m      //以上三个文件为apriori算法在matlab中的实现

    csv2arff.java
    csv2txt.java      //以上两个文件为对第一个数据集进行处理的代码，分别将其转为了.arff和.txt文件

    file2arff.java
    file2txt.java     //以上两个文件为对第二个数据集进行处理的代码，分别将其转为了.arff和.txt文件

data
    Groceries.arff     //将数据集一的.csv文件处理为.arff文件
    Groceries.txt      //将数据集一的.csv文件处理为.txt文件

    UNIX_usage.arff     //将数据集二的所有数据合并为一张表并存为.arff文件
    UNIX_usage.txt      //将数据集二的所有数据合并为一张表并存为.txt文件

report
    141220132.pdf    //实验报告

result
    rules.txt          //数据集一在Apriori中当支持度为0.01，置信度为0.5时的关联规则输出结果
    rules2.txt         //数据集二在Apriori中当支持度为0.01，置信度为0.5时的关联规则输出结果