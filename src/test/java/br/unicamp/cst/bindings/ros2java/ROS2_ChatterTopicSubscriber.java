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

/**
 * @author jrborelli
 *
 */


public class ROS2_ChatterTopicSubscriber extends RosTopicSubscriberCodelet<StringMessage> {

    public ROS2_ChatterTopicSubscriber(String topic) {
        super("teste",topic, StringMessage.class);
    }

    @Override
    public void fillMemoryWithReceivedMessage(StringMessage message, Memory sensoryMemory) {
        if (message == null || sensoryMemory == null) return;

        String data = message.data;
        sensoryMemory.setI(data);
        System.out.println("Received chatter message: " + data);
    }
}
