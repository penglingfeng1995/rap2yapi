# rap转换yapi工具

[yapi使用教程](https://yapi.baidu.com/doc/documents/index.html)

## 使用方法

### 导出

进入rap项目，点击`更多功能` -> `导出备份` ，导出rap的json数据

然后会弹出一个新的页面，使用 Ctrl + A 全选，Ctrl + C 复制全文

保存为 utf-8 编码格式的 `.json` 文件，建议用notepad++，编码不对无法读取

### 转换

调用测试用例，进行转换，rap的json文件作为第一个参数，第二个参数指定要生成的文件路径。

```java
@Test
void translate(){
    File sourceRapJsonFile = new File("E:/rap/ex.json");
    File targetYapiJsonFile = new File("E:/rap/gen.json");
    yapiService.transJsonFile(sourceRapJsonFile,targetYapiJsonFile);
}
```

### 导入

进入yapi项目-->数据管理，格式选择json，分类选择默认的公共分类即可，首次导入无需勾选 `开启数据同步` ，后续如果会导入重复的数据的话，勾选上。上传文件即可导入。

## 项目差异

### 1，目录结构

rap 支持二级目录，而yapi只要一级

这边导入后 ，会把二级目录与接口名称合并作为 yapi 的接口名称

### 2，接口路径

rap 上的接口路径，不会做限制，可以为空。

yapi 上的接口不能为空，且必须以 `/` 开头。

转换的时候会补全 `/` ，但是 url 为空的接口会导入失败。

### 3，json 参数格式数据展示

rap 是以 节点的形式 展示参数。

yapi 支持两种模式

不开启 json-schema ，直接展示 json5 的格式，可以加注释

开启 json-schema ，以节点的形式展示

两种格式 yapi 只保存一种，使用节点的形式，符合之前使用rap的习惯，而且功能更丰富，可以指定类型，是否必选，默认值，最大值最小值等。

### 4，GET请求的参数限制

rap 不会做任何限制，即使GET请求也可以定义object的参数。

yapi 选择了 GET 请求，则只能传 Query params 。

转换时，rap 上GET请求的子节点上的参数会舍弃。