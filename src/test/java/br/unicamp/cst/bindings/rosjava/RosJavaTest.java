/**
 * 
 */
package br.unicamp.cst.bindings.rosjava;

import br.unicamp.cst.core.entities.MemoryObject;
import br.unicamp.cst.core.entities.Mind;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.ros.RosCore;
import org.ros.node.DefaultNodeMainExecutor;
import org.ros.node.NodeConfiguration;
import org.ros.node.NodeMainExecutor;
import rosjava_test_msgs.AddTwoIntsResponse;

import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.Assert.assertEquals;

/**
 * @author andre
 *
 */
public class RosJavaTest {
	
	private static RosCore rosCore;
	
	@BeforeClass
    public static void beforeAllTestMethods() {
		rosCore  = RosCore.newPublic("127.0.0.1",11311);
	    rosCore.start();
    }

	@AfterClass
    public static void afterAllTestMethods() {
        rosCore.shutdown();
    }
    
    @Test
    public void testRosTopics() throws URISyntaxException {
    	
    	Mind mind = new Mind();
	    
	    ChatterTopicSubscriber chatterTopicSubscriber = new ChatterTopicSubscriber("127.0.0.1",new URI("http://127.0.0.1:11311"));	    
	    MemoryObject sensoryMemory = mind.createMemoryObject(chatterTopicSubscriber.getName());
	    chatterTopicSubscriber.addOutput(sensoryMemory);	    
	    mind.insertCodelet(chatterTopicSubscriber);
	    
	    ChatterTopicPublisher chatterTopicPublisher = new ChatterTopicPublisher("127.0.0.1",new URI("http://127.0.0.1:11311"));	    
	    MemoryObject motorMemory = mind.createMemoryObject(chatterTopicPublisher.getName());
	    chatterTopicPublisher.addInput(motorMemory);
	    String messageExpected = "Hello World";
	    motorMemory.setI(messageExpected);    
	    mind.insertCodelet(chatterTopicPublisher);
	    
	    mind.start();
	    
	    try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	    
	    String messageActual = (String) sensoryMemory.getI();
	    
	    assertEquals(messageExpected, messageActual);
	    
	    mind.shutDown();
	    
	    try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    }
    
    @Test
    public void testRosServiceSync() throws URISyntaxException, InterruptedException {
    	
		AddTwoIntService addTwoIntService = new AddTwoIntService();
		NodeMainExecutor nodeMainExecutor = DefaultNodeMainExecutor.newDefault();
		NodeConfiguration nodeConfiguration = NodeConfiguration.newPublic("127.0.0.1",new URI("http://127.0.0.1:11311"));
		nodeMainExecutor.execute(addTwoIntService, nodeConfiguration);
				
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
				
		AddTwoIntServiceClientSync addTwoIntServiceClient = new AddTwoIntServiceClientSync("127.0.0.1",new URI("http://127.0.0.1:11311"));
		
		long expectedSum = 5L;
		
		Integer[] numsToSum = new Integer[] {2,3};
		
		AddTwoIntsResponse addTwoIntsResponse = addTwoIntServiceClient.callService(numsToSum);
		
		long actualSum = addTwoIntsResponse.getSum();
		
		assertEquals(expectedSum, actualSum);
		
		long expectedSum2 = 6L;
		
		Integer[] numsToSum2 = new Integer[] {2,4};
		
		AddTwoIntsResponse addTwoIntsResponse2 = addTwoIntServiceClient.callService(numsToSum2);
		
		long actualSum2 = addTwoIntsResponse2.getSum();
		
		assertEquals(expectedSum2, actualSum2);
		
		addTwoIntServiceClient.stopRosNode();
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		nodeMainExecutor.shutdownNodeMain(addTwoIntService);
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    }
    
    @Test
    public void testRosService() throws URISyntaxException {
    	
		AddTwoIntService addTwoIntService = new AddTwoIntService();
		NodeMainExecutor nodeMainExecutor = DefaultNodeMainExecutor.newDefault();
		NodeConfiguration nodeConfiguration = NodeConfiguration.newPublic("127.0.0.1",new URI("http://127.0.0.1:11311"));
		nodeMainExecutor.execute(addTwoIntService, nodeConfiguration);
				
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		Mind mind = new Mind();
		
		AddTwoIntServiceClient addTwoIntServiceClient = new AddTwoIntServiceClient("127.0.0.1",new URI("http://127.0.0.1:11311"));
		
		MemoryObject motorMemory = mind.createMemoryObject(addTwoIntServiceClient.getName());
		addTwoIntServiceClient.addInput(motorMemory);
		
		Integer expectedSum = 5;
		
		Integer[] numsToSum = new Integer[] {2,3};
		motorMemory.setI(numsToSum);
		
		mind.insertCodelet(addTwoIntServiceClient);
		
		mind.start();
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		assertEquals(expectedSum, addTwoIntServiceClient.getSum());
		
		mind.shutDown();
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		nodeMainExecutor.shutdownNodeMain(addTwoIntService);
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		
    }
    
    @Test
    public void testRosServiceCalledTwice() throws URISyntaxException {
    	
    	AddTwoIntService addTwoIntService = new AddTwoIntService();
		NodeMainExecutor nodeMainExecutor = DefaultNodeMainExecutor.newDefault();
		NodeConfiguration nodeConfiguration = NodeConfiguration.newPublic("127.0.0.1",new URI("http://127.0.0.1:11311"));
		nodeMainExecutor.execute(addTwoIntService, nodeConfiguration);
				
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		Mind mind = new Mind();
		
		AddTwoIntServiceClient addTwoIntServiceClient = new AddTwoIntServiceClient("127.0.0.1",new URI("http://127.0.0.1:11311"));
		
		MemoryObject motorMemory = mind.createMemoryObject(addTwoIntServiceClient.getName());
		addTwoIntServiceClient.addInput(motorMemory);
		
		Integer expectedSum = 5;
		
		Integer[] numsToSum = new Integer[] {2,3};
		motorMemory.setI(numsToSum);	
		
		mind.insertCodelet(addTwoIntServiceClient);
		
		mind.start();
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		assertEquals(expectedSum, addTwoIntServiceClient.getSum());
		
		expectedSum = 6;
		
		numsToSum = new Integer[] {3,3};
		motorMemory.setI(numsToSum);
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		assertEquals(expectedSum, addTwoIntServiceClient.getSum());
		
		mind.shutDown();
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		nodeMainExecutor.shutdownNodeMain(addTwoIntService);
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    	
    }
}
