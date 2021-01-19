# docker install

### 加速
原始值：

{
  "experimental": false,
  "features": {
    "buildkit": true
  }
}

改为：

{
  "debug": true,
  "experimental": false,
  "registry-mirrors": [
    "https://docker.mirrors.ustc.edu.cn",
    "https://hub-mirror.c.163.com"
  ]
}


