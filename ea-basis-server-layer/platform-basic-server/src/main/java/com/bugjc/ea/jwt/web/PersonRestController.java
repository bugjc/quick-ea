package com.bugjc.ea.auth.web;

import com.bugjc.ea.http.opensdk.core.dto.Result;
import com.bugjc.ea.http.opensdk.core.dto.ResultGenerator;
import com.bugjc.ea.auth.model.Person;
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
    public static Result getPerson(@PathVariable("name") String name) {
        Person person = persons.stream()
                .filter(p -> name.equalsIgnoreCase(p.getName()))
                .findAny().orElse(null);
        return ResultGenerator.genSuccessResult(person);
    }
}
