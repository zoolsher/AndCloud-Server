import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Server Main Class.
 *
 * @author zoolsher
 * @author Sumy
 */
public class Main {

    private final static Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        logger.info("Server Start...");
        logger.info("Loading Spring Context");
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext("com.safecode.andcloud.configuration");
        logger.info("Server initialization finished.");
    }

}