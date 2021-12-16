package org.byuen;

import org.byuen.model.Contact;
import org.byuen.model.Experience;
import org.byuen.model.Person;
import org.byuen.model.Phone;
import org.junit.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ConnectedAppTest {

    @Test
    public void givenOverlappingExperienceOver6Months_ThenReturnConnected() {
        //given
        Person p1 = new Person();
        Experience e1 = new Experience("Acme", "", LocalDate.of(2020, 1, 1), LocalDate.of(2021, 1, 1));
        p1.getExperience().add(e1);

        Person p2 = new Person();
        Experience e2 = new Experience("Acme", "", LocalDate.of(2020, 1, 1), LocalDate.of(2021, 1, 1));
        p2.getExperience().add(e2);
        //when
        boolean isConnected = ConnectedApp.isConnectedByExperience(p1, p2);

        //then
        assertTrue(isConnected);

        //given
        p1 = new Person();
        e1 = new Experience("Acme", "", LocalDate.of(2018, 1, 1), LocalDate.of(2020, 7, 1));
        p1.getExperience().add(e1);

        p2 = new Person();
        e2 = new Experience("Acme", "", LocalDate.of(2020, 1, 1), LocalDate.of(2021, 1, 1));
        p2.getExperience().add(e2);

        //when
        isConnected = ConnectedApp.isConnectedByExperience(p1, p2);

        //then
        assertTrue(isConnected);
    }

    @Test
    public void givenOverlappingExperienceLess6Months_ThenReturnNotConnected() {
        //given
        Person p1 = new Person();
        Experience e1 = new Experience("Acme", "", LocalDate.of(2020, 10, 1), LocalDate.of(2021, 1, 1));
        p1.getExperience().add(e1);

        Person p2 = new Person();
        Experience e2 = new Experience("Acme", "", LocalDate.of(2020, 1, 1), LocalDate.of(2021, 1, 1));
        p2.getExperience().add(e2);
        //when
        boolean isConnected = ConnectedApp.isConnectedByExperience(p1, p2);

        //then
        assertFalse(isConnected);
    }


    @Test
    public void givenInContactList_ThenReturnConnected() {
        //given
        Person p1 = new Person();
        p1.setPhone("917-555-1234");

        Person p2 = new Person();
        p2.setId(2);
        List<Phone> phoneList = new ArrayList<>();
        phoneList.add(new Phone("(917)555-1234", "cell"));
        Contact c2 = new Contact(1, 2, "nick", phoneList);
        p2.setContacts(Arrays.asList(c2));

        //when
        boolean isConnected = ConnectedApp.isConnectedByContact(p1, p2);

        //then
        assertTrue(isConnected);
    }
}
