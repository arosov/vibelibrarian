package ovh.devcraft.vibe.books.domain.usecase

import ovh.devcraft.vibe.books.domain.model.Book
import ovh.devcraft.vibe.books.domain.repository.BookRepository
import kotlin.Result

/**
 * Use case (Interactor) responsible for the business logic of fetching book details.
 * It orchestrates the interaction with the repository.
 */
class GetBookDetailsUseCase(private val bookRepository: BookRepository) {
    /** Executes the use case to get book details by ISBN. */
    suspend operator fun invoke(isbn: String): Result<Book> {
        return bookRepository.getBookByIsbn(isbn)
    }
}
