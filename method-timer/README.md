# Method Timer

To count method execution time.

## Usage

Package
```shell
mvn clean package
```

Run test
```shell
cd target/test-clesses
# to count methods with tk.zhangh.javaagent.Timer annotation
java -javaagent:../method-timer-1.0-SNAPSHOT-jar-with-dependencies.jar tk.zhangh.javaagent.AgentTest
# to count all methods
java -javaagent:../method-timer-1.0-SNAPSHOT-jar-with-dependencies.jar=ALL tk.zhangh.javaagent.AgentTest
```

Other
```shell
java -javaagent:{YOUR_JAR_LOCATION}/method-timer-1.0-SNAPSHOT-jar-with-dependencies.jar {YOUR_MAIN_CLASS_FILE}
# or
java -javaagent:{YOUR_JAR_LOCATION}/method-timer-1.0-SNAPSHOT-jar-with-dependencies.jar=ALL {YOUR_MAIN_CLASS_FILE}
```
