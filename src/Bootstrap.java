import connector.Connector;
import connector.ConnectorUtils;

public final class Bootstrap {
    public static void main(String[] args) {
        Connector connector=new Connector();
        connector.start();
    }
}
