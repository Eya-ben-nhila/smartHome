package com.smarthome.ui.auth

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.smarthome.data.model.LoginResponse
import com.smarthome.data.model.User
import com.smarthome.data.model.UserPreferences
import com.smarthome.data.model.Subscription
import com.smarthome.data.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.junit.Assert.*

@ExperimentalCoroutinesApi
class AuthViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var authRepository: AuthRepository

    private lateinit var authViewModel: AuthViewModel

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        authViewModel = AuthViewModel(authRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `login with valid credentials should succeed`() = runTest {
        // Given
        val email = "test@example.com"
        val password = "password123"
        val mockUser = User(
            id = "user123",
            name = "Test User",
            email = email,
            phone = null,
            avatar = null,
            createdAt = "2024-01-01T00:00:00Z",
            lastLogin = "2024-01-15T10:00:00Z",
            preferences = UserPreferences(),
            subscription = Subscription()
        )
        val mockResponse = LoginResponse(
            success = true,
            token = "mock_token",
            user = mockUser,
            message = "Login successful",
            expiresAt = "2024-01-16T10:00:00Z"
        )

        `when`(authRepository.login(email, password)).thenReturn(Result.success(mockResponse))

        // When
        authViewModel.login(email, password)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        verify(authRepository).login(email, password)
        assertEquals(LoginState.Success(mockResponse), authViewModel.loginState.value)
        assertEquals(mockUser, authViewModel.currentUser.value)
        assertFalse(authViewModel.isLoading.value)
        assertNull(authViewModel.errorMessage.value)
    }

    @Test
    fun `login with invalid credentials should fail`() = runTest {
        // Given
        val email = "invalid@example.com"
        val password = "wrongpassword"
        val errorMessage = "Invalid credentials"

        `when`(authRepository.login(email, password)).thenReturn(Result.failure(Exception(errorMessage)))

        // When
        authViewModel.login(email, password)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        verify(authRepository).login(email, password)
        assertTrue(authViewModel.loginState.value is LoginState.Error)
        assertEquals(errorMessage, authViewModel.errorMessage.value)
        assertFalse(authViewModel.isLoading.value)
        assertNull(authViewModel.currentUser.value)
    }

    @Test
    fun `login with empty email should show validation error`() = runTest {
        // Given
        val email = ""
        val password = "password123"

        // When
        authViewModel.login(email, password)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertEquals("Email cannot be empty", authViewModel.errorMessage.value)
        assertFalse(authViewModel.isLoading.value)
    }

    @Test
    fun `login with invalid email format should show validation error`() = runTest {
        // Given
        val email = "invalid-email"
        val password = "password123"

        // When
        authViewModel.login(email, password)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertEquals("Please enter a valid email", authViewModel.errorMessage.value)
        assertFalse(authViewModel.isLoading.value)
    }

    @Test
    fun `login with short password should show validation error`() = runTest {
        // Given
        val email = "test@example.com"
        val password = "123"

        // When
        authViewModel.login(email, password)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertEquals("Password must be at least 6 characters", authViewModel.errorMessage.value)
        assertFalse(authViewModel.isLoading.value)
    }

    @Test
    fun `logout should clear user data`() = runTest {
        // Given
        `when`(authRepository.logout()).thenReturn(Result.success(Unit))

        // When
        authViewModel.logout()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        verify(authRepository).logout()
        assertNull(authViewModel.currentUser.value)
        assertEquals(LoginState.Idle, authViewModel.loginState.value)
    }

    @Test
    fun `refreshUser should update current user`() = runTest {
        // Given
        val mockUser = User(
            id = "user123",
            name = "Updated User",
            email = "test@example.com",
            phone = null,
            avatar = null,
            createdAt = "2024-01-01T00:00:00Z",
            lastLogin = "2024-01-15T10:00:00Z",
            preferences = UserPreferences(),
            subscription = Subscription()
        )

        `when`(authRepository.getCurrentUser()).thenReturn(Result.success(mockUser))

        // When
        authViewModel.refreshUser()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        verify(authRepository).getCurrentUser()
        assertEquals(mockUser, authViewModel.currentUser.value)
    }

    @Test
    fun `clearError should remove error message`() = runTest {
        // Given
        authViewModel.login("", "password") // This will set an error
        testDispatcher.scheduler.advanceUntilIdle()
        assertNotNull(authViewModel.errorMessage.value)

        // When
        authViewModel.clearError()

        // Then
        assertNull(authViewModel.errorMessage.value)
    }
}
