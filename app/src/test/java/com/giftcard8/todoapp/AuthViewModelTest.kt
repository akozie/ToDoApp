package com.giftcard8.todoapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.giftcard8.todoapp.db.user.UserEntity
import com.giftcard8.todoapp.repository.UserRepository
import com.giftcard8.todoapp.utils.UserPreferences
import com.giftcard8.todoapp.viewmodel.AuthViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class AuthViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var viewModel: AuthViewModel
    private lateinit var authRepository: UserRepository
    private lateinit var userPreferences: UserPreferences

    @Before
    fun setup() {
        // Override Main dispatcher for unit tests
        Dispatchers.setMain(testDispatcher)

        authRepository = mock()
        userPreferences = mock()
        viewModel = AuthViewModel(authRepository, userPreferences)
    }

    @After
    fun tearDown() {
        // Reset Main dispatcher to original
        Dispatchers.resetMain()
    }

    @Test
    fun `login success sets loginResult true`() =
        runTest(testDispatcher) {
            whenever(authRepository.loginUser("user", "pass")).thenReturn(true)
            whenever(authRepository.getUser("user", "pass")).thenReturn(
                UserEntity(id = 1, username = "user", password = "1234"),
            )

            viewModel.login("user", "pass")

            testDispatcher.scheduler.advanceUntilIdle()

            assertEquals(true, viewModel.loginResult.getOrAwaitValue())
            assertNull(viewModel.errorMessage.value)
        }

    @Test
    fun `login failure sets error message`() =
        runTest {
            whenever(authRepository.loginUser("wrong", "pass")).thenReturn(false)

            viewModel.login("wrong", "pass")

            testDispatcher.scheduler.advanceUntilIdle()

            assertEquals(false, viewModel.loginResult.getOrAwaitValue())
            assertEquals("Invalid credentials", viewModel.errorMessage.getOrAwaitValue())
        }

    @Test
    fun `register success sets registrationResult true`() =
        runTest(testDispatcher) {
            // Mock checkUserExists returns false (user does not exist)
            whenever(authRepository.checkUserExists("newuser")).thenReturn(false)
            // Mock registerUser returns true (registration succeeds)
            whenever(authRepository.registerUser("newuser", "pass")).thenReturn(true)
            // Mock getUser returns a non-null user
            whenever(authRepository.getUser("newuser", "pass")).thenReturn(
                UserEntity(id = 1, username = "newuser", password = "1234"), // replace User with your data class
            )

            viewModel.register("newuser", "pass")

            testDispatcher.scheduler.advanceUntilIdle()

            assertEquals(true, viewModel.registrationResult.getOrAwaitValue())
            assertNull(viewModel.errorMessage.value)
        }

    @Test
    fun `register failure when user exists`() =
        runTest(testDispatcher) {
            whenever(authRepository.checkUserExists("existing")).thenReturn(true)

            viewModel.register("existing", "pass")

            testDispatcher.scheduler.advanceUntilIdle()

            assertEquals(false, viewModel.registrationResult.getOrAwaitValue())
            assertEquals("Username already taken", viewModel.errorMessage.getOrAwaitValue())
        }
}
