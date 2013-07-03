/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ccrawl;

/**
 *
 * @author Bedirhan
 */
public class CcrawlDetails {
    
    private int numberOfFiles;
    private int numberOfMatchingFiles;
    private int numberOfSeconds;
    private int numberOfMatches;
    private int numberOfDirectories;
    private String currentCrawlPattern;
    private String currentFilterPattern;
    private String currentFilePattern;
    private String title;

    
    public synchronized void clearDetails(){
        setNumberOfDirectories(0);
        setNumberOfFiles(0);
        setNumberOfMatches(0);
        setNumberOfMatchingFiles(0);
        setNumberOfSeconds(0);
        setTitle("");        
    }
    
    /**
     * @return the numberOfFiles
     */
    public int getNumberOfFiles() {
        return numberOfFiles;
    }

    /**
     * @param numberOfFiles the numberOfFiles to set
     */
    public void setNumberOfFiles(int numberOfFiles) {
        this.numberOfFiles = numberOfFiles;
    }

    /**
     * @return the numberOfSeconds
     */
    public int getNumberOfSeconds() {
        return numberOfSeconds;
    }

    /**
     * @param numberOfSeconds the numberOfSeconds to set
     */
    public void setNumberOfSeconds(int numberOfSeconds) {
        this.numberOfSeconds = numberOfSeconds;
    }

    /**
     * @return the numberOfMatches
     */
    public int getNumberOfMatches() {
        return numberOfMatches;
    }

    /**
     * @param numberOfMatches the numberOfMatches to set
     */
    public void setNumberOfMatches(int numberOfMatches) {
        this.numberOfMatches = numberOfMatches;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the numberOfDirectories
     */
    public int getNumberOfDirectories() {
        return numberOfDirectories;
    }
    
    /**
     * @param numberOfDirectories the numberOfDirectories to set
     */
    public void setNumberOfDirectories(int numberOfDirectories) {
        this.numberOfDirectories = numberOfDirectories;
    }
    
    public void incrementNumberOfDirectories(){
        this.numberOfDirectories++;
    }

    public void incrementNumberOfFiles(){
        this.numberOfFiles++;
    }
    
    public void incrementNumberOfMatchingFiles(){
        this.numberOfMatchingFiles++;
    }    

    public void incrementNumberOfMatches(){
        this.numberOfMatches++;
    }

    /**
     * @return the numberOfMatchingFiles
     */
    public int getNumberOfMatchingFiles() {
        return numberOfMatchingFiles;
    }

    /**
     * @param numberOfMatchingFiles the numberOfMatchingFiles to set
     */
    public void setNumberOfMatchingFiles(int numberOfMatchingFiles) {
        this.numberOfMatchingFiles = numberOfMatchingFiles;
    }
    
    /**
     * @return the currentCrawlPattern
     */
    public String getCurrentCrawlPattern() {
        return currentCrawlPattern;
    }

    /**
     * @param currentCrawlPattern the currentCrawlPattern to set
     */
    public void setCurrentCrawlPattern(String currentCrawlPattern) {
        this.currentCrawlPattern = currentCrawlPattern;
    }

    /**
     * @return the currentFilterPattern
     */
    public String getCurrentFilterPattern() {
        return currentFilterPattern;
    }

    /**
     * @param currentFilterPattern the currentFilterPattern to set
     */
    public void setCurrentFilterPattern(String currentFilterPattern) {
        this.currentFilterPattern = currentFilterPattern;
    }

    /**
     * @return the currentFilePattern
     */
    public String getCurrentFilePattern() {
        return currentFilePattern;
    }

    /**
     * @param currentFilePattern the currentFilePattern to set
     */
    public void setCurrentFilePattern(String currentFilePattern) {
        this.currentFilePattern = currentFilePattern;
    }
    
}
