package com.example.dg.common.exception;

import com.example.dg.common.exception.base.BaseException;

public class DeletionNotAllowedException extends BaseException {

    public DeletionNotAllowedException(String msg) {
        super(msg);
    }

}
