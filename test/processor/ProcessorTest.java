package processor;

import Util.TestUtils;
import connector.Request;
import org.junit.Test;

public class ProcessorTest {
    private static final String servletRequest = "GET /servlet/TimeServlet HTTP/1.1";

    @Test
    public void givenServletRequest_thenLoadServlet() {
            Request request = TestUtils.createRequest(servletRequest);
        ServletProcessor processor=new ServletProcessor();

        }
}
