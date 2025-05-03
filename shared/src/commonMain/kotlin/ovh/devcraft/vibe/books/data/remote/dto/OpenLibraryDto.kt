package ovh.devcraft.vibe.books.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// Using Map because the top-level key is dynamic (e.g., "ISBN:9780140328721")
typealias OpenLibrarySearchResponse = Map<String, OpenLibraryBookDto>

@Serializable
data class OpenLibraryBookDto(
    val title: String? = null,
    val authors: List<AuthorDto>? = null,
    val publishers: List<PublisherDto>? = null,
    @SerialName("publish_date")
    val publishDate: String? = null,
    @SerialName("number_of_pages")
    val numberOfPages: Int? = null,
    val subjects: List<SubjectDto>? = null, // Categories/Subjects
    val cover: CoverDto? = null,
    @SerialName("url")
    val infoUrl: String? = null, // Link to OpenLibrary page
    // We don't get the description directly from this endpoint, might need another call or different API if required
    // val description: String? = null
)

@Serializable
data class AuthorDto(
    val name: String? = null
    // val url: String? = null // URL to author page if needed
)

@Serializable
data class PublisherDto(
    val name: String? = null
)

@Serializable
data class SubjectDto(
    val name: String? = null
    // val url: String? = null // URL to subject page if needed
)

@Serializable
data class CoverDto(
    val small: String? = null,
    val medium: String? = null,
    val large: String? = null
)
