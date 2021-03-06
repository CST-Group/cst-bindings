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

import org.ros.exception.RemoteException;
import org.ros.exception.ServiceNotFoundException;
import org.ros.namespace.GraphName;
import org.ros.node.*;
import org.ros.node.service.ServiceClient;
import org.ros.node.service.ServiceResponseListener;

import java.net.URI;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Wrapper defining a synchronous RosJava Service Client from 
 * the ROSJAVA asynchronous implementation. This is a one shot 
 * object, meaning it will spin up a ROS node when created and
 * stop it after responding. Re-create another object if you
 * want to call again.
 * 
 * @author andre
 *
 * @param <S> Service Message Request - Ex: AddTwoIntsRequest from ROS Tutorials
 * @param <T> Service Message Response - Ex: AddTwonIntsResponse from ROS Tutorials
 */
public abstract class RosServiceClientSync<S,T> implements NodeMain {
	
	protected String nodeName;

	protected String service;

	protected String messageServiceType;
	
	protected S serviceMessageRequest;
	
	protected BlockingQueue<T> blockingQueueServiceMessageResponse;
	
	protected BlockingQueue<Boolean> blockingQueueConnection;
	
	protected ServiceClient<S, T> serviceClient;

	protected NodeMainExecutor nodeMainExecutor;

	protected NodeConfiguration nodeConfiguration;
	
	protected ServiceResponseListener<T> serviceResponseListener;
	
	/**
	 * Constructor for the RosServiceClientSync.
	 * 
	 * @param nodeName the name of this ROS node.
	 * @param service the service that this node will be a client of.
	 * @param messageServiceType the ROS message type. Ex: "rosjava_test_msgs.AddTwoInts".
	 * @param host the host IP where to run. Ex: "127.0.0.1".
	 * @param masterURI the URI of the master ROS node. Ex: new URI("http://127.0.0.1:11311").
	 */
	public RosServiceClientSync(String nodeName, String service, String messageServiceType, String host, URI masterURI) {

		super();
		this.nodeName = nodeName;
		this.service = service;
		this.messageServiceType = messageServiceType;
		blockingQueueServiceMessageResponse = new ArrayBlockingQueue<T>(1);
		blockingQueueConnection = new ArrayBlockingQueue<Boolean>(1);
		
		serviceResponseListener = new ServiceResponseListener<T>() {
			@Override
			public void onSuccess(T response) {			    	  
				if(response != null) {
					blockingQueueServiceMessageResponse.add(response);
					blockingQueueConnection.add(true);
				}						
			}

			@Override
			public void onFailure(RemoteException e) {
				e.printStackTrace();
			}
		};

		nodeMainExecutor = DefaultNodeMainExecutor.newDefault();
		nodeConfiguration = NodeConfiguration.newPublic(host,masterURI);
		startRosNode();
	}

	private void startRosNode() {
		nodeMainExecutor.execute(this, nodeConfiguration);
	}
 
	public void stopRosNode() {
		nodeMainExecutor.shutdownNodeMain(this);
	}
	
	/**
	 * Prepare the service request to be sent, formatting it with the contents of the args.
	 * 
	 * @param args the arguments with the content to be formatted in the form of a service request.
	 * @param serviceMessageRequest the service message request to be sent.
	 */
	public abstract void formatServiceRequest(Object[] args, S serviceMessageRequest);
	
	public T callService(Object[] args) throws InterruptedException {
		blockingQueueConnection.take();		
		formatServiceRequest(args, serviceMessageRequest);
		serviceClient.call(serviceMessageRequest, serviceResponseListener);
		T result = blockingQueueServiceMessageResponse.take();		
		return result;
	}
	
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
		serviceMessageRequest = serviceClient.newMessage();
		
		blockingQueueConnection.add(true);
	}
	
	@Override
	public void onShutdown(Node node) {
		// empty
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