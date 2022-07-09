@file:OptIn(ExperimentalMaterial3Api::class)

package com.federicopeyrani.repose.main

import android.icu.text.SimpleDateFormat
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.federicopeyrani.repose.main.MainViewModel.PermissionStatus.Denied
import com.federicopeyrani.repose.sleep.data.SleepClassifyEntity
import java.util.Date

@Composable
fun Main(
    viewModel: MainViewModel = viewModel(),
    onGrantActivityRecognitionClick: () -> Unit,
    onGrantNotificationPolicyAccessClick: () -> Unit,
) {
    val lastSleepClassifyEntity by viewModel.lastSleepClassify.collectAsState()

    Scaffold(topBar = {
        LargeTopAppBar(title = { Text("Sleep") })
    }, content = { innerPadding ->
        LazyColumn(
            contentPadding = innerPadding,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            if (viewModel.isActivityRecognitionPermissionGranted == Denied) {
                item(key = "activityRecognitionPermission") {
                    MissingPermissionCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(),
                        title = "Grant access to sleep data",
                        onGrantButtonClick = onGrantActivityRecognitionClick
                    )
                }
            }

            if (viewModel.isNotificationPolicyAccessGranted == Denied) {
                item(key = "notificationPolicyAccess") {
                    MissingPermissionCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(),
                        title = "Grant permission to set DND",
                        onGrantButtonClick = onGrantNotificationPolicyAccessClick
                    )
                }
            }

            lastSleepClassifyEntity?.let {
                item(key = "lastSleepClassify") {
                    SleepConfidenceCard(
                        sleepClassifyEntity = it,
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                    )
                }
            }
        }
    })
}

@Composable
fun MissingPermissionCard(
    modifier: Modifier = Modifier,
    title: String,
    onGrantButtonClick: () -> Unit = {},
    content: @Composable () -> Unit = {}
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer,
            contentColor = MaterialTheme.colorScheme.onErrorContainer
        ),
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            MissingPermissionCardHeader(
                title = title
            )
            content()
            MissingPermissionCardActions(
                modifier = Modifier.fillMaxWidth(), onGrantButtonClick = onGrantButtonClick
            )
        }
    }
}

@Composable
fun MissingPermissionCardHeader(
    title: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.error,
            contentColor = MaterialTheme.colorScheme.onError,
        ) {
            Icon(
                imageVector = Icons.Rounded.Warning,
                contentDescription = "Warning",
                modifier = Modifier.padding(8.dp)
            )
        }
        Text(
            text = title, style = MaterialTheme.typography.titleMedium
        )
    }
}

@Composable
fun MissingPermissionCardActions(
    modifier: Modifier = Modifier,
    onGrantButtonClick: () -> Unit
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.End,
    ) {
        Button(
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error,
                contentColor = MaterialTheme.colorScheme.onError
            ),
            onClick = onGrantButtonClick
        ) {
            Text(text = "Grant")
        }
    }
}

@Composable
fun SleepConfidenceCard(
    sleepClassifyEntity: SleepClassifyEntity,
    modifier: Modifier
) {
    Card(
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = "Sleep confidence", style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "${sleepClassifyEntity.confidence}%",
                style = MaterialTheme.typography.displayMedium
            )
            val time = SimpleDateFormat.getDateTimeInstance().format(
                Date(sleepClassifyEntity.timestampMillis)
            )
            Text(
                text = "At $time", style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
