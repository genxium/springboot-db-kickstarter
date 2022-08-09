package com.mytrial.classloader;

import lombok.SneakyThrows;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class DynamicClassLoader extends ClassLoader {

    public DynamicClassLoader(ClassLoader parent) {
        super(parent);
    }

    @SneakyThrows
    @Override
    public Class loadClass(String name) throws ClassNotFoundException {
        if (null == name) throw new ClassNotFoundException("Classname to be loaded being null is unacceptable!");
        if (!name.startsWith("com.mytrial.ha.hot")) {
            return super.loadClass(name);
        }
        final String filepath = name.substring(0, name.lastIndexOf('.')).replace('.', '/');
        final String onlyClassName = name.substring(name.lastIndexOf('.') + 1, name.length());
        final String cp = String.format("classpath:%s/%s.class", filepath, onlyClassName);
        final URLConnection connection = new URL(cp).openConnection();

        try (final InputStream input = connection.getInputStream()) {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            int data = input.read();

            while (data != -1) {
                buffer.write(data);
                data = input.read();
            }

            byte[] classData = buffer.toByteArray();

            return defineClass(name,
                    classData, 0, classData.length);

        } catch (IOException e) {
            throw new ClassNotFoundException(e.getMessage());
        }
    }
}
