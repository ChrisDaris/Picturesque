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

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.TransferHandler;

/**
 * Stores the available process block types in a JList, provides Drag&Drop support
 *
 * @author Christos Darisaplis
 * @param <Str> required JList template
 */
public class ProceduresList<Str> extends JList{
    
    private String dragData;

    /**
     * Constructor, calls default JList constructor and adds drag support
     */
    public ProceduresList(){
        super();
        addDragSupport();
    }
    
    private void addDragSupport(){
        dragData = null;
        ProceduresList<Str> ref =  this;//convenience
        setTransferHandler(new TransferHandler("dragData"));
        addMouseListener(new MouseAdapter() {
            
            @Override
            public void mousePressed(MouseEvent e){//export as drag
                getTransferHandler().exportAsDrag((JComponent)e.getSource(), e,
                        TransferHandler.COPY);
                dragData = (String) ref.getSelectedValue();
            }
        });   
    }

    public String getDragData() {
        return dragData;
    }

    public void setDragData(String dragData) {
        this.dragData = dragData;
    }
    
    
}
