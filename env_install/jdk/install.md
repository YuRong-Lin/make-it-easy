# jdk安装

### mac安装

1. brew install openjdk@8
2. sudo ln -sfn /usr/local/opt/openjdk@8/libexec/openjdk.jdk /Library/Java/JavaVirtualMachines/openjdk-8.jdk

        提示信息：
        For the system Java wrappers to find this JDK, symlink it with
        sudo ln -sfn /usr/local/opt/openjdk@8/libexec/openjdk.jdk /Library/Java/JavaVirtualMachines/openjdk-8.jdk

        openjdk@8 is keg-only, which means it was not symlinked into /usr/local,
        because this is an alternate version of another formula.

        If you need to have openjdk@8 first in your PATH run:
        echo 'export PATH="/usr/local/opt/openjdk@8/bin:$PATH"' >> ~/.zshrc

        For compilers to find openjdk@8 you may need to set:
        export CPPFLAGS="-I/usr/local/opt/openjdk@8/include"

### linux安装
1. 下载安装包：jdk-8u181-linux-x64.tar.gz
2. tar -zxvf jdk-8u181-linux-x64.tar.gz -C /opt/module/
3. 配置环境变量
		1. vim /etc/profile # 在末尾添加：
		#JAVA_HOME
		export JAVA_HOME=/opt/module/jdk1.8.0_144
		export PATH=$PATH:$JAVA_HOME/bin
		
		2. source /etc/profile
		3. java -version