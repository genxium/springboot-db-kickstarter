package com.mytrial.ha;

import com.mytrial.classloader.DynamicClassLoader;
import com.mytrial.ha.hot.MagicMath;
import com.mytrial.pb.GreeterGrpc;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.remoting.RemoteInvocationFailureException;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Data
@Slf4j
public class RpcServerImpl extends GreeterGrpc.GreeterImplBase {

    public RpcServerImpl(int id) throws RemoteInvocationFailureException {
        super();
        try {
            final ClassLoader usingClassLoader = Thread.currentThread().getContextClassLoader();
            final DynamicClassLoader ctxDependentClassLoader = new DynamicClassLoader(usingClassLoader);
            final Class<?> clazz = ctxDependentClassLoader.loadClass(MagicMath.class.getName());
            final Object magicMathServiceStub = clazz.newInstance(); // This "magicMathServiceStub" cannot be casted into an MagicMath instance because it's loaded by a different class loader than the original "usingClassLoader".
            final Method getVersionMethod = clazz.getDeclaredMethod("getVersion");
            final int magicMathVer = (int) getVersionMethod.invoke(magicMathServiceStub);
            log.info("For rpc server id: {}, using Spring ApplicationContext class loader {}, magicMathService.version is {}", id, usingClassLoader, magicMathVer);
        } catch (Exception ex) {
            throw new RemoteInvocationFailureException("Failed to initiate RpcServerImpl id = " + id, ex);
        }
    }
}
