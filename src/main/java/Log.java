import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class Log {
    static Logger logger;

    public static void init(){
        try {
            logger = Logger.getGlobal();
            if (logger.getHandlers().length == 0){
                Path logPath = Paths.get(System.getProperty("user.home") + "/server/log");
                if (!Files.exists(logPath.getParent())) {
                    Files.createDirectories(logPath);
                }
                Path logFile = Paths.get(logPath + "/log.txt");
                try {
                    Files.createFile(logFile);
                }catch (Exception e){
                    Log.d("AlreadyExists!");
                }
                FileHandler fh = new FileHandler(logFile.toString() , true);
                CustomFormat mFormat = new CustomFormat();
                fh.setFormatter(mFormat);
                logger.addHandler(fh);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void d(String msg){
        logger.info(msg);
    }

    public static void e(String msg , Exception e){
        logger.severe(msg);
        logger.severe(e.getMessage());
    }

    public static void e(String msg){
        logger.severe(msg);
    }

    public static class CustomFormat extends Formatter{

        @Override
        public String format(LogRecord record) {
            StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[9];
            StringBuilder sb = new StringBuilder();
            sb.append(calcDate(record.getMillis()));

            sb.append(" [");
            sb.append(record.getLevel());
            sb.append("]");

            sb.append("[");
            sb.append(stackTraceElement.getClassName());
            sb.append(".");
            sb.append(stackTraceElement.getLineNumber());
            sb.append("] - ");

            sb.append(record.getMessage());
            sb.append("\n");
            return sb.toString();
        }

        private String calcDate(long sec) {
            SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date(sec);
            return date_format.format(date);
        }
    }
}
