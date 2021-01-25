# centos dfn install

	yum install epel-release -y
	yum install dnf -y
	

问题：
1、执行dnf命令报错：

	Traceback (most recent call last):
	  File "/usr/bin/dnf", line 57, in <module>
		from dnf.cli import main
	  File "/usr/lib/python2.7/site-packages/dnf/__init__.py", line 30, in <module>
		import dnf.base
	  File "/usr/lib/python2.7/site-packages/dnf/base.py", line 29, in <module>
		import libdnf.transaction
	  File "/usr/lib64/python2.7/site-packages/libdnf/__init__.py", line 3, in <module>
		from . import conf
	  File "/usr/lib64/python2.7/site-packages/libdnf/conf.py", line 17, in <module>
		_conf = swig_import_helper()
	  File "/usr/lib64/python2.7/site-packages/libdnf/conf.py", line 16, in swig_import_helper
		return importlib.import_module('_conf')
	  File "/usr/lib64/python2.7/importlib/__init__.py", line 37, in import_module
		__import__(name)
	ImportError: No module named _conf
	
	
解决：
	
	先升级python:

	yum update python*


	再安装以下软件：

	yum install dnf-data dnf-plugins-core libdnf-devel libdnf python2-dnf-plugin-migrate dnf-automatic