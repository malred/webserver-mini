package connector;

import java.io.IOException;
import java.io.InputStream;

/*
GET /index.html HTTP/1.1
        Host: localhost:8888
        Connection: keep-alive
        Cache-Control: max-age=0
        Upgrade-Insecure-Requests: 1
        User-Agent: Mozilla/5.0 (Macintosh; Intel ...)
 */
public class Request implements ServletRequest{
    private static final int BUFFER_SIZE = 1024;
    // 和服务器的socket链接
    private InputStream input;
    private String uri;

    public Request(InputStream input) {
        this.input = input;
    }

    public String getRequestURI() {
        return uri;
    }

    // 解析请求
    public void parse() {
        int length = 0;
        byte[] buffer = new byte[BUFFER_SIZE];
        try {
            // length: 本次读取多少字节
            length = input.read(buffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        StringBuilder request = new StringBuilder();
        // 形成字符串
        for (int j = 0; j < length; j++) {
            request.append((char) buffer[j]);
        }
        uri = parseUri(request.toString());
    }
    // 解析请求,获取uri
    private String parseUri(String s) {
        int index1, index2;
        // 第一行是空格分割的
        // GET /index.html HTTP/1.1
        index1 = s.indexOf(' ');
        if (index1 != -1) {
            // 寻找下一个空格
            index2 = s.indexOf(' ', index1 + 1);
            if (index2 > index1) {
                // 获取uri: /index.html
                return s.substring(index1 + 1, index2);
            }
        }
        return "";
    }
}
