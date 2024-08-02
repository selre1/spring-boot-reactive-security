package com.bhb.reactivesecurity;

import org.springframework.data.repository.CrudRepository;
import reactor.core.publisher.Mono;

public interface UserRepository extends CrudRepository<User,String> {
    Mono<User> findByName(String name);
}
