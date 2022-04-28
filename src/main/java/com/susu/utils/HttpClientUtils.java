package com.susu.utils;

import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.*;

/**
 * <p>Description: Image generation and recognition</p>
 * <p>HTTP 请求工具</p>
 * @author sujay
 * @version 15:09 2022/2/18
 * @since JDK1.8 <br/>
 */
public class HttpClientUtils {

	/**
	 *	编码格式。发送编码格式统一用UTF-8
	 */
	private static final String ENCODING = "UTF-8";


	/**
	 * setConnectTimeout：设置连接超时时间，单位毫秒。 setConnectionRequestTimeout：设置从connect
	 * Manager(连接池)获取Connection 超时时间，单位毫秒。这个属性是新加的属性，因为目前版本是可以共享连接池的。
	 * setSocketTimeout：请求获取数据的超时时间(即响应时间)，单位毫秒。 如果访问一个接口，多少时间内无法返回数据，就直接放弃此次调用。
	 *
	 *	设置连接超时时间，单位毫秒
	 */
	private static final int CONNECT_TIMEOUT = 6000;

	/**
	 *	请求获取数据的超时时间(即响应时间)，单位毫秒
	 */
	private static final int SOCKET_TIMEOUT = 6000;

	/**
	 * <p>Description: get request</p>
	 * <p>GET 请求</p>
	 * @param url					请求地址 URL
	 * @return HttpClientResult    	http请求响应镀锡
	 */
	public static HttpClientResult doGet(String url) {
		return doGet(url, null,ENCODING);
	}

	/**
	 * <p>Description: get request</p>
	 * <p>GET 请求</p>
	 * @param url					请求地址 URL
	 * @param params 				请求体参数
	 * @return HttpClientResult    	http请求响应镀锡
	 */
	public static HttpClientResult doGet(String url, Map<String, String> params) {
		return doGet(url, null, params,ENCODING);
	}

	/**
	 * <p>Description: get request</p>
	 * <p>GET 请求</p>
	 * @param url					请求地址 URL
	 * @param params 				请求体参数
	 * @param charset 				响应字符集
	 * @return HttpClientResult    	http请求响应镀锡
	 */
	public static HttpClientResult doGet(String url, Map<String, String> params,String charset) {
		return doGet(url, null, params,charset);
	}

	/**
	 * p>Description<: get request</p>
	 * <p>GET 请求</p>
	 * @param url					请求地址 URL
	 * @param headers				请求头参数
	 * @param params 				请求体参数
	 * @param charset 				响应字符集
	 * @return HttpClientResult    	http请求响应镀锡
	 */
	public static HttpClientResult doGet(String url, Map<String, String> headers, Map<String, String> params,String charset) {

		CloseableHttpClient httpClient = HttpClients.createDefault();

		HttpGet httpGet = new HttpGet();


		try {
			URIBuilder uriBuilder = new URIBuilder(url);;
			if (params != null) {
				Set<Map.Entry<String, String>> entrySet = params.entrySet();
				for (Map.Entry<String, String> entry : entrySet) {
					uriBuilder.setParameter(entry.getKey(), entry.getValue());
				}
			}
			httpGet.setURI(uriBuilder.build());
		}catch (Exception e) {
			throw new RuntimeException("请求参数解析异常！");
		}


		RequestConfig requestConfig = RequestConfig.custom()
				.setConnectTimeout(CONNECT_TIMEOUT)
				.setSocketTimeout(SOCKET_TIMEOUT)
				.build();
		httpGet.setConfig(requestConfig);

		packageHeader(headers, httpGet);

		return getHttpClientResult( httpClient, httpGet, charset);
	}

	/**
	 * p>Description<: post request</p>
	 * <p>POST 请求</p>
	 * @param url					请求地址 URL
	 * @param params 				请求体参数
	 * @return HttpClientResult    	http请求响应镀锡
	 */
	public static HttpClientResult doPost(String url,Map<String, String> params) {
		return doPost(url,null,params,ENCODING);
	}

	/**
	 * p>Description<: post request</p>
	 * <p>POST 请求</p>
	 * @param url					请求地址 URL
	 * @param params 				请求体参数
	 * @param charset 				响应字符集
	 * @return HttpClientResult    	http请求响应镀锡
	 */
	public static HttpClientResult doPost(String url,Map<String, String> params,String charset) {
		return doPost(url,null,params,charset);
	}

	/**
	 * p>Description<: post request</p>
	 * <p>POST 请求</p>
	 * @param url					请求地址 URL
	 * @param headers				请求头参数
	 * @param params 				请求体参数
	 * @param charset 				响应字符集
	 * @return HttpClientResult    	http请求响应镀锡
	 */
	public static HttpClientResult doPost(String url, Map<String, String> headers, Map<String, String> params,String charset) {

		CloseableHttpClient httpClient = SSLHttpClientBuild();
		HttpPost httpPost = new HttpPost(url);

		RequestConfig requestConfig = RequestConfig.custom().
				setConnectTimeout(CONNECT_TIMEOUT)
				.setSocketTimeout(SOCKET_TIMEOUT)
				.build();
		httpPost.setConfig(requestConfig);

		packageHeader(headers, httpPost);
		packageParam(params, httpPost);

		return getHttpClientResult(httpClient, httpPost,charset);
	}

	/**
	 * p>Description<: post request</p>
	 * <p>POST 请求</p>
	 * @param url					请求地址 URL
	 * @param params 				请求体参数
	 * @return HttpClientResult    	http请求响应镀锡
	 */
	public static HttpClientResult doPostJson(String url,String params) {
		return doPostJson(url,null,params,ENCODING);
	}

	/**
	 * p>Description<: post request</p>
	 * <p>POST 请求</p>
	 * @param url					请求地址 URL
	 * @param params 				请求体参数
	 * @param charset 				响应字符集
	 * @return HttpClientResult    	http请求响应镀锡
	 */
	public static HttpClientResult doPostJson(String url,String params,String charset) {
		return doPostJson(url,null,params,charset);
	}

	/**
	 * p>Description<: post request</p>
	 * <p>POST 请求</p>
	 * @param url					请求地址 URL
	 * @param headers				请求头参数
	 * @param json 					请求体参数
	 * @param charset 				响应字符集
	 * @return HttpClientResult    	http请求响应镀锡
	 */
	public static HttpClientResult doPostJson(String url, Map<String, String> headers, String json, String charset) {

		CloseableHttpClient httpClient = SSLHttpClientBuild();
		HttpPost httpPost = new HttpPost(url);

		RequestConfig requestConfig = RequestConfig.custom()
				.setConnectTimeout(CONNECT_TIMEOUT)
				.setSocketTimeout(SOCKET_TIMEOUT)
				.build();

		httpPost.setConfig(requestConfig);
		httpPost.setHeader("Content-type", "application/json");
		packageHeader(headers, httpPost);

		StringEntity requestEntity = new StringEntity(json, ENCODING);
		requestEntity.setContentEncoding(ENCODING);
		httpPost.setEntity(requestEntity);

		return getHttpClientResult(httpClient, httpPost,charset);
	}

	/**
	 * p>Description<: put request</p>
	 * <p>PUT 请求</p>
	 * @param url					请求地址 URL
	 * @param params 				请求体参数
	 * @return HttpClientResult    	http请求响应镀锡
	 */
	public static HttpClientResult doPut(String url, Map<String, String> params) {
		return doPut(url,params,ENCODING);
	}

	/**
	 * p>Description<: put request</p>
	 * <p>PUT 请求</p>
	 * @param url					请求地址 URL
	 * @param params 				请求体参数
	 * @param charset 				响应字符集
	 * @return HttpClientResult    	http请求响应镀锡
	 */
	public static HttpClientResult doPut(String url, Map<String, String> params, String charset) {

		CloseableHttpClient httpClient = HttpClients.createDefault();

		HttpPut httpPut = new HttpPut(url);

		RequestConfig requestConfig = RequestConfig.custom()
				.setConnectTimeout(CONNECT_TIMEOUT)
				.setSocketTimeout(SOCKET_TIMEOUT)
				.build();
		httpPut.setConfig(requestConfig);

		packageParam(params, httpPut);

		return getHttpClientResult(httpClient, httpPut, charset);
	}

	/**
	 * p>Description<: put request</p>
	 * <p>PUT 请求</p>
	 * @param url					请求地址 URL
	 * @param json 					请求体参数
	 * @return HttpClientResult    	http请求响应镀锡
	 */
	public static HttpClientResult doPutJson(String url, String json) {
		return doPutJson(url,json,ENCODING);
	}

	/**
	 * p>Description<: put request</p>
	 * <p>PUT 请求</p>
	 * @param url					请求地址 URL
	 * @param json 					请求体参数
	 * @param charset				响应字符集
	 * @return HttpClientResult    	http请求响应镀锡
	 */
	public static HttpClientResult doPutJson(String url, String json, String charset) {

		CloseableHttpClient httpClient =  HttpClients.createDefault();
		HttpPut httpPut = new HttpPut(url);

		RequestConfig requestConfig = RequestConfig.custom()
				.setConnectTimeout(CONNECT_TIMEOUT)
				.setSocketTimeout(SOCKET_TIMEOUT)
				.build();
		httpPut.setConfig(requestConfig);

		httpPut.setHeader("Content-type", "application/json");

		StringEntity requestEntity = new StringEntity(json, ENCODING);
		requestEntity.setContentEncoding(ENCODING);
		httpPut.setEntity(requestEntity);

		return getHttpClientResult(httpClient, httpPut,charset);
	}

	/**
	 * p>Description<: delete request</p>
	 * <p>DELETE 请求</p>
	 * @param url					请求地址 URL
	 * @return HttpClientResult    	http请求响应镀锡
	 */
	public static HttpClientResult doDelete(String url)  {
		return doDelete(url, ENCODING);
	}


	/**
     * p>Description<: delete request</p>
     * <p>DELETE 请求</p>
     * @param url					请求地址 URL
     * @param charset 				响应字符集
     * @return HttpClientResult    	http请求响应镀锡
     */
	public static HttpClientResult doDelete(String url, String charset) {

		CloseableHttpClient httpClient = HttpClients.createDefault();

		HttpDelete httpDelete = new HttpDelete(url);

		RequestConfig requestConfig = RequestConfig.custom()
				.setConnectTimeout(CONNECT_TIMEOUT)
				.setSocketTimeout(SOCKET_TIMEOUT)
				.build();

		httpDelete.setConfig(requestConfig);

		return getHttpClientResult( httpClient, httpDelete, charset);
	}

	/**
	 * <p>Description: encapsulation request header</p>
	 *
	 * httpPost.setHeader("Cookie", "");
	 * httpPost.setHeader("Connection","keep-alive");
	 * httpPost.setHeader("Accept", "application/json");
	 * httpPost.setHeader("Accept-Language", "zh-CN,zh;q=0.9");
	 * httpPost.setHeader("Accept-Encoding", "gzip, deflate, br");
	 * httpPost.setHeader("User-Agent",
	 * "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (HTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36"
	 *
	 * <p>封装请求头</p>
	 * @param params		请求头参数
	 * @param httpMethod 	请求体
	 */
	public static void packageHeader(Map<String, String> params, HttpRequestBase httpMethod) {

		if (params != null) {

			Set<Map.Entry<String, String>> entrySet = params.entrySet();

			for (Map.Entry<String, String> entry : entrySet) {
				httpMethod.setHeader(entry.getKey(), entry.getValue());
			}
		}
	}

	/**
	 * <p>Description: encapsulation request parameters</p>
	 * <p>封装请求参数</p>
	 * @param params		请求参数
	 */
	public static void packageParam(Map<String, String> params, HttpEntityEnclosingRequestBase httpMethod) {

		if (params != null) {

			try {

				List<NameValuePair> pairs = new ArrayList<>();
				Set<Map.Entry<String, String>> entrySet = params.entrySet();

				for (Map.Entry<String, String> entry : entrySet) {
					pairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
				}

				httpMethod.setEntity(new UrlEncodedFormEntity(pairs, ENCODING));

			} catch (UnsupportedEncodingException e) {
				throw new RuntimeException("封装请求体参数失败！");
			}
		}
	}



	/**
	 * <p>Description: Get request results</p>
	 * <p>获取请求结果</p>
	 * @param httpMethod    请求对象
	 * @param httpClient 	客户端
	 * @param charset   	字符集
	 * @return HttpClientResult   响应对象
	 * @exception RuntimeException 发送请求失败异常
	 */
	public static HttpClientResult getHttpClientResult(CloseableHttpClient httpClient, HttpRequestBase httpMethod,String charset) throws RuntimeException {

		CloseableHttpResponse httpResponse = null;
		String content = "";

		try {
			httpResponse = httpClient.execute(httpMethod);

			if (httpResponse != null && httpResponse.getStatusLine() != null) {

				if (httpResponse.getEntity() != null) {
					content = EntityUtils.toString(httpResponse.getEntity(), StringUtils.isBlank(charset) ?ENCODING : charset);
				}
				return new HttpClientResult(httpResponse.getStatusLine().getStatusCode(), content);
			}
			return new HttpClientResult(HttpStatus.SC_INTERNAL_SERVER_ERROR);

		} catch (Exception e) {
			throw new RuntimeException("发送请求失败！");
		}finally {
			close(httpResponse,httpClient);
		}
	}


	public static CloseableHttpClient SSLHttpClientBuild() {

		Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
				.register("http", PlainConnectionSocketFactory.INSTANCE)
				.register("https", trustAllHttpsCertificates())
				.build();

		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);

		return HttpClients.custom().setConnectionManager(connectionManager).build();
	}

	/**
	 * <p>Description: Trust all certificates</p>
	 * <p>信任所有证书</p>
	 */
	private static SSLConnectionSocketFactory trustAllHttpsCertificates() {

		SSLConnectionSocketFactory socketFactory = null;
		TrustManager[] trustAllCerts = new TrustManager[1];
		TrustManager tm = new miTM();
		trustAllCerts[0] = tm;
		SSLContext sc = null;

		try {
			sc = SSLContext.getInstance("TLS");
			sc.init(null, trustAllCerts, null);
			socketFactory = new SSLConnectionSocketFactory(sc, NoopHostnameVerifier.INSTANCE);
			// HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
		} catch (NoSuchAlgorithmException | KeyManagementException e) {
			e.printStackTrace();
		}

		return socketFactory;
	}


	/**
	 * <p>Description: close resource</p>
	 * <p>关闭资源</p>
	 * @param response       响应体
	 * @param httpClient 	 客户端
	 */
	public static void close(CloseableHttpResponse response, CloseableHttpClient httpClient)  {

		if (response != null) {
			try {
				response.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		if (httpClient != null) {
			try {
				httpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	static class miTM implements TrustManager, X509TrustManager {

		public X509Certificate[] getAcceptedIssuers() {
			return null;
		}

		public void checkServerTrusted(X509Certificate[] certs, String authType) {
			// don't check
		}

		public void checkClientTrusted(X509Certificate[] certs, String authType) {
			// don't check
		}
	}

	/**
	 * <p>Description: Request response object</p>
	 * <p>请求响应对象</p>
	 * @author sujay
	 * @version 15:09 2022/2/18
	 * @since JDK1.8 <br/>
	 */
	static class HttpClientResult {

		/**
		 * 响应状态码
		 */
		private int code;

		/**
		 * 响应数据
		 */
		private String content;

		public HttpClientResult(int statusCode, String content) {
			this.code = statusCode;
			this.content = content;
		}

		public HttpClientResult(int statusCode) {
			this.code = statusCode;
		}

		public int getCode() {
			return code;
		}

		public void setCode(int code) {
			this.code = code;
		}

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}
	}
}




