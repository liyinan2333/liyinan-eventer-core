# liyinan-eventer-core

一种进程内部组件间消息通信的解决方案。

* 支持异步处理；

* 支持发布-订阅模式、请求响应模式；

* 支持将任意对象作为消息体，无需实现接口或继承父类；

使用示例参考[liyinan-eventer-sample](https://github.com/liyinan2333/liyinan-eventer-sample)

maven仓库坐标：

```xml
<dependency>
    <groupId>io.github.liyinan2333</groupId>
    <artifactId>liyinan-eventer-core</artifactId>
    <version>2.0.0-RELEASE</version>
</dependency>
```

### 发布-订阅

发布-订阅模式无返回值，支持一次发布，多个Handler处理。

Publisher调用如下：

```java
TestEvent event = new TestEvent();
EventManager.get().publish(event);
```

Handler注册方式：

1、将实例注册到Spring容器中；

2、继承EventHandler，并使用泛型指定订阅的事件类型；

3、实现handle(T event)方法；

```java
@Component
public class PublishEventHandler extends EventHandler<TestEvent> {
    @Override
    protected void handle(TestEvent event) {
        // Do something.
    }
}
```

### 请求-响应

请求-响应模式有返回值，且只能有一个handler，若有多个handler订阅同一个事件，会抛出MultipleRequestHandlersException异常。

Request调用如下：

```java
TestEvent event = new TestEvent();
Object response = EventManager.get().request(event);
```

RequestHandler注册方式与发布-订阅模式基本一致，只是实现的方法为handleRequest(T event)。

```java
@Component
public class RequestHandler extends EventHandler<TestEvent> {
    @Override
    protected Object handleRequest(TestEvent event) {
        // Do something.
        return someObject;
    }
}
```

### 异步处理

需要注意的是，发布-订阅模式和请求-响应模式默认均为同步处理，待所有Handler均处理完成后才继续向下执行。若需要异步处理，为Handler类添加@Async注解即可：

```java
@Async
@Component
public class PublishEventHandler extends EventHandler<TestEvent> {
    @Override
    protected void handle(TestEvent event) {
        // Do something.
    }
}
```

### 消息路由

EventManager默认会根据Handler类的泛型来路由消息。此机制依赖于父类EventHandler中注册的DefaultRouter，此Router不做任何逻辑处理直接返回true。如果需要在泛型的基础上自定义路由规则，使用自定义的Router类覆盖默认的Router即可：

1、自定义Router类

```java
public class TestRouter implements Router<TestEvent> {
    @Override
    public boolean route(SayEvent event) {
    	try {
            // Some check logic.
        	return true;
        } catch (Exception e) {
    		return false;
        }
    }
}
```

2、注册自定义的Router

```java
@Async
@Component
public class PublishEventRouterHandler extends EventHandler<TestEvent> {
    @Override
    protected Router registRouter() {
        return new TestRouter();
    }
    
    @Override
    protected void handle(TestEvent event) {
        // Do something.
    }
}
```

注册Router后，只有route(T event)方法返回true，对应的handle或handleRequest方法才会被调用。
