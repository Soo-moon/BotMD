import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class Log {
    Logger logger;


    public Log() throws IOException {
        logger = Logger.getGlobal();
        if (logger.getHandlers() == null){
            FileHandler fh = new FileHandler(Server.Log_path + "log.txt",true);
            CustomFormat mFormat = new CustomFormat();
            fh.setFormatter(mFormat);
            logger.addHandler(fh);
        }
        //todo
    }

    public void d(String msg){
        logger.info(msg);
    }

    public void e(String msg){
        logger.severe(msg);
    }

    public class CustomFormat extends Formatter{

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
            System.out.println(sb);
            return sb.toString();
        }

        private String calcDate(long sec) {
            SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date(sec);
            return date_format.format(date);
        }
    }
}
