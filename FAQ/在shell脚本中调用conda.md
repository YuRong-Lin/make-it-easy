# 在shell脚本中切换conda环境

    常用的conda activate切换conda环境的方法只能在命令行中使用，放在脚本中会报错，该如何解决呢？
    
## 解决方案
1.my path to miniconda

    conda info | grep -i 'base environment'
    
2.尝试使用source ~/conda.sh

    在shell脚本中添加：
    source {your miniconda path}/etc/profile.d/conda.sh
