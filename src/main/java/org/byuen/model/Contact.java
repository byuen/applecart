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
public class Contact {
    private Integer id;
    private Integer owner_id;
    private String contact_nickname;
    private List<Phone> phone  = new ArrayList<>();
}
