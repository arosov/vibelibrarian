package ovh.devcraft.vibe.books.presentation

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers // Import Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow // Import asStateFlow
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
    // SupervisorJob prevents failure of one child from cancelling the scope.
    private val viewModelScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private val _uiState = MutableStateFlow(BookSearchState())
    val uiState: StateFlow<BookSearchState> = _uiState.asStateFlow()

    /**
     * Updates the ISBN input text in the state.
     */
    fun onIsbnInputChange(newIsbn: String) {
        _uiState.update { it.copy(isbnInput = newIsbn, errorMessage = null, book = null) } // Clear error and previous book on new input
    }

    /**
     * Initiates the search for the book based on the current ISBN input.
     */
    fun searchBook() {
        val isbn = _uiState.value.isbnInput.trim()
        if (isbn.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Please enter an ISBN.", isLoading = false) }
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
    // In a real app with lifecycle management (e.g., using moko-mvvm), this would be handled automatically.
    // fun clear() {
    //     viewModelScope.cancel() // Cancel all coroutines started by this scope
    // }
}
