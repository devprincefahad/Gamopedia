package dev.prince.gamopedia.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.prince.gamopedia.util.GlowYellow
import dev.prince.gamopedia.viewmodels.GamesViewModel
import dev.prince.gamopedia.viewmodels.SearchViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun GamingSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = koinViewModel()
) {

    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier
            .fillMaxWidth(),
        singleLine = true,
        shape = RoundedCornerShape(14.dp),

        placeholder = {
            Text(
                text = "Type to search games...",
                color = Color.White.copy(alpha = 0.5f)
            )
        },

        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = GlowYellow
            )
        },

        trailingIcon = {
            if (query.isNotEmpty()) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Clear",
                    modifier = Modifier.clickable {
                        viewModel.clearSearch()
                    }
                )
            }
        },

        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,

            cursorColor = Color(0xFFCFFF4A),

            focusedBorderColor = Color(0xFFCFFF4A),
            unfocusedBorderColor = Color.White.copy(alpha = 0.3f),

            focusedContainerColor = Color(0xFF1C1C1C),
            unfocusedContainerColor = Color(0xFF1C1C1C)
        )
    )
}
