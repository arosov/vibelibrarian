package ovh.devcraft.vibe.books.domain.repository

import ovh.devcraft.vibe.books.domain.model.Book
import kotlin.Result

/**
 * Interface defining the contract for accessing book data.
 * The implementation will handle fetching data from remote or local sources.
 */
interface BookRepository {
    /** Fetches book details based on its ISBN. Returns a Result wrapping the Book or an error. */
    suspend fun getBookByIsbn(isbn: String): Result<Book>
}
