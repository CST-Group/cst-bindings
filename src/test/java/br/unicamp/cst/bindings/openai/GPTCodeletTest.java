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
package br.unicamp.cst.bindings.openai;

import br.unicamp.cst.core.entities.Mind;
import com.google.gson.JsonPrimitive;
import io.github.sashirestela.openai.SimpleOpenAI;
import io.github.sashirestela.openai.domain.chat.Chat;
import io.github.sashirestela.openai.domain.chat.ChatMessage;
import io.github.sashirestela.openai.domain.chat.ChatRequest;
import org.json.JSONObject;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author wander
 *
 */

public class GPTCodeletTest {


    GPTCodelet gptCodelet = new GPTCodelet(System.getenv("OPENAI_API_KEY")) {

        /*public HashMap<String, Object> arrangeParams(String userMsg) {
            HashMap<String, Object> params = new HashMap<String, Object>();

            HashMap<String, Object> fullContent = new HashMap<>();
            fullContent.put("role", "user");
            fullContent.put("content", userMsg);
            JSONObject json = new JSONObject(fullContent);


            String message = "[" + json + "]";

            params.put("Authorization", OPENAI_API_KEY);
            params.put("model", "gpt-4o-mini");
            params.put("messages", message);
            params.put("temperature", 0.7);

            return params;
        } */


        @Override
        public void accessMemoryObjects() {

        }

        @Override
        public void calculateActivation() {

        }

        @Override
        public void proc() {
            String userMsg = "Say this is a test!";
            /*HashMap<String, Object> params = arrangeParams(userMsg);
            String answer = "Nothing";
            try {
                if(i == 0){
                answer = sendPOST(chatCompletionEndpoint, params, "application/json");
                i++;}
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
             */

        }
    };

    @Test
    public void testResponse() throws InterruptedException {
        Mind mind = new Mind();
        String expected = "this is a test!";

        mind.insertCodelet(gptCodelet);

        mind.start();

        String answer = gptCodelet.completionOpenAI("Say this is a test!");

        Thread.sleep(2000L);

        mind.shutDown();
        //assertEquals(expected, answer);
        System.out.println(answer.toLowerCase());
        assertTrue(answer.toLowerCase().contains(expected));
        //System.exit(0);
    }

}

