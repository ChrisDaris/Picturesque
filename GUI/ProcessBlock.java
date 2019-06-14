/* 
 * Copyright (c) 2002 JSON.org
 * Copyright (c) 2019 Christos Darisaplis
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * The Software shall be used for Good, not Evil.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package GUI;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Stroke;

/**
 * Stores a single process block's display data and draws it
 *
 * @author Christos Darisaplis
 */
public class ProcessBlock {


    /*Rendering properties, expressed as ratios and percentages*/
    private static final int XOFFSET = 25;
    private static final double BLOCKGAP = 0.33;//horizontal gap between blocks
    private static final double BLOCKRATIO = 0.75;//width/height ratio
    private static final double YOFFSET = 0.02;
    
    /*Height percentage of each rectangle, adding up to 1*/
    private static final double HEADRATIO = 0.1;
    private static final double NAMERATIO = 0.2;
    private static final double ENTRATIO = 0.2;
    private static final double ATTRRATIO = 0.5;

    protected String blockName;
    protected String attrString;
    protected String entName;
    private final ProcessFrame parentFrame;//frame the block belongs to
    private int index;//index of the block in its frame's list


    /*Rendering rectangles*/
    private Rectangle mainRect, headRect, nameRect, entRect, attrRect;

    protected boolean isSelected;//block is selected
    private boolean dropTarg;//block is drop target
    private boolean onHead;//mouse on Header

    public ProcessBlock(int index, int yPos, String name, String entName,
            String attrString, ProcessFrame parent) {
        this.blockName = name;
        this.attrString = attrString;
        this.entName = entName;
        this.index = index;
        this.parentFrame = parent;
        
        isSelected = false;
        dropTarg = false;
        onHead = false;

        setRectangles(yPos);
    }

    /**
     * Calculates the block's rendering rectangles
     *
     * @param yPos
     */
    protected final void setRectangles(int yPos) {
        int width, height;

        /*size depends on screen size, modified by rendering ratios*/
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().
                getDefaultScreenDevice();

        double screenPercentage = ProcessFrame.screenRatio - YOFFSET;
        height = (int) (gd.getDisplayMode().getHeight() * screenPercentage);
        width = (int) (height * BLOCKRATIO);
        Point position = new Point(XOFFSET + (int) (index * (width + width * BLOCKGAP)),
                yPos + (int) (gd.getDisplayMode().getHeight() * (YOFFSET / 2)));

        /*Calculate rectangles*/
        mainRect = new Rectangle(position.x, position.y, width, height);
        headRect = new Rectangle(position.x, position.y, width, (int) (height * HEADRATIO));
        nameRect = new Rectangle(position.x, position.y + (int) (height * HEADRATIO),
                width, (int) (height * NAMERATIO));
        entRect = new Rectangle(position.x, position.y + (int) (height * (NAMERATIO
                + HEADRATIO)), width, (int) (height * ENTRATIO));
        attrRect = new Rectangle(position.x, position.y + (int) (height * (ENTRATIO
                + NAMERATIO + HEADRATIO)), width, (int) (height * ATTRRATIO));
    }

    /**
     * Paints the block on a JComponent (ProcessFrame in this case)
     *
     * @param g2D painter of the component
     * @param textMetr font rendering details
     */
    public void paintSelf(Graphics2D g2D, FontMetrics textMetr) {
        g2D.setColor(Color.WHITE);
        g2D.fill(mainRect);
        
        /*Change header color if mouse hovers on it or the block is a drop target*/
        if (onHead || dropTarg) {
            g2D.setColor(Color.GRAY);
        } else {
            g2D.setColor(Color.BLACK);
        }
        g2D.fill(headRect);
        
        /*Draw all the rectangles*/
        g2D.setColor(Color.BLACK);
        g2D.draw(mainRect);
        g2D.draw(nameRect);
        g2D.draw(entRect);
        g2D.draw(attrRect);

        /*Draw a colorful border if the block is selected*/
        if (isSelected) {
            Stroke prev = g2D.getStroke();
            Stroke dashed = new BasicStroke(4);//thicker line
            
            Rectangle selRec = new Rectangle(mainRect.x - 2, mainRect.y - 2,
                    mainRect.width + 5, mainRect.height + 5);
            g2D.setStroke(dashed);
            g2D.setColor(Color.ORANGE);
            g2D.draw(selRec);
            g2D.setStroke(prev);
            g2D.setColor(Color.BLACK);
        }
        

        /*Draw all strings*/
        splitDrawString(g2D, textMetr, blockName, (int) (mainRect.height * HEADRATIO));
        splitDrawString(g2D, textMetr, entName, (int) (mainRect.height * (HEADRATIO + NAMERATIO)));
        splitDrawString(g2D, textMetr, attrString, (int) (mainRect.height * (HEADRATIO + NAMERATIO + ENTRATIO)));
    }

    /**
     * Splits strings based on block width and draws them
     *
     * @param g2D painter of the component
     * @param textMetr font rendering details
     * @param toWrite string to process
     * @param startY y position of the block's top left corner
     */
    private void splitDrawString(Graphics2D g2D, FontMetrics textMetr, String toWrite,
            int startY) {
        int textHeight = textMetr.getHeight();

        int charSum = 0, lines = 1, j = 0, i;
        for (i = 0; i < toWrite.length(); i++) {//iterate string
            char curChar = toWrite.charAt(i);
            charSum += textMetr.charWidth(curChar);
            /*Split here*/
            if (charSum >= mainRect.width - 5 - textMetr.charWidth(curChar) ||
                    curChar == '\n') {
                g2D.drawString(toWrite.substring(j, i), mainRect.x + 5,
                        mainRect.y + startY + lines * textHeight);
                charSum = 0;
                j = i;
                lines++;
            }
        }
        /*Draw last segment (or the whole string if it fits in one line)*/
        g2D.drawString(toWrite.substring(j, i), mainRect.x + 5,
                mainRect.y + startY + lines * textHeight);
    }

    /*Getters and setters below*/
    
    public Rectangle getMainRectangle() {
        return mainRect;
    }

    public Rectangle getHeaderRectangle() {
        return headRect;
    }
    
    public String getName(){
        return this.blockName;
    }
    
    public String getEntName(){
        return this.entName;
    }
    
    public String getAttrString(){
        return this.attrString;
    }



    protected int getIndex() {
        return this.index;
    }

    protected void setIndex(int newIndex) {
        this.index = newIndex;
    }


    protected void isDropTarg(boolean dropTarg) {
        this.dropTarg = dropTarg;
    }
    
    protected void setOnHead(boolean onHead){
        this.onHead = onHead;
    }
    
    protected ProcessFrame getFrame(){
        return this.parentFrame;
    }
}
