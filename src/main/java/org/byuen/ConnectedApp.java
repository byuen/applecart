package org.byuen;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.commons.lang3.StringUtils;
import org.byuen.model.Contact;
import org.byuen.model.Experience;
import org.byuen.model.Person;
import org.byuen.model.Phone;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class ConnectedApp {

    private static final long DAYS_CONNECTED_THRESHOLD = 180;

    public static void main(String[] args) throws IOException {

        List<String> argsList = Arrays.asList(args);

        String personFileName = (argsList.size() > 0) ? args[0] : "persons.json";
        String contactsFileName = (argsList.size() > 1) ? args[1] : "contacts.json";
        Integer personId = (argsList.size() > 2) ? Integer.parseInt(args[2]) : 1;

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        File file = new File(personFileName);
        Person[] persons = mapper.readValue(file, Person[].class);

        File contactFile = new File(contactsFileName);
        List<Contact> contacts = mapper.readValue(contactFile, new TypeReference<List<Contact>>() {
        });

        Map<Integer, Person> personMap = IntStream.range(0, persons.length).boxed().collect(Collectors.toMap(i -> persons[i].getId(), i -> persons[i]));

        for (Contact c : contacts) {
            Person p = personMap.get(c.getOwner_id());
            if (p != null) {
                p.getContacts().add(c);
            }
        }

        List<Person> connectedPeople = getConnected(personMap.get(personId), persons);

        for (Person connectedPerson : connectedPeople) {
            System.out.printf("%s %s %n", connectedPerson.getFirst(), connectedPerson.getLast());
        }
    }

    private static List<Person> getConnected(Person person1, Person[] persons) {
        ArrayList<Person> connectedList = new ArrayList<>();
        for (Person person2 : persons) {
            boolean connected = false;
            if (!person1.getId().equals(person2.getId())) {
                if (isConnectedByExperience(person1, person2)) {
                    connectedList.add(person2);
                    connected = true;
                }
                if (!connected) {
                    if (isConnectedByContact(person1, person2)) {
                        connectedList.add(person2);
                    }
                }
            }
        }
        return connectedList;
    }


    public static boolean isConnectedByExperience(Person person1, Person person2) {
        for (Experience e1 : person1.getExperience()) {
            for (Experience e2 : person2.getExperience()) {
                if (e1.getCompany().equalsIgnoreCase(e2.getCompany())) {

                    if (e1.getEnd() == null) {
                        e1.setEnd(LocalDate.now());
                    }

                    if (e2.getEnd() == null) {
                        e2.setEnd(LocalDate.now());
                    }

                    if (e1.getStart().isBefore(e2.getEnd()) && e2.getStart().isBefore(e1.getEnd())) {
                        long overlapDays;
                        if (e1.getStart().isBefore(e2.getStart())) {
                            overlapDays = ChronoUnit.DAYS.between(e2.getStart(), e1.getEnd());
                        } else {
                            overlapDays = ChronoUnit.DAYS.between(e1.getStart(), e2.getEnd());
                        }
                        if (overlapDays > DAYS_CONNECTED_THRESHOLD) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }


    public static boolean isConnectedByContact(Person person1, Person person2) {
        if (isInContactList(person1, person2)) {
            return true;
        }

        if (isInContactList(person2, person1)) {
            return true;
        }

        return false;
    }

    private static boolean isInContactList(Person person1, Person person2) {
        if (person1.getPhone() != null) {
            String person1Phone = formatPhone(person1.getPhone());

            for (Contact contact2 : person2.getContacts()) {
                for (Phone phone2 : contact2.getPhone()) {
                    String p2 = formatPhone(phone2.getNumber());
                    if (person1Phone.equals(p2)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    private static String formatPhone(String phone) {
        if (phone == null) {
            return null;
        }
        return StringUtils.stripStart(phone.replaceAll("[^\\d]", ""), "1");
    }
}
