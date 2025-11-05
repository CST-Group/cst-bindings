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

import org.ros.namespace.GraphName;
import org.ros.node.AbstractNodeMain;
import org.ros.node.ConnectedNode;
import org.ros.node.service.ServiceResponseBuilder;

/**
 * @author andre
 *
 */
public class AddTwoIntService extends AbstractNodeMain {

	@Override
	  public GraphName getDefaultNodeName() {
	    return GraphName.of("AddTwoIntService");
	  }

	  @Override
	  public void onStart(ConnectedNode connectedNode) {
	    connectedNode.newServiceServer("add_two_ints", rosjava_test_msgs.AddTwoInts._TYPE,
	        new ServiceResponseBuilder<rosjava_test_msgs.AddTwoIntsRequest, rosjava_test_msgs.AddTwoIntsResponse>() {
	          @Override
	          public void
	              build(rosjava_test_msgs.AddTwoIntsRequest request, rosjava_test_msgs.AddTwoIntsResponse response) {
	            response.setSum(request.getA() + request.getB());
	          }
	        });
	  }
}
