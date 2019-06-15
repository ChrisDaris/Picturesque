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

import Application.ModelExporter;
import Application.ModelImporter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.json.JSONException;

/**
 * Automatically generated GUI code + listeners and GUI methods
 *
 * @author Christos Darisaplis
 */
public class PicturesqueFrame extends javax.swing.JFrame {

    private JFileChooser chooser;

    /**
     * Creates the application frame
     */
    public PicturesqueFrame() {
        initComponents();//run automatically generated GUI code

        setBlockEditPanel(false);//no selected block at initialization

        jButton1.setMnemonic(KeyEvent.VK_INSERT);//add subprocess shortcut
        jButton2.setMnemonic(KeyEvent.VK_BACK_SPACE);//remove block shortcut
        jButton3.setMnemonic(KeyEvent.VK_DELETE);//remove process shortcut
        jButton4.setMnemonic(KeyEvent.VK_ENTER);//rename process shortcut

        chooser = new JFileChooser();//set up file chooser
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "JSON File", "json");
        chooser.setFileFilter(filter);

        this.pack();//pack and show
        this.setVisible(true);
    }

    /**
     * Sets all block edit components as enabled/disabled.
     *
     * @param state true to enable the panel, false to disable
     */
    protected static void setBlockEditPanel(boolean state) {
        jEditorPane1.setEnabled(state);
        jEditorPane2.setEnabled(state);
        jButton2.setEnabled(state);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSplitPane5 = new javax.swing.JSplitPane();
        jToolBar1 = new javax.swing.JToolBar();
        jLabel1 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox<>();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
        jButton1 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jSplitPane1 = new javax.swing.JSplitPane();
        jSplitPane6 = new javax.swing.JSplitPane();
        jScrollPane5 = new javax.swing.JScrollPane();
        proceduresList1 = new GUI.ProceduresList<>();
        jPanel1 = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        jSplitPane2 = new javax.swing.JSplitPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        jEditorPane1 = new javax.swing.JEditorPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        jEditorPane2 = new javax.swing.JEditorPane();
        jScrollPane4 = new javax.swing.JScrollPane();
        workspacePanel2 = new GUI.WorkspacePanel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Picturesque");
        setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new java.awt.Dimension(300, 300));

        jSplitPane5.setDividerSize(15);
        jSplitPane5.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPane5.setContinuousLayout(true);
        jSplitPane5.setDoubleBuffered(true);

        jToolBar1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));
        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);
        jToolBar1.setMinimumSize(new java.awt.Dimension(2, 50));

        jLabel1.setFont(jLabel1.getFont());
        jLabel1.setText(" Model Size: ");
        jToolBar1.add(jLabel1);

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "0.25", "0.50", "0.75", "1", "1.25", "1.5", "1.75", "2" }));
        jComboBox1.setSelectedIndex(3);
        jComboBox1.setToolTipText("");
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });
        jToolBar1.add(jComboBox1);

        jLabel2.setText(" Font Size");
        jToolBar1.add(jLabel2);

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "6", "8", "10", "12", "14", "16", "18", "20", "22", "28", "34", "40" }));
        jComboBox2.setSelectedIndex(5);
        jComboBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox2ActionPerformed(evt);
            }
        });
        jToolBar1.add(jComboBox2);
        jToolBar1.add(filler1);

        jButton1.setText("+new subprocess");
        jButton1.setFocusable(false);
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton1.setSelected(true);
        jButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton1);

        jLabel4.setText(" ");
        jToolBar1.add(jLabel4);

        jButton3.setText("-remove subprocess");
        jButton3.setFocusable(false);
        jButton3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton3.setSelected(true);
        jButton3.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton3);

        jLabel5.setText(" ");
        jToolBar1.add(jLabel5);

        jButton4.setText("rename process");
        jButton4.setFocusable(false);
        jButton4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton4.setSelected(true);
        jButton4.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton4);

        jLabel3.setText(" ");
        jToolBar1.add(jLabel3);

        jSplitPane5.setTopComponent(jToolBar1);

        jSplitPane1.setDividerSize(15);
        jSplitPane1.setResizeWeight(1.0);
        jSplitPane1.setContinuousLayout(true);
        jSplitPane1.setDoubleBuffered(true);

        jSplitPane6.setDividerSize(15);
        jSplitPane6.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPane6.setResizeWeight(0.5);
        jSplitPane6.setContinuousLayout(true);
        jSplitPane6.setDoubleBuffered(true);
        jSplitPane6.setMinimumSize(new java.awt.Dimension(250, 150));

        proceduresList1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Process Blocks", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 1, 14))); // NOI18N
        proceduresList1.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Sign Document", "Archive Document", "Send Document", "Enter Data to IT", "Get Data from IT", "Execute Transaction", "Formal Assessment", "Run Subprocess", "Receive Document", "Compose Document", "Parallel Execution" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        proceduresList1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        proceduresList1.setDoubleBuffered(true);
        proceduresList1.setMinimumSize(new java.awt.Dimension(0, 0));
        jScrollPane5.setViewportView(proceduresList1);

        jSplitPane6.setTopComponent(jScrollPane5);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Selected Block", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 1, 14), new java.awt.Color(3, 3, 3))); // NOI18N
        jPanel1.setMinimumSize(new java.awt.Dimension(0, 0));

        jButton2.setText("Remove Block");
        jButton2.setToolTipText("");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jSplitPane2.setDividerSize(15);
        jSplitPane2.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPane2.setResizeWeight(0.5);
        jSplitPane2.setContinuousLayout(true);

        jEditorPane1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Attributes", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 1, 12))); // NOI18N
        jEditorPane1.setAutoscrolls(false);
        jEditorPane1.setMinimumSize(new java.awt.Dimension(0, 0));
        jEditorPane1.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                jEditorPane1CaretUpdate(evt);
            }
        });
        jScrollPane2.setViewportView(jEditorPane1);

        jSplitPane2.setBottomComponent(jScrollPane2);

        jEditorPane2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Entity", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 1, 12))); // NOI18N
        jEditorPane2.setAutoscrolls(false);
        jEditorPane2.setMinimumSize(new java.awt.Dimension(0, 0));
        jEditorPane2.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                jEditorPane2CaretUpdate(evt);
            }
        });
        jScrollPane1.setViewportView(jEditorPane2);

        jSplitPane2.setLeftComponent(jScrollPane1);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jSplitPane2)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(jSplitPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 256, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2)
                .addGap(9, 9, 9))
        );

        jSplitPane6.setBottomComponent(jPanel1);

        jSplitPane1.setRightComponent(jSplitPane6);

        jScrollPane4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Model", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 1, 14))); // NOI18N
        jScrollPane4.setMinimumSize(new java.awt.Dimension(300, 300));

        workspacePanel2.setBackground(new java.awt.Color(238, 237, 230));
        workspacePanel2.setPreferredSize(new java.awt.Dimension(5000, 5000));

        javax.swing.GroupLayout workspacePanel2Layout = new javax.swing.GroupLayout(workspacePanel2);
        workspacePanel2.setLayout(workspacePanel2Layout);
        workspacePanel2Layout.setHorizontalGroup(
            workspacePanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 5000, Short.MAX_VALUE)
        );
        workspacePanel2Layout.setVerticalGroup(
            workspacePanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 5000, Short.MAX_VALUE)
        );

        jScrollPane4.setViewportView(workspacePanel2);

        jSplitPane1.setLeftComponent(jScrollPane4);

        jSplitPane5.setRightComponent(jSplitPane1);

        jMenuBar1.setDoubleBuffered(true);

        jMenu1.setText("File");

        jMenuItem1.setText("Open JSON file");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuItem2.setText("Export JSON file");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);
        jMenu1.add(jSeparator1);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Edit");

        jMenuItem3.setText("Clear Model");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem3);

        jMenuBar1.add(jMenu2);

        jMenu3.setText("Help");
        jMenuBar1.add(jMenu3);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jSplitPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 797, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jSplitPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 546, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Changes size of the model panel.
     *
     * @param evt model size combo box state changed
     */
    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        double percChange = Double.valueOf((String) jComboBox1.getSelectedItem());
        workspacePanel2.resizeContents(percChange);
    }//GEN-LAST:event_jComboBox1ActionPerformed

    /**
     * Changes font size on the model panel.
     *
     * @param evt font size combo box state changed
     */
    private void jComboBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox2ActionPerformed
        int newSize = Integer.valueOf((String) jComboBox2.getSelectedItem());
        workspacePanel2.setFont(newSize);
    }//GEN-LAST:event_jComboBox2ActionPerformed

    /**
     * Adds new subprocess.
     *
     * @param evt "+new subprocess" button pressed
     */
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        String name = (String) JOptionPane.showInputDialog(this,
                "Enter the name of the new subprocess:",
                "New Subprocess",
                JOptionPane.PLAIN_MESSAGE, null, null, "Subprocess "
                + workspacePanel2.getTotalSubs());
        if (name != null) {
            workspacePanel2.addFrame(name);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * Updates attributes string of selected block.
     *
     * @param evt attributes text box caret changed
     */
    private void jEditorPane1CaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_jEditorPane1CaretUpdate
        if (jEditorPane1.isEnabled() && WorkspacePanel.selBlock != null) {
            WorkspacePanel.selBlock.attrString = jEditorPane1.getText();
            workspacePanel2.repaint();
        }
    }//GEN-LAST:event_jEditorPane1CaretUpdate

    /**
     * Remove selected block.
     *
     * @param evt "Remove Block" button pressed
     */
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        if (WorkspacePanel.selBlock != null) {
            WorkspacePanel.selBlock.getFrame().removeBlock(
                    WorkspacePanel.selBlock.getIndex());
            
            workspacePanel2.setEditPane(false);
            WorkspacePanel.selBlock = null;
            workspacePanel2.repaint();
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    /**
     * Imports model from JSON file.
     *
     * @param evt "Open JSON file" menu option clicked
     */
    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        int returnVal = chooser.showOpenDialog(this);//open open dialog
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            try {
                ModelImporter importer = new ModelImporter(chooser.getSelectedFile());
                workspacePanel2.setFramesList(importer.importModel());
                workspacePanel2.setEditPane(false);
                WorkspacePanel.selBlock = null;
                WorkspacePanel.selFrame = null;
            } catch (FileNotFoundException | JSONException ex) {//show exceptions
                JOptionPane.showMessageDialog(this, "Import Error",
                    ex.getMessage(), JOptionPane.ERROR_MESSAGE);
                System.out.println(Arrays.toString(ex.getStackTrace()));
                System.out.println(ex.getMessage());
            }
        }
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    /**
     * Exports model to JSON file.
     *
     * @param evt "Export JSON file" menu option clicked
     */
    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        int returnVal = chooser.showSaveDialog(this);//open save dialog
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            try {
                File file = chooser.getSelectedFile();
                /*Check if extension has to be added*/
                if (!file.getPath().endsWith(".json")) {//add extension if needed
                    file = new File(file.toString() + ".json");
                }
                
                ModelExporter exporter = new ModelExporter(workspacePanel2.getFrames());
                exporter.exportModel(file);
            } catch (IOException | JSONException ex) {//show exceptions
                JOptionPane.showMessageDialog(this, "Export Error",
                    ex.getMessage(), JOptionPane.ERROR_MESSAGE);
                System.out.println(Arrays.toString(ex.getStackTrace()));
                System.out.println(ex.getMessage());
            }
        }

    }//GEN-LAST:event_jMenuItem2ActionPerformed

    /**
     * Clear the current model.
     *
     * @param evt "Clear model" menu option clicked
     */
    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        Object[] options = {"Yes", "No"};
        int n = JOptionPane.showOptionDialog(this, "The current model will be cleared,"
                + " proceed?", "Clear Model", JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE, null, options, options[1]);
        
        if (n == JOptionPane.YES_OPTION) {
            workspacePanel2.clearModel();
            workspacePanel2.addFrame("Main Process");
            workspacePanel2.getFrames().get(0).isSelected(false);
            workspacePanel2.setEditPane(false);
            WorkspacePanel.selBlock = null;
            WorkspacePanel.selFrame = null;
        }
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    /**
     * Removes selected subprocess if possible.
     *
     * @param evt "-remove subprocess" button pressed
     */
    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        ProcessFrame selFrame = WorkspacePanel.selFrame;
        
        /*Check if frame can be removed*/
        if (selFrame == null) {//no frame selected
            JOptionPane.showMessageDialog(this, "Select a subprocess to remove!",
                    "Select Subprocess", JOptionPane.WARNING_MESSAGE);
        } else if (selFrame.getIndex() == 0) {//first frame selected
            JOptionPane.showMessageDialog(this, "The main process can't be removed!",
                    "Main Process Selected", JOptionPane.WARNING_MESSAGE);
        } else if (selFrame.getBlocks().size() > 0) {//frame has blocks
            JOptionPane.showMessageDialog(this, "Subprocess contains process"
                    + " blocks!",
                    "Subprocess Not Empty", JOptionPane.WARNING_MESSAGE);
        } else {//remove frame
            workspacePanel2.getFrames().remove(selFrame.getIndex());
            workspacePanel2.SlideFrames(selFrame.getIndex());
            WorkspacePanel.selFrame = null;
            workspacePanel2.repaint();
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    /**
     * Renames the selected subprocess.
     *
     * @param evt "rename subprocess" button pressed
     */
    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        ProcessFrame selFrame = WorkspacePanel.selFrame;
        
        if (selFrame == null) {//no frame selected
            JOptionPane.showMessageDialog(this, "Select a subprocess to rename!",
                    "Select Subprocess", JOptionPane.WARNING_MESSAGE);
        } else {
            String name = (String) JOptionPane.showInputDialog(this,
                    "Enter the new name of this subprocess:",
                    "Rename Subprocess",
                    JOptionPane.PLAIN_MESSAGE, null, null, selFrame.name);
            if (name != null) {
                selFrame.name = name;
                workspacePanel2.repaint();
            }
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    /**
     * Updates entity string of selected block.
     * 
     * @param evt entity text box caret changed
     */
    private void jEditorPane2CaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_jEditorPane2CaretUpdate
        if (jEditorPane1.isEnabled() && WorkspacePanel.selBlock != null) {
            WorkspacePanel.selBlock.entName = jEditorPane2.getText();
            workspacePanel2.repaint();
        }
    }//GEN-LAST:event_jEditorPane2CaretUpdate


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.Box.Filler filler1;
    private javax.swing.JButton jButton1;
    private static javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    protected static javax.swing.JEditorPane jEditorPane1;
    protected static javax.swing.JEditorPane jEditorPane2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JSplitPane jSplitPane2;
    private javax.swing.JSplitPane jSplitPane5;
    private javax.swing.JSplitPane jSplitPane6;
    private javax.swing.JToolBar jToolBar1;
    private GUI.ProceduresList<String> proceduresList1;
    private GUI.WorkspacePanel workspacePanel2;
    // End of variables declaration//GEN-END:variables
}
