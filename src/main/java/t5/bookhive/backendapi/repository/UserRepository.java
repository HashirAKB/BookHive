package t5.bookhive.backendapi.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import t5.bookhive.backendapi.entity.User;

import java.util.Collection;

public interface UserRepository extends JpaRepository<User, String> {
    User findByEmail(String email);
    Collection<User> findAllByRole(String role);

}
