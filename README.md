# DistributedID-SDK
分布式ID生成器的SDK,[DistributedID](https://github.com/beyondfengyu/DistributedID)的SDK方式接入。
<br>
* 基于Netty框架实现通信交互；
* 提供同步请求的方法；
* 提供异步请求的方法；
* 支持并发数控制；
* 易接入，不需要额外配置；
<br>

<br>
## 压测
对同步和异步的请求方式分别写了压测的程序，在自己本机CPU4核、内存8G的环境下，同步方式跟异步方式的并发请求量相差会比较大，同步方式可能比异步方式少十几倍。这压测的数据也不一定准确，但是从设计的模式来看，异步请求必然比同步请求的性能要好。
<br>
<br>
## 交流
如果希望讨论交流技术，可以加Q群：**136798125**
<br>
我的网站：[http://www.wolfbe.com](http://www.wolfbe.com)
