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
 * Definition for example_interfaces/AddTwoInts_Request
 *
 * @author lambdaprime intid@protonmail.com
 */
@MessageMetadata(
        name = AddTwoIntsRequestMessage.NAME,
        fields = {"a", "b"},
        interfaceType = RosInterfaceType.SERVICE)
public class AddTwoIntsRequestMessage implements Message {

    static final String NAME = "troca_ros/AddTwoIntsServiceRequest";
    public long a;
    public long b;

    public AddTwoIntsRequestMessage() {}

    public AddTwoIntsRequestMessage(long a, long b) {
        this.a = a;
        this.b = b;
    }

    public AddTwoIntsRequestMessage withA(long a) {
       this.a = a;
       return this;
   }
   
   public AddTwoIntsRequestMessage withB(long b) {
       this.b = b;
       return this;
   }
    
    @Override
    public int hashCode() {
        return Objects.hash(a, b);
    }

    @Override
    public boolean equals(Object obj) {
        var other = (AddTwoIntsRequestMessage) obj;
        return Objects.equals(a, other.b) && Objects.equals(a, other.b);
    }

    @Override
    public String toString() {
        return XJson.asString("a", a,"b", b);
    }
}