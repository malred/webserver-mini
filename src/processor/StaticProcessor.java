package processor;

import connector.Request;
import connector.Response;

import java.io.IOException;

public class StaticProcessor {
    // 处理请求
public void process(Request request, Response response){
    try {
        response.sendStaticResource();
    } catch (IOException e) {
        throw new RuntimeException(e);
    }
}
}
