package com.bugjc.ea.jwt.web;

import com.bugjc.ea.jwt.model.Person;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class PersonRestController {

    private static final List<Person> persons;
    static {
        persons = new ArrayList<>();
        persons.add(new Person("Hello", "World"));
        persons.add(new Person("Foo", "Bar"));
    }

    @GetMapping(path = "/persons")
    public static List<Person> getPersons() {
        return persons;
    }

    @GetMapping(path = "/persons/{name}")
    public static Person getPerson(@PathVariable("name") String name) {
        return persons.stream()
                .filter(person -> name.equalsIgnoreCase(person.getName()))
                .findAny().orElse(null);
    }
}
