package connector;

import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

/*
   HTTP/1.1 200 OK
 */
public class Response implements ServletResponse {
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

    @Override
    public String getCharacterEncoding() {
        return null;
    }

    @Override
    public String getContentType() {
        return null;
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        return null;
    }

    /**
     * 获取writer,以便操作outputstream
     *
     * @return
     * @throws IOException
     */
    @Override
    public PrintWriter getWriter() throws IOException {
        PrintWriter writer = new PrintWriter(output, true);
        return writer;
    }

    @Override
    public void setCharacterEncoding(String s) {

    }

    @Override
    public void setContentLength(int i) {

    }

    @Override
    public void setContentLengthLong(long len) {

    }

    @Override
    public void setContentType(String s) {

    }

    @Override
    public void setBufferSize(int i) {

    }

    @Override
    public int getBufferSize() {
        return 0;
    }

    @Override
    public void flushBuffer() throws IOException {

    }

    @Override
    public void resetBuffer() {

    }

    @Override
    public boolean isCommitted() {
        return false;
    }

    @Override
    public void reset() {

    }

    @Override
    public void setLocale(Locale locale) {

    }

    @Override
    public Locale getLocale() {
        return null;
    }
}
