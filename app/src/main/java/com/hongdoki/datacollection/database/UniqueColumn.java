package com.hongdoki.datacollection.database;

public class UniqueColumn extends Column{
    public UniqueColumn(String name, String type) {
        super(name, type);
    }

    @Override
    public String toString() {
        return super.toString() + " unique";
    }
}
