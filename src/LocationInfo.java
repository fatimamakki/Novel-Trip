import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import javax.imageio.ImageIO;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.util.IOUtils;
import org.apache.lucene.util.Version;
import org.apache.poi.hslf.blip.Bitmap;
import org.json.JSONException;
import org.tartarus.snowball.ext.EnglishStemmer;


public class LocationInfo implements Comparable<LocationInfo>{
	int totalTF;
	int listTF;
	int beginTF;
	int prepTF;
	double wieght;
	String title;
	String name;
	BufferedImage image;
	ArrayList<String> characters;
	TreeSet<Integer>sentences;
	String text;
	String imageUrl;
	public LocationInfo(String name){
		this.name = name;
		totalTF = 1;
		listTF = 0;
		beginTF = 0;
		wieght = 0;
		characters = new ArrayList<String>();
		sentences = new TreeSet<Integer>();
		text = "";
	}
	@Override
	public int compareTo(LocationInfo o) {
		// TODO Auto-generated method stub
		if(wieght == o.wieght) return 0;
		if(wieght > o.wieght) return 1;
		else
			return -1;
	}
	public void chooseImage() throws Exception{
		GoogleGet getImage = new GoogleGet();
		getImage.query(name);
		String[] resultsUrl = getImage.cResults;
		String[] imagesUrl  = getImage.iResults;
		System.out.println("Size of urls: " + resultsUrl.length);
		int index = getTopRelated(resultsUrl);
		this.imageUrl = imagesUrl[index];
		saveImage(imagesUrl[index]);

	}
	public int getTopRelated(String[] texts) throws Exception{
		text = text + "medieval " + this.title;
		Map<String,Double> Q = preprocessP(text);
		double[] scores = new double[texts.length];
		double epsilon = 0.00000001;
		double denom;
		Object[] w = Q.keySet().toArray();
		Arrays.fill(scores, 0.0);
		Map<String,Double> M;
		for (int i=0; i<texts.length; i++){
			M = preprocessP(texts[i]);
			for(int j=0;j<w.length;j++){
				if(!M.containsKey(w[j]))
					denom = epsilon;
				else
					denom = M.get(w[j]);
				scores[i] = scores[i] + Q.get(w[j])*Math.log(Q.get(w[j])/denom);
			}
		}
		//find best score and return its index
		double min = scores[0];
		int index = 0;
		for(int i=1; i<scores.length; i++){
			System.out.print(scores[i]+" ");
			if(scores[i] > min){
				min = scores[i];
				index = i;
			}
		}
		return index;
	}
	public void saveImage(String imageUrl) throws IOException {
		URL url = new URL(imageUrl);
		String destinationFile = "Images/" + name + ".jpg";
		InputStream is = url.openStream();
		OutputStream os = new FileOutputStream(destinationFile);

		byte[] b = new byte[2048];
		int length;

		while ((length = is.read(b)) != -1) {
			os.write(b, 0, length);
		}
		File fileImage = new File(destinationFile);
		image = ImageIO.read(fileImage);
		is.close();
		os.close();
	}
	public Map<String, Double> preprocessP(String text) throws Exception {
		double tokensNumber;
		Map<String, Double> termsFrequency = new HashMap<String,Double>();
		text = text.toLowerCase(); //to lower case
		text = text.replaceAll("[,.']", "");
		text = text.replaceAll("\\d", "");
		text = text.replaceAll("\\P{InBasic_Latin}", "");
		text = removeStopWords(text);
		tokensNumber = text.split(" ").length*1.0;
		EnglishStemmer english = new EnglishStemmer();
		TokenStream tokenStream = new StandardTokenizer(Version.LUCENE_48, new StringReader(text.trim()));
		CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);
		tokenStream.reset();
		while (tokenStream.incrementToken()) {
			String term = charTermAttribute.toString();
			english.setCurrent(term);
			english.stem();
			term = english.getCurrent();
			if(!termsFrequency.containsKey(term)) 
				termsFrequency.put(term, 1.0);
			else{
				double count = termsFrequency.get(term);
				count++;
				termsFrequency.put(term, count);
			}
		}
		Object[] terms = termsFrequency.keySet().toArray();
		for(int i=0; i<terms.length; i++)
			termsFrequency.put((String)terms[i],termsFrequency.get(terms[i])/tokensNumber);
		return termsFrequency;
	}
	public static String removeStopWords(String textFile) throws Exception {
		CharArraySet stopWords = EnglishAnalyzer.getDefaultStopSet();
		TokenStream tokenStream = new StandardTokenizer(Version.LUCENE_48, new StringReader(textFile.trim()));

		tokenStream = new StopFilter(Version.LUCENE_48, tokenStream, stopWords);
		StringBuilder sb = new StringBuilder();
		CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);
		tokenStream.reset();
		while (tokenStream.incrementToken()) {
			String term = charTermAttribute.toString();
			sb.append(term + " ");
		}
		return sb.toString();
	}
}
