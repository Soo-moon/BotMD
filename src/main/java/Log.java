import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.logging.*;

public class Log {
    static Logger logger = init();
    static String time;

    public static Logger init() {
        Logger log = Logger.getGlobal();
        time = nowTime();
        try {
            if (log.getHandlers().length == 0) {
                String logPath = System.getProperty("user.home") + "/server/log/log_" + time + ".txt";
                File logFile = new File(logPath);
                if (logFile.getParentFile() != null && !logFile.getParentFile().exists() && !logFile.getParentFile().mkdirs()) {
                    throw new IllegalStateException("Couldn't create dir: " + logFile.getParentFile());
                }
                FileHandler fh = new FileHandler(logPath , true);
                CustomFormat mFormat = new CustomFormat();
                fh.setFormatter(mFormat);
                log.addHandler(fh);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return log;
    }

    public static String nowTime(){
        LocalDate now = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMdd");
        return now.format(formatter);
    }

    public static void createLog(String msg , Level level){
        createLog(msg,level,null);
    }

    public static void createLog(String msg , Level level , Exception e){
        if (!time.equals(nowTime())){
            logger = init();
        }

        if (Level.INFO.equals(level)) {
            logger.info(msg);
        }
        else if (Level.SEVERE.equals(level)) {
            logger.severe(msg);
            if (e != null) {
                logger.severe(e.getMessage());
            }
        }
    }

    public static void d(String msg) {
        createLog(msg , Level.INFO);
//        logger.info(msg);
    }

    public static void e(String msg, Exception e) {
        createLog(msg , Level.SEVERE , e);

//        logger.severe(msg);
//        logger.severe(e.getMessage());
    }

    public static void e(String msg) {
        createLog(msg , Level.SEVERE);
//        logger.severe(msg);
    }

    public static class CustomFormat extends Formatter {

        @Override
        public String format(LogRecord record) {
            int count = Thread.currentThread().getStackTrace().length - 1;
            for(StackTraceElement st : Thread.currentThread().getStackTrace()){
                System.out.println("t // " + st.getClassName() + "." + st.getMethodName());
            }

            StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[count];


            StringBuilder sb = new StringBuilder();
            sb.append(calcDate(record.getMillis()));

            String level = String.valueOf(record.getLevel());
            System.out.println(level);

            if (level.equals(Level.SEVERE.toString())){
                level = "ERROR";
            }

            sb.append(" [");
            sb.append(level);
            sb.append("]");

            sb.append("[");
            sb.append(stackTraceElement.getClassName());
            sb.append(".");
//            sb.append(stackTraceElement.getLineNumber());
            sb.append(stackTraceElement.getMethodName());
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
