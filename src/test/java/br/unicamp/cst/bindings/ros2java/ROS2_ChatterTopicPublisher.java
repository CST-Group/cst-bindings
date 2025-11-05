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

import br.unicamp.cst.core.entities.Memory;
import id.jrosmessages.std_msgs.StringMessage;

import java.net.URI;

/**
 * @author jrborelli
 *
 */


public class ROS2_ChatterTopicPublisher extends RosTopicPublisherCodelet<StringMessage> {

    public ROS2_ChatterTopicPublisher(String topic) {
        super("teste",topic, StringMessage.class);
    }

    @Override
    protected StringMessage createNewMessage() {
        return new StringMessage();
    }

    @Override
    protected void fillMessageToBePublished(Memory motorMemory, StringMessage message) {
        Object data = motorMemory.getI();
        if (data instanceof String) {
            message.withData((String) data);
        } else {
            message.withData("");
        }
    }
}
