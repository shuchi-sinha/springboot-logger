package com.shuchi.springboot.demo.mycoolapp.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface Adapter<A, B> {
    void beforeAdapt(A input);

    void afterAdapt(A input, B output);

    B onAdapt(A input);

    B adapt(A input);

    void onSuccess(A input, B output);

    void onFailure(A input, Throwable t);

}

abstract class AbstractAdapter<A, B> implements Adapter<A, B> {
    static Logger log = LoggerFactory.getLogger(AbstractAdapter.class);

    public void beforeAdapt(A input) {
        if (log.isTraceEnabled())
            log.trace("beforeAdapt() invoked");
    }

    public B adapt(A in) {
        if (log.isTraceEnabled())
            log.trace("adapt() invoked");
        B out = null;
        beforeAdapt(in);
        if (log.isTraceEnabled())
            log.trace("onAdapt() starting");
        try {
            out = onAdapt(in);
            onSuccess(in, out);
        } catch (Exception t) {
            try {
                onFailure(in, t);
            } catch (Exception e) {
                log.error("on failure() invoked");
                e.printStackTrace();
            }
        }
        if (log.isTraceEnabled())
            log.trace("adapt() finishing ");
        afterAdapt(in, out);
        return out;
    }
}