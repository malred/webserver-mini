package connector;

import Util.TestUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class RequestTest {
    private static final String validRequest = "GET /index.html HTTP/1.1";

    @Test
    public void givenValidRequest_thenExtrackUri() {
        Request request = TestUtils.createRequest(validRequest);
        // 断言
        Assert.assertEquals("/index.html", request.getRequestURI());
    }
}
