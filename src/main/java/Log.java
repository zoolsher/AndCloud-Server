import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by zoolsher on 2016/12/16.
 */
public class Log {

        private final static Logger logger = LoggerFactory.getLogger(Log.class);

        public static void main(String[] args) {
            logger.info("logback {}", "INFO ( TRACE < DEBUG < INFO < WARN < ERROR )");
            logger.error("logback {}", "ERROR ( TRACE < DEBUG < INFO < WARN < ERROR )");
            logger.error("shit");
        }
}
