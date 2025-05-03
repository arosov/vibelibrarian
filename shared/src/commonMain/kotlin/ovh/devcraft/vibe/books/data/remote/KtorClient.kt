package ovh.devcraft.vibe.books.data.remote

import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

/**
 * Provides a pre-configured Ktor HttpClient instance for network requests.
 */
object KtorClient {
    val instance: HttpClient by lazy {
        HttpClient { // Use platform-specific engine automatically
            // Configure JSON serialization
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true // Be lenient about unknown keys, etc.
                    ignoreUnknownKeys = true // Ignore keys in JSON not present in DTOs
                })
            }

            // Configure logging (optional, useful for debugging)
            install(Logging) {
                logger = Logger.DEFAULT // Simple logger to stdout/logcat
                level = LogLevel.INFO // Log headers, body, etc. Adjust level as needed (ALL, HEADERS, BODY, INFO, NONE)
            }

            // Default request configuration (e.g., base URL, headers) can be added here
            // install(DefaultRequest) {
            //     header(HttpHeaders.ContentType, ContentType.Application.Json)
            // }
        }
    }
}
