package com.susu.utils;

import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import java.io.IOException;
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
		return doGet(url, null,null);
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
	 * <p>Description: get request</p>
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

		if (params != null) {
			try {
				URIBuilder uriBuilder = new URIBuilder(url);;
				Set<Map.Entry<String, String>> entrySet = params.entrySet();
				for (Map.Entry<String, String> entry : entrySet) {
					uriBuilder.setParameter(entry.getKey(), entry.getValue());
				}
				httpGet.setURI(uriBuilder.build());
			}catch (Exception e) {
				throw new RuntimeException("请求参数解析异常！");
			}
		}


		/**
		 * setConnectTimeout：设置连接超时时间，单位毫秒。 setConnectionRequestTimeout：设置从connect
		 * Manager(连接池)获取Connection 超时时间，单位毫秒。这个属性是新加的属性，因为目前版本是可以共享连接池的。
		 * setSocketTimeout：请求获取数据的超时时间(即响应时间)，单位毫秒。 如果访问一个接口，多少时间内无法返回数据，就直接放弃此次调用。
		 */
		RequestConfig requestConfig = RequestConfig.custom()
				.setConnectTimeout(CONNECT_TIMEOUT)
				.setSocketTimeout(SOCKET_TIMEOUT)
				.build();
		httpGet.setConfig(requestConfig);

		packageHeader(headers, httpGet);

		return getHttpClientResult( httpClient, httpGet, charset);
	}


	/**
	 * <p>Description: encapsulation request header</p>
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
	 * <p>Description: close resource</p>
	 * <p>关闭资源</p>
	 * @param response       响应体
	 * @param httpClient 	 客户端
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
}


/**
 * <p>Description: Request response object</p>
 * <p>请求响应对象</p>
 * @author sujay
 * @version 15:09 2022/2/18
 * @since JDK1.8 <br/>
 */
class HttpClientResult {

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
