package com.mytrial.ha;

import com.mytrial.ha.hot.MagicMath;
import com.mytrial.pb.GreeterGrpc;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

@Data
@Slf4j
public class RpcServerImpl extends GreeterGrpc.GreeterImplBase {
    protected ApplicationContext context;

    public RpcServerImpl(int id) throws ClassNotFoundException {
        super();
        context = new ClassPathXmlApplicationContext("hot-update-context.xml"); // Supposed to reload all beans under its own context
        final MagicMath magicMathService = context.getBean(MagicMath.class);
        context.getClassLoader().loadClass(MagicMath.class.getName());
        log.info("For rpc server id: {}, magicMathService.version is {}", id, magicMathService.getVersion());
    }
}
