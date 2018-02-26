package model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@EqualsAndHashCode
@ToString
public class Group {
    private @Getter @Setter int id;
    private @Getter @Setter String name;

    public Group() {
    }

    public Group(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
