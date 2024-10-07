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
import org.junit.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author wander
 *
 */

public class GPTCodeletTest {
    final String OPENAI_API_KEY = System.getenv("OPENAI_API_KEY");

    GPTCodelet gptCodelet = new GPTCodelet(OPENAI_API_KEY)
    {
        @Override
        public void accessMemoryObjects() {}

        @Override
        public void calculateActivation() {}

        @Override
        public void proc() {
            String userMsg = "Say this is a test!";
        }
    };

    GPTCodelet gptCodelet_2 = new GPTCodelet()
    {
        @Override
        public void accessMemoryObjects() {}

        @Override
        public void calculateActivation() {}

        @Override
        public void proc() {
            String userMsg = "Say this is a test!";
        }
    };

    HashMap<String, Object> config = gptCodelet.configDefaultOpenAI();
    GPTCodelet gptCodelet_3 = new GPTCodelet(config, OPENAI_API_KEY)
    {
        @Override
        public void accessMemoryObjects() {}

        @Override
        public void calculateActivation() {}

        @Override
        public void proc() {
            String userMsg = "Say this is a test!";
        }
    };

    GPTCodelet gptCodelet_4 = new GPTCodelet(config)
    {
        @Override
        public void accessMemoryObjects() {}

        @Override
        public void calculateActivation() {}

        @Override
        public void proc() {
            String userMsg = "Say this is a test!";
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

    @Test
    public void testResponse_2() throws InterruptedException {
        Mind mind = new Mind();
        String expected = "this is a test!";

        mind.insertCodelet(gptCodelet_2);

        mind.start();

        String answer = gptCodelet_2.completionOpenAI("Say this is a test!");

        Thread.sleep(2000L);

        mind.shutDown();
        //assertEquals(expected, answer);
        System.out.println(answer.toLowerCase());
        assertTrue(answer.toLowerCase().contains(expected));
        //System.exit(0);
    }

    @Test
    public void testResponse_3() throws InterruptedException {
        Mind mind = new Mind();
        String expected = "this is a test!";

        mind.insertCodelet(gptCodelet_3);

        mind.start();

        String answer = gptCodelet_3.completionOpenAI("Say this is a test!");

        Thread.sleep(2000L);

        mind.shutDown();
        //assertEquals(expected, answer);
        System.out.println(answer.toLowerCase());
        assertTrue(answer.toLowerCase().contains(expected));
        //System.exit(0);
    }

    @Test
    public void testResponse_4() throws InterruptedException {
        Mind mind = new Mind();
        String expected = "this is a test!";

        mind.insertCodelet(gptCodelet_4);

        mind.start();

        String answer = gptCodelet_4.completionOpenAI("Say this is a test!");

        Thread.sleep(2000L);

        mind.shutDown();
        //assertEquals(expected, answer);
        System.out.println(answer.toLowerCase());
        assertTrue(answer.toLowerCase().contains(expected));
        //System.exit(0);
    }

}

