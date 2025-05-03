package ovh.devcraft.vibe.books.data.repository

import io.ktor.client.request.*
import io.ktor.http.*
import ovh.devcraft.vibe.books.data.remote.KtorClient
import ovh.devcraft.vibe.books.data.remote.dto.OpenLibraryBookDto
import ovh.devcraft.vibe.books.data.remote.dto.OpenLibrarySearchResponse
import ovh.devcraft.vibe.books.domain.model.Book
import ovh.devcraft.vibe.books.domain.repository.BookRepository
import io.ktor.client.call.body // Extension function for body deserialization
import kotlin.Result // Ensure using kotlin.Result

/**
 * Implementation of BookRepository using the Open Library API via Ktor.
 */
class BookRepositoryImpl : BookRepository {

    private val client = KtorClient.instance
    private val baseUrl = "https://openlibrary.org" // Base URL for Open Library

    override suspend fun getBookByIsbn(isbn: String): Result<Book> {
        val bibKey = "ISBN:$isbn"
        return try { // Using runCatching is often cleaner for wrapping external calls
            val response: OpenLibrarySearchResponse = client.get(baseUrl) {
                url {
                    appendPathSegments("api", "books") // Build path safely
                    parameters.append("bibkeys", bibKey)
                    parameters.append("format", "json")
                    parameters.append("jscmd", "data") // Request detailed data
                }
            }.body() // Deserialize the JSON response body

            // The response is a map like {"ISBN:12345": { book details }}
            val bookDto = response[bibKey]

            if (bookDto != null) {
                // Map DTO to Domain model
                val book = mapDtoToDomain(isbn, bookDto)
                Result.success(book)
            } else {
                // ISBN not found by the API
                Result.failure(NoSuchElementException("Book with ISBN $isbn not found on Open Library."))
            }
        } catch (e: Exception) {
            // Handle network errors, serialization errors, etc.
            // TODO: Replace println with a proper logging framework
            println("Error fetching book data: ${e.message}") // Replace with proper logging
            Result.failure(e)
        }
    }

    // Mapper function to convert DTO to Domain model
    private fun mapDtoToDomain(isbn: String, dto: OpenLibraryBookDto): Book {
        return Book(
            isbn = isbn, // Use the original ISBN passed to the function
            title = dto.title,
            authors = dto.authors?.mapNotNull { it.name }, // Extract author names
            publisher = dto.publishers?.mapNotNull { it.name }?.joinToString(), // Combine publishers
            publishedDate = dto.publishDate,
            description = null, // Open Library basic API doesn't provide description here
            pageCount = dto.numberOfPages,
            categories = dto.subjects?.mapNotNull { it.name }, // Extract subject names
            thumbnailUrl = dto.cover?.medium ?: dto.cover?.small ?: dto.cover?.large, // Prefer medium cover
            infoLink = dto.infoUrl
        )
    }
}
