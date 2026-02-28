package com.wipro.hotpot.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.wipro.hotpot.entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.wipro.hotpot.dto.RegisterRequest;
import com.wipro.hotpot.repository.IUserRepository;
@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private IUserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    
    @InjectMocks
    private AuthServiceImpl authService;

 
    private RegisterRequest registerRequest;
    private User savedUser;

    
    @BeforeEach
    public void setUp() {

     
        registerRequest = new RegisterRequest();
        registerRequest.setName("John Doe");
        registerRequest.setEmail("john@gmail.com");
        registerRequest.setPassword("password123");
        registerRequest.setContactNumber("9876543210");
        registerRequest.setAddress("Chennai");
        registerRequest.setGender("Male");

        
        savedUser = new User();
        savedUser.setId(1L);
        savedUser.setName("John Doe");
        savedUser.setEmail("john@gmail.com");
        savedUser.setPassword("encodedPassword");
        savedUser.setRole(User.Role.USER);
        savedUser.setActive(true);
    }

   
    @Test
    public void testRegisterUser_Success() {

        
        when(userRepository.isEmailExists(registerRequest.getEmail()))
                .thenReturn(false);
  when(passwordEncoder.encode(registerRequest.getPassword()))
                .thenReturn("encodedPassword");

       when(userRepository.save(any(User.class)))
                .thenReturn(savedUser);

        
        User result = authService.registerUser(registerRequest);

       
        assertNotNull(result);                              // result should not be null
        assertEquals("John Doe", result.getName());        // name should match
        assertEquals("john@gmail.com", result.getEmail()); // email should match
        assertEquals(User.Role.USER, result.getRole());    // role should be USER
        assertTrue(result.isActive());                     // should be active

        
        verify(userRepository, times(1)).save(any(User.class));

        System.out.println("✅ Register User Test Passed!");
    }


    @Test
    public void testRegisterUser_EmailAlreadyExists() {

       
        when(userRepository.isEmailExists(registerRequest.getEmail()))
                .thenReturn(true);

       
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authService.registerUser(registerRequest);
        });

       
        assertEquals("Email already registered!", exception.getMessage());

      
        verify(userRepository, never()).save(any(User.class));

        System.out.println("✅ Duplicate Email Test Passed!");
    }

   
    @Test
    public void testGetUserById_Success() {

     
        when(userRepository.findById(1L))
                .thenReturn(java.util.Optional.of(savedUser));

       
        User result = authService.getUserById(1L);

        
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("John Doe", result.getName());

        System.out.println("✅ Get User By Id Test Passed!");
    }

 
    @Test
    public void testGetUserById_NotFound() {

       
        when(userRepository.findById(99L))
                .thenReturn(java.util.Optional.empty());

        
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authService.getUserById(99L);
        });

        assertEquals("User not found with id: 99", exception.getMessage());

        System.out.println("✅ User Not Found Test Passed!");
    }
}
