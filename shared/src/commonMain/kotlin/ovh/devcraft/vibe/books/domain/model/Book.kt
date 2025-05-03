package ovh.devcraft.vibe.books.domain.model

/**
 * Represents the core Book entity in the domain layer.
 * Contains essential information about a book.
 */
data class Book(
    val isbn: String, // Assuming ISBN-13 or ISBN-10 used for lookup
    val title: String?,
    val authors: List<String>?,
    val publisher: String?,
    val publishedDate: String?,
    val description: String?, // May not always be available from simple lookups
    val pageCount: Int?,
    val categories: List<String>?,
    val thumbnailUrl: String?, // URL to a thumbnail image
    val infoLink: String? // URL for more details (e.g., on the source website)
)

