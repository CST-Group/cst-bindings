/*******************************************************************************
 * Copyright (c) 2012  DCA-FEEC-UNICAMP
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v3
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl.html
 * 
 * Contributors to this module:
 *     W. Gibaut, R. R. Gudwin 
 ******************************************************************************/

package br.unicamp.cst.bindings.soar;

import br.unicamp.cst.core.entities.Codelet;
import br.unicamp.cst.representation.idea.Idea;
import com.google.common.primitives.Doubles;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.jsoar.kernel.symbols.Identifier;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.lang.reflect.Field;
import java.util.*;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author wander
 */
public abstract class JSoarCodelet extends Codelet {
    
    private String agentName;
    private File productionPath;

    private SOARPlugin jsoar;

    private static final String ARRAY = "ARRAY";

    public static final String OUTPUT_COMMAND_MO = "OUTPUT_COMMAND_MO";
    
    public void SilenceLoggers() {
        ((ch.qos.logback.classic.Logger) LoggerFactory.getLogger("org.jsoar")).setLevel(ch.qos.logback.classic.Level.OFF);
        Logger.getLogger("Simulation").setLevel(Level.SEVERE);
    }
    
    public void initSoarPlugin(String _agentName, File _productionPath, Boolean startSOARDebugger){
        this.setJsoar(new SOARPlugin(_agentName, _productionPath, startSOARDebugger));
    }


    public synchronized String getOutputLinkAsString(){
        return getJsoar().getOutputLinkAsString();
    }

    public synchronized String getInputLinkAsString(){
        return getJsoar().getInputLinkAsString();
    }

    public synchronized int getPhase(){
        return getJsoar().getPhase();
    }

    public synchronized void setDebugState(int state){
        getJsoar().setDebugState(state);
    }

    public synchronized int getDebugState(){
        return getJsoar().getDebugState();
    }

    public synchronized ArrayList<Object> getOutputInObject(String package_with_beans_classes){

        ArrayList<Object> commandList = null;
        Idea ol = getJsoar().getOutputLinkIdea();

        if(ol != null) {
            commandList = new ArrayList<>();
            for (Idea command : ol.getL()) {
                commandList.add(buildObject(command, package_with_beans_classes));
            }
        }
        else {
            System.out.println("Error in cst.JSoarCodelet: getOutputInObject was not able to get a reference to Soar OutputLink");
        }
        return commandList;
    }


    public synchronized Object buildObject(Idea command, String package_with_beans_classes){

        ArrayList<Object> arrayList = new ArrayList<>();
        String commandType = command.getName();
        Object commandObject = null;
        Class type = null;
        if(!commandType.toUpperCase().contains(ARRAY)) {
            try {
                type = Class.forName(package_with_beans_classes + "." + commandType);
                commandObject = type.newInstance();
                type.cast(commandObject);

            } catch (Exception e) {
                e.printStackTrace();
            }
            for (Idea p : command.getL()) {
                try {
                    for (Field field : type.getDeclaredFields()) {
                        if (p.getName().equals(field.getName())) {
                           if(p.getL().isEmpty()){
                               Object value = ((Idea) p.getValue()).getValue();
                               if (Doubles.tryParse(value.toString()) != null) {
                                   Double fvalue = Doubles.tryParse(value.toString());
                                   field.set(commandObject, fvalue);
                               }
                               else {
                                   field.set(commandObject, value.toString());
                               }
                           }
                           else{
                               for(Idea subP : p.getL()){
                                   Object newObj = buildObject(subP, package_with_beans_classes);

                                   field.set(commandObject, newObj);
                               }
                           }


                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }

        return arrayList.size() > 0 ? arrayList : commandObject;
    }

    
    public ArrayList<Object> getCommandsJSON(String package_with_beans_classes){
        ArrayList<Object> commandList = new ArrayList<Object>();
        JsonObject templist = getJsoar().getOutputLinkJSON();
        Set<Map.Entry<String,JsonElement>> set = templist.entrySet();
        Iterator <Entry<String,JsonElement>> it = set.iterator();
        while(it.hasNext()){
            Entry<String,JsonElement> entry = it.next();
            String key = entry.getKey();
            JsonObject commandtype = entry.getValue().getAsJsonObject();
            try{
                Class type = Class.forName(package_with_beans_classes+"."+key);
                Object command = type.newInstance();
                type.cast(command);
                for(Field field : type.getDeclaredFields()){
                    if(commandtype.has(field.getName())){
                        if(commandtype.get(field.getName()).getAsJsonPrimitive().isNumber()){
                            field.set(command, commandtype.get(field.getName()).getAsFloat());
                        }else{
                            field.set(command, commandtype.get(field.getName()).getAsString());
                        }
                    }
                }
                commandList.add(command);
                
            }catch(Exception e){
                 e.printStackTrace();
            }
        }
        return commandList;
    }

    public List<Identifier> getOperatorsPathList(){
        return getJsoar().getOperatorsPathList();
    }
    
    public JsonObject createJson(String pathToLeaf, Object value){
        JsonObject json = new JsonObject();
        if(value instanceof String){
            String specvalue =(String)value;
            json = getJsoar().createJsonFromString(pathToLeaf,specvalue);
        }
        else if(value instanceof Number){
            double specvalue = (double) (int) value;
            json = getJsoar().createJsonFromString(pathToLeaf,specvalue);
        }
        return json;
    }
    
    public void addToJson(JsonObject newBranch, JsonObject json, String property){
        json.add(property, newBranch);
    }

    public void addToJson(String newBranch, JsonObject json, Object value){
        if(value==null){
            JsonObject specvalue =(JsonObject)value;
            getJsoar().addBranchToJson(newBranch, json, specvalue);
            return;
        }
        if(value instanceof String){
            String specvalue =(String)value;
            getJsoar().addBranchToJson(newBranch, json, specvalue);
        }
        else if(value instanceof  Number){
            Double specvalue =(Double)value;
            getJsoar().addBranchToJson(newBranch, json, specvalue);
        }
        else{
            JsonObject specvalue = (JsonObject)value;
            getJsoar().addBranchToJson(newBranch, json, specvalue);
        }
    }

    
    public void setInputLinkJson(JsonObject json){
        getJsoar().setInputLinkIdea((Idea)getJsoar().createIdeaFromJson(json));
    }
    
    public void setInputLinkIdea(Idea wo){
        getJsoar().setInputLinkIdea(wo);
    }

    public void removeJson(String pathToOldBranch, JsonObject json){
        getJsoar().removeBranchFromJson(pathToOldBranch, json);
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public File getProductionPath() {
        return productionPath;
    }

    public void setProductionPath(File productionPath) {
        this.productionPath = productionPath;
    }

    public SOARPlugin getJsoar() {
        return jsoar;
    }

    public void setJsoar(SOARPlugin jsoar) {
        this.jsoar = jsoar;
    }
}
