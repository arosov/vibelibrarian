package ovh.devcraft.vibe.books.presentation

import ovh.devcraft.vibe.books.domain.model.Book // Import the domain model

/**
 * Represents the state of the Book Search UI.
 *
 * @param isbnInput The current text entered in the ISBN input field.
 * @param isLoading Whether a search operation is currently in progress.
 * @param book The successfully fetched book details, or null if no search succeeded yet or an error occurred.
 * @param errorMessage An error message to display, or null if no error occurred.
 */
data class BookSearchState(
    val isbnInput: String = "",
    val isLoading: Boolean = false,
    val book: Book? = null,
    val errorMessage: String? = null
)
