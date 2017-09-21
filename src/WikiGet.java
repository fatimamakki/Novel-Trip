import java.util.List;
import info.bliki.api.Page;
import info.bliki.api.User;
import info.bliki.wiki.model.WikiModel;


public class WikiGet {
	public static void main(String[] args){
		String[] listOfTitleStrings = { "Paris" };
		User user = new User("", "", "http://en.wikipedia.org/w/api.php");
		user.login();
		List<Page> listOfPages = user.queryContent(listOfTitleStrings);
		for (Page page : listOfPages) {
		  WikiModel wikiModel = new WikiModel("${image}", "${title}");
		  String html = wikiModel.render(page.toString());
		  System.out.println(html);
		}
	}
}
