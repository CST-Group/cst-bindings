/***********************************************************************************************
 * Copyright (c) 2012  DCA-FEEC-UNICAMP
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v3
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl.html
 * <p>
 * Contributors:
 * K. Raizer, A. L. O. Paraense, E. M. Froes, R. R. Gudwin - initial API and implementation
 ***********************************************************************************************/
package br.unicamp.cst.bindings.rosjava;

import br.unicamp.cst.core.entities.Codelet;
import br.unicamp.cst.core.entities.Memory;
import br.unicamp.cst.core.exceptions.CodeletActivationBoundsException;
import org.ros.exception.RemoteException;
import org.ros.exception.ServiceNotFoundException;
import org.ros.namespace.GraphName;
import org.ros.node.*;
import org.ros.node.service.ServiceClient;
import org.ros.node.service.ServiceResponseListener;

import java.net.URI;
import java.util.concurrent.Semaphore;

/**
 * Wrapper binding a RosJava Service Client and a Codelet.
 * The object of this class is a hybrid of Codelet and ROS Service Client,
 * not only operating in both lifetime cycles of each one but also having these
 * lifetime cycle coupled and integrated.
 * 
 * @author andre
 *
 * @param <S> Service Message Request - Ex: AddTwoIntsRequest from ROS Tutorials
 * @param <T> Service Message Response - Ex: AddTwonIntsResponse from ROS Tutorials
 */
public abstract class RosServiceClientCodelet<S,T> extends Codelet implements NodeMain {

	protected String nodeName;

	protected String service;

	protected String messageServiceType;

	protected Memory motorMemory;

	protected S serviceMessageRequest;

	protected ServiceClient<S, T> serviceClient;

	protected NodeMainExecutor nodeMainExecutor;

	protected NodeConfiguration nodeConfiguration;
	
	protected ServiceResponseListener<T> serviceResponseListener;
	
	private Semaphore callInProgressSemaphore = new Semaphore(1);

	/**
	 * Constructor for the RosServiceClientCodelet.
	 * 
	 * @param nodeName the name of this ROS node.
	 * @param service the service that this node will be a client of.
	 * @param messageServiceType the ROS message type. Ex: "rosjava_test_msgs.AddTwoInts".
	 * @param host the host IP where to run. Ex: "127.0.0.1".
	 * @param masterURI the URI of the master ROS node. Ex: new URI("http://127.0.0.1:11311").
	 */
	public RosServiceClientCodelet(String nodeName, String service, String messageServiceType, String host, URI masterURI) {

		super();
		this.nodeName = nodeName;
		this.service = service;
		this.messageServiceType = messageServiceType;
		setName(nodeName);
		
		serviceResponseListener = new ServiceResponseListener<T>() {
			@Override
			public void onSuccess(T response) {	
				if(response != null) {
					processServiceResponse(response);
					callInProgressSemaphore.release();
				}						
			}

			@Override
			public void onFailure(RemoteException e) {
                                e.printStackTrace();
			}
		};

		nodeMainExecutor = DefaultNodeMainExecutor.newDefault();
		nodeConfiguration = NodeConfiguration.newPublic(host,masterURI);
	}

	@Override
	public synchronized void start() {
                startRosNode();
		super.start();
	}

	@Override
	public synchronized void stop() {
                stopRosNode();
		super.stop();
	}

	private void startRosNode() {
                nodeMainExecutor.execute(this, nodeConfiguration);
	}

	private void stopRosNode() {
                serviceClient = null;
		serviceMessageRequest = null;
		nodeMainExecutor.shutdownNodeMain(this);
	}

	@Override
	public void accessMemoryObjects() {
		int index = 0;

		if(motorMemory == null)
			motorMemory = this.getInput(nodeName, index);
	}

	@Override
	public void calculateActivation() {
		try{
			setActivation(0.0d);
		} catch (CodeletActivationBoundsException e) {
			e.printStackTrace();
		}	
	}

	@Override
	public void proc() {
		if(motorMemory != null 
				&& motorMemory.getI() != null 
				&& serviceMessageRequest != null 
				&& formatServiceRequest(motorMemory, serviceMessageRequest)) 
		{
			callService();
		}
	}

	/**
	 * 
	 * Prepare the service request to be sent, formatting it with the contents of the motor memory.
	 * @param motorMemory the memory with the content to be formatted in the form of a service request.
	 * @param serviceMessageRequest the service message request to be sent.
	 * @return true if the ROS service should be called, otherwise false.
	 */
	public abstract boolean formatServiceRequest(Memory motorMemory, S serviceMessageRequest);

	private void callService() {
		if(serviceClient != null && serviceMessageRequest != null) {
			try {
				callInProgressSemaphore.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
				Thread.currentThread().interrupt();
			}
			serviceClient.call(serviceMessageRequest, serviceResponseListener);
		}
	}

	/**
	 * Processes the service response in a free way.
	 * @param serviceMessageResponse the response after the service has been executed.
	 */
	public abstract void processServiceResponse(T serviceMessageResponse);

	@Override
	public GraphName getDefaultNodeName() {
		return GraphName.of(nodeName);
	}

	@Override
	public void onStart(ConnectedNode connectedNode) {	    
		try {
			serviceClient = connectedNode.newServiceClient(service, messageServiceType);
		} catch (ServiceNotFoundException e) {
			e.printStackTrace();
		}
                if (serviceClient != null) serviceMessageRequest = serviceClient.newMessage();
                else System.out.println("ROSServiceClientCodelet: I was not able to create a new Service Client");
	}

	@Override
	public void onShutdown(Node node) {
	}

	@Override
	public void onShutdownComplete(Node node) {
		// empty
	}

	@Override
	public void onError(Node node, Throwable throwable) {
		// empty
	}
}
