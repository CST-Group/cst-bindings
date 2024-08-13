/*******************************************************************************
 * Copyright (c) 2024  DCA-FEEC-UNICAMP
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v3
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors to this module:
 *     W. Gibaut
 ******************************************************************************/

package br.unicamp.cst.bindings.langchain;
import br.unicamp.cst.io.rest.HttpCodelet;
import com.hw.langchain.llms.ollama.Ollama;

import java.util.HashMap;


/**
 *
 * @author wander
 */
public abstract class OllamaCodelet extends HttpCodelet {
    Ollama llm;

    public OllamaCodelet (){
        super();
        llm = defaultOllama();
    }
    public OllamaCodelet (HashMap<String, Object> params){
        super();
        llm = configureOllama(params);
    }

    private Ollama defaultOllama(){
        HashMap<String, Object> params = new HashMap<>();
        params.put("baseURL", "http://localhost:11434");
        params.put("model", "llama3");
        params.put("temperature", 0f);

        return configureOllama(params);
        /*return Ollama.builder()
                .baseUrl("http://localhost:11434")
                .model("llama3")
                .temperature(0f)
                .build()
                .init();*/
    }

    private Ollama configureOllama(HashMap<String, Object> params){
        return Ollama.builder()
                .baseUrl((String)params.get("baseURL"))
                .model((String)params.get("model"))
                .temperature((Float)params.get("temperature"))
                .build()
                .init();
    }



    public String predict(String msg){
        return llm.predict(msg);
    }
    //"What is the capital of China?"

    // The capital of China is Beijing.
    // println(result);

}
