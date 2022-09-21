本SDK运行环境：

1、Android API级别28及以上
2、工作空间保持UTF-8编码环境
3、如果是Eclipse开发，JDK须7.0及以下
4、如果是Android Studio开发，JDK随Android Studio
5、android-support-v4包请务必使用本SDK中已有的jar或者保持gradle中的配置不要改动
6、模块要求targetSdkVersion为28
7、Android Studio请自行配置Gradle，Gradle版本大于等于3.3，Android Gradle tools大于等2.3
8、使用Android Studio开发，导入工程前应先将local.properties中的SDK以及NDK路径配置为你本地安装的路径，加快运行速度

模块包说明：

1、moduleDemo.zip为SDK中带的模块开发实例：moduleDemo模块，演示基本模块开发的基本知识，所导出的模块包示例。
2、scrollPicture.zip为SDK中带的模块开发实例：scrollPicture模块，演示如何开发UI类的模块，所导出的模块包示例。
3、cusHeader.zip为SDK中带的模块开发实例：cusHeader模块，演示如何开发自定义下拉刷新头模块，所导出的模块包示例。
4、syncModule.zip为SDK中带的模块开发实例：syncModule模块，演示如何开发同步接口模块，所导出的模块包示例。
5、代码中EasDelegate.java为ApplicationDelegate使用实例，演示如何在模块中，通过ApplicationDelegate代理原本需要在Application中初始化的代码。applicationDelegate.zip为该模块导出的包

模块开发快速入门文档：http://docs.apicloud.com/Module-Dev/module-dev-guide-for-android
模块API文档：http://docs.apicloud.com/superwebview/Android/