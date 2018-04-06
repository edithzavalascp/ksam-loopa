package org.ksam.model.configuration.supportedvalues;

public enum Operator {
    EQUAL {

	@Override
	public double apply(Object x, Object y) {
	    // TODO Auto-generated method stub
	    return 0;
	}

    },
    NOT {

	@Override
	public double apply(Object x, Object y) {
	    // TODO Auto-generated method stub
	    return 0;
	}

    },
    GT {

	@Override
	public double apply(Object x, Object y) {
	    // TODO Auto-generated method stub
	    return 0;
	}

    },
    LT {

	@Override
	public double apply(Object x, Object y) {
	    // TODO Auto-generated method stub
	    return 0;
	}

    },
    GOE {

	@Override
	public double apply(Object x, Object y) {
	    // TODO Auto-generated method stub
	    return 0;
	}

    },
    LOT {

	@Override
	public double apply(Object x, Object y) {
	    // TODO Auto-generated method stub
	    return 0;
	}

    };

    public abstract double apply(Object x, Object y);
}
