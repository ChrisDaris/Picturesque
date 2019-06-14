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
package Application;

import GUI.ProcessFrame;
import GUI.WorkspacePanel;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Imports a model from a JSON file. Uses "stleary/JSON-java" library to handle
 * JSON objects (https://github.com/stleary/JSON-java)
 * 
 *
 * @author Christos Darisaplis
 */
public class ModelImporter {

    /*JSON compiler export keys*/
    private final static String MAIN = "execute";
    private final static String COMMS = "commands";
    private final static String NAME = "name";
    private final static String TYPE = "type";
    private final static String COMMAND = "singleCommand";
    private final static String SUBPROCESS = "process";
    private final static String PARALLEL = "parallelSteps";
    private final static String CHANGE_ENT = "changeUsers";
    private final static String CONDITION_STEPS = "conditionSteps";
    private final static String DIVISION = "div";
    private final static String DEPARTMENT = "dep";
    private final static String POSITION = "pos";
    private final static String USERS = "users";
    private final static String CUR_USERS = "currentUsers";
    private final static String TRY = "try";
    private final static String YES = "yes";
    private final static String NO = "no";
    private final static String RETRY = "retry";
    private final static String CONDITION = "condition";

    /*JSON app export keys*/
    private static final String APP_EXP = "appExport";
    private static final String FRAME_TITLE = "title";
    private static final String FRAMES_KEY = "frames";
    private static final String BLOCKS_KEY = "blocks";
    private static final String BLOCK_NAME = "name";
    private static final String BLOCK_ENT = "entity";
    private static final String BLOCK_ATTR = "attributes";

    private JSONObject jo;//the entire JSONObject as read from the file
    private ArrayList<ProcessFrame> framesList;//imported data

    private int totalSubs;//total subprocess count

    private int subCount;//regular subprocess count
    private int parlCount;//parallel subprocess count
    private int failCount;//assestment failure subprocess count

    /**
     * Initializes importer and read JSON string from file
     *
     * @param JSONFile input file chosen by JChooser
     * 
     * @throws FileNotFoundException file read error
     * @throws JSONException        JSON read error
     */
    public ModelImporter(File JSONFile) throws
            FileNotFoundException, JSONException {
        String JSONString;
        /*Try with resources, build JSON string from file*/
        try (Scanner scan = new Scanner(JSONFile)) {
            JSONString = new String();
            while (scan.hasNext()) {
                JSONString += scan.nextLine();
            }
        }

        jo = new JSONObject(JSONString);
        framesList = new ArrayList<>();
        
        /*All counters to zero*/
        totalSubs = 0;
        
        subCount = 0;
        parlCount = 0;
        failCount = 0;
    }

    /**
     * Import model and return an ordered list of all of its frames
     *
     * @return list containing all of the model's frames
     */
    public ArrayList<ProcessFrame> importModel(){
        int curFrame = 0;
        /*Check if file comes from this app or compiler*/
        if (jo.has(APP_EXP)) {//import file has been generated from app
            processAppImport(jo.getJSONArray(FRAMES_KEY));
        } else {//import file has been generated from compiler
            JSONObject execObj = jo.getJSONObject(MAIN);
            /*Get list of commands to execute*/
            JSONArray comArr = execObj.getJSONArray(COMMS);
            
            ProcessFrame mainFrame = new ProcessFrame(execObj.getString(NAME),
                    curFrame);
            framesList.add(mainFrame);//initialize model with main frame
            WorkspacePanel.increaseSubCount();

            String entName = buildEntString(execObj);//get ent names
            processArray(curFrame, comArr, entName, false);
        }

        return framesList;
    }

    /**
     * Builds the list of frames from an application JSON file
     *
     * @param framesArr JSON representation of the frames
     */
    private void processAppImport(JSONArray framesArr) {
        for (int i = 0; i < framesArr.length(); i++) {//iterate all frames
            JSONObject frameObj = framesArr.getJSONObject(i);
            framesList.add(buildFrame(frameObj, i));
        }
    }

    /**
     * Builds a single frame from an application JSON file
     *
     * @param frameObj JSON representation of the frame
     * @param index index of the frame in the list of frames
     *
     * @return the new frame that has been constructed
     */
    private ProcessFrame buildFrame(JSONObject frameObj, int index) {
        String title = frameObj.getString(FRAME_TITLE);
        ProcessFrame newFrame = new ProcessFrame(title, index);
        JSONArray blocksArr = frameObj.getJSONArray(BLOCKS_KEY);

        for (int i = 0; i < blocksArr.length(); i++) {//iterate blocks
            JSONObject blockObj = blocksArr.getJSONObject(i);
            
            /*Add block to frame*/
            String blockName = blockObj.getString(BLOCK_NAME);
            String blockEnt = blockObj.getString(BLOCK_ENT);
            String blockAttr = blockObj.getString(BLOCK_ATTR);
            newFrame.addBlock(blockName, blockEnt, blockAttr);
        }

        return newFrame;
    }

    /**
     * Processes an array of commands in JSON form, recursively processes the entire
     * compile-generated JSON file.
     *
     * @param curFrame index of current process frame
     * @param comArr current commands array
     * @param entName current entity name
     * @param inPar true if inside a parallel branch
     */
    private void processArray(int curFrame, JSONArray comArr, String entName,
            boolean inPar){
        for (int i = 0; i < comArr.length(); i++) {//process all commands
            /*Catch the "retry" loop string and ignore it*/
            String retryCase = comArr.get(i).toString();
            if (retryCase.equals(RETRY)) {
                return;//ignore
            }
            
            /*Get current command type*/
            JSONObject curObj = comArr.getJSONObject(i);
            String curType = curObj.getString(TYPE);

            String blockName, blockAttr;
            JSONArray newArr;
            
            /*Check for type of command*/
            switch (curType) {
                case COMMAND:
                    /*Split name and attributes*/
                    blockName = curObj.getString(COMMS).split(" ")[0];
                    blockAttr = curObj.getString(COMMS).split(" ")[1];

                    /*Create a unique subprocess for each command if inside a
                    parallel branch*/
                    if (!inPar) {//normal case
                        framesList.get(curFrame).addBlock(blockName, entName, blockAttr);
                    } else {//parallel case
                        parlCount++;//new parallel subprocess added
                        totalSubs++;
                        ProcessFrame parFrame = new ProcessFrame("Parallel "
                                + "Subprocess" + Integer.toString(parlCount), totalSubs);
                        parFrame.addBlock(blockName, entName, blockAttr);
                        framesList.add(parFrame);
                        WorkspacePanel.increaseSubCount();
                    }
                    break;
                case SUBPROCESS:
                    /*Do not add a subprocess block if inside a parallel branch,
                    create a parallel subprocess frame instead*/
                    if (!inPar) {//normal case
                        /*Add "Run Subprocess" block to current frame*/
                        blockName = "Run Subprocess";
                        blockAttr = "Subprocess: " + curObj.getString(NAME);
                        framesList.get(curFrame).addBlock(blockName, entName, blockAttr);

                        /*Add new frame for the subprocess and process it*/
                        newArr = curObj.getJSONArray(COMMS);
                        totalSubs++;
                        subCount++;
                        ProcessFrame subFrame = new ProcessFrame(curObj.getString(NAME),
                                totalSubs);
                        framesList.add(subFrame);
                        WorkspacePanel.increaseSubCount();

                        processArray(totalSubs, newArr, buildEntString(curObj), false);
                    } else {//parallel case
                        /*The "Parallel Procedures" block has been already added,
                        just add a new frame for the subprocess and process it*/
                        newArr = curObj.getJSONArray(COMMS);
                        totalSubs++;
                        parlCount++;//new parallel subprocess added
                        ProcessFrame parlFrame = new ProcessFrame("Parallel Subprocess"
                                + Integer.toString(parlCount), totalSubs);
                        framesList.add(parlFrame);
                        WorkspacePanel.increaseSubCount();

                        processArray(totalSubs, newArr, buildEntString(curObj), false);
                    }
                    break;
                case PARALLEL:
                    /*Skip parallel branches if already inside one*/
                    if (!inPar) {//normal case
                        int temp = parlCount;

                        /*Process the parallel commands*/
                        newArr = curObj.getJSONArray(COMMS);
                        processArray(totalSubs, newArr, entName, true);

                        /*Add "Parallel Procedures" block*/
                        blockName = "Parallel Procedures";
                        blockAttr = this.buildParlString(temp, parlCount);
                        framesList.get(curFrame).addBlock(blockName, entName, blockAttr);
                    } else {//move deeper in the parallel branch
                        newArr = curObj.getJSONArray(COMMS);
                        processArray(totalSubs, newArr, entName, true);
                    }
                    break;
                case CHANGE_ENT:
                    /*Only gets the first entity listed, app does not support
                    multiple entities for one block*/
                    JSONObject curUser = curObj.getJSONArray(CUR_USERS).getJSONObject(0);
                    entName = curUser.getString(DIVISION) + " " + curUser.getString(DEPARTMENT)
                            + " " + curUser.getString(POSITION) + " " + curUser.getString(NAME);
                    break;
                case CONDITION_STEPS:
                    /*Process try blocks normally in current frame*/
                    newArr = curObj.getJSONArray(TRY);
                    processArray(curFrame, newArr, entName, false);

                    blockName = "Formal Assestment";//add block to curr process
                    blockAttr = "Condition: " + curObj.getString(CONDITION) + "\n";
                    if (curObj.getJSONArray(NO).isEmpty()) {//nothing on failure
                        blockAttr = "";
                    } else {//failure subprocess needed
                        failCount++;//increase count
                        totalSubs++;
                        blockAttr += "On Failure: Assestment Failure "
                                + Integer.toString(failCount);
                    }
                    
                    /*Success blocks are added normally to the current frame*/
                    framesList.get(curFrame).addBlock(blockName, entName, blockAttr);
                    newArr = curObj.getJSONArray(YES);
                    processArray(curFrame, newArr, entName, false);

                    /*Failure blocks are added in a new subprocess*/
                    newArr = curObj.getJSONArray(NO);
                    if (!newArr.isEmpty()) {//new subprocess for failure if needed
                        String frameName = "Assestment Failure " + Integer.toString(failCount);
                        ProcessFrame subFrame = new ProcessFrame(frameName, totalSubs);
                        framesList.add(subFrame);
                        WorkspacePanel.increaseSubCount();

                        processArray(totalSubs, newArr, entName, false);
                    }
                    break;
            }

        }
    }

    /**
     * Builds the entity string from the corresponding JSON fields
     *
     * @param curObj JSON representation of a block
     * 
     * @return entity string
     */
    private String buildEntString(JSONObject curObj){
        String entString = "";
        if (!curObj.getJSONArray(USERS).isEmpty()) {
            JSONObject entity = curObj.getJSONArray(USERS).getJSONObject(0);
            entString = entity.getString(DIVISION) + " " + entity.getString(DEPARTMENT)
                    + " " + entity.getString(POSITION) + " " + entity.getString(NAME);

        }
        return entString;
    }

    /**
     * Builds a "Parallel Procedures" block's attributes string to point to all
     * the parallel subprocess it adds.
     * 
     * @param start points at the first subprocess added
     * @param end   points at the last subprocess added
     * 
     * @return string pointers to the parallel subprocesses
     */
    private String buildParlString(int start, int end) {
        String parString = "";//shouldn't remain empty!
        for (int i = start; i < end; i++) {
            parString = parString.concat("Parallel Subprocess "
                    + Integer.toString(i + 1) + "\n");
        }
        return parString;
    }

}
