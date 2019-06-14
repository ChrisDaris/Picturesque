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

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import static java.awt.MouseInfo.getPointerInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.TransferHandler;
import javax.swing.event.MouseInputAdapter;

/**
 * Draws the main panel, communicates with the main frame
 *
 * @author Christos Darisaplis
 */
public class WorkspacePanel extends JPanel {

    private Dimension size;//convenience variable, size of the panel
    private boolean dragOperation;//drag operation happening
    private ArrayList<ProcessFrame> frames;//list of all frames

    protected static ProcessBlock selBlock;//selected block
    protected static ProcessFrame selFrame;//selected frame

    protected static final double DEFRATIO = 0.25;//default model ratio

    private static int fontSize = 16;//starting font size
    private static int totalSubs = 1;//main process

    public WorkspacePanel() {
        super();

        size = new Dimension(2000, 2000);
        dragOperation = false;
        frames = new ArrayList<>();
        selBlock = null;
        selFrame = null;

        setTransferHandler(new TransferHandler("text"));//dnd support
        frames.add(new ProcessFrame("Main Process", 0));
        setPreferredSize(size);
        setMouseListener();
    }

    /**
     * Adds new subprocess
     *
     * @param name name of the new subprocess
     * 
     * @return result of add operation to the list
     */
    protected boolean addFrame(String name) {
        ProcessFrame toAdd = new ProcessFrame(name, frames.size());
        boolean res = frames.add(toAdd);

        totalSubs++;
        if (selFrame != null) {//deselect prev frame 
            selFrame.isSelected(false);
        }
        
        selFrame = toAdd;
        selFrame.isSelected(true);
        repaint();

        return res;
    }

    /**
     * Paints itself on a JComponent
     * 
     * @param g graphics tool
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);//initialize
        Graphics2D g2D;
        g2D = (Graphics2D) g.create();

        /*set font parameters*/
        g2D.setRenderingHint(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        Font font = new Font("TimesRoman", Font.BOLD, fontSize);
        g2D.setFont(font);

        // get metrics from the graphics
        FontMetrics textMetrics = g2D.getFontMetrics(font);


        /*draw all process frames*/
        for (ProcessFrame aFrame : frames) {
            aFrame.paintSelf(g2D, textMetrics);
        }

        g2D.dispose();//finalize
    }

    /**
     * Required for D&D support, accepts data in String form
     *
     * @param dropData incoming data in String form
     */
    public void setText(String dropData) {
        Point pointerDropLocation = calculateRelPointerPos();

        for (ProcessFrame aFrame : frames) {
            if (aFrame.getFrameRectangle().contains(pointerDropLocation)) {
                if (selBlock != null) {
                    selBlock.isSelected = false;
                }
                selBlock = aFrame.addBlock(dropData, "Sample Entity",
                        "The quick brown fox jumped over the lazy doggo.");
                selBlock.isSelected = true;
                setEditPane(true);
            }
        }

        repaint();
    }

    /**
     * Dummy, required for D&D support
     *
     * @return nothing
     */
    public String getText() {
        return null;
    }

    /**
     * Returns relative pointer position to top left corner of the panel
     *
     * @return
     */
    private Point calculateRelPointerPos() {
        return new Point(getPointerInfo().getLocation().x - this.getLocationOnScreen().x,
                getPointerInfo().getLocation().y - this.getLocationOnScreen().y);
    }

    /**
     * Set mouse handlers for mouse operations
     */
    private void setMouseListener() {
        MouseInputAdapter mouseAdapter;
        mouseAdapter = new MouseInputAdapter() {

            private Dimension offset;//offset between the block header/pointer pos
            private ProcessBlock dragBlock;//block that is being dragged
            private ProcessFrame dragFrame;//frame that the dragBlock belongs to

            /**
             * Changes cursor when it enters block or frame header
             *
             * @param e mouse movement in the panel
             */
            @Override
            public void mouseMoved(MouseEvent e) {
                Point pointer = e.getPoint();
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                
                for (ProcessFrame aFrame : frames) {
                    /*Check frame headers*/
                    if (aFrame.getFrameHeader() != null
                            && aFrame.getFrameHeader().contains(pointer)) {
                        aFrame.setOnHead(true);
                    } else {
                        aFrame.setOnHead(false);
                    }
                    
                    for (ProcessBlock aBlock : aFrame.getBlocks()) {
                        /*Check block headers*/
                        if (aBlock.getHeaderRectangle().contains(pointer)) {
                            setCursor(new Cursor(Cursor.MOVE_CURSOR));
                            aBlock.setOnHead(true);
                        } else {
                            aBlock.setOnHead(false);
                        }
                    }
                }
                repaint();
            }

            /**
             * Initiates drag operation
             *
             * @param e mouse pressed in the panel
             */
            @Override
            public void mousePressed(MouseEvent e) {
                Point pointer = e.getPoint();
                if (!dragOperation) {
                    /*Scan if a drag operation can be initiated,
                     store the block being dragged if it can*/
                    for (ProcessFrame aFrame : frames) {
                        for (ProcessBlock aBlock : aFrame.getBlocks()) {
                            if (aBlock.getHeaderRectangle().contains(pointer)) {
                                dragOperation = true;
                                dragBlock = aBlock;
                                dragFrame = aFrame;
                                dragFrame.dragOp = true;
                                dragFrame.validSwitch = true;
                                dragFrame = aFrame;
                                offset = new Dimension(pointer.x - dragBlock.getHeaderRectangle().x,
                                        pointer.y - dragBlock.getHeaderRectangle().y);
                            }
                        }
                    }
                }
            }

            /**
             * Handles drag operation
             *
             * @param e mouse dragged across the panel
             */
            @Override
            public void mouseDragged(MouseEvent e) {
                /*Drag the block on the panel*/
                if (dragOperation) {
                    Rectangle dragRect = dragBlock.getMainRectangle();
                    Point pointerX = new Point(e.getX(), dragRect.y);//only drag at x axis
                    dragFrame.setDragPrev(new Rectangle(e.getX() - offset.width,
                            dragRect.y, dragRect.width, dragRect.height));
                    dragFrame.validSwitch = false;
                    
                    /*Check all blocks to determine if the current drag position
                    is swap or insert*/
                    for (ProcessBlock aBlock : dragFrame.getBlocks()) {
                        if (aBlock.getMainRectangle().contains(pointerX)) {
                            aBlock.isDropTarg(true);//switch
                            dragFrame.validSwitch = true;
                        } else {//insert
                            aBlock.isDropTarg(false);
                        }
                    }
                    repaint();

                }
            }

            /**
             * Terminates drag operation
             *
             * @param e mouse released in the panel
             */
            @Override
            public void mouseReleased(MouseEvent e) {
                if (dragOperation) {
                    Point pointer = new Point(e.getX(), dragBlock.getMainRectangle().y);
                    boolean dropped = false;
                    
                    /*Check for swap or insert operation between blocks*/
                    for (ProcessBlock aBlock : dragFrame.getBlocks()) {
                        if (!dropped && aBlock.getMainRectangle().contains(pointer)) {
                            aBlock.isDropTarg(false);
                            dragFrame.swapBlocks(aBlock, dragBlock);//swap
                            dropped = true;
                            break;
                        } else if (!dropped && pointer.x < aBlock.getMainRectangle().x) {
                            /*To insert, remove first and then insert block*/
                            dragFrame.removeBlock(dragBlock.getIndex());
                            dragFrame.insertBlock(aBlock.getIndex(), dragBlock.blockName,
                                    dragBlock.entName, dragBlock.attrString);
                            dropped = true;
                            break;
                        } 
                    }
                    /*Set all drag variables to false.*/
                    dragOperation = false;
                    dragFrame.dragOp = false;
                    dragFrame.validSwitch = false;
                    dragBlock.setOnHead(false);
                    dragBlock = null;
                    dragFrame = null;
                    repaint();
                    setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }

            }

            /**
             * Selects a block or frame when a mouse click is detected anywhere 
             * inside it
             *
             * @param e mouse clicked in the panel
             */
            @Override
            public void mouseClicked(MouseEvent e) {
                Point pointer = e.getPoint();
                
                /*Check if the click happens in frame header or anywhere inside
                a block*/
                for (ProcessFrame aFrame : frames) {
                    for (ProcessBlock aBlock : aFrame.getBlocks()) {
                        /*Select block*/
                        if (aBlock.getMainRectangle().contains(pointer)) {
                            if (selBlock != null) {//prev selection removed
                                selBlock.isSelected = false;
                            }
                            selBlock = aBlock;
                            selBlock.isSelected = true;
                            setEditPane(true);
                        }
                    }
                    /*Select frame*/
                    if (aFrame.getFrameHeader().contains(pointer)) {
                        if (selFrame != null) {//previous selection removed
                            selFrame.isSelected(false);
                        }
                        selFrame = aFrame;
                        selFrame.isSelected(true);
                    }
                }

                repaint();
            }

        };

        addMouseMotionListener(mouseAdapter);
        addMouseListener(mouseAdapter);
    }

    /**
     * Enables or disables the edit pane, adds block info when enabled, clears
     * them if disabled
     *
     * @param state true to enable, false to disable
     */
    protected void setEditPane(boolean state) {
        JEditorPane entPane = PicturesqueFrame.jEditorPane2;
        JEditorPane attrPane = PicturesqueFrame.jEditorPane1;
        
        if (state) {
            PicturesqueFrame.setBlockEditPanel(true);
            entPane.setText(selBlock.entName);
            attrPane.setText(selBlock.attrString);
        } else {
            PicturesqueFrame.setBlockEditPanel(false);
            entPane.setText("");
            attrPane.setText("");
        }
    }

    /**
     * Slides all frames to correct position
     * 
     * @param start index of the frames list to start sliding from
     */
    protected void SlideFrames(int start) {
        for (int i = start; i < frames.size(); i++) {
            frames.get(i).setIndex(i);//update index
            frames.get(i).setFrameRectangles();//update rectangles
            /*Slide all blocks as well*/
            for (ProcessBlock aBlock : frames.get(i).getBlocks()) {
                aBlock.setRectangles(frames.get(i).getFrameRectangle().y);
            }
        }
    }

    /**
     * Change zoom level of the panel and all frames inside of it
     *
     * @param percResize new model zoom ratio
     */
    protected void resizeContents(double percResize) {
        double newRatio = percResize * DEFRATIO;
        for (ProcessFrame aFrame : frames) {
            aFrame.resize(newRatio);
        }
        repaint();
    }

    /**
     * Changes font size
     *
     * @param newSize
     */
    protected void setFont(int newSize) {
        fontSize = newSize;
        repaint();
    }

    protected void setFramesList(ArrayList<ProcessFrame> newList) {
        this.frames = newList;
        repaint();
    }

    protected void clearModel() {
        this.frames = new ArrayList<>();
        totalSubs = 0;
        repaint();
    }

    protected ArrayList<ProcessFrame> getFrames() {
        return this.frames;
    }

    protected int getTotalSubs() {
        return WorkspacePanel.totalSubs;
    }

    public static void increaseSubCount() {
        WorkspacePanel.totalSubs++;
    }
}
