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

import br.unicamp.cst.core.entities.Memory;

import java.net.URI;

/**
 * @author andre
 *
 */
public class ChatterTopicPublisher extends RosTopicPublisherCodelet<std_msgs.String> {

	public ChatterTopicPublisher(String host, URI masterURI) {
		super("ChatterTopicPublisher", "chatter", std_msgs.String._TYPE, host, masterURI);
	}

	@Override
	public void fillMessageToBePublished(Memory motorMemory, std_msgs.String message) {
		
		if(motorMemory == null) {
			return;
		}
		
		String messageData = (String) motorMemory.getI();
		
		if(messageData == null) {
			return;
		}
		
		if(message == null) {
			return;
		}
		
		message.setData(messageData);	
	}
}
