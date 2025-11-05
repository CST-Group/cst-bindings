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
import troca_ros.AddTwoIntsRequestMessage;
import troca_ros.AddTwoIntsResponseMessage;
import troca_ros.AddTwoIntsServiceDefinition;

/**
 *
 * @author jrborelli
 */


public class AddTwoIntsServiceClientSyncRos2 extends RosServiceClientSync<AddTwoIntsRequestMessage, AddTwoIntsResponseMessage> {

    public AddTwoIntsServiceClientSyncRos2(String serviceName) {
        super(serviceName, new AddTwoIntsServiceDefinition());
    }

    @Override
    public void formatServiceRequest(Object[] args, AddTwoIntsRequestMessage requestMessage) {
        requestMessage.withA((Long) args[0]).withB((Long) args[1]);
    }

    @Override
    protected AddTwoIntsRequestMessage createNewRequest() {
        return new AddTwoIntsRequestMessage();
    }
}
