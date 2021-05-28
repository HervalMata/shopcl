package com.shopcl.shopclbackend.user;

import com.shopcl.common.entity.Role;
import com.shopcl.common.entity.User;
import com.shopcl.shopclbackend.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = true)
public class UserRepositoryTests {

    private UserRepository repo;
    private TestEntityManager entityManager;

    @Autowired
    public UserRepositoryTests(UserRepository repo, TestEntityManager entityManager) {
        this.repo = repo;
        this.entityManager = entityManager;
    }

    @Test
    public void testCreateNewUserWithOneRole() {
        Role roleAdmin = entityManager.find(Role.class, 1L);
        User userWithOneRole = new User("y@a.net", "ya2021", "Yagmur", "Aksac");
        userWithOneRole.addRole(roleAdmin);
        User savedUser = repo.save(userWithOneRole);
        assertThat(savedUser.getId()).isGreaterThan(0);
    }

    @Test
    public void testCreateNewUserWithTwoRoles() {
        User userWithTwoRole = new User("r@g.net", "rg2021", "Remzi", "Guloglu");
        Role roleEditor = new Role(3L);
        Role roleAssistant = new Role(5L);
        userWithTwoRole.addRole(roleEditor);
        userWithTwoRole.addRole(roleAssistant);
        User savedUser = repo.save(userWithTwoRole);
        assertThat(savedUser.getId()).isGreaterThan(0);
    }

    @Test
    public void testListAllUsers() {
        Iterable<User> listUsers = repo.findAll();
        listUsers.forEach(user -> System.out.println(user));
    }

    @Test
    public void testGetUserById() {
        User userById = repo.findById(1L).get();
        System.out.println(userById);
        assertThat(userById).isNotNull();
    }

    @Test
    public void testUpdateUserDetails() {
        User userUpdateUserDetails = repo.findById(1L).get();
        userUpdateUserDetails.setEnabled(true);
        userUpdateUserDetails.setEmail("ya@a.com");
        repo.save(userUpdateUserDetails);
    }

    @Test
    public void testUpdateUserRoles() {
        User userUpdateUserRoles = repo.findById(1L).get();
        Role roleEditor = new Role(3L);
        Role roleSalesperson = new Role(2L);
        userUpdateUserRoles.getRoles().remove(roleEditor);
        userUpdateUserRoles.addRole(roleSalesperson);
        repo.save(userUpdateUserRoles);
    }

    @Test
    public void testDeleteUser() {
        Long userDeleteUser = 1L;
        repo.deleteById(userDeleteUser);
    }

    @Test
    public void testGetUserByEmail() {
        String email = "ya@a.com";
        User userByEmail = repo.getUserByEmail(email);
        assertThat(userByEmail).isNotNull();
    }

    @Test
    public void testCountById() {
        Long id = 1L;
        Long countById = repo.countById(id);
        assertThat(countById).isNotNull().isGreaterThan(0);
    }

    @Test
    public void testEnabledUser() {
        Long id = 3L;
        repo.updateEnabledStatus(id, true);
    }

    @Test
    public void testDisabledUser() {
        Long id = 3L;
        repo.updateEnabledStatus(id, false);
    }
}
