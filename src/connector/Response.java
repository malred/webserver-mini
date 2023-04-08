package connector;

import java.io.*;
import java.nio.Buffer;
import java.nio.charset.StandardCharsets;

/*
   HTTP/1.1 200 OK
 */
public class Response {
    private static final int BUFFER_SIZE = 1024;
    Request request;
    OutputStream output;

    public Response(OutputStream output) {
        this.output = output;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    /**
     * 将资源写入output,发送给用户
     */
    public void sendStaticResource() throws IOException {
        // 静态资源
        File file = new File(ConnectorUtils.WEB_ROOT, request.getRequestURI());
        try {
            write(file, HttpStatus.SC_OK);
        } catch (IOException e) {
            // 返回404
            write(new File(ConnectorUtils.WEB_ROOT, "404.html"),
                    HttpStatus.SC_NOT_FOUND);
        }
    }

    private void write(File resource, HttpStatus status) throws IOException {
        // try()里的资源会自动释放
        try (FileInputStream fis = new FileInputStream(resource)) {
            output.write(ConnectorUtils.renderStatus(status).getBytes(StandardCharsets.UTF_8));
//            output.write(ConnectorUtils.renderStatus(status).getBytes());
            byte[] buffer = new byte[BUFFER_SIZE];
            int length = 0;
            // 读文件
            while ((length = fis.read(buffer, 0, BUFFER_SIZE)) != -1) {
                // 写文件到客户端
                output.write(buffer, 0, length);
            }
        }

    }
}
