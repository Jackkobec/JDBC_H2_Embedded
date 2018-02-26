package model;

import lombok.Data;

/**
 * Created by Jack on 12.11.2016.
 */
@Data
public class Prepod {

    private int id;
    private String name;
    private int experience;
    private int lesson_id;//(Один препод ведет один предмет)


    public Prepod() {
    }
}
