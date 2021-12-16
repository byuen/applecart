package org.byuen.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Person {
    private Integer id;
    private String first;
    private String last;
    private String phone;
    private List<Experience> experience  = new ArrayList<>();

    private List<Contact> contacts = new ArrayList<>();

}
