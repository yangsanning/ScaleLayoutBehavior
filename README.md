# ScaleLayoutBehavior
[![](https://jitpack.io/v/yangsanning/ScaleLayoutBehavior.svg)](https://jitpack.io/#yangsanning/ScaleLayoutBehavior)
[![API](https://img.shields.io/badge/API-19%2B-orange.svg?style=flat)](https://android-arsenal.com/api?level=19)

## 效果预览

| [ScaleLayoutBehavior]                      |
| ------------------------------- |
| [<img src="images/image.gif" height="512"/>][ScaleLayoutBehavior] |


## 主要文件
| 名字             | 摘要           |
| ---------------- | -------------- |
|[ScaleLayoutBehavior]  | 缩放layout的Behavior |

### 1.基本用法 
> 由于使用起来相当复杂，所以还是自己下载Demo参考吧~

### 2.添加方法

#### 2.1 添加仓库

在项目的 `build.gradle` 文件中配置仓库地址。

```android
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```

#### 2.2 添加项目依赖

在需要添加依赖的 Module 下添加以下信息，使用方式和普通的远程仓库一样。

```android
implementation 'com.github.yangsanning:ScaleLayoutBehavior:1.0.0'
```

[ScaleLayoutBehavior]:https://github.com/yangsanning/ScaleLayoutBehavior/blob/master/scalelayoutbehavior/src/main/java/ysn/com/behavior/scalelayout/ScaleLayoutBehavior.java
