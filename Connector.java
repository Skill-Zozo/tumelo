package meloApp;

import java.util.ArrayList;
import java.util.List;

import javax.swing.text.html.HTMLDocument.HTMLReader.IsindexAction;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Connector {
	private CloseableHttpClient httpclient;

	public Connector() {
		httpclient = HttpClients.createDefault();
	}

	public Profile getUser(String usr, String pwd) {
		String hash = BCrypt.hashpw(pwd, BCrypt.gensalt(12));
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("type", "login"));
		nvps.add(new BasicNameValuePair("username", usr));
		nvps.add(new BasicNameValuePair("password", hash));
		JSONArray array_data= loginToServer(nvps);
		try {
			Object obj = array_data.get(0);
			System.out.println(obj instanceof Profile);
			Profile user = (Profile)(array_data.get(0));
			return user;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	private JSONArray loginToServer(List<NameValuePair> nvps) {
		try {
			HttpPost httpPost = new HttpPost(
					"http://137.158.58.29:8080/meloApp/Sever");
			httpPost.setEntity(new UrlEncodedFormEntity(nvps));
			CloseableHttpResponse response = httpclient.execute(httpPost);
			System.out.println(response.getStatusLine());
			HttpEntity entity = response.getEntity();
			String content = EntityUtils.toString(entity, "UTF-8");
			JSONObject json = new JSONObject(content);
			JSONArray usr = (JSONArray) json.get("user");
			EntityUtils.consume(entity);
			response.close();
			return usr;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) {
		Connector conn = new Connector();
		Profile user = conn.getUser("Banele", "secret");
		System.out.println(user.toString());
		/*CloseableHttpResponse response;
		try {
			CloseableHttpClient httpclient = HttpClients.createDefault();
			HttpPost httpPost = new HttpPost(
					"http://137.158.58.29:8080/meloApp/Sever");
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			nvps.add(new BasicNameValuePair("username", "vip"));
			nvps.add(new BasicNameValuePair("password", "secret"));
			httpPost.setEntity(new UrlEncodedFormEntity(nvps));
			response = httpclient.execute(httpPost);
			System.out.println(response.getStatusLine());
			HttpEntity entity = response.getEntity();
			String content = EntityUtils.toString(entity, "UTF-8");
			JSONObject json = new JSONObject(content);
			JSONArray usr = (JSONArray) json.get("user");
			System.out.println(usr.get(0));
			EntityUtils.consume(entity);
			response.close();
		} catch (IOException | ParseException | JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}
}