package com.mytrial.ha;

import com.mytrial.ha.hot.MagicMath;
import com.mytrial.pb.GreeterGrpc;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.OverridingClassLoader;
import org.springframework.core.SmartClassLoader;

import java.net.URLClassLoader;

@Data
@Slf4j
public class RpcServerImpl extends GreeterGrpc.GreeterImplBase {
    protected ClassPathXmlApplicationContext context;

    public RpcServerImpl(int id) throws ClassNotFoundException {
        super();
        context = new ClassPathXmlApplicationContext("hot-update-context.xml"); // Supposed to reload all beans under its own context
        final ClassLoader usingClassLoader = context.getClassLoader(); // Would be the same class loader (w.r.t. object id) for all newly created contexts if not otherwise specified
        final OverridingClassLoader ctxDependentClassLoader = new OverridingClassLoader(usingClassLoader);
        context.setClassLoader(ctxDependentClassLoader);
        context.refresh();
        final MagicMath magicMathService = context.getBean(MagicMath.class);
        log.info("For rpc server id: {}, using Spring ApplicationContext class loader {}, magicMathService.version is {}", id, usingClassLoader, magicMathService.getVersion());
    }
}
