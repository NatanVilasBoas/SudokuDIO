package com.sudoku.model;

import java.util.List;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class Board {
    private final List<List<Field>> fields;

    public Board(List<List<Field>> fields) {
        this.fields = fields;
    }

    public List<List<Field>> getFields() {
        return fields;
    }

    public GameStatus getStatus() {
        if (fields.stream().flatMap(f -> f.stream())
                .noneMatch(field -> !field.isFixed() && nonNull(field.getActual()))) {
            return GameStatus.NON_INITIAZE;
        }

        return fields.stream().flatMap(f -> f.stream())
                .anyMatch((field -> isNull(field.getActual()))) ? GameStatus.INCOMPLETE : GameStatus.COMPLETE;
    }

    public boolean hasErrors() {
        if (getStatus() == GameStatus.NON_INITIAZE) {
            return false;
        }

        return fields.stream().flatMap(f -> f.stream())
                .anyMatch(field -> nonNull(field.getActual()) && !field.getActual().equals(field.getExpected()));
    }

    public boolean changeValue(final int col, final int row, final int value){
        Field field = fields.get(col).get(row);
        if(field.isFixed()){
            return false;
        }

        field.setActual(value);
        return true;
    }

    public boolean clearField(final int col, final int row){
        Field field = fields.get(col).get(row);
        if(field.isFixed()){
            return false;
        }

        field.clearField();
        return true;
    }

    public void reset(){
        fields.forEach(c -> c.forEach(f -> f.clearField()));
    }

    public boolean gameIsFinished(){
        return !hasErrors() && getStatus().equals(GameStatus.COMPLETE);
    }

}
