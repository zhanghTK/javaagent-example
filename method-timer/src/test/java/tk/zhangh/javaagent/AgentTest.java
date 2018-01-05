package tk.zhangh.javaagent;

/**
 * 测试执行类
 * Created by ZhangHao on 2018/1/5.
 */
public class AgentTest {
    @Timer
    public static void main(String[] args) throws InterruptedException {
        say("hi");
        Thread.sleep(1000);
        say("hello");
    }

    @Timer
    private static void say(String msg) {
        System.out.println(msg);
    }
}
