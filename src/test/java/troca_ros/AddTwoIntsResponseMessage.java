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

import id.jrosmessages.Message;
import id.jrosmessages.MessageMetadata;
import id.jrosmessages.RosInterfaceType;
import id.xfunction.XJson;
import java.util.Objects;

/**
 * Definition for example_interfaces/AddTwoInts_Response
 *
 * @author lambdaprime intid@protonmail.com
 */
@MessageMetadata(name = AddTwoIntsResponseMessage.NAME, interfaceType = RosInterfaceType.SERVICE)
public class AddTwoIntsResponseMessage implements Message {

    static final String NAME = "troca_ros/AddTwoIntsServiceResponse";

    public long sum;

    public AddTwoIntsResponseMessage() {}

    public AddTwoIntsResponseMessage(long sum) {
        this.sum = sum;
    }

    @Override
    public int hashCode() {
        return Objects.hash(sum);
    }

    @Override
    public boolean equals(Object obj) {
        var other = (AddTwoIntsResponseMessage) obj;
        return sum == other.sum;
    }

    @Override
    public String toString() {
        return XJson.asString("sum", sum);
    }
}