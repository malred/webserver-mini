package connector;

import processor.ServletProcessor;
import processor.StaticProcessor;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Set;

public class Connector implements Runnable {
    private static final int DEFAULT_PORT = 8888;
    //    private ServerSocket server;
    private int port;
    private ServerSocketChannel server;
    private Selector selector;

    public Connector(int port) {
        this.port = port;
    }

    public Connector() {
        this(DEFAULT_PORT);
    }

    public void start() {
        // 运行
        Thread thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        try {
//            server = new ServerSocket(port);
            server = ServerSocketChannel.open();
            // 非阻塞
            server.configureBlocking(false);
            server.socket().bind(new InetSocketAddress(port));
            selector = Selector.open();
            // 注册连接事件,有客户端连接就触发
            server.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("启动服务器,监听端口: " + port);
            while (true) {
                selector.select();
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                for (SelectionKey key : selectionKeys) {
                    // 处理被触发的事件
                    handler(key);
                }
                // 清理触发过的key
                selectionKeys.clear();
            }
//            while (true) {
//                // 阻塞等待客户端连接
//                Socket socket = server.accept();
//                InputStream input = socket.getInputStream();
//                OutputStream output = socket.getOutputStream();
//                Request request = new Request(input);
//                request.parse();
//                Response response = new Response(output);
//                response.setRequest(request);
//                // 如果是动态资源
//                if (request.getRequestURI().startsWith("/servlet/")) {
//                    ServletProcessor processor = new ServletProcessor();
//                    processor.process(request, response);
//                } else {
//                    // 发送静态资源回去
//                    StaticProcessor processor = new StaticProcessor();
//                    processor.process(request, response);
//                }
//                // 处理完就断开连接
//                close(socket);
//            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            close(server);
        }
    }

    private void handler(SelectionKey key) throws IOException {
        // ACCEPT
        if (key.isAcceptable()) {
            ServerSocketChannel server = (ServerSocketChannel) key.channel();
            // 获得连接的channel
            SocketChannel client = server.accept();
            client.configureBlocking(false);
            // 注册读事件
            client.register(selector, SelectionKey.OP_READ);
        }
        // READ
        else {
            SocketChannel client = (SocketChannel) key.channel();
            // 此条channel和selector断开,不再监听
            key.cancel();
            // 可以重新设置阻塞状态
            client.configureBlocking(true);
            Socket clientSocket = client.socket();
            InputStream inputStream = clientSocket.getInputStream();
            OutputStream outputStream = clientSocket.getOutputStream();
            Request request = new Request(inputStream);
            request.parse();
            Response response = new Response(outputStream);
            response.setRequest(request);
            // 如果是动态资源
            if (request.getRequestURI().startsWith("/servlet/")) {
                ServletProcessor processor = new ServletProcessor();
                processor.process(request, response);
            } else {
                // 发送静态资源回去
                StaticProcessor processor = new StaticProcessor();
                processor.process(request, response);
            }
            // 处理完就断开连接
            close(client);
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
