/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 * 
 * This class reads the OWASP code review categories and keywords from the XML file.
 */

package ccrawl.xml;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import java.io.InputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author tatli
 */
public class OwaspCodeReviewKeywords {
    
    
    private static final String KEYWORDS_FILE="/ccrawl/resources/owasp_codereview_keywords.xml";
    
    private static String[] array_categories;
    
    private static Multimap<String,String> keywords = HashMultimap.create(); 
    
    private static OwaspCodeReviewKeywords theInstance  = null;
    

    private OwaspCodeReviewKeywords() {
        
        readKeywordsConfiguration();
        
    }
    

    public static OwaspCodeReviewKeywords getheInstance() {
        
        if (theInstance == null) {
            theInstance = new OwaspCodeReviewKeywords();
        }
        
        return theInstance;
    }
    
    private void readKeywordsConfiguration() { 

    try {
    		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                InputStream in=getClass().getResourceAsStream(KEYWORDS_FILE);
		org.w3c.dom.Document doc = dBuilder.parse(in);
		doc.getDocumentElement().normalize();
 
		NodeList categoryList = doc.getElementsByTagName("category");
                
                array_categories = new String[categoryList.getLength()+1];
                array_categories[0]="";
 
		for (int temp = 0; temp < categoryList.getLength(); temp++) {
 
		   Node categoryNode = categoryList.item(temp);
                   
		   if (categoryNode.getNodeType() == Node.ELEMENT_NODE) {
 
		      Element eCategory = (Element) categoryNode;
                      
                      array_categories[temp+1]=  eCategory.getAttribute("name");
                      
                      NodeList keywordList = eCategory.getElementsByTagName("keyword");
                      
                      for (int temp2 = 0; temp2 < keywordList.getLength(); temp2++) {
                          
                          Node keywordNode = keywordList.item(temp2);
                          
                          Element eKeyword = (Element) keywordNode;
                          
                          keywords.put(eCategory.getAttribute("name"), eKeyword.getAttribute("value"));
                      
                      }
 
		   }
		}
	  } catch (Exception e) {
		e.printStackTrace();
	  }
  }


public String[] getCategories() {

    return array_categories;
}

public Multimap getKeywords() {

    return keywords;
}
 
}
