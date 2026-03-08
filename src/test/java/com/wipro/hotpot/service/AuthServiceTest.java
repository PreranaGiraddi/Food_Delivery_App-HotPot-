package com.wipro.hotpot.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.wipro.hotpot.config.JwtUtil;
import com.wipro.hotpot.dto.LoginRequest;
import com.wipro.hotpot.dto.LoginResponse;
import com.wipro.hotpot.dto.RegisterRequest;
import com.wipro.hotpot.entity.User;
import com.wipro.hotpot.repository.IUserRepository;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    // ─── Mocks ────────────────────────────────────────────────────────────────

    @Mock
    private IUserRepository userRepository;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthServiceImpl authService;

    // ─── Test Data ───────────────────────────────────────────────────────────

    private RegisterRequest registerRequest;
    private User savedUser;

    @BeforeEach
    public void setUp() {

        // A standard registration request (no role = defaults to USER)
        registerRequest = new RegisterRequest();
        registerRequest.setName("Thushara S");
        registerRequest.setEmail("thusharasatheesh1@gmail.com");
        registerRequest.setPassword("thushara");
        registerRequest.setContactNumber("9900251959");
        registerRequest.setAddress("bhavani nagar hubli");
        registerRequest.setGender("Male");

        // What the repository returns after save
        savedUser = new User();
        savedUser.setId(1L);
        savedUser.setName("Thushara S");
        savedUser.setEmail("thusharasatheesh1@gmail.com");
        savedUser.setPassword("encodedPassword");
        savedUser.setRole(User.Role.USER);
        savedUser.setActive(true);
    }

    // =========================================================================
    // REGISTER TESTS
    // =========================================================================

    /**
     * Happy path: new email, no role in request → defaults to USER.
     * NOTE: BCryptPasswordEncoder is a final field in AuthServiceImpl (not @Autowired),
     * so we cannot mock it. We just verify the user is saved with the right fields.
     */
    @Test
    public void testRegisterUser_Success_DefaultRole() {
        when(userRepository.isEmailExists(registerRequest.getEmail())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        User result = authService.registerUser(registerRequest);

        assertNotNull(result);
        assertEquals("Thushara S", result.getName());
        assertEquals("thusharasatheesh1@gmail.com", result.getEmail());
        assertEquals(User.Role.USER, result.getRole());   // no role sent → defaults to USER
        assertTrue(result.isActive());

        verify(userRepository, times(1)).isEmailExists(registerRequest.getEmail());
        verify(userRepository, times(1)).save(any(User.class));

        System.out.println("✅ testRegisterUser_Success_DefaultRole passed");
    }

    
    @Test
    public void testRegisterUser_WithRole_Restaurant() {
        registerRequest.setRole("ROLE_RESTAURANT");   // what admin-users.html sends

        User restaurantUser = new User();
        restaurantUser.setId(2L);
        restaurantUser.setName("Thushara S");
        restaurantUser.setEmail("thusharasatheesh1@gmail.com");
        restaurantUser.setPassword("encodedPassword");
        restaurantUser.setRole(User.Role.RESTAURANT);  // role must be saved as RESTAURANT
        restaurantUser.setActive(true);

        when(userRepository.isEmailExists(registerRequest.getEmail())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(restaurantUser);

        User result = authService.registerUser(registerRequest);

        assertNotNull(result);
        assertEquals(User.Role.RESTAURANT, result.getRole());  // ← key assertion

        verify(userRepository, times(1)).save(any(User.class));
        System.out.println("✅ testRegisterUser_WithRole_Restaurant passed");
    }

    
    @Test
    public void testRegisterUser_WithRole_PlainRestaurant() {
        registerRequest.setRole("RESTAURANT");

        User restaurantUser = new User();
        restaurantUser.setId(3L);
        restaurantUser.setName("Thushara S");
        restaurantUser.setEmail("thusharasatheesh1@gmail.com");
        restaurantUser.setPassword("encodedPassword");
        restaurantUser.setRole(User.Role.RESTAURANT);
        restaurantUser.setActive(true);

        when(userRepository.isEmailExists(registerRequest.getEmail())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(restaurantUser);

        User result = authService.registerUser(registerRequest);

        assertEquals(User.Role.RESTAURANT, result.getRole());
        System.out.println("✅ testRegisterUser_WithRole_PlainRestaurant passed");
    }

    /**
     * Duplicate email → RuntimeException, user is never saved.
     * Error message format: "Email already registered: <email>"
     */
    @Test
    public void testRegisterUser_EmailAlreadyExists() {
        when(userRepository.isEmailExists(registerRequest.getEmail())).thenReturn(true);

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
            authService.registerUser(registerRequest)
        );

        // Match the exact message in AuthServiceImpl
        assertTrue(ex.getMessage().contains("Email already registered"),
            "Expected message to contain 'Email already registered' but got: " + ex.getMessage());

        verify(userRepository, never()).save(any(User.class));
        System.out.println("✅ testRegisterUser_EmailAlreadyExists passed");
    }

    
    @Test
    public void testRegisterUser_InvalidRole() {
        registerRequest.setRole("SUPERUSER");

        when(userRepository.isEmailExists(registerRequest.getEmail())).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
            authService.registerUser(registerRequest)
        );

        assertTrue(ex.getMessage().contains("Invalid role"),
            "Expected 'Invalid role' in message but got: " + ex.getMessage());

        verify(userRepository, never()).save(any(User.class));
        System.out.println("✅ testRegisterUser_InvalidRole passed");
    }

    
    @Test
    public void testLoginUser_Success() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("thusharasatheesh1@gmail.com");
        loginRequest.setPassword("thushara");

        
        BCryptPasswordEncoder realEncoder = new BCryptPasswordEncoder();
        savedUser.setPassword(realEncoder.encode("thushara"));

        when(userRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.of(savedUser));
        when(jwtUtil.generateToken(savedUser.getEmail(), "USER")).thenReturn("mock.jwt.token");

        LoginResponse response = authService.loginUser(loginRequest);

        assertNotNull(response);
        assertEquals("mock.jwt.token", response.getToken());
        assertEquals("ROLE_USER", response.getRole());
        assertEquals(savedUser.getId(), response.getId());
        assertEquals("Login successful!", response.getMessage());

        verify(jwtUtil, times(1)).generateToken(savedUser.getEmail(), "USER");
        System.out.println("✅ testLoginUser_Success passed");
    }

   
    @Test
    public void testLoginUser_EmailNotFound() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("unknown@gmail.com");
        loginRequest.setPassword("anypass");

        when(userRepository.findByEmail("unknown@gmail.com")).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
            authService.loginUser(loginRequest)
        );

        assertTrue(ex.getMessage().contains("No account found"),
            "Expected 'No account found' but got: " + ex.getMessage());

        System.out.println("✅ testLoginUser_EmailNotFound passed");
    }

   
    @Test
    public void testLoginUser_WrongPassword() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("thusharasatheesh1@gmail.com");
        loginRequest.setPassword("wrongpassword");

        BCryptPasswordEncoder realEncoder = new BCryptPasswordEncoder();
        savedUser.setPassword(realEncoder.encode("thushara")); // real password is "thushara"

        when(userRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.of(savedUser));

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
            authService.loginUser(loginRequest)
        );

        assertEquals("Incorrect password!", ex.getMessage());
        System.out.println("✅ testLoginUser_WrongPassword passed");
    }

   
    @Test
    public void testLoginUser_AccountDeactivated() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("thusharasatheesh1@gmail.com");
        loginRequest.setPassword("thushara");

        savedUser.setActive(false); 

        when(userRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.of(savedUser));

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
            authService.loginUser(loginRequest)
        );

        assertTrue(ex.getMessage().contains("deactivated"),
            "Expected 'deactivated' in message but got: " + ex.getMessage());

        System.out.println("✅ testLoginUser_AccountDeactivated passed");
    }

   
    @Test
    public void testGetUserById_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(savedUser));

        User result = authService.getUserById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Thushara S", result.getName());

        System.out.println("✅ testGetUserById_Success passed");
    }

    @Test
    public void testGetUserById_NotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
            authService.getUserById(99L)
        );

        
        assertTrue(ex.getMessage().contains("User not found"),
            "Expected 'User not found' but got: " + ex.getMessage());

        System.out.println("✅ testGetUserById_NotFound passed");
    }

    
    @Test
    public void testGetAllUsers_ReturnsList() {
        User user2 = new User();
        user2.setId(2L);
        user2.setName("Admin User");
        user2.setEmail("admin@hotpot.com");
        user2.setRole(User.Role.ADMIN);
        user2.setActive(true);

        when(userRepository.findAll()).thenReturn(Arrays.asList(savedUser, user2));

        List<User> result = authService.getAllUsers();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Thushara S", result.get(0).getName());
        assertEquals(User.Role.ADMIN, result.get(1).getRole());

        System.out.println("✅ testGetAllUsers_ReturnsList passed");
    }


    @Test
    public void testDeleteUser_Success() {
        when(userRepository.existsById(1L)).thenReturn(true);
        doNothing().when(userRepository).deleteById(1L);

        assertDoesNotThrow(() -> authService.deleteUser(1L));

        verify(userRepository, times(1)).deleteById(1L);
        System.out.println("✅ testDeleteUser_Success passed");
    }

    @Test
    public void testDeleteUser_NotFound() {
        when(userRepository.existsById(99L)).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
            authService.deleteUser(99L)
        );

        assertTrue(ex.getMessage().contains("User not found"),
            "Expected 'User not found' but got: " + ex.getMessage());

        verify(userRepository, never()).deleteById(any());
        System.out.println("✅ testDeleteUser_NotFound passed");
    }

    
    @Test
    public void testUpdateUser_Success() {
        RegisterRequest updateRequest = new RegisterRequest();
        updateRequest.setName("Thushara Updated");
        updateRequest.setContactNumber("9999999999");
        updateRequest.setRole("ROLE_RESTAURANT");   // promote to restaurant owner

        User updatedUser = new User();
        updatedUser.setId(1L);
        updatedUser.setName("Thushara Updated");
        updatedUser.setEmail("thusharasatheesh1@gmail.com");
        updatedUser.setRole(User.Role.RESTAURANT);
        updatedUser.setActive(true);

        when(userRepository.findById(1L)).thenReturn(Optional.of(savedUser));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        User result = authService.updateUser(1L, updateRequest);

        assertNotNull(result);
        assertEquals("Thushara Updated", result.getName());
        assertEquals(User.Role.RESTAURANT, result.getRole());

        verify(userRepository, times(1)).save(any(User.class));
        System.out.println("✅ testUpdateUser_Success passed");
    }

   

    @Test
    public void testIsEmailExists_True() {
        when(userRepository.isEmailExists("thusharasatheesh1@gmail.com")).thenReturn(true);
        assertTrue(authService.isEmailExists("thusharasatheesh1@gmail.com"));
        System.out.println("testIsEmailExists_True passed");
    }

    @Test
    public void testIsEmailExists_False() {
        when(userRepository.isEmailExists("new@hotpot.com")).thenReturn(false);
        assertFalse(authService.isEmailExists("new@hotpot.com"));
        System.out.println(" testIsEmailExists_False passed");
    }
}