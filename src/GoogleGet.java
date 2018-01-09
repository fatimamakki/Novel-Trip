import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class GoogleGet {
	public String[] cResults;
	public String[] iResults;
    //testinghghg
	public void query(String name) throws IOException, JSONException {
		name = name.replaceAll(" ", "%20");
		URL url = new URL("https://ajax.googleapis.com/ajax/services/search/images?v=1.0&imgtype=photo&rsz=8&q="+name);
		URLConnection connection = url.openConnection();
		connection.addRequestProperty("Referer", "");
		String line;
		StringBuilder builder = new StringBuilder();
		BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		while((line = reader.readLine()) != null) {
			builder.append(line);
		}
		JSONObject json = new JSONObject(builder.toString());
		JSONArray jResults = (JSONArray) ((JSONObject) json.get("responseData")).get("results");
		cResults = new String[8];
		iResults = new String[8];
		for(int i=0; i<jResults.length();i++){
			String contentUrl = (String) ((JSONObject)jResults.get(i)).get("originalContextUrl");
			String imageUrl = (String) ((JSONObject)jResults.get(i)).get("url");
			Document document = Jsoup.connect(contentUrl).timeout(0).userAgent("Macintosh").get();
			cResults[i] = document.body().text(); 
			iResults[i] = imageUrl;
		}
	}
}
