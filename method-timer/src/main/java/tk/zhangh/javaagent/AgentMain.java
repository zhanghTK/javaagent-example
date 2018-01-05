package tk.zhangh.javaagent;

import java.lang.instrument.Instrumentation;

/**
 * javaagent执行入口
 * Created by ZhangHao on 2018/1/5.
 */
public class AgentMain {
    /**
     * 启动时传入all作为参数则统计所有方法执行时间，否则只统计@Timer标记的方法
     * 该方法在 main 方法之前运行
     * 对应 Premain-Class 配置
     */
    public static void premain(String agentOps, Instrumentation inst) {
        if (agentOps != null && "all".equalsIgnoreCase(agentOps)) {
            inst.addTransformer(new ClassTimerTransformer(true));
        } else {
            inst.addTransformer(new ClassTimerTransformer(false));
        }
    }
}
