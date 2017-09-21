import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.Sentence;

public class ParseNovel {
	String text;
	String title;
	TreeMap<String, LocationInfo> locations;
	List<LocationInfo> sorted;
	HashMap<Integer, ArrayList<String>> characters;
	ArrayList<LocationInfo> conflicts;
	ArrayList<String>chars;
	public ParseNovel(String path, String titleN) throws IOException{
		this.title = titleN;
		System.out.println("The title is "+this.title);
		locations = new TreeMap<String, LocationInfo>();
		characters = new HashMap<Integer, ArrayList<String>>();
		chars = new ArrayList<String>();
		conflicts = new ArrayList<LocationInfo>();
		String type = path.split("\\.")[1];
		if(type.equals("pdf")){
			PDDocument doc = PDDocument.load(path);
			PDFTextStripper stripper = new PDFTextStripper();
			text = stripper.getText(doc);
			doc.close();
		}
		else if(type.equals("docx")){
			InputStream fs = null;  
			XWPFWordExtractor extractor = null ;
			fs = new FileInputStream(path);
			XWPFDocument hdoc;
			try {
				hdoc = new XWPFDocument(OPCPackage.open(fs));
				extractor = new XWPFWordExtractor(hdoc);
				text=extractor.getText();
			}
			catch (InvalidFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else{
			//List<String> lines = Files.readAllLines(Paths.get(path), StandardCharsets.UTF_8);
			text = new String(Files.readAllBytes(Paths.get(path)), StandardCharsets.UTF_8);
		}
		//System.out.println(text);
	}
	public void parsing() throws ClassCastException, ClassNotFoundException, IOException{
		String serializedClassifier = "classifiers/english.all.3class.distsim.crf.ser.gz";
		AbstractSequenceClassifier<CoreLabel> classifier = CRFClassifier.getClassifier(serializedClassifier);
		PrintStream p = new PrintStream(new File("story.txt"));
		p.print(text);
		int counter = 1;
		List<List<CoreLabel>> out = classifier.classify(text);
		int countSentences = -1;
		//*******************************Locations******************************************
		for (List<CoreLabel> sentence : out) {
			String loc = "";
			boolean before = false;
			countSentences++;
			boolean startingLoc = true;
			characters.put(countSentences, new ArrayList<String>());
			for (CoreLabel word : sentence) {
				String type = word.get(CoreAnnotations.AnswerAnnotation.class);
				if(type.equalsIgnoreCase("location")){
					if(before){
						loc = loc + " "+ word.word();
					}
					else{
						loc = word.word();
						before = true;
					}

					//check position

				}
				//not a location
				else 
				{
					
					if(type.equalsIgnoreCase("person")){
						if(!chars.contains(word.word().toLowerCase())) chars.add(word.word().toLowerCase());
						if(!characters.get(countSentences).contains(word.word().toLowerCase())){
							characters.get(countSentences).add(word.word().toLowerCase());
						}
					}
					
					if(before){
						//add the location
							if(locations.containsKey(loc.toLowerCase())){

								locations.get(loc.toLowerCase()).totalTF++;
								locations.get(loc.toLowerCase()).sentences.add(countSentences);
								locations.get(loc.toLowerCase()).text += Sentence.listToString(sentence);
							}
							else{
								locations.put(loc.toLowerCase(), new LocationInfo(loc.toLowerCase()));
								locations.get(loc.toLowerCase()).sentences.add(countSentences);
								locations.get(loc.toLowerCase()).title = this.title;
							}
							if(startingLoc) locations.get(loc.toLowerCase()).beginTF++;
					}
					startingLoc = false;
					before = false;
					loc = "";
					
				}
			}
		}

		setLocations();
		rankLocations();
		
	}
	public void printLocations(Map<String,LocationInfo> locs){
		Object[] array = locs.keySet().toArray();
		for(int i=0; i<array.length; i++){
			System.out.println(i +": " + array[i] + " count: " + locs.get(array[i]).totalTF + " count Staring: " + locs.get(array[i]).beginTF);
		}
	}
	public void setLocations(){
		Object[] array = locations.keySet().toArray();
		for(int i=0; i<array.length; i++){
			locations.get(array[i]).wieght = locations.get(array[i]).totalTF + 2*locations.get(array[i]).beginTF;
			try {
				locations.get(array[i]).chooseImage();
			}
			catch (Exception e) {
				System.out.println("Error in saving image");
				e.printStackTrace();
			}
			System.out.println(array[i] + " " + locations.get(array[i]).wieght);
			Integer[] sentences = new Integer[locations.get(array[i]).sentences.size()];
			locations.get(array[i]).sentences.toArray(sentences);
			//add characters
			for(int j=0; j<sentences.length; j++ ){
				locations.get(array[i]).characters.addAll(characters.get(sentences[j]));
				//locations.get(array[i]).text += sentences[j];
			}
			//remove duplicates in characters
			HashSet hs = new HashSet();
			hs.addAll(locations.get(array[i]).characters);
			locations.get(array[i]).characters.clear();
			locations.get(array[i]).characters.addAll(hs);
			//remove locations that are names of persons
			if(chars.contains(array[i])){ 
				System.out.println("--> " + array[i]);
				locations.remove(array[i]); 
				conflicts.add(locations.get(array[i]));
				
				}
		}
	}
	public void rankLocations(){
		sorted = new ArrayList<LocationInfo>(locations.values());

	    Collections.sort(sorted, new Comparator<LocationInfo>() {

	        public int compare(LocationInfo o1, LocationInfo o2) {
	        	if(o1.wieght == o2.wieght) return 0;
	    		if(o1.wieght < o2.wieght) return 1;
	    		else
	    			return -1;
	        }
	    });

//	    for (LocationInfo p : sorted) {
//	        System.out.println(p.name + "\t" + p.wieght + "\n Characters: " + p.characters);
//	    }
	}
	public List<LocationInfo> getList(){
		return sorted;
	}
}
class ValueComparator implements Comparator<String> {

	TreeMap<String, LocationInfo> base;
	public ValueComparator(TreeMap<String, LocationInfo> base) {
		this.base = base;
	}

	// Note: this comparator imposes orderings that are inconsistent with equals.    
	public int compare(String a, String b) {
		if (base.get(a).wieght >= base.get(b).wieght) {
			return 1;
		} else {
			return -1;
		} // returning 0 would merge keys
	}
}
