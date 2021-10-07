# Reactive UI for Micronaut in Java

Provides `@ObservableUI` annotation - a simple, yet powerful extension
to [Micronaut](https://micronaut.io/) concepts. It allows one to
write portable reactive UI applications like 
[TODO demo](https://github.com/jtulach/micronautui/blob/master/micronautui-demo/src/main/java/io/micronaut/ui/demo/Demo.java).

```bash
micronautui$ JAVA_HOME=/jdk-11/ mvn install -DskipTests
micronautui$ JAVA_HOME=/jdk-11/ mvn -f micronautui-demo/ -Pdesktop exec:exec
```

Share coding concepts and even **code** between your **server** and **client**!

### Debugging

To understand the behavior of the sample, use:

```
micronautui$ JAVA_HOME=/jdk-11/ mvn -f micronautui-demo/ -Pdesktop exec:exec
    -Dexec.debug.arg=-Xrunjdwp:transport=dt_socket,server=y,address=8000
```

and connect to the application from your favorite IDE.

### Connecting to Server

The UI contains "Load from Server" button. Once clicked, the system connects
to http://localhost:8080/todos/ - it expects JSON document in following format:

```js
[
  { "title" : "string", "done" : false }
]
```

I usually use one generated via [Micronaut Launch Wizard](http://micronaut.io/launch) and following
resource:

```java
@Controller("todos")
public class Application {
    @Get
    public List<Item> todos() {
        return Collections.nCopies(2, new Item("Repeat yourself", false));
    }

    public static void main(String[] args) {
        Micronaut.run(Application.class, args);
    }

    @Introspected
    static class Item {

        private final String title;
        private final boolean done;

        public Item(String title, boolean done) {
            super();
            this.title = title;
            this.done = done;
        }

        public String getTitle() {
            return title;
        }

        public boolean isDone() {
            return done;
        }
    }
}
```

In such case putting breakpoints into `Demo::readTodos` method in the client
code allows one to observe the behavior of the client when reading TODO items
from the server.
