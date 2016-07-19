
import java.io.IOException;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;

public class Patch {

    private static void allowTLS12() {
        // debug mode
        // System.setProperty("javax.net.debug", "ssl");
        System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2");

        String scheme = "https";
        Protocol baseHttps = Protocol.getProtocol(scheme);
        int defaultPort = baseHttps.getDefaultPort();

        ProtocolSocketFactory baseFactory = baseHttps.getSocketFactory();
        ProtocolSocketFactory customFactory = new CustomHttpsSocketFactory(baseFactory);

        Protocol customHttps = new Protocol(scheme, customFactory, defaultPort);
        Protocol.registerProtocol(scheme, customHttps);
    }

    public static String[] patch(String url, String token, String Token_type, String body) throws IOException {
        allowTLS12();

        PostMethod m = new PostMethod(url) {
            @Override
            public String getName() {
                return "PATCH";
            }
        };

        m.setRequestHeader("Authorization", Token_type + " " + token);

        m.setRequestEntity(new StringRequestEntity(body, "application/json", "UTF-8"));

        HttpClient c = new HttpClient();
        int sc = 0;
        try {
            sc = c.executeMethod(m);
        } catch (Exception e) {

        } finally {

        }

        // System.out.println("PATCH call returned a status code of " + sc + "
        // "+ m.getStatusText() +" "+ m.getResponseBodyAsString());
        Header[] responseHeader = m.getRequestHeaders();
        for (Header a : responseHeader)
            System.out.println(a);
        String[] value = { sc + " " + m.getStatusText(), m.getResponseBodyAsString() };
        for (String a : value)
            System.out.println(a);
        return value;
    }
}
