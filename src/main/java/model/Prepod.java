package model;

import lombok.Data;

@Data
public class Prepod {

    private int id;
    private String name;
    private int experience;
    private int lesson_id;//(Один препод ведет один предмет)


    public Prepod() {
    }
}
