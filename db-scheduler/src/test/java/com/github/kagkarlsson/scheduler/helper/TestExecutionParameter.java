package com.github.kagkarlsson.scheduler.helper;

import java.io.Serializable;


public class TestExecutionParameter implements Serializable {
    private String testStr;

    public TestExecutionParameter(String testStr) {
        this.testStr = testStr;
    }

    public TestExecutionParameter() {

    }

    public String getTestStr() {
        return testStr;
    }

}
