# sizeof-object

Sizeof Java object instance.

## Usage

Package
```shell
mvn clean package
```

Run test
```shell
cd target
java -javaagent:sizeof-object-1.0-SNAPSHOT-jar-with-dependencies.jar -jar sizeof-object-1.0-SNAPSHOT-jar-with-dependencies.jar
```

Other

```java
// add dependency to your classpath,example like this
import static tk.zhangh.javaagent.SizeOf.deepSizeOf;
import static tk.zhangh.javaagent.SizeOf.sizeOf;

public class SizeOfTest {

    public static void main(String[] args) throws IllegalAccessException {
        System.out.println("sizeOf(new Integer(1)) = " + sizeOf(new Integer(1)));
        System.out.println("sizeOf(new char[1]) = " + sizeOf(new char[1]));
        System.out.println("sizeOf(new String(\"str\")) = " + sizeOf(new String("str")));
        System.out.println("deepSizeOf(new String(\"str\")) = " + deepSizeOf(new String("str")));
    }
}
```
