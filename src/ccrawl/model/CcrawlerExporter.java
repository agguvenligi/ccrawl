/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ccrawl.model;

import ccrawl.CcrawlContext;
import ccrawl.CcrawlDetails;
import ccrawl.CcrawlMainView;
import ccrawl.CcrawlMatch;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
/**
 *
 * @author TCBURGUN
 */
public class CcrawlerExporter {
    
    private CcrawlMainView parent;
    private CcrawlContext context;
    private CcrawlDetails details;
    
    private int counter;
    
    public CcrawlerExporter(CcrawlMainView parent){
        this.parent = parent;
        this.context = parent.getContext();
        this.details = parent.getDetails();
        counter = 1;
    }
    
    public void export(File fileToExport){
        
        if(fileToExport == null)
            return;
         
        StringBuilder template = new StringBuilder();
        
        try{
            readTemplate(template);
        }
        catch(Exception e){
            parent.updateStatus("Can not read template file " + e.getMessage());
            return;
        }
        
        HashMap<String, List<CcrawlMatch>> groupMatchesByFileName = null;
        
        try{
            groupMatchesByFileName = groupMatchesByFileName();
        }
        catch(Exception pse){
            parent.updateStatus("Filter regex syntax problem: " + pse.getMessage());
            return;
        }     
        
        StringBuilder matchesContent = new StringBuilder();
        for(String aFileName : groupMatchesByFileName.keySet())
            produceAFile(aFileName, groupMatchesByFileName.get(aFileName), matchesContent);
        
        
        String document = template.toString().replace("#content", matchesContent.toString());  
        
        BufferedWriter output = null;
        try {
            output = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileToExport)));
            output.write(document);
        }
        catch(Exception e){
            parent.updateStatus("Error when exporting results: " + e.getMessage());
        }
        finally {
            if(output != null){
                try{
                    output.close();
                }
                catch(Exception e){}
            }
        }
        
        
        parent.updateStatus("Results are exported to " + fileToExport.getAbsolutePath());
    }
    
    private void readTemplate(StringBuilder template) throws IOException, FileNotFoundException{        
        InputStream in = getClass().getResourceAsStream("/ccrawl/resources/ccrawl_template.html"); 
        Reader fr = new InputStreamReader(in);                
        BufferedReader br = new BufferedReader(fr);   
        
        String line;         
        while((line = br.readLine()) != null) {              
            template.append(fillTemplate(line));
        }         
    }
    
    private String fillTemplate(String line){
        String titleRegex = "#title";
        String crawlRegex = "#crawlregex";
        String filterRegex = "#filterregex";
        String noOfFilesRegex = "#nooffiles";
        String noOfMatchesRegex = "#noofmatches";
        String fileRegex = "#fileregex";
                
        line = line.replace(titleRegex, details.getTitle());
        line = line.replace(crawlRegex, details.getCurrentCrawlPattern());
        line = line.replace(filterRegex, details.getCurrentFilterPattern());
        line = line.replace(noOfFilesRegex, Integer.toString(details.getNumberOfMatchingFiles()));        
        line = line.replace(fileRegex, details.getCurrentFilePattern());
        line = line.replace(noOfMatchesRegex, Integer.toString(details.getNumberOfMatches()));
        
        return line;
    }
    
    private HashMap<String, List<CcrawlMatch>> groupMatchesByFileName() throws PatternSyntaxException{
        
        Pattern filter = Pattern.compile(details.getCurrentFilterPattern());
        
        HashMap<String, List<CcrawlMatch>> matchesByFileName = new HashMap<String, List<CcrawlMatch>>();        
        
        List<CcrawlMatch> currentMatches = context.getCurrentMatches();
        
        for (CcrawlMatch aMatch : currentMatches){           
            Matcher matcher = filter.matcher(aMatch.getMatchContext());        
            if(filter != null && matcher.find()){
                if(matchesByFileName.containsKey(aMatch.getPath())){
                    List<CcrawlMatch> matches = matchesByFileName.get(aMatch.getPath());
                    matches.add(aMatch);
                }
                else{
                    List<CcrawlMatch> matches = new ArrayList<CcrawlMatch>();
                    matches.add(aMatch);
                    matchesByFileName.put(aMatch.getPath(), matches);
                }
            }
        }         
        
        return matchesByFileName;
    }
    
    private void produceAFile(String fileName, List<CcrawlMatch> matches, StringBuilder matchesContent){
        
        List<String> lines = null;
        
        try{
            lines = readFileIntoLines(fileName);
        }
        catch(Exception e){}
        
        
        
        matchesContent.append("<div class='file'>File: "+new File(fileName).getAbsolutePath()+"</div>");
        matchesContent.append("<div class='matches'>");
        String previousRelatedContext = "";
        int previousMatchIndex = 0;
        for(CcrawlMatch aMatch : matches){
                matchesContent.append("<div class='result'>");
                //matchesContent.append("<span class='label'>Index:</span>");
                //matchesContent.append("<span class='value'>" + counter + "</span>");
                matchesContent.append("<span class='label'>Line:</span>");
                matchesContent.append("<span class='value'>" + aMatch.getFirstLineNumber() + "</span>");
                matchesContent.append("<span class='label'>Column:</span>");
                matchesContent.append("<span class='value'>" + aMatch.getMatchStartIndex() + "</span>");
                matchesContent.append("<div class='matchcontext'>");
                
                String relatedContext = fetchRelatedLines(lines, aMatch);
                int beginIndex = relatedContext.indexOf(aMatch.getMatch());
                
                // a control for matches in a single line
                if(relatedContext.equals(previousRelatedContext))
                    beginIndex = relatedContext.indexOf(aMatch.getMatch(), previousMatchIndex);
                
                previousRelatedContext = relatedContext;
                
                int lastIndex = beginIndex + aMatch.getMatch().length();
                
                previousMatchIndex = lastIndex; // store this
                
                String prefix = relatedContext.substring(0, beginIndex);
                String match = relatedContext.substring(beginIndex, lastIndex);
                String postfix = relatedContext.substring(lastIndex, relatedContext.length());
                
                matchesContent.append(encodeHTML(prefix).replace("\n", "<br/>"));
                matchesContent.append("<span class='finding'>" + encodeHTML(match).replace("\n", "<br/>") + "</span>");
                matchesContent.append(encodeHTML(postfix).replace("\n", "<br/>"));
                matchesContent.append("</div>");
            matchesContent.append("</div>");    
            counter++;
        }
        matchesContent.append("</div>");     
        
        
        
    }
    
    private List<String> readFileIntoLines(String fileName) throws IOException, FileNotFoundException{        

        FileInputStream fstream = new FileInputStream(fileName);
        DataInputStream in = new DataInputStream(fstream);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));

        List<String> lines = new ArrayList<String>();
        String line;         
        while((line = br.readLine()) != null)
            lines.add(line);
        
        return lines;
    }
    
    private String fetchRelatedLines(List<String> lines, CcrawlMatch match){
        StringBuilder sb = new StringBuilder();
        
        for(int i=0; i < lines.size(); i++){
            if((i >= match.getFirstLineNumber() - 3) && i <= (match.getLastLineNumber() + 3)){
                sb.append(lines.get(i));
                sb.append(match.getNewline());
            }
        }
        
        return sb.toString();
    }
       
    public String encodeHTML(String s) {     
        StringBuffer out = new StringBuffer();     
        for(int i=0; i<s.length(); i++)     {         
            char c = s.charAt(i);         
            if(c > 127 || c=='"' || c=='<' || c=='>')         {            
                out.append("&#"+(int)c+";");         
            }         
            else {             
                out.append(c);         
            }     
        }
        return out.toString(); 
    }    
}
