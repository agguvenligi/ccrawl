/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ccrawl;

/**
 *
 * @author Bedirhan
 */
public class CcrawlMatch{
    
    private int firstLineNumber;
    private int lastLineNumber;
    private String path;
    private CharSequence matchContext;
    private String match;
    private int matchStartIndex, matchEndIndex;
    private String newline;
    
    /**
     * @return the firstLineNumber
     */
    public int getFirstLineNumber() {
        return firstLineNumber;
    }

    /**
     * @param firstLineNumber the firstLineNumber to set
     */
    public void setFirstLineNumber(int firstLineNumber) {
        this.firstLineNumber = firstLineNumber;
    }

    /**
     * @return the lastLineNumber
     */
    public int getLastLineNumber() {
        return lastLineNumber;
    }

    /**
     * @param lastLineNumber the lastLineNumber to set
     */
    public void setLastLineNumber(int lastLineNumber) {
        this.lastLineNumber = lastLineNumber;
    }

    /**
     * @return the path
     */
    public String getPath() {
        return path;
    }

    /**
     * @param path the path to set
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * @return the matchContext
     */
    public CharSequence getMatchContextRaw() {
        return matchContext;
    }

    public String getMatchContext() {
        return matchContext.toString().replaceAll("\r", "\\\\r").replaceAll("\n", "\\\\n");
    }

    /**
     * @param matchContext the matchContext to set
     */
    public void setMatchContext(CharSequence matchContext) {
        this.matchContext = matchContext;
    }

    /**
     * @return the match
     */
    public String getMatch() {
        return match;
    }

    /**
     * @param match the match to set
     */
    public void setMatch(String match) {
        this.match = match;
    }

    /**
     * @return the matchStartIndex
     */
    public int getMatchStartIndex() {
        return matchStartIndex;
    }

    /**
     * @param matchStartIndex the matchStartIndex to set
     */
    public void setMatchStartIndex(int matchStartIndex) {
        this.matchStartIndex = matchStartIndex;
    }

    /**
     * @return the matchEndIndex
     */
    public int getMatchEndIndex() {
        return matchEndIndex;
    }

    /**
     * @param matchEndIndex the matchEndIndex to set
     */
    public void setMatchEndIndex(int matchEndIndex) {
        this.matchEndIndex = matchEndIndex;
    }

    public String getNewline() {
        return newline;
    }

    public void setNewline(String newline) {
        this.newline = newline;
    }
    
    public boolean equals(CcrawlMatch aMatch) {
        if(aMatch == null)
            return false;
        
        if(aMatch.match == this.match && 
                aMatch.path == this.path &&
                aMatch.firstLineNumber == this.firstLineNumber &&
                aMatch.lastLineNumber == this.lastLineNumber)
            return true;
            
        return false;
    }
    
    
}
