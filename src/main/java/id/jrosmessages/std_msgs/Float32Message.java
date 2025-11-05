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

package id.jrosmessages.std_msgs;

import id.jrosmessages.Message;
import id.jrosmessages.MessageMetadata;
import id.jrosmessages.RosInterfaceType;

@MessageMetadata(name = "std_msgs/Float32", interfaceType = RosInterfaceType.MESSAGE, fields = { "data" })
public class Float32Message implements Message {
    public float data;

    public Float32Message() {
    }

    public Float32Message(float data) {
        this.data = data;
    }
}

/*
package id.jrosmessages.std_msgs;

import id.jrosmessages.Message;
import id.xfunction.XJsonStringBuilder;

public class Float32Message implements Message {
    public float data;

    public Float32Message() {}
    public Float32Message(float data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return new XJsonStringBuilder("Float32Message")
            .append("data", data)
            .toString();
    }
}
*/