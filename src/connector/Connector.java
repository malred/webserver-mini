package connector;

import processor.StaticProcessor;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Connector implements Runnable {
    private static final int DEFAULT_PORT = 8888;
    private ServerSocket server;
    private int port;

    public Connector(int port) {
        this.port = port;
    }

    public Connector() {
        this(DEFAULT_PORT);
    }

    public void start() {
        // 运行
        Thread thread=new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        try {
            server = new ServerSocket(port);
            System.out.println("启动服务器,监听端口: " + port);
            while (true) {
                // 阻塞等待客户端连接
                Socket socket = server.accept();
                InputStream input = socket.getInputStream();
                OutputStream output = socket.getOutputStream();
                Request request = new Request(input);
                request.parse();
                Response response = new Response(output);
                response.setRequest(request);
                // 发送静态资源回去
                StaticProcessor processor = new StaticProcessor();
                processor.process(request, response);
                // 处理完就断开连接
                close(socket);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }finally {
            close(server);
        }
    }

    // 关闭资源
    private void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
                System.out.println("关闭" + closeable);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
