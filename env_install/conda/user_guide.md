# conda

### 官方安装源
https://repo.anaconda.com/miniconda/

### 清华源
https://mirrors.tuna.tsinghua.edu.cn/help/anaconda/

    Anaconda 镜像使用帮助
    Anaconda 是一个用于科学计算的 Python 发行版，支持 Linux, Mac, Windows, 包含了众多流行的科学计算、数据分析的 Python 包。

    Anaconda 安装包可以到 https://mirrors.tuna.tsinghua.edu.cn/anaconda/archive/ 下载。

    TUNA 还提供了 Anaconda 仓库与第三方源（conda-forge、msys2、pytorch等，查看完整列表）的镜像，各系统都可以通过修改用户目录下的 .condarc 文件。Windows 用户无法直接创建名为 .condarc 的文件，可先执行 conda config --set show_channel_urls yes 生成该文件之后再修改。

    注：由于更新过快难以同步，我们不同步pytorch-nightly, pytorch-nightly-cpu, ignite-nightly这三个包。

    channels:
      - defaults
    show_channel_urls: true
    default_channels:
      - https://mirrors.tuna.tsinghua.edu.cn/anaconda/pkgs/main
      - https://mirrors.tuna.tsinghua.edu.cn/anaconda/pkgs/free
      - https://mirrors.tuna.tsinghua.edu.cn/anaconda/pkgs/r
      - https://mirrors.tuna.tsinghua.edu.cn/anaconda/pkgs/pro
      - https://mirrors.tuna.tsinghua.edu.cn/anaconda/pkgs/msys2
    custom_channels:
      conda-forge: https://mirrors.tuna.tsinghua.edu.cn/anaconda/cloud
      msys2: https://mirrors.tuna.tsinghua.edu.cn/anaconda/cloud
      bioconda: https://mirrors.tuna.tsinghua.edu.cn/anaconda/cloud
      menpo: https://mirrors.tuna.tsinghua.edu.cn/anaconda/cloud
      pytorch: https://mirrors.tuna.tsinghua.edu.cn/anaconda/cloud
      simpleitk: https://mirrors.tuna.tsinghua.edu.cn/anaconda/cloud
    即可添加 Anaconda Python 免费仓库。

    运行 conda clean -i 清除索引缓存，保证用的是镜像站提供的索引。

    运行 conda create -n myenv numpy 测试一下吧。

    Miniconda 镜像使用帮助
    Miniconda 是一个 Anaconda 的轻量级替代，默认只包含了 python 和 conda，但是可以通过 pip 和 conda 来安装所需要的包。

    Miniconda 安装包可以到 https://mirrors.tuna.tsinghua.edu.cn/anaconda/miniconda/ 下载。


    其他配置方式：
    conda config --add channels https://mirrors.tuna.tsinghua.edu.cn/anaconda/pkgs/free/
    conda config --add channels https://mirrors.tuna.tsinghua.edu.cn/anaconda/pkgs/main/
    conda config --add channels https://mirrors.tuna.tsinghua.edu.cn/anaconda/cloud/conda-forge
    conda config --add channels https://mirrors.tuna.tsinghua.edu.cn/anaconda/cloud/msys2
    conda config --set show_channel_urls yes
    
    pip config set global.index-url https://pypi.tuna.tsinghua.edu.cn/simple
    pip install xx -i https://pypi.tuna.tsinghua.edu.cn/simple


### docs
https://docs.conda.io/projects/conda/en/latest/


### macos install
1. brew install miniconda(更新brew国内源，但始终用的官方源下载，导致特别慢最后失败)
2. 到清华镜像站下载pkg包直接安装

### linux
https://docs.conda.io/projects/conda/en/latest/user-guide/install/index.html

    下载安装文件：Miniconda3-py37_4.9.2-Linux-x86_64.sh
    
    执行：
    bash Miniconda3-py37_4.9.2-Linux-x86_64.sh
