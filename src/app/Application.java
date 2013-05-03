package app;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;

public class Application {

	/**
	 * @author yeshwanth
	 * @param args
	 */

	private String clientID = "IwGDetafsx39DiZK3SpS6PlAH6esP4SLoztEUrIyirJPpDm62E";
	private String clientSecret = "piUXineq0p1ouQTk66MX9IVMfnj1qg6ozr36xvZaxweWBFZIpi";

	private String getRequest() throws IllegalStateException, IOException {

		String getUrl = "http://join.agiliq.com/oauth/app_authorize?redirect_uri=https://www.google.co.in/&client_id="
				+ clientID;
		HttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet(getUrl);
		HttpResponse response = client.execute(request);
		for (Header header : response.getAllHeaders()) {
			System.out.println(header.getName() + "\t" + header.getValue());
		}
		System.out.println(response.getAllHeaders() + "\n headers");
		System.out.println(response.getParams() + "\nparameters");
		System.out.println(response.toString() + "\nresponse");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response
				.getEntity().getContent()));

		String line = "";
		while ((line = rd.readLine()) != null) {
			System.out.println(line);
		}
		return null;
	}

	private String getAccessToken() {
		String postUrl = "http://join.agiliq.com/oauth/access_token?client_id="
				+ clientID
				+ "&redirect_uri=https://www.google.co.in/&client_secret="
				+ clientSecret
				+ "&code=OZGVHMvJtJ1SzPZhnDMUTDGCGoGOYCYlYa0l0pBps1qdkIoRva";
		String accessToken = "";
		System.out.println(postUrl+"\n post url");
		HttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet(postUrl);
		try {

			HttpResponse response = client.execute(get);
			BufferedReader rd = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));

			String line = "";
			while ((line = rd.readLine()) != null) {
				System.out.println(line);
				if(line.contains("access_token")){
					int startIndex = 0,endIndex = 0;
					startIndex = line.indexOf("access_token");
					startIndex = line.indexOf(":",startIndex);
					startIndex = line.indexOf('"',startIndex)+1;
					endIndex = line.indexOf('"',startIndex);
					accessToken = line.substring(startIndex, endIndex);
					System.out.println(accessToken+"\n access");
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return accessToken;
	}

	public void postRequest(){
		String postUrl ="http://join.agiliq.com/api/resume/upload/?access_token="+getAccessToken();
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(postUrl);
		try {
			File file = new File("././Resume-yesh.pdf");

			FileBody fbody = new FileBody(file);
			MultipartEntity entity = new MultipartEntity();
			entity.addPart("first_name", new StringBody("Yeshwanth"));
			entity.addPart("last_name", new StringBody("Kumar"));
			entity.addPart("projects_url", new StringBody(
					"https://github.com/yeshwanth43"));
			entity.addPart("code_url", new StringBody(
					"https://github.com/yeshwanth43/agiliq"));

			entity.addPart("resume", fbody);
			post.setEntity(entity);
			HttpResponse response = client.execute(post);
			System.out.println(response.getStatusLine());
			BufferedReader rd = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));

			String line = "";
			while ((line = rd.readLine()) != null) {
				System.out.println(line);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) throws IllegalStateException,
			IOException {

		Application app = new Application();
		app.postRequest();
	}

}