%% 使用Apriori算法挖掘关联规则
clear;
t1 = clock;
% 参数初始化
inputfile = 'Groceries.txt'; % 销量及其他属性数据
outputfile='as.txt';% 输出转换后0,1矩阵文件
minSup = 0.01; % 最小支持度
minConf = 0.5;% 最小置信度
nRules = 100;% 输出最大规则数
sortFlag = 1;% 按照支持度排序
rulefile = 'rules.txt'; % 规则输出文件

%% 调用转换程序 ，把数据转换为0,1矩阵，自定义函数
[transactions,code] = trans2matrix(inputfile,outputfile,','); 

%% 调用Apriori关联规则算法，自定义函数
[Rules,FreqItemsets] = findRules(transactions, minSup, minConf, nRules, sortFlag, code, rulefile);

t2=clock;
disp('Apriori算法挖掘菜品订单关联规则完成！');
disp('用时：');
etime(t2,t1)