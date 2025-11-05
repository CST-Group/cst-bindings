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
import rosjava_test_msgs.AddTwoIntsRequest;
import rosjava_test_msgs.AddTwoIntsResponse;

import java.net.URI;

/**
 * @author andre
 *
 */
public class AddTwoIntServiceClient extends RosServiceClientCodelet<AddTwoIntsRequest, AddTwoIntsResponse> {
	
	private Integer sum;

	public AddTwoIntServiceClient(String host,URI masterURI) {
		
		super("AddTwoIntServiceClient", "add_two_ints", rosjava_test_msgs.AddTwoInts._TYPE, host, masterURI);
	}

	@Override
	public boolean formatServiceRequest(Memory motorMemory, AddTwoIntsRequest serviceMessageRequest) {
		
		if(motorMemory == null || motorMemory.getI() == null) {
			return false;
		}
		
		Integer[] numsToSum = (Integer[]) motorMemory.getI();
		serviceMessageRequest.setA(numsToSum[0]);
		serviceMessageRequest.setB(numsToSum[1]);
		
		return true;	
	}

	@Override
	public void processServiceResponse(AddTwoIntsResponse serviceMessageResponse) {
		sum = (int) serviceMessageResponse.getSum();
		System.out.println("Sum = "+sum);
	}

	/**
	 * @return the sum
	 */
	public Integer getSum() {
		return sum;
	}
}
