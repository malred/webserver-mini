package connector;

import java.io.File;

public class ConnectorUtils {
    // 静态资源文件夹
    public static final String WEB_ROOT =
                System.getProperty("user.dir") + File.separator + "webroot";
    public static final String PROTOCOL = "HTTP/1.1";
    public static final String CARRIAGE = "\r";
    public static final String NEWLINE = "\n";
    public static final String SPACE = " ";

    // 响应头
    public static String renderStatus(HttpStatus status) {
        StringBuilder sb = new StringBuilder(PROTOCOL)
                .append(SPACE)
                .append(status.getStatusCode())
                .append(SPACE)
                .append(status.getReason())
                // 这两行是格式需求,后面跟静态资源
                .append(CARRIAGE).append(NEWLINE)
                .append(CARRIAGE).append(NEWLINE);
        return sb.toString();
    }
}
