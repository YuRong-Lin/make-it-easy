# 修改CentOS默认yum源为国内yum镜像源

### 修改CentOS默认yum源为mirrors.163.com
1、首先备份系统自带yum源配置文件/etc/yum.repos.d/CentOS-Base.repo

　　mv /etc/yum.repos.d/CentOS-Base.repo /etc/yum.repos.d/CentOS-Base.repo.backup

2、进入yum源配置文件所在的文件夹

　　cd /etc/yum.repos.d/

3、下载163的yum源配置文件到上面那个文件夹内

　　CentOS7

　　　　wget http://mirrors.163.com/.help/CentOS7-Base-163.repo

　　CentOS6

　　　　wget http://mirrors.163.com/.help/CentOS6-Base-163.repo

　　CentOS5

　　　　wget http://mirrors.163.com/.help/CentOS5-Base-163.repo

4、运行yum makecache生成缓存

　　yum makecache

5、这时候再更新系统就会看到以下mirrors.163.com信息

　　yum -y update

 

### 修改CentOS默认yum源为mirrors.aliyun.com
　　步骤跟上面的一样只是下载源不一样

　　CentOS7

　　　　wget -O /etc/yum.repos.d/CentOS-Base.repo http://mirrors.aliyun.com/repo/Centos-7.repo

　　CentOS6

　　　　wget -O /etc/yum.repos.d/CentOS-Base.repo http://mirrors.aliyun.com/repo/Centos-6.repo

　　CentOS5

　　　　wget -O /etc/yum.repos.d/CentOS-Base.repo http://mirrors.aliyun.com/repo/Centos-5.repo

 

详情查看

　　https://blog.csdn.net/inslow/article/details/54177191