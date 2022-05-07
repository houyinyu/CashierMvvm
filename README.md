# CashierMvvm
收银系统（附带mvvm模式）

##使用方式

```Java
    implementation 'com.github.houyinyu:mvvmLib:1.0'
```



###Github依赖创建方式

在依赖的build.gradle 下添加如下代码
```Java
   id 'maven-publish'
```
和
```Java
   afterEvaluate {
       publishing {
           publications {
               release(MavenPublication) {
                   from components.release
                   groupId = 'com.github.houyinyu'//修改成自己的
                   artifactId = 'mvvmLib'//修改成自己的
                   version = '1.0'//修改成自己的
               }
           }
       }
   }
```

在主项目project的build.gradle下添加如下代码（不是app的build.gradle）
```Java
      dependencies {
           classpath 'com.github.dcendents:android-maven-gradle-plugin:2.1'
       }
```

