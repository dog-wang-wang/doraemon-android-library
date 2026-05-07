# 1. 仓库简介
放一些平时使用的基础的Android基建类
命名参考我最喜欢的动漫![哆啦A梦](assets/img/readme_bg.webp "哆啦A梦")

# 2. 分别介绍一下吧
## 2.1. app介绍
app目录下是本项目的demoApp，目标是涵盖本项目的所有library的用法
## 2.2. library介绍
### 2.2.1. foundation 
作为一个Android-Library，该模块作为所有模块的基础设施。
涵盖以下几个内容
1. 基础Application
2. 基础的Activity兼容平板与手机（横竖屏）
3. 基础的Fragment兼容平板与手机（横竖屏）
4. Activity的基础共享元素动画配置
5. 在Activity，ViewModel，Fragment等含有context的环境下无痛使用协程的扩展函数：launchIO，launchMain，withMain，withIO。主要是省略了每次用作用域调用的看起来很累的写法