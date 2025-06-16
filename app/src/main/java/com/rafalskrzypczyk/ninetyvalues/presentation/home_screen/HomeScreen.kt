package com.rafalskrzypczyk.ninetyvalues.presentation.home_screen

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.HistoryEdu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rafalskrzypczyk.ninetyvalues.R
import com.rafalskrzypczyk.ninetyvalues.ui.theme.NinetyValuesTheme
import com.rafalskrzypczyk.ninetyvalues.ui.theme.Typography
import com.rafalskrzypczyk.ninetyvalues.utils.toFormattedDate

@Composable
fun HomeScreen(
    state: HomeScreenState,
    onNavigateToEntriesList: () -> Unit,
    onNavigateToNewEntry: () -> Unit
) {


    Scaffold { innerPadding ->
        val modifier = Modifier.padding(innerPadding)

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(15.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(contentAlignment = Alignment.Center) {
                    Image(
                        painter = painterResource(R.drawable.icon_grayscale),
                        contentDescription = stringResource(R.string.app_name),
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .aspectRatio(1f)
                            .blur(12.dp)
                    )
                    Image(
                        painter = painterResource(R.drawable.icon_grayscale),
                        contentDescription = stringResource(R.string.app_name),
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .aspectRatio(1f)
                    )
                }
                Text(
                    text = stringResource(R.string.app_name_logo),
                    color = MaterialTheme.colorScheme.primary,
                    style = Typography.displayMedium,
                )
            }

            Column (
                modifier = Modifier.fillMaxWidth(0.6f),
                verticalArrangement = Arrangement.spacedBy(25.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                MainMenuButton(
                    text = stringResource(R.string.btn_start),
                    icon = Icons.Default.AutoAwesome,
                    onClick = onNavigateToNewEntry
                )
                MainMenuButton(
                    text = stringResource(R.string.btn_entries_history),
                    icon = Icons.Default.HistoryEdu,
                    onClick = onNavigateToEntriesList
                )
                state.lastEntryDate?.let { date ->
                    Row {
                        Text(
                            text = "${stringResource(R.string.last_entry_title)}: "
                        )
                        Text(
                            text = date.toFormattedDate(LocalContext.current),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MainMenuButton(
    text: String,
    icon: ImageVector? = null,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.onPrimary,
            contentColor = MaterialTheme.colorScheme.onBackground
        ),
        contentPadding = PaddingValues(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(12.dp),
                ambientColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f),
                spotColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f)
            )
    ) {
        Row (verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = text,
                style = Typography.titleLarge
            )
            icon?.let {
                Spacer(Modifier.width(8.dp))
                Icon(
                    imageVector = icon,
                    contentDescription = text
                )
            }
        }
    }
}

@Composable
@Preview(name = "Light Mode")
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Preview(name = "Full Preview", showSystemUi = true)
private fun PreviewHomeScreen() {
    NinetyValuesTheme {
        Surface {
            HomeScreen(
                state = HomeScreenState(
                    lastEntryDate = 294572948
                ),
                onNavigateToEntriesList = {},
                onNavigateToNewEntry = {},
            )
        }
    }
}