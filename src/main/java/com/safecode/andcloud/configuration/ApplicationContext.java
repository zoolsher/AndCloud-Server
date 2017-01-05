package com.safecode.andcloud.configuration;

import com.safecode.andcloud.compoment.ControlACWebSocketServer;
import com.safecode.andcloud.compoment.LogACWebSocketServer;
import com.safecode.andcloud.compoment.ScreenCastServer;
import com.safecode.andcloud.util.SpringContextUtil;
import com.safecode.andcloud.worker.MessageReciverWorker;
import org.libvirt.Connect;
import org.libvirt.LibvirtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.zeromq.ZMQ;

import java.net.UnknownHostException;

/**
 * Spring Context configuration class.
 *
 * @author Sumy
 */
@Configuration
@EnableTransactionManagement
@EnableScheduling
@PropertySource(value = {"classpath:application.properties"})
@ComponentScan(basePackages = "com.safecode.andcloud")
public class ApplicationContext {

    private final static Logger logger = LoggerFactory.getLogger(ApplicationContext.class);

    @Autowired
    private Environment environment;

    /**
     * 初始化 libvirt 连接
     *
     * @return 连接
     * @throws LibvirtException libvirt连接异常
     */
    @Bean
    public Connect libvirtConnect() throws LibvirtException {
        Connect conn = new Connect(environment.getProperty("libvirt.endpoint"));
        return conn;
    }

    /**
     * 线程池
     *
     * @return 线程池管理对象
     */
    @Bean
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor threadPoolExecutor = new ThreadPoolTaskExecutor();
        threadPoolExecutor.setCorePoolSize(Integer.parseInt(environment.getProperty("threadpool.coresize")));
        threadPoolExecutor.setMaxPoolSize(Integer.parseInt(environment.getProperty("threadpool.maxsize")));
        return threadPoolExecutor;
    }

    /**
     * 前端任务消息接收者
     *
     * @return 消息接受者
     */
    @Bean
    public MessageReciverWorker messageReciverWorker() {
        MessageReciverWorker worker = new MessageReciverWorker();
        return worker;
    }

    /**
     * Spring 上下文持有类，用于一些特殊情况下无法注入而需要获取 Bean 的情况
     *
     * @return Spring 上下文工具类对象
     */
    @Bean
    public SpringContextUtil springContextUtil() {
        return new SpringContextUtil();
    }

    @Bean(name = "domaindefine.xml")
    public Resource domainDefineXmlResource() {
        Resource resource = new ClassPathResource("domaindefine.xml");
        return resource;
    }

    @Bean(name = "logmq")
    public ZMQ.Socket bindLogMQ() {
        ZMQ.Context ctx = ZMQ.context(1);
        ZMQ.Socket socket = ctx.socket(ZMQ.PUB);
        String endpoint = environment.getRequiredProperty("mq.log.endpoint");
        socket.bind(endpoint);
        logger.info("log message queen bind on " + endpoint);
        return socket;
    }

    @Bean(name = "logMQSub")
    public ZMQ.Socket connectLogMQ() {
        ZMQ.Context ctx = ZMQ.context(1);
        ZMQ.Socket socket = ctx.socket(ZMQ.SUB);
        String endpoint = environment.getRequiredProperty("mq.log.endpoint");
        socket.connect(endpoint);
        socket.subscribe("".getBytes());
        logger.info("log message queue connect");
        return socket;
    }

    @Bean(name = "logACWebSocketServer")
    public LogACWebSocketServer bindLogWebSocketServer() throws UnknownHostException {
        bindLogMQ();
        String logPortStr = environment.getRequiredProperty("ws.log.port");
        int logPort = Integer.parseInt(logPortStr);
        return new LogACWebSocketServer(logPort);
    }

    /**
     * 屏幕收取服务
     *
     * @return 服务
     */
    @Bean
    public ScreenCastServer screenCastServer() {
        ScreenCastServer screenCastServer = new ScreenCastServer(environment.getProperty("screencast.server.address"),
                environment.getProperty("screencast.server.port"));
        return screenCastServer;
    }

    @Bean
    public ControlACWebSocketServer controlACWebSocketServer() throws UnknownHostException {
        return new ControlACWebSocketServer(environment.getProperty("ws.control.port"));
    }

    @Bean(name = "controlMQ")
    public ZMQ.Socket createControlMQ() {
        ZMQ.Context ctx = ZMQ.context(1);
        ZMQ.Socket socket = ctx.socket(ZMQ.PUB);
        String endpoint = environment.getRequiredProperty("mq.command.endpoint");
        socket.bind(endpoint);
        logger.info("command mq connect on" + endpoint);
        return socket;

    }

    @Bean(name = "touchMQ")
    public ZMQ.Socket prepareTouchMQ() {
        ZMQ.Context ctx = ZMQ.context(1);
        ZMQ.Socket socket = ctx.socket(ZMQ.PUB);
        String endpoint = environment.getRequiredProperty("mq.touch.endpoint");
        socket.bind(endpoint);
        logger.info("Touch MQ bind on " + endpoint);
        return socket;
    }
}
