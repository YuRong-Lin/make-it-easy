# scala安装

### mac安装
brew install scala@2.11
echo 'export PATH="/usr/local/opt/scala@2.11/bin:$PATH"' >> ~/.zshrc

问题：安装包下多了一个idea目录（软连接），导致项目编译时报存在多个scala-library.jar包。
解决：删掉idea目录即可。

	scala@2.11 is keg-only, which means it was not symlinked into /usr/local,
	because this is an alternate version of another formula.

	If you need to have scala@2.11 first in your PATH run:
 	echo 'export PATH="/usr/local/opt/scala@2.11/bin:$PATH"' >> ~/.zshrc
