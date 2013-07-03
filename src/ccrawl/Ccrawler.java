/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ccrawl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 *
 * @author TCBURGUN
 */
public class Ccrawler implements Runnable {

    private CcrawlMainView mainview;
    private CcrawlContext context;
    private CcrawlDetails details;
    
    public Ccrawler(CcrawlMainView mainview){
        this.mainview = mainview;
        this.context = this.mainview.getContext();
        this.details = this.mainview.getDetails();
    }
        
    public void run() {
        context.cleanCurrentMatches();
        details.clearDetails();
        long crawlStartTime = System.currentTimeMillis();
        try{
            visitAllFiles(context.getCurrentDirectory());
            
        }
        catch(IOException ioe){
            this.mainview.updateStatus("I/O exception: " + ioe.getMessage());
            return;
        }
        long duration = System.currentTimeMillis() - crawlStartTime;
        int numberOfSeconds = (int)TimeUnit.MILLISECONDS.toSeconds(duration);
        details.setNumberOfSeconds(numberOfSeconds);
        String m = details.getNumberOfFiles() + " files analyzed " ;
        m += ", " + details.getNumberOfMatches() + " matches in " + details.getNumberOfMatchingFiles() + " files in " + details.getNumberOfSeconds() + " seconds";
        this.mainview.updateStatus(m);
        
    }
    
    private void visitAllFiles(File dir) throws IOException{
        if (dir.isDirectory()) {
            details.incrementNumberOfDirectories();
            String[] children = dir.list();
            for (int i=0; i<children.length; i++) {                
                visitAllFiles(new File(dir, children[i]));
            }
        } else {            
            if(isMatchingFile(dir.getName())){
                details.incrementNumberOfFiles();                
                applyRegularExpression(dir);
            }
        }
    }    

    private void applyRegularExpression(File aFile) throws IOException{
        this.mainview.updateStatus("Crawling code started");        
        
        Pattern pattern = null;

        try{
            pattern = Pattern.compile(mainview.getCrawlPattern());
        }
        catch(PatternSyntaxException pse){
            this.mainview.updateStatus("Crawl regex syntax problem: " + pse.getMessage());
            return;
        }
        // read the whole file as char sequence
        CharSequence cs = fromFile(aFile);
        int lengthOfCharSequence = cs.length();
        
        Matcher matcher = pattern.matcher(cs);
        
        int currentCSIndex = 0;
        int currentLineNumber = 1;
                
        boolean found = false;
        // Find all matches
        while (matcher.find()) {
            found = true;
            details.incrementNumberOfMatches();
            CcrawlMatch aCcrawlMatch = new CcrawlMatch();

            // Get the matching string
            String match = matcher.group();
            aCcrawlMatch.setMatch(match);
            aCcrawlMatch.setPath(aFile.getAbsolutePath());
            
            MatchResult matchResult = matcher.toMatchResult();
            
            int matchStartIndex = matchResult.start();
            aCcrawlMatch.setMatchStartIndex(matchStartIndex);
            int matchEndIndex = matchResult.end();
            aCcrawlMatch.setMatchEndIndex(matchEndIndex);
            
            int indexOfCurrentLine = 0;
            int currentCSIndexAHead = 0;
            int indexOfMatchStartLine = 0;
            int indexOfMatchEndLine = 0;
            String newLine = "";
            while(currentCSIndex < lengthOfCharSequence){
                
                if(currentCSIndex == matchStartIndex){
                    aCcrawlMatch.setFirstLineNumber(currentLineNumber);
                    indexOfMatchStartLine = indexOfCurrentLine;
                }
                
                if(currentCSIndex == matchEndIndex){
                    aCcrawlMatch.setLastLineNumber(currentLineNumber);
                    currentCSIndexAHead = currentCSIndex;
                    
                    // get the index of the end of this line                    
                    while(currentCSIndexAHead < lengthOfCharSequence){
                        if(cs.charAt(currentCSIndexAHead) == '\r' || cs.charAt(currentCSIndexAHead) == '\n'){
                            indexOfMatchEndLine = currentCSIndexAHead;
                            break;
                        }
                        currentCSIndexAHead++;
                    }
                    if(currentCSIndexAHead >= lengthOfCharSequence)
                        indexOfMatchEndLine = lengthOfCharSequence - 1;
                    break;
                }

                // check \r\n
                if((currentCSIndex < lengthOfCharSequence - 1) && cs.charAt(currentCSIndex) == '\r' && cs.charAt(currentCSIndex + 1) == '\n'){
                    if(newLine.isEmpty())
                        newLine = "\r\n";
                    // a quick but vital control
                    //if(currentCSIndex == matchStartIndex)
                    //    aCcrawlMatch.setFirstLineNumber(currentLineNumber);
                    currentCSIndex++; // consume \r, but be careful with the matchStartIndex
                    continue;
                }
                else if(cs.charAt(currentCSIndex) == '\r' || cs.charAt(currentCSIndex) == '\n'){
                    if(newLine.isEmpty() && cs.charAt(currentCSIndex) == '\r')
                        newLine = "\r";
                    else if(newLine.isEmpty() && cs.charAt(currentCSIndex) == '\n')
                        newLine = "\n";                        
                    // handle new line here                    
                    if(currentCSIndex < cs.length() - 1)
                        indexOfCurrentLine = currentCSIndex + 1;
                    else
                        indexOfCurrentLine = currentCSIndex;
                    
                    currentLineNumber++;
                }
                
                currentCSIndex++;
            }
            aCcrawlMatch.setNewline(newLine);
            aCcrawlMatch.setMatchContext(cs.subSequence(indexOfMatchStartLine, indexOfMatchEndLine));
            context.addMatch(aCcrawlMatch);                       
        }
        if(found)
            details.incrementNumberOfMatchingFiles();
    }    
    
    private CharSequence fromFile(File aFile) throws IOException {
        FileInputStream input = new FileInputStream(aFile);
        FileChannel channel = input.getChannel();
        ByteBuffer bbuf = channel.map(FileChannel.MapMode.READ_ONLY, 0, (int)channel.size());
        CharBuffer cbuf = Charset.forName("8859_9").newDecoder().decode(bbuf);        
        try{
            input.close();
        }
        catch(IOException ioe){
            
        }
        return cbuf;
    } 
    
    private boolean isMatchingFile(String fileName){
        if(context.getCurrentFilePattern() == null)
            return false;
        Matcher matcher = context.getCurrentFilePattern().matcher(fileName);
        if(matcher.matches())
            return true;        
        return false;
    }    
}
