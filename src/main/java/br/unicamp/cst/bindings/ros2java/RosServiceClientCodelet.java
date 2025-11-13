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

package br.unicamp.cst.bindings.ros2java;

import br.unicamp.cst.core.entities.Codelet;
import br.unicamp.cst.core.entities.Memory;
import br.unicamp.cst.core.exceptions.CodeletActivationBoundsException;

import id.jrosmessages.Message;
import id.jros2client.JRos2Client;
import id.jros2client.JRos2ClientFactory;
import pinorobotics.jros2services.JRos2ServiceClient;
import pinorobotics.jros2services.JRos2ServicesFactory;
import pinorobotics.jrosservices.msgs.ServiceDefinition;

import java.util.concurrent.Semaphore;
import java.util.concurrent.CompletableFuture;

/**
 * The RosServiceClientCodelet is an abstract general codelet to be used as a codelet having a Memory as Input, 
 * meant to have the same name as the codelet itself and using the I field of this Memory as the input
 * to a ROS2 service. The RosServiceClientCodelet, in being an abstract codelet, must be extended by
 * another class, where the createNewRequest, formatServiceRequest and processServiceResponse must
 * be implemented, according to the specific Messages used as Request and Response for the ROS2 Service. 
 * In practice, this codelet uses the info I as data for using a ROS2 Service to do something useful. 
 * 
 * @author jrborelli & rgudwin
 * @param <S> the RequestMessage for the ROS2 Service
 * @param <T> the ResponseMessage for the ROS2 Service
 */
public abstract class RosServiceClientCodelet<S extends Message, T extends Message> extends Codelet {

    protected String serviceName;
    protected ServiceDefinition<S, T> serviceDefinition;
    protected Memory inputMemory;

    protected S requestMessage;
    protected JRos2ServiceClient<S, T> serviceClient;
    protected JRos2Client ros2Client;

    private final Semaphore callInProgressSemaphore = new Semaphore(1);

    public RosServiceClientCodelet(String serviceName, ServiceDefinition<S, T> serviceDefinition) {
        this.serviceName = serviceName;
        this.serviceDefinition = serviceDefinition;
    }

    @Override
    public synchronized void start() {
        try {
            ros2Client = new JRos2ClientFactory().createClient();
            serviceClient = new JRos2ServicesFactory().createClient(ros2Client, serviceDefinition, serviceName);
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize ROS 2 client for service: " + serviceName, e);
        }
        super.start();
    }

    @Override
    public synchronized void stop() {
        try {
            if (serviceClient != null) serviceClient.close();
            if (ros2Client != null) ros2Client.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.stop();
    }

    @Override
    public void accessMemoryObjects() {
        if (inputMemory == null) {
            inputMemory = this.getInput(this.getName(), 0);
        }
    }

    @Override
    public void calculateActivation() {
        try {
            setActivation(1.0);  // always ready to run
        } catch (CodeletActivationBoundsException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void proc() {
        if (inputMemory == null || inputMemory.getI() == null) return;

        if (!callInProgressSemaphore.tryAcquire()) {
            // Call in progress, skip this cycle
            return;
        }

        try {
            requestMessage = createNewRequest();
            if (!formatServiceRequest(inputMemory, requestMessage)) {
                callInProgressSemaphore.release();
                return; // no need to send request
            }
            
            CompletableFuture<T> responseFuture = serviceClient.sendRequestAsync(requestMessage);
            responseFuture.thenAccept(response -> {
                if (response != null) processServiceResponse(response);
                callInProgressSemaphore.release();
            }).exceptionally(ex -> {
                System.err.println("ROS 2 service call failed: " + ex.getMessage());
                callInProgressSemaphore.release();
                return null;
            });
        } catch (Exception e) {
            System.err.println("Error in ROS 2 service call: " + e.getMessage());
            e.printStackTrace();
            callInProgressSemaphore.release();
        }
    }

    /**
     * Create a new empty request message instance.
     * This method cannot be automatically set up because it depends on the 
     * RequestMessage type used in the service. To Generate the Java classes
     * for RequestMessage and ResponseMessage classes, together with the 
     * ServiceDefinition class one should be using the msgmonster package 
     * available at https://github.com/pinorobotics/msgmonster together with
     * the .msg and .srv files where the service's interface is defined. 
     * @return a proper instance of a RequestMessage according to the service interface
     */
    protected abstract S createNewRequest();
    /*@Override //exemple: assuming the AddTwoInts ROS2 service ...
    protected AddTwoIntsRequestMessage createNewRequest() {
        return new AddTwoIntsRequestMessage();
    } */
    
    

    /**
     * Fills the request message using the information available from memory.
     * @param memory 
     * @param request
     * @return true if the request should be sent.
     */
    protected abstract boolean formatServiceRequest(Memory memory, S request);
    /*
    @Override  //exemplo:
    protected boolean formatServiceRequest(Memory memory, AddTwoIntsRequestMessage request) {
        Integer[] inputs = (Integer[]) memory.getI(); // example cast
        if (inputs == null || inputs.length < 2) return false;
        request.setA(inputs[0]);
        request.setB(inputs[1]);
        return true;
}
    */
    

    /**
     * If necessary, handles the response message obtained after running the ROS2 service.
     * If the service in itself do all that is necessary, this method could simply do nothing. 
     * @param response the response message to be processed
     */
    protected abstract void processServiceResponse(T response);
    /*
    @Override
    protected void processServiceResponse(AddTwoIntsResponseMessage response) {
        int sum = response.getSum();
        System.out.println("Sum received: " + sum);
        // Update CST memory or state as needed
    }
    */   
}
