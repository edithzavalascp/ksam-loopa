package org.ksam.model.configuration.supportedvalues;

public enum Operator {
    EQUAL {

	@Override
	double apply(Object x, Object y) {
	    // TODO Auto-generated method stub
	    return 0;
	}

    },
    NOT {

	@Override
	double apply(Object x, Object y) {
	    // TODO Auto-generated method stub
	    return 0;
	}

    },
    GT {

	@Override
	double apply(Object x, Object y) {
	    // TODO Auto-generated method stub
	    return 0;
	}

    },
    LT {

	@Override
	double apply(Object x, Object y) {
	    // TODO Auto-generated method stub
	    return 0;
	}

    },
    GOE {

	@Override
	double apply(Object x, Object y) {
	    // TODO Auto-generated method stub
	    return 0;
	}

    },
    LOT {

	@Override
	double apply(Object x, Object y) {
	    // TODO Auto-generated method stub
	    return 0;
	}

    };

    abstract double apply(Object x, Object y);
}
