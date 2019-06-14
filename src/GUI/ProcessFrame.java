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
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Represents the frame of an entire process/subprocess
 *
 * @author Christos Darisaplis
 */
public class ProcessFrame {

    /*Rendering properties, expressed as ratios and percentages*/
    private static final int XOFFSET = 15;
    private static final double BLOCKGAP = 0.33;
    
    protected static double screenRatio = 0.25;//GETS MODIFIED TO SET ZOOM LEVEL

    private Rectangle frameRectangle, headerRectangle;//rendering rectangles
    private Point position;//top left corner of the main rectangle
    private Dimension size;//size of the main rectangle

    private final ArrayList<ProcessBlock> blocks;//blocks in the frame
    protected String name;//title of the frame
    private int index;//index of the frame in the main panel's list

    private Rectangle dragPrev;//displays drag operation
    
    protected boolean dragOp;//drag operation happening
    protected boolean validSwitch;//blocks can switch positions during drag
    protected boolean onHead;//pointer on header of the frame
    protected boolean selected;//frame is selected
            

    public ProcessFrame(String name, int index) {
        blocks = new ArrayList<>();
        this.index = index;
        this.name = name;
        dragOp = false;
        validSwitch = false;
        onHead= false;
        selected = false;

        setFrameRectangles();
    }

    /**
     * Calculates the frame's rendering details
     */
    protected final void setFrameRectangles() {

        /*size depends on screen size, modified by rendering ratios*/
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().
                getDefaultScreenDevice();
        int width = gd.getDisplayMode().getWidth();
        int height = (int) (gd.getDisplayMode().getHeight() * screenRatio);

        position = new Point(XOFFSET, index * (height + height / 8) + height / 8);
        size = new Dimension(width, height);
        frameRectangle = new Rectangle(position, size);
    }

    /**
     * Sets new zoom level for the frame AND all of its contents
     *
     * @param newPerc new zoom percentage
     */
    protected void resize(double newPerc) {
        ProcessFrame.screenRatio = newPerc;
        setFrameRectangles();//recalculate all rendering details
        for (ProcessBlock aBlock : blocks) {
            aBlock.setRectangles(frameRectangle.y);//recalculate all contents
            checkWidth();//resize the frame if needed
        }
    }

    /**
     * Paints itself on a JComponent
     *
     * @param g2D painter of the component
     * @param textMetrics text font rendering details
     */
    public void paintSelf(Graphics2D g2D, FontMetrics textMetrics) {
        int textHeight = textMetrics.getHeight();
        int textWidth = textMetrics.stringWidth(name);

        headerRectangle = new Rectangle(position.x, position.y - textHeight - 4,
                textWidth + 10, textHeight + 4);

        g2D.setColor(Color.WHITE);
        g2D.fill(frameRectangle);
        g2D.setColor(Color.BLACK);

        if (index == 0) {//main process
            g2D.draw(frameRectangle);
            g2D.setColor(Color.WHITE);
            g2D.fill(headerRectangle);
            if (onHead || selected){
                g2D.setColor(Color.GRAY);
                g2D.fill(headerRectangle);
            }
            g2D.setColor(Color.BLACK);
            g2D.draw(headerRectangle);
        } else {//subprocess
            Stroke prev = g2D.getStroke();
            Stroke dashed = new BasicStroke(2, BasicStroke.CAP_BUTT,
                    BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0);
            g2D.setStroke(dashed);

            g2D.draw(frameRectangle);

            g2D.setColor(Color.WHITE);
            g2D.fill(headerRectangle);
            if (onHead || selected){
                g2D.setColor(Color.GRAY);
                g2D.fill(headerRectangle);
            }
            g2D.setColor(Color.BLACK);
            g2D.draw(headerRectangle);

            g2D.setStroke(prev);
        }

        /*Draw name*/
        g2D.drawString(name, headerRectangle.x + 5, headerRectangle.y
                + textHeight);

        /*Draw blocks and arrows between blocks*/
        for (int i = 0; i < blocks.size(); i++) {
            blocks.get(i).paintSelf(g2D, textMetrics);//draw block
            if (i > 0) {//first block doesn't need arrows
                ProcessBlock previous = blocks.get(i - 1);
                Rectangle prevRec = previous.getMainRectangle();
                int lineWidth = (int) (ProcessFrame.BLOCKGAP * prevRec.width);

                int startX = prevRec.x + prevRec.width;
                int endX = prevRec.x + prevRec.width + lineWidth;

                g2D.drawLine(startX, prevRec.y + prevRec.height / 2, endX,
                        prevRec.y + prevRec.height / 2);

                int touchX = endX;
                int touchY = prevRec.height / 2 + prevRec.y;
                int arrOffset = prevRec.height / 10;
                int xpoints[] = {touchX - arrOffset, touchX, touchX - arrOffset};
                int ypoints[] = {touchY - arrOffset, touchY, touchY + arrOffset};
                int npoints = 3;

                g2D.fillPolygon(xpoints, ypoints, npoints);
            }
        }
        if (dragOp) {//render drag indication
            Stroke prev = g2D.getStroke();
            Stroke dashed = new BasicStroke(4, BasicStroke.CAP_BUTT,
                    BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0);
            g2D.setStroke(dashed);
            
            /*Check if blocks can switch or it's just an insert operation*/
            if (validSwitch) {
                g2D.setColor(Color.BLUE);
            } else {
                g2D.setColor(Color.ORANGE);
            }
            g2D.draw(dragPrev);
            g2D.setStroke(prev);
        }
    }

    /**
     * Swaps two blocks if they exist in the frame
     *
     * @param a block to swap
     * @param b block to swap
     * 
     * @return true if both exist in the frame, false otherwise
     */
    protected boolean swapBlocks(ProcessBlock a, ProcessBlock b) {
        if (blocks.contains(a) && blocks.contains(b)) {
            int aIndex = blocks.indexOf(a);
            int bIndex = blocks.indexOf(b);
            Collections.swap(blocks, aIndex, bIndex);

            a.setIndex(bIndex);//change a parameters
            a.setRectangles(position.y);

            b.setIndex(aIndex);//change b parameters
            b.setRectangles(position.y);
            return true;
        } else {
            return false;
        }

    }

    /**
     * Removes a block from the frame and updates all variables
     *
     * @param index position of the block in the list of blocks
     */
    protected void removeBlock(int index) {
        int temp;
        blocks.remove(index);
        for (int i = index; i < blocks.size(); i++) {
            temp = blocks.get(i).getIndex();
            blocks.get(i).setIndex(temp - 1);//update indexes
            blocks.get(i).setRectangles(frameRectangle.y);//update all blocks
        }
    }
    
    /**
     * Inserts a new block between two other ones
     *
     * @param pos position of the new block
     * @param blockName name of the new block
     * @param blockAttr attributes of the new block
     * @param entName entity of the new block
     */
    protected void insertBlock(int pos, String blockName, String entName, 
            String blockAttr){
        ProcessBlock blockToInsert = new ProcessBlock(pos, frameRectangle.y, 
                blockName, entName, blockAttr, this);
        blocks.add(pos, blockToInsert);
        
        for (int i = pos + 1; i < blocks.size(); i++) {
            blocks.get(i).setIndex(i);//update indexes
            blocks.get(i).setRectangles(frameRectangle.y);//update all blocks
        }
        checkWidth();
    }

    protected Rectangle getFrameHeader(){
        return this.headerRectangle;
    }
    
    public Rectangle getFrameRectangle() {
        return frameRectangle;
    }

    /**
     * Adds a new block
     *
     * @param blockName name of the block
     * @param blockAttr attributes of the block
     * @param entName entity of the block
     * 
     * @return result of the add operation
     */
    public ProcessBlock addBlock(String blockName, String entName, String blockAttr) {
        ProcessBlock blockToAdd = new ProcessBlock(blocks.size(),
                frameRectangle.y, blockName, entName, blockAttr, this);
        blocks.add(blockToAdd);
        checkWidth();

        return blockToAdd;
    }
    
    /**
     * Checks if the width of the frame needs to be increased
     */
    private void checkWidth(){
        ProcessBlock lastBlock = blocks.get(blocks.size() - 1);
        if (!frameRectangle.contains(lastBlock.getMainRectangle())){
            frameRectangle.width += 2*lastBlock.getMainRectangle().width;
        }
        
    }

    public ArrayList<ProcessBlock> getBlocks() {
        return this.blocks;
    }

    protected void setDragPrev(Rectangle dragPrev) {
        this.dragPrev = dragPrev;
    }
    
    protected void setOnHead(boolean onHead){
        this.onHead = onHead;
    }

    protected void isSelected(boolean selected){
        this.selected = selected;
    }
    
    protected int getIndex(){
        return this.index;
    }
    
    protected void setIndex(int index){
        this.index = index;
    }
    
    public String getName(){
        return this.name;
    }

}
