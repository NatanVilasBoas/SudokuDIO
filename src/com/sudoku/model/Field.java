package com.sudoku.model;

public class Field {
    private Integer actual;
    private final int expected;
    private final boolean fixed;

    public Field(int expected, boolean fixed) {
        this.expected = expected;
        this.fixed = fixed;
        if(fixed){
            actual = expected;
        }
    }

    public Integer getActual() {
        return actual;
    }

    public void setActual(Integer actual) {
        if(fixed) return;
        this.actual = actual;
    }

    public int getExpected() {
        return expected;
    }

    public boolean isFixed() {
        return fixed;
    }

    public void clearField(){
        setActual(null);
    }
}
