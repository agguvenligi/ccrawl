/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ccrawl;

import java.io.File;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import ccrawl.model.BeanTableModel;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import javax.swing.JTable;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
/**
 *
 * @author Bedirhan
 */
public class CcrawlContext {
    
    private File currentDirectory;
    private Pattern currentFilePattern;
    private ArrayList<CcrawlMatch> currentMatches;
    private BeanTableModel<CcrawlMatch> ccrawlMatchModel;
    private String encoding;
    private int linesBefore;
    private int linesAfter;
            

    public CcrawlContext(){
        currentDirectory = new File(".");
        currentMatches = new ArrayList<CcrawlMatch>();
        
        try{  
            currentFilePattern = Pattern.compile(".+\\.txt");                   
        }
        catch(PatternSyntaxException pse){     
            currentFilePattern = null;
        }      
        
        HashMap<String, String> columnNameMapping = new HashMap<String, String>();
        columnNameMapping.put("FirstLineNumber", "Line");
        columnNameMapping.put("Path", "File Path");
        columnNameMapping.put("MatchContext", "Match Context");
        
        List<String> columnNamesSorted = new ArrayList<String>();
        columnNamesSorted.add("Line");
        columnNamesSorted.add("File Path");
        columnNamesSorted.add("Match Context");                    

        this.ccrawlMatchModel = new BeanTableModel(CcrawlMatch.class, columnNameMapping, columnNamesSorted);        
        
        encoding = "8859_9"; // default
        linesAfter = 3;
        linesBefore = 3;
    }
    
    /**
     * @param currentMatches the currentMatches to set
     */
    public synchronized void setCurrentMatches(ArrayList<CcrawlMatch> currentMatches) {
        this.currentMatches = currentMatches;
    }
    
    public synchronized void cleanCurrentMatches(){
        this.currentMatches.clear();
        
        while(this.ccrawlMatchModel.getRowCount() > 0)
            this.ccrawlMatchModel.removeRowRange(0, 0);        
    }
    
    public synchronized void removeMatch(CcrawlMatch aMatch){
        this.currentMatches.remove(aMatch);

        for(int i=0; i<this.ccrawlMatchModel.getRowCount(); i++){
            if(this.ccrawlMatchModel.getRow(i).equals(aMatch)){
                this.ccrawlMatchModel.removeRowRange(i, i);    
                return;
            }
        }        
    }
    
    public synchronized void addMatch(CcrawlMatch aMatch){
        this.currentMatches.add(aMatch);        
        this.ccrawlMatchModel.addRow(aMatch);
    }
    
    public synchronized int filterMatches(Pattern filter){             
        
        while(this.ccrawlMatchModel.getRowCount() > 0)
            this.ccrawlMatchModel.removeRowRange(0, 0);        

        for (CcrawlMatch aMatch : this.getCurrentMatches()){           
            Matcher matcher = filter.matcher(aMatch.getMatchContext());        
            if(matcher.find())
                this.ccrawlMatchModel.addRow(aMatch);
        }          
        return this.ccrawlMatchModel.getRowCount();
    }    
    
    public void setMinMaxPrefferedWidths(JTable table, int columnIndex, int maxWidth, int minWidth, int preferredWidth){
        TableColumnModel columns = table.getColumnModel();
        if(columnIndex >= columns.getColumnCount())
            return;
        TableColumn column = columns.getColumn(columnIndex);
        if(maxWidth > 0)
            column.setMaxWidth(maxWidth);
        if(minWidth > 0)
            column.setMinWidth(minWidth);
        if(preferredWidth > 0)
            column.setPreferredWidth(preferredWidth);        
    }
    
   public BeanTableModel<CcrawlMatch> getCcrawlMatchModel() {
     return this.ccrawlMatchModel;
   }   

   public void setCcrawlMatchModel(BeanTableModel<CcrawlMatch> ccrawlMatchModel) {
     this.ccrawlMatchModel = ccrawlMatchModel;
   }


   
    /**
     * @return the currentDirectory
     */
    public File getCurrentDirectory() {
        return currentDirectory;
    }

    /**
     * @param currentDirectory the currentDirectory to set
     */
    public void setCurrentDirectory(File currentDirectory) {
        this.currentDirectory = currentDirectory;
    }

    /**
     * @return the currentFilePattern
     */
    public Pattern getCurrentFilePattern() {
        return currentFilePattern;
    }

    /**
     * @param currentFilePattern the currentFilePattern to set
     */
    public void setCurrentFilePattern(Pattern currentFilePattern) {
        this.currentFilePattern = currentFilePattern;
    }

    /**
     * @return the currentMatches
     */
    public ArrayList<CcrawlMatch> getCurrentMatches() {
        return currentMatches;
    }

    /**
     * @return the encoding
     */
    public String getEncoding() {
        return encoding;
    }

    /**
     * @param encoding the encoding to set
     */
    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    /**
     * @return the linesBefore
     */
    public int getLinesBefore() {
        return linesBefore;
    }

    /**
     * @param linesBefore the linesBefore to set
     */
    public void setLinesBefore(int linesBefore) {
        this.linesBefore = linesBefore;
    }

    /**
     * @return the linesAfter
     */
    public int getLinesAfter() {
        return linesAfter;
    }

    /**
     * @param linesAfter the linesAfter to set
     */
    public void setLinesAfter(int linesAfter) {
        this.linesAfter = linesAfter;
    }
        
}
