package ovh.devcraft.vibe.books

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button // Explicit imports
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview


// Simple manual DI - instantiate dependencies here
// In a larger app, use a DI framework like Koin or Kodein
private fun createViewModel(): BookSearchViewModel {
    val bookRepository = BookRepositoryImpl()
    val getBookDetailsUseCase = GetBookDetailsUseCase(bookRepository) // Corrected instantiation
    return BookSearchViewModel(getBookDetailsUseCase)
}

@Composable
@Preview
fun App(viewModel: BookSearchViewModel = remember { createViewModel() }) {
    // Import necessary classes within the composable scope if not already imported
    // import ovh.devcraft.vibe.books.data.repository.BookRepositoryImpl
    // import ovh.devcraft.vibe.books.domain.usecase.GetBookDetailsUseCase
    // import ovh.devcraft.vibe.books.presentation.BookSearchViewModel

    MaterialTheme {
        val state by viewModel.uiState.collectAsState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("ISBN Book Finder", style = MaterialTheme.typography.h5)

            // ISBN Input Field
            OutlinedTextField(
                value = state.isbnInput,
                onValueChange = { viewModel.onIsbnInputChange(it) },
                label = { Text("Enter ISBN") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = state.errorMessage != null // Highlight field if any error exists
            )

            // Search Button
            Button(
                onClick = { viewModel.searchBook() },
                enabled = !state.isLoading, // Disable button while loading
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Search")
            }

            // Loading Indicator
            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.padding(top = 16.dp))
            }

            // Error Message Display
            state.errorMessage?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colors.error,
                    style = MaterialTheme.typography.caption,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            // Book Details Display
            state.book?.let { book ->
                BookDetailsView(book)
            }
        }
    }
}

@Composable
@Preview // Add preview for the details view
fun BookDetailsView(book: Book) {
    Card(modifier = Modifier.fillMaxWidth().padding(top = 16.dp), elevation = 4.dp) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text("Title: ${book.title ?: "N/A"}", style = MaterialTheme.typography.h6)
            Text("Authors: ${book.authors?.joinToString() ?: "N/A"}")
            Text("Publisher: ${book.publisher ?: "N/A"}")
            Text("Published Date: ${book.publishedDate ?: "N/A"}")
            Text("Pages: ${book.pageCount?.toString() ?: "N/A"}")
            Text("Categories: ${book.categories?.joinToString() ?: "N/A"}")
            // TODO: Add Image loading for book.thumbnailUrl
            book.infoLink?.let { link ->
                Text("More Info: $link") // Display link, could be made clickable later
            }
        }
    }
}
