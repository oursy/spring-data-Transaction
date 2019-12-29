Spring Data Redis TransactionSupport

Easy to ignore issues:


**RedisConfig**
``` java
    StringRedisTemplate customerTemplate(RedisConnectionFactory redisConnectionFactory) {
        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate(redisConnectionFactory);
        stringRedisTemplate.setEnableTransactionSupport(true);
        return stringRedisTemplate;
    }
```

RedisTransactionSupportTest 
``` java

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

```
always `/test` interface return null，other `/test/v2` interface return person;




.....


[https://github.com/spring-projects/spring-data-redis/blob/master/src/main/asciidoc/reference/redis-transactions.adoc#transactional-support](https://github.com/spring-projects/spring-data-redis/blob/master/src/main/asciidoc/reference/redis-transactions.adoc#transactional-support)

[https://stackoverflow.com/questions/47707628/spring-data-redis-when-within-a-transaction-get-operations-would-return-null](https://stackoverflow.com/questions/47707628/spring-data-redis-when-within-a-transaction-get-operations-would-return-null)
                                                                                                                

