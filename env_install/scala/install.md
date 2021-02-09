# scala安装

### mac安装
brew install scala@2.11


    scala@2.11 is keg-only, which means it was not symlinked into /usr/local,
    because this is an alternate version of another formula.

    If you need to have scala@2.11 first in your PATH run:
    echo 'export PATH="/usr/local/opt/scala@2.11/bin:$PATH"' >> ~/.zshrc

echo 'export PATH="/usr/local/opt/scala@2.11/bin:$PATH"' >> ~/.zshrc

**问题1**：安装包下多了一个idea目录（软连接），导致项目编译时报存在多个scala-library.jar包。

解决：删掉idea目录即可。

**问题2**：命令行输入：scala，会报错：

    [ERROR]Failed to construct terminal;falling back to unsupported
    java.lang.NumberFormatException: For input string: "0x100"

解决：

    vim ~/.bash_profile
    添加：
    export TERM=xterm-color

    source ~/.bash_profile


### windows安装

下载链接：https://www.scala-lang.org/download/2.11.12.html

下载该包：
scala-2.11.12.msi	Windows (msi installer)	109.82M

配置环境变量：
* SCALA_HOME
* PATH
* CLASSPATH
