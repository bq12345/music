package org.bq;

import java.io.IOException;
import java.io.PrintWriter;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;

/**
 * @author 白强
 * 
 */
public class Search extends ActionSupport {

	private String song;
	private String page;

	private static final long serialVersionUID = 1L;

	private String contentType = "text/html;charset=utf-8";

	@Override
	public String execute() throws Exception {
		String s = getList();
		if (s == "") {
			return ERROR;
		}
		ServletActionContext.getResponse().setContentType(contentType);
		PrintWriter out = ServletActionContext.getResponse().getWriter();
		try {
			out.print(s);
			out.flush();
			out.close();
		} catch (Exception ex) {
			out.println(ex.toString());
		}
		return SUCCESS;
	}

	public String getList() {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
			song = new String(song.getBytes("ISO-8859-1"), "utf-8");
			HttpGet httpGet = new HttpGet(
					"http://www.xiami.com/app/nineteen/search/key/"
							+ this.getSong() + "/page/" + this.getPage());
			httpGet.setHeader("Referer", ":http://localhost:8080/");
			CloseableHttpResponse response = httpclient.execute(httpGet);
			System.out.println(response.getStatusLine());
			ResponseHandler<String> responseHandler = new ResponseHandler<String>() {
				public String handleResponse(final HttpResponse response)
						throws ClientProtocolException, IOException {
					int status = response.getStatusLine().getStatusCode();
					if (status >= 200 && status < 300) {
						HttpEntity entity = response.getEntity();
						return entity != null ? EntityUtils.toString(entity)
								: null;
					} else {
						throw new ClientProtocolException(
								"Unexpected response status: " + status);
					}
				}
			};
			String responseBody = httpclient.execute(httpGet, responseHandler);
			System.out.println("----------------------------------------");
			System.out.println(responseBody);
			return responseBody;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			try {
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return contentType;
	}

	public String getPage() {
		return page;
	}

	public String getSong() {
		return song;
	}

	public void setPage(String page) {
		this.page = page;
	}

	public void setSong(String song) {
		this.song = song;
	}

}
