
import java.io.IOException;
import java.net.ConnectException;
import java.net.NoRouteToHostException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpClientError;
import org.apache.commons.httpclient.HttpException;
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
	
	public static String[] patch(String url, String token,String Token_type,String body,int timeout) throws IOException  {
		allowTLS12();
		String[] value=new String[2];
		PostMethod m = new PostMethod(url) {
			@Override public String getName() { return "PATCH"; }
		};
		
		m.setRequestHeader("Authorization", Token_type + " " + token);
		m.setRequestEntity(new StringRequestEntity(body, "application/json", "UTF-8"));
		
		HttpClient c = new HttpClient();
		c.getParams().setParameter("http.socket.timeout", new Integer(timeout));
		int sc = 0;
		try{
			m.getResponseBodyAsStream();
			sc = c.executeMethod(m);
			value[0]=sc+" "+ m.getStatusText();
			value[1]=m.getResponseBodyAsStream().toString();
		} catch(HttpException e){
			value[0]="0899";
			value[1]=e.toString();
		} catch(HttpClientError e){
			value[0]="0899";
			value[1]=e.toString();
		} catch(SocketTimeoutException e) {
			value[0]="0802";
			value[1]=e.toString();
		} catch(UnknownHostException e){
			value[0]="0899";
			value[1]=e.toString();
		} catch(ConnectException e){
			value[0]="0899";
			value[1]=e.toString();
		} catch(NoRouteToHostException e){
			value[0]="0899";
			value[1]=e.toString();
		}catch(Exception e){
			value[0]="0999";
			value[1]=e.toString();
		}
		return value;
	}
}
