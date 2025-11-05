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
package br.unicamp.cst.bindings.soar;

public class SoarCommandNested {
    String productionName = null;
    double quantity = 0;
    String apply = "false";
    SoarCommandChange nestedClass = null;

    public void setProductionName(String productionName) {
        this.productionName = productionName;
    }

    public String getProductionName() {
        return productionName;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setApply(String apply) {
        this.apply = apply;
    }

    public String isApply() {
        return apply;
    }

    public SoarCommandChange getNestedClass() {
        return nestedClass;
    }

    public void setNestedClass(SoarCommandChange nestedClass) {
        this.nestedClass = nestedClass;
    }
}
