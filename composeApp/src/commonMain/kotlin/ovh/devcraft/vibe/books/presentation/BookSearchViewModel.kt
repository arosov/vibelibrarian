package ovh.devcraft.vibe.books.presentation

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ovh.devcraft.vibe.books.domain.usecase.GetBookDetailsUseCase

/**
 * ViewModel responsible for managing the state and logic of the book search screen.
 *
 * @param getBookDetailsUseCase The use case for fetching book details.
 */
class BookSearchViewModel(
    private val getBookDetailsUseCase: GetBookDetailsUseCase
) {
    // Create a CoroutineScope for launching background tasks.
    // SupervisorJob ensures that if one child coroutine fails, others are not cancelled.
    // Dispatchers.Default is suitable for CPU-bound work, but IO might be better for network calls.
    // Consider injecting the scope or using a dedicated lifecycle-aware scope library later.
    private val viewModelScope = CoroutineScope(SupervisorJob() + Dispatchers.IO) // Use IO dispatcher for network

    private val _uiState = MutableStateFlow(BookSearchState())
    val uiState: StateFlow<BookSearchState> = _uiState.asStateFlow()

    /**
     * Updates the ISBN input text in the state.
     */
    fun onIsbnInputChange(newIsbn: String) {
        _uiState.update { it.copy(isbnInput = newIsbn, errorMessage = null) } // Clear error on new input
    }

    /**
     * Initiates the search for the book based on the current ISBN input.
     */
    fun searchBook() {
        val isbn = _uiState.value.isbnInput.trim()
        if (isbn.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Please enter an ISBN.") }
            return
        }

        _uiState.update { it.copy(isLoading = true, errorMessage = null, book = null) }

        viewModelScope.launch {
            val result = getBookDetailsUseCase(isbn)
            result.fold(
                onSuccess = { book ->
                    _uiState.update {
                        it.copy(isLoading = false, book = book, errorMessage = null)
                    }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            book = null,
                            errorMessage = "Error: ${error.message ?: "Unknown error"}"
                        )
                    }
                }
            )
        }
    }

    // Optional: Add a function to clear the scope when the ViewModel is no longer needed.
    // This is important in Android, less critical in Desktop/Wasm but good practice.
    // fun clear() {
    //     viewModelScope.cancel() // Cancel all coroutines started by this scope
    // }
}
