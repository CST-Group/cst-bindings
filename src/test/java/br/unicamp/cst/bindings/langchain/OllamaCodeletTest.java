package br.unicamp.cst.bindings.langchain;

import br.unicamp.cst.bindings.openai.GPTCodelet;
import br.unicamp.cst.core.entities.Mind;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author wander
 *
 */
public class OllamaCodeletTest {
    OllamaCodelet ollamaCodelet = new OllamaCodelet(){

        @Override
        public void accessMemoryObjects() {

        }

        @Override
        public void calculateActivation() {

        }

        @Override
        public void proc() {
        }
    };

    @Test
    public void testResponse() throws InterruptedException {
        Mind mind = new Mind();
        String expected = "beijing";

        mind.insertCodelet(ollamaCodelet);

        mind.start();

        String answer = ollamaCodelet.predict("What is the capital of China?");

        Thread.sleep(2000L);

        mind.shutDown();
        System.out.println(answer.toLowerCase());
        assertTrue(answer.toLowerCase().contains(expected));
    }
}
