package model;

import lombok.Data;

/**
 * Created by Jack on 12.11.2016.
 */
@Data
public class Lesson {

    private int id;
    private String name;
    private String description;

    public Lesson() {
    }
}
