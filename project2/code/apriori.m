%% ʹ��Apriori�㷨�ھ��������
clear;
t1 = clock;
% ������ʼ��
inputfile = 'Groceries.txt'; % ������������������
outputfile='as.txt';% ���ת����0,1�����ļ�
minSup = 0.01; % ��С֧�ֶ�
minConf = 0.5;% ��С���Ŷ�
nRules = 100;% �����������
sortFlag = 1;% ����֧�ֶ�����
rulefile = 'rules.txt'; % ��������ļ�

%% ����ת������ ��������ת��Ϊ0,1�����Զ��庯��
[transactions,code] = trans2matrix(inputfile,outputfile,','); 

%% ����Apriori���������㷨���Զ��庯��
[Rules,FreqItemsets] = findRules(transactions, minSup, minConf, nRules, sortFlag, code, rulefile);

t2=clock;
disp('Apriori�㷨�ھ��Ʒ��������������ɣ�');
disp('��ʱ��');
etime(t2,t1)