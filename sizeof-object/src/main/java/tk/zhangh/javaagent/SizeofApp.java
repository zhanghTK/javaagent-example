package tk.zhangh.javaagent;

import static tk.zhangh.javaagent.Sizeof.*;

/**
 * Sizeof 执行实例
 * Created by ZhangHao on 2018/1/4.
 */
public class SizeofApp {

    public static void main(String[] args) throws IllegalAccessException {
        System.out.println("sizeOf(new Integer(1)) = " + sizeOf(new Integer(1)));
        System.out.println("sizeOf(new char[1]) = " + sizeOf(new char[1]));
        System.out.println("sizeOf(new String(\"1\")) = " + sizeOf(new String("1")));
        System.out.println("deepSizeOf(new String(\"1\")) = " + deepSizeOf(new String("1")));
    }
}
