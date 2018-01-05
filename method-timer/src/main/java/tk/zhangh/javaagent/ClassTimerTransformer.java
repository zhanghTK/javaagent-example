package tk.zhangh.javaagent;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

/**
 * class转换器，为定义的方法增加计时操作
 * Created by ZhangHao on 2018/1/3.
 */
public class ClassTimerTransformer implements ClassFileTransformer {

    private static final String START = " long startTime = System.currentTimeMillis(); ";
    private static final String END = " long endTime = System.currentTimeMillis(); ";
    private static final String OUT = " System.out.println(\"%s.%s COST:\" + (endTime - startTime) + \"ms.\"); ";
    private static final String RETURN_METHOD_BODY = "{" + START + " Object result = %s($$); " + END + "%s return result;}";
    private static final String VOID_METHOD_BODY = "{" + START + " %s($$); " + END + "%s}";
    private boolean transformAllClass = false;

    public ClassTimerTransformer(boolean transformAllClass) {
        this.transformAllClass = transformAllClass;
    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        className = className.replace("/", ".");
        String methodName = "";
        try {
            CtClass ctclass = ClassPool.getDefault().get(className);
            if (ctclass.isInterface()) {
                // 不处理接口类型
                return null;
            }
            for (CtMethod ctMethod : ctclass.getDeclaredMethods()) {
                if (!transformAllClass && ctMethod.getAnnotation(Timer.class) == null) {
                    // 如果不是统计所有类，则过滤非 Timer 注解标示的方法
                    continue;
                }
                methodName = ctMethod.getName();

                CtMethod ctmethod = ctclass.getDeclaredMethod(methodName);
                // 将方法重命名
                String oldMethodName = methodName + "$old$" + (System.currentTimeMillis() % 10000);
                ctmethod.setName(oldMethodName);// 将原来的方法名字修改

                // 创建新的方法，复制原方法，与原方法名同名
                CtMethod newMethod = CtNewMethod.copy(ctmethod, methodName, ctclass, null);

                // 构建新方法方法体，调用原方法且前后进行时间统计
                // ($$)表示所有的参数
                String newMethodBody;
                String outputStatement = String.format(OUT, className, methodName);
                if (ctmethod.getReturnType().equals(CtClass.voidType)) {
                    newMethodBody = String.format(VOID_METHOD_BODY, oldMethodName, outputStatement);
                } else {
                    newMethodBody = String.format(RETURN_METHOD_BODY, oldMethodName, outputStatement);
                }
                newMethod.setBody(newMethodBody);  // 设置方法体
                ctclass.addMethod(newMethod);  // 增加新方法
            }
            return ctclass.toBytecode();
        } catch (Exception e) {
            System.out.println(className + methodName + " has error in transform");
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
