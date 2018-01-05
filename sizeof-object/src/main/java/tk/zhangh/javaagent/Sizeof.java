package tk.zhangh.javaagent;

import java.lang.instrument.Instrumentation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;

/**
 * 使用javaagent计算对象大小
 * Created by ZhangHao on 2018/1/5.
 */
public class Sizeof {
    private static Instrumentation inst;

    public static void premain(String agentOps, Instrumentation inst) {
        Sizeof.inst = inst;
    }

    /**
     * 计算基本类型（数组）对象、引用类型对象大小，不计算子对象大小
     */
    public static long sizeOf(Object o) {
        if (inst == null) {
            throw new IllegalStateException("Can't access Instrumentation instance");
        }
        return inst.getObjectSize(o);
    }

    /**
     * 计算对象完整大小
     */
    public static long deepSizeOf(Object object) {
        Set<Object> visited = new HashSet<>();
        Deque<Object> deque = new ArrayDeque<>();
        deque.add(object);
        long size = 0L;
        while (deque.size() > 0) {
            Object obj = deque.poll();
            size += isSkip(visited, obj) ? 0L : sizeOf(obj);

            Class<?> objClass = obj.getClass();

            if (isReferenceArray(objClass)) {
                sizeOfReferenceArray(deque, obj);
            } else {
                sizeOfChildObj(deque, obj, objClass);

            }
        }
        return size;
    }

    private static void sizeOfChildObj(Deque<Object> deque, Object obj, Class<?> objClass) {
        while (objClass != null) {
            Field[] fields = objClass.getDeclaredFields();
            for (Field field : fields) {
                if (isFieldShared(field)) {
                    continue;
                }
                field.setAccessible(true);
                Object fieldValue = null;
                try {
                    fieldValue = field.get(obj);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                if (fieldValue != null) {
                    deque.add(fieldValue);
                }
            }
            objClass = objClass.getSuperclass();
        }
    }

    private static boolean isFieldShared(Field field) {
        return Modifier.isStatic(field.getModifiers()) || field.getType().isPrimitive();
    }

    private static void sizeOfReferenceArray(Deque<Object> deque, Object obj) {
        for (int i = 0, len = Array.getLength(obj); i < len; i++) {
            Object tmp = Array.get(obj, i);
            if (tmp != null) {
                deque.add(Array.get(obj, i));
            }
        }
    }

    private static boolean isReferenceArray(Class<?> clazz) {
        return clazz.isArray() && clazz.getName().length() > 2;
    }

    private static boolean isSkip(Set<Object> visited, Object obj) {
        return obj instanceof String && obj == ((String) obj).intern() || visited.contains(obj);
    }
}
