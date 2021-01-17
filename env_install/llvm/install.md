# llvm 安装

### mac安装

brew install llvm

    export PATH="/usr/local/opt/llvm/bin:$PATH"
    export LDFLAGS="-L/usr/local/opt/llvm/lib"
    export CPPFLAGS="-I/usr/local/opt/llvm/include"

    To use the bundled libc++ please add the following LDFLAGS:
    LDFLAGS="-L/usr/local/opt/llvm/lib -Wl,-rpath,/usr/local/opt/llvm/lib"

    llvm is keg-only, which means it was not symlinked into /usr/local,
    because macOS already provides this software and installing another version in
    parallel can cause all kinds of trouble.

    If you need to have llvm first in your PATH run:
    echo 'export PATH="/usr/local/opt/llvm/bin:$PATH"' >> ~/.zshrc

    For compilers to find llvm you may need to set:
    export LDFLAGS="-L/usr/local/opt/llvm/lib"
    export CPPFLAGS="-I/usr/local/opt/llvm/include"

### 常用命令

* clang -emit-llvm -S fun1.c -o fun1.ll
    
    -emit-llvm 参数告诉 Clang 生成 LLVM 的汇编码，也就是 IR 代码（如果不带这个参数，就会生成针对目标机器的汇编码）
    
* clang -emit-llvm -S -O2 fun1.c -o fun1.ll

    -O2代码优化
    
* llvm-as fun1.ll -o fun1.bc

    调用 llvm-as 命令，把文本格式转换成字节码格式
    
* clang -emit-llvm -c fun1.c -o fun1.bc

    用 clang 直接生成字节码，这时不需要带 -S 参数，而是要用 -c 参数
    
* llc fun1.bc -o fun1.s

    把字节码编译成目标平台的汇编代码
    
* clang -S fun1.bc -o fun1.s

    用 clang 命令也能从字节码生成汇编代码，要注意带上 -S 参数就行了
