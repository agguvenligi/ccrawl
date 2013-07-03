/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ccrawl;

import java.awt.*;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.io.File;
import java.util.Scanner;
import javax.swing.JTextPane;
import javax.swing.text.*;
import javax.swing.text.Highlighter.HighlightPainter;

/**
 *
 * @author Bedirhan
 */
public class CcrawlMatchDetailsView extends javax.swing.JFrame {


    /**
     * Creates new form CcrawlMatchDetailsView
     */
    public CcrawlMatchDetailsView() {
        initComponents();   
        
        // Listen for value changes in the scroll pane's scrollbars
        AdjustmentListener listener = new MyAdjustmentListener();
        jScrollPane1.getVerticalScrollBar().addAdjustmentListener(listener);
        
    }
    
    public void setDetailsViewLocation(CcrawlMainView parent){
        this.setSize(this.getWidth(), parent.getHeight());
        Point parentPosition = parent.getLocation();
        Point detailsViewPosition = new Point();
        detailsViewPosition.x = parentPosition.x + parent.getWidth();
        detailsViewPosition.y = parentPosition.y;        
        this.setLocation(detailsViewPosition);   
    }
    
    public void showDetails(CcrawlMatch selectedCcrawlMatch){
        
        jTextPane1.setText("");
        jTextPane2.setText("");
        
        SimpleAttributeSet attribs = new SimpleAttributeSet();   
        StyleConstants.setAlignment(attribs , StyleConstants.ALIGN_RIGHT);   
        jTextPane2.setParagraphAttributes(attribs, true);  
        
        int lineNumber = 1;
        int lineNumberCaretIndex = 0;
        try {
            // jTextPage read, deletes carriage returns from the file
            //jTextPane1.read(new FileReader(filePath), null);
            File sourceFile = new File(selectedCcrawlMatch.getPath());
            Scanner fileScanner =  new Scanner(sourceFile);            
            
            StyledDocument doc = jTextPane1.getStyledDocument();
            StyledDocument doc2 = jTextPane2.getStyledDocument();

            
            SimpleAttributeSet lineHighlightStyle = new SimpleAttributeSet(); 
            StyleConstants.setBackground(lineHighlightStyle, Color.YELLOW); 
            StyleConstants.setFontFamily(lineHighlightStyle, "Tahoma");
            StyleConstants.setFontSize(lineHighlightStyle, 12);

            SimpleAttributeSet lineNumberStyle = new SimpleAttributeSet(); 
            StyleConstants.setFontFamily(lineNumberStyle, "Tahoma");
            StyleConstants.setFontSize(lineNumberStyle, 12);            
            
            SimpleAttributeSet normalLineStyle = new SimpleAttributeSet(); 
            StyleConstants.setFontFamily(normalLineStyle, "Tahoma");
            StyleConstants.setFontSize(normalLineStyle, 12);            
            

            boolean isMatchHit = false;
            while(fileScanner.hasNextLine()){         

                
                if(selectedCcrawlMatch.getFirstLineNumber() <= lineNumber && selectedCcrawlMatch.getLastLineNumber() >= lineNumber){
                    doc.insertString(doc.getLength(), fileScanner.nextLine() + selectedCcrawlMatch.getNewline(), lineHighlightStyle);
                    isMatchHit = true;
                }
                else
                    doc.insertString(doc.getLength(), fileScanner.nextLine() + selectedCcrawlMatch.getNewline(), normalLineStyle);
                
                doc2.insertString(doc2.getLength(), lineNumber + selectedCcrawlMatch.getNewline(), lineNumberStyle);
                
                if(!isMatchHit)
                    lineNumberCaretIndex += String.valueOf(lineNumber).length() + selectedCcrawlMatch.getNewline().length();
                
                lineNumber++;
            }
            
            fileScanner.close();
                        
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
            
        Highlighter hilite = jTextPane1.getHighlighter();
        HighlightPainter myHighlightPainter = new MyHighlightPainter(Color.red);

        try{
            // jTextPage read, deletes carriage returns from the file            
            hilite.addHighlight(selectedCcrawlMatch.getMatchStartIndex(), selectedCcrawlMatch.getMatchEndIndex(), myHighlightPainter);
        }
        catch(BadLocationException ble){
            System.out.println(ble.getMessage());
        }
        
        this.setTitle(selectedCcrawlMatch.getPath());
        
        String numberOfDigits = String.valueOf(lineNumber);
        jTextPane2.setPreferredSize(new Dimension( numberOfDigits.length() * 20 ,80));
        jScrollPane2.setPreferredSize(new Dimension( numberOfDigits.length() * 20 ,80));

        jTextPane1.setCaretPosition(selectedCcrawlMatch.getMatchEndIndex());
        jTextPane2.setCaretPosition(lineNumberCaretIndex);
        
        
    }

    class MyHighlightPainter extends DefaultHighlighter.DefaultHighlightPainter {
        public MyHighlightPainter(Color color) {
            super(color);
        }
    }
    
    class MyTextPane extends JTextPane {         
        public MyTextPane() {             
            super(); 
            setOpaque(false);              
            // this is needed if using Nimbus L&F - see http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6687960             
            setBackground(new Color(0,0,0,0));          
        }          
        protected void paintComponent(Graphics g) {             
            // set background green - but can draw image here too             
            g.setColor(Color.orange);             
            g.fillRect(0, 0, getWidth(), getHeight());              
            // uncomment the following to draw an image             
            // Image img = ...;            
            // g.drawImage(img, 0, 0, this);               
            super.paintComponent(g);         
        }     
    }     
    
    class MyAdjustmentListener implements AdjustmentListener {
        public void adjustmentValueChanged(AdjustmentEvent evt) {
            Adjustable source = evt.getAdjustable();
            if (evt.getValueIsAdjusting()) {
                return;
            }
            int orient = source.getOrientation();
            /*
            if (orient == Adjustable.HORIZONTAL) {
                System.out.println("from horizontal scrollbar"); 
            } else {
                System.out.println("from vertical scrollbar");
            }
            */
            int type = evt.getAdjustmentType();
            int value = evt.getValue();
            switch (type) {
            case AdjustmentEvent.UNIT_INCREMENT:
                //System.out.println("Scrollbar was increased by one unit");
                break;
            case AdjustmentEvent.UNIT_DECREMENT:
                //System.out.println("Scrollbar was decreased by one unit");
                break;
            case AdjustmentEvent.BLOCK_INCREMENT:
                //System.out.println("Scrollbar was increased by one block");
                break;
            case AdjustmentEvent.BLOCK_DECREMENT:
                //System.out.println("Scrollbar was decreased by one block");
                break;
            case AdjustmentEvent.TRACK:
                //System.out.println("The knob on the scrollbar was dragged");
                jScrollPane2.getVerticalScrollBar().setValue(value);

                break;
            }
            
        }
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTextPane1 = new JTextPane()
        {
            public boolean getScrollableTracksViewportWidth()
            {
                return getUI().getPreferredSize(this).width
                <= getParent().getSize().width;
            }
        };
        jScrollPane2 = new javax.swing.JScrollPane();

        setTitle("Match Details View");

        jScrollPane1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jScrollPane1.setPreferredSize(new java.awt.Dimension(771, 541));

        jTextPane1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 6, 1, 1));
        jTextPane1.setPreferredSize(new java.awt.Dimension(771, 541));
        jScrollPane1.setViewportView(jTextPane1);

        getContentPane().add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jTextPane2 = new MyTextPane();
        jTextPane2.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jTextPane2.setPreferredSize(new java.awt.Dimension(25, 2));
        jScrollPane2.setViewportView(jTextPane2);
        jScrollPane2.setBackground(new java.awt.Color(0, 153, 153));
        jScrollPane2.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jScrollPane2.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane2.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        jScrollPane2.setPreferredSize(new java.awt.Dimension(25, 541));
        getContentPane().add(jScrollPane2, java.awt.BorderLayout.WEST);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextPane jTextPane1;
    // End of variables declaration//GEN-END:variables
    private MyTextPane jTextPane2;
}


