package com.yevhensuturin.rest.webservices.restfulcoursemanagement.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserJPARepository extends JpaRepository<User, Long> {
    void deleteById(Long id);
}
