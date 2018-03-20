# my_fontello
fontello快速生成字体


## 1. 安装node.js, npm
#### 官网: [https://node.js.org/en/](https://nodejs.org/en/)
#### ubuntu
        下载最新版本tar.xz文件后解压  先xz -d xxx.tar.xz   再tar xvf xxx.tar
        在bin目录下可以看到两个可执行文件 node 和 npm，然后执行

        sudo ln -s /home/用户名/tools/node-v6.9.1-linux-x64/bin/node /usr/local/bin/node
        sudo ln -s /home/用户名/tools/node-v6.9.1-linux-x64/bin/npm /usr/local/bin/npm

#### windows直接官网下载安装

## 2. 安装svgpath 和 svg2ttf

#### 安装命令
    sudo npm install svgpath -g
    sudo npm install svg2ttf -g
        (windows系统不需要sudo)
#### ubuntu配置软链接
    sudo ln -s /home/用户名/tools/node-v6.9.1-linux-x64/lib/node_modules/svg2ttf/svg2ttf.js /usr/local/bin/svg2ttf
#### windows不需要


## 3. 配置changePath.js文件
#### ubuntu
    1. 将changePath.js文件复制到/home/用户名/tools/node-v6.9.1-linux-x64/lib/node_modules/svgpath目录下
    2. 配置软链接sudo ln -s /home/用户名/tools/node-v6.9.1-linux-x64/lib/node_modules/svgpath/changePath.js /usr/local/bin/svgpath
#### windows
    1. 将changePath.js文件复制到C:\Users\用户名\AppData\Roaming\npm\node_modules\svgpath目录下
    2. svgpath和svgpath.cmd文件复制到C:\Users用户名\AppData\Roaming\npm目录下


## 4. 安装potrace ,imagemagick
#### ubuntu
    sudo apt-get install potrace imagemagick
#### win7
    将目录下的ImageMagick-7.0.1-4-Q16-x64-dll.zip和potrace-1.15.win64.zip解压安装并设置环境变量
        PATH后面添加D:\Program Files\ImageMagick-7.0.1-Q16和D:\Program Files\potrace-1.15.win64(D:\Program Files是安装目录)

## 5. 功能