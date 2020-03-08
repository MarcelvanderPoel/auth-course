package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {

    private UserController userController;

    // Mocks

    private UserRepository userRepository = mock(UserRepository.class);
    private CartRepository cartRepository = mock(CartRepository.class);
    private BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setup() {

        userController = new UserController();

        //UserController class expects three @Autowired objects that TestUtils will inject

        TestUtils.injectObjects(userController,"userRepository", userRepository);
        TestUtils.injectObjects(userController,"cartRepository", cartRepository);
        TestUtils.injectObjects(userController,"bCryptPasswordEncoder", encoder);

    }

    @Test
    public void create_user_happy_path() throws Exception {

        // Stub

        when(encoder.encode("testPassword")).thenReturn("thisIsHashed");

        // Given

        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("test");
        r.setPassword("testPassword");
        r.setConfirmPassword("testPassword");

        // When

        final ResponseEntity<User> response = userController.createUser(r);

        // Then

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User u = response.getBody();
        assertNotNull(u);
        assertEquals(0,u.getId());
        assertEquals("test", u.getUsername());
        assertEquals("thisIsHashed", u.getPassword());
    }

    @Test
    public void password_too_short_returns_bad_request() throws Exception {

        // Given

        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("test2");
        r.setPassword("Short");
        r.setConfirmPassword("Short");

        // When

        final ResponseEntity<User> response = userController.createUser(r);

        // Then

        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    public void confirmed_password_not_correct_returns_bad_request() throws Exception {

        // Given

        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("test");
        r.setPassword("testPassword");
        r.setConfirmPassword("testxPassword");

        // When

        final ResponseEntity<User> response = userController.createUser(r);

        // Then

        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    public void get_user_by_name_happy_flow() throws Exception {

        // Stub

        when(encoder.encode("testPassword")).thenReturn("thisIsHashed");

        // Given

        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("test");
        r.setPassword("testPassword");
        r.setConfirmPassword("testPassword");

        // When

        final ResponseEntity<User> response = userController.createUser(r);

        // Then

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User u = response.getBody();

        when(userRepository.findByUsername("test")).thenReturn(u);

        final ResponseEntity<User> getResponse = userController.findByUserName("test");
        assertNotNull(getResponse);
        assertEquals(200, getResponse.getStatusCodeValue());

    }

    @Test
    public void get_user_by_name_that_does_not_exist() throws Exception {

        // Stub

        when(encoder.encode("testPassword")).thenReturn("thisIsHashed");

        // Given

        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("test");
        r.setPassword("testPassword");
        r.setConfirmPassword("testPassword");

        // When

        final ResponseEntity<User> response = userController.createUser(r);

        // Then

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User u = response.getBody();

        when(userRepository.findByUsername("test")).thenReturn(u);

        final ResponseEntity<User> getResponse = userController.findByUserName("nonExistent");
        assertNotNull(getResponse);
        assertEquals(404, getResponse.getStatusCodeValue());

    }

    @Test
    public void get_user_by_id_happy_flow() throws Exception {

        // Stub

        when(encoder.encode("testPassword")).thenReturn("thisIsHashed");

        // Given

        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("test");
        r.setPassword("testPassword");
        r.setConfirmPassword("testPassword");

        // When

        final ResponseEntity<User> response = userController.createUser(r);

        // Then

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User u = response.getBody();

        when(userRepository.findById(0L)).thenReturn(java.util.Optional.ofNullable(u));

        final ResponseEntity<User> getResponse = userController.findById(0L);
        assertNotNull(getResponse);
        assertEquals(200, getResponse.getStatusCodeValue());

    }

    @Test
    public void get_user_by_id_that_does_not_exist() throws Exception {

        // Stub

        when(encoder.encode("testPassword")).thenReturn("thisIsHashed");

        // Given

        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("test");
        r.setPassword("testPassword");
        r.setConfirmPassword("testPassword");

        // When

        final ResponseEntity<User> response = userController.createUser(r);

        // Then

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User u = response.getBody();

        when(userRepository.findById(0L)).thenReturn(java.util.Optional.ofNullable(u));

        final ResponseEntity<User> getResponse = userController.findById(1L);
        assertNotNull(getResponse);
        assertEquals(404, getResponse.getStatusCodeValue());

    }



}
