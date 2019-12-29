package com.example.demo;


import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
public class PersonController {

    private final StringRedisTemplate customerTemplate;

    private final PersonRepository personRepository;

    @GetMapping(value = "/test")
    @Transactional
    public ResponseEntity<?> testRedisTransaction(@RequestParam String name) {
        Person save = personRepository.save(new Person(null, name));
        customerTemplate.opsForValue().set(save.getId().toString(), save.toString());
        String s = customerTemplate.opsForValue().get(save.getId().toString());
        return ResponseEntity.ok(Objects.requireNonNull(s));
    }

    @GetMapping(value = "/test/v2")
    @Transactional
    public ResponseEntity<?> testRedisTransactionv2(@RequestParam String name) {
        Person save = personRepository.save(new Person(null, name));
        customerTemplate.getRequiredConnectionFactory().getConnection().set(save.getId().toString().getBytes(StandardCharsets.UTF_8),
                save.toString().getBytes(StandardCharsets.UTF_8));
        byte[] s = customerTemplate.getRequiredConnectionFactory().getConnection().get(save.getId().toString().getBytes(StandardCharsets.UTF_8));
        return ResponseEntity.ok(new String(s, StandardCharsets.UTF_8));
    }
}
