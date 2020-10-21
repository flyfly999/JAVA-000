package code;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MyClassLoader extends ClassLoader{

    public static void main(String[] args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Class<?> classObj = new MyClassLoader().findClass("Hello");
        Object newInstance = classObj.getDeclaredConstructor().newInstance();
//        Method[] methods = classObj.getMethods();
//        for (Method m : methods){
//            System.out.println(m.getName());
//
//        }

        Method hello = classObj.getMethod("hello");
        hello.invoke(newInstance);
    }


    @Override
    protected Class<?> findClass(String name) {

        String classPath = "/Users/wzy/work/java/stud/src/main/java/code/Hello.xlass";



        System.out.println(classPath);

        File file = new File(classPath);


        byte[] bytes = new byte[(int) file.length()];
        try {
            new FileInputStream(classPath).read(bytes);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = doByte(bytes[i]);
        }
        return defineClass(name, bytes, 0, bytes.length);
    }


    private byte doByte(byte b) {
        return (byte)((byte)255 - b);
    }
}
