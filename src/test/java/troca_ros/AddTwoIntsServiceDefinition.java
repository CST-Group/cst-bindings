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
package troca_ros;

import id.jrosmessages.MessageDescriptor;
import pinorobotics.jrosservices.msgs.ServiceDefinition;

/**
 * @author lambdaprime intid@protonmail.com
 */
public class AddTwoIntsServiceDefinition implements ServiceDefinition<AddTwoIntsRequestMessage, AddTwoIntsResponseMessage> {

    private static final MessageDescriptor<AddTwoIntsRequestMessage> REQUEST_MESSAGE_DESCRIPTOR = new MessageDescriptor<>(AddTwoIntsRequestMessage.class);
    private static final MessageDescriptor<AddTwoIntsResponseMessage> RESPONSE_MESSAGE_DESCRIPTOR = new MessageDescriptor<>(AddTwoIntsResponseMessage.class);

    @Override
    public MessageDescriptor<AddTwoIntsRequestMessage> getServiceRequestMessage() {
        return REQUEST_MESSAGE_DESCRIPTOR;
    }

    @Override
    public MessageDescriptor<AddTwoIntsResponseMessage> getServiceResponseMessage() {
        return RESPONSE_MESSAGE_DESCRIPTOR;
    }
}