import numpy as np 
data = np.loadtxt("sample.csv",delimiter=',')
 X1 = data[0:400,0:11]
 Y1 = data[0:400,12]
 X2 = data[400:,0:11]
 Y2 = data[400:,12]
from sklearn import svm
clf = svm.SVC(gamma=0.001, C=100.)
clf.fit(X1,Y1)
svmres = clf.predict(X2)
print("Number of mislabeled points out of a total %d points : %d"
% (X2.shape[0],(Y2 != svmres).sum()))