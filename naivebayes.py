import numpy as np
data = np.loadtxt(“sample.csv”,delimiter=’,’)
X = data[:,0:11]
Y = data[:,12]
from sklearn.naive_bayes import MultinomialNB
nb = MultinomialNB()
res = nb.fit(X,Y).predict(X)
print("Number of mislabeled points out of a total %d points : %d"
...  % (X.shape[0],(Y != res).sum()))
