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

import GUI.ProcessBlock;
import GUI.ProcessFrame;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Exports a model to a JSON file. Uses "stleary/JSON-java" library to handle
 * JSON objects (https://github.com/stleary/JSON-java)
 *
 * @author Christos Darisaplis
 */
public class ModelExporter {

    private ArrayList<ProcessFrame> toWrite;//data to write
    
    /*JSONObject keys*/
    private static final int IDENTATION = 4;//write identation
    private static final String APP_EXP = "appExport";
    private static final String FRAME_TITLE = "title";
    private static final String FRAMES_KEY = "frames";
    private static final String BLOCKS_KEY = "blocks";
    private static final String BLOCK_NAME = "name";
    private static final String BLOCK_ENT = "entity";
    private static final String BLOCK_ATTR = "attributes";

    /**
     * Creates the exporter and links it a list of frames to write
     * 
     * @param toWrite list of frames to write
     */
    public ModelExporter(ArrayList<ProcessFrame> toWrite) {
        this.toWrite = toWrite;
    }

    /**
     * Writes its linked list of Process Frames to the specified file
     * 
     * @param JSONFile file to write
     * 
     * @throws IOException file writing exception
     * @throws JSONException  JSON writing exception
     */
    public void exportModel(File JSONFile) throws IOException, JSONException {
        JSONObject jo = buildJSONObject();
        
        try (FileWriter file = new FileWriter(JSONFile)) {
            file.write(jo.toString(IDENTATION));
            
        }

    }
    
    /**
     * Builds a JSONObject from the linked list of process frames
     * 
     * @return JSONObject representation of the frames
     */
    private JSONObject buildJSONObject(){
        JSONObject jo = new JSONObject();
        JSONObject frameObject, blockObject;
        
        jo.put(APP_EXP, true);
        jo.put(FRAMES_KEY, new JSONArray());//in case of no frames just an empty array
        
        for (ProcessFrame aFrame : toWrite){//write all frames
            frameObject = new JSONObject();
            frameObject.put(FRAME_TITLE, aFrame.getName());
            /*Initialize array with an empty array in case of no blocks*/
            frameObject.put(BLOCKS_KEY, new JSONArray());
            
            for (ProcessBlock aBlock : aFrame.getBlocks()){
                blockObject = new JSONObject();
                blockObject.put(BLOCK_NAME, aBlock.getName());
                blockObject.put(BLOCK_ENT, aBlock.getEntName());
                blockObject.put(BLOCK_ATTR, aBlock.getAttrString());
                frameObject.append(BLOCKS_KEY, blockObject);
            }
            jo.append(FRAMES_KEY, frameObject);
        }
        
        return jo;
    }

}
