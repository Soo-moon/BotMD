import java.util.logging.Logger;

public class Log {
    Logger logger;


    public Log(String tag){
        logger = Logger.getLogger(tag);
    }

    public void d(String msg){
        logger.info(msg);
    }

    public void e(String msg){
        logger.severe(msg);
    }
}
