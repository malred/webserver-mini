import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

// 测试连接服务器
public class TestClient {
    public static void main(String[] args) throws Exception {
        Socket socket = new Socket("localhost", 8888);
        // 模拟发送请求
        OutputStream output = socket.getOutputStream();
        output.write("GET /index.html HTTP/1.1".getBytes(StandardCharsets.UTF_8));
        socket.shutdownOutput();
        InputStream input = socket.getInputStream();
        byte[] buffer = new byte[2048];
        int length = input.read();
        StringBuilder response = new StringBuilder();
        for (int j = 0; j < length; j++) {
            response.append((char) buffer[j]);
        }
        System.out.println(response.toString());
        socket.shutdownInput();
        socket.close();
    }
}
