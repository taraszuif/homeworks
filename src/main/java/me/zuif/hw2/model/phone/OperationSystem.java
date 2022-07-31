package me.zuif.hw2.model.phone;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OperationSystem {
    private String designation;
    private int version;

    public OperationSystem(String designation, int version) {
        this.designation = designation;
        this.version = version;
    }
}
