package screens

import com.example.eventtrackingapp.ViewModel.UserViewModel
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.eventtrackingapp.sendSms
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class Event(
    var title: String,
    var note: String = "",
    var date: String = "",
    var time: String = "",
    var isDone: Boolean = false,
    var isCancelled: Boolean = false
)

@Composable
fun EventListScreen(navController: NavHostController, userViewModel: UserViewModel) {
    val pendingEvents = remember { mutableStateListOf<Event>() }
    val completedEvents = remember { mutableStateListOf<Event>() }
    val cancelledEvents = remember { mutableStateListOf<Event>() }
    var newEventTitle by remember { mutableStateOf("") }
    var newEventNote by remember { mutableStateOf("") }
    var newEventDate by remember { mutableStateOf("") }
    var newEventTime by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var isEditing by remember { mutableStateOf(false) }
    var editingEventIndex by remember { mutableStateOf(-1) }
    var feedbackMessage by remember { mutableStateOf("") }
    var showPendingEvents by remember { mutableStateOf(true) }
    var showCompletedEvents by remember { mutableStateOf(true) }
    var showCancelledEvents by remember { mutableStateOf(true) }

    val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            if (feedbackMessage.isNotEmpty()) {
                Text(
                    text = feedbackMessage,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
            TextField(
                value = newEventTitle,
                onValueChange = { newEventTitle = it },
                label = { Text("Event Title") },
                modifier = Modifier.fillMaxWidth()
            )
            TextField(
                value = newEventNote,
                onValueChange = { newEventNote = it },
                label = { Text("Event Note") },
                modifier = Modifier.fillMaxWidth()
            )
            TextField(
                value = newEventDate,
                onValueChange = { newEventDate = it },
                label = { Text("Event Date (yyyy-MM-dd)") },
                modifier = Modifier.fillMaxWidth()
            )
            TextField(
                value = newEventTime,
                onValueChange = { newEventTime = it },
                label = { Text("Event Time (HH:mm)") },
                modifier = Modifier.fillMaxWidth()
            )
            TextField(
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                label = { Text("Phone Number") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {
                    if (newEventTitle.isNotBlank()) {
                        val event = Event(
                            title = newEventTitle,
                            note = newEventNote,
                            date = newEventDate,
                            time = newEventTime
                        )
                        if (isEditing && editingEventIndex != -1) {
                            if (event.isDone) {
                                completedEvents[editingEventIndex] = event
                            } else {
                                pendingEvents[editingEventIndex] = event
                            }
                            feedbackMessage = "Event updated: ${LocalDateTime.now().format(dateTimeFormatter)}"
                            sendSms(context, phoneNumber, "Event Updated: $newEventTitle")
                            isEditing = false
                            editingEventIndex = -1
                        } else {
                            pendingEvents.add(event)
                            feedbackMessage = "Event added: ${LocalDateTime.now().format(dateTimeFormatter)}"
                            sendSms(context, phoneNumber, "New Event Added: $newEventTitle")
                        }
                        newEventTitle = ""
                        newEventNote = ""
                        newEventDate = ""
                        newEventTime = ""
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (isEditing) "Update Event" else "Add Event")
            }
            Spacer(modifier = Modifier.height(16.dp))

            ToggleSection(
                title = "Pending Events",
                showSection = showPendingEvents,
                onToggle = { showPendingEvents = !showPendingEvents }
            )
            if (showPendingEvents) {
                EventGrid(
                    events = pendingEvents,
                    onEdit = { event, index ->
                        newEventTitle = event.title
                        newEventNote = event.note
                        newEventDate = event.date
                        newEventTime = event.time
                        isEditing = true
                        editingEventIndex = index
                    },
                    onDelete = { event, index ->
                        pendingEvents.removeAt(index)
                        feedbackMessage = "Event deleted: ${LocalDateTime.now().format(dateTimeFormatter)}"
                        sendSms(context, phoneNumber, "Event Deleted: ${event.title}")
                    },
                    onToggleDone = { event, index ->
                        event.isDone = !event.isDone
                        if (event.isDone) {
                            pendingEvents.removeAt(index)
                            completedEvents.add(event)
                            feedbackMessage = "Event marked as done: ${LocalDateTime.now().format(dateTimeFormatter)}"
                            sendSms(context, phoneNumber, "Event Completed: ${event.title}")
                        } else {
                            completedEvents.removeAt(index)
                            pendingEvents.add(event)
                            feedbackMessage = "Event marked as pending: ${LocalDateTime.now().format(dateTimeFormatter)}"
                            sendSms(context, phoneNumber, "Event Pending: ${event.title}")
                        }
                    },
                    onToggleCancelled = { event, index ->
                        event.isCancelled = !event.isCancelled
                        if (event.isCancelled) {
                            pendingEvents.removeAt(index)
                            cancelledEvents.add(event)
                            feedbackMessage = "Event cancelled: ${LocalDateTime.now().format(dateTimeFormatter)}"
                            sendSms(context, phoneNumber, "Event Cancelled: ${event.title}")
                        } else {
                            cancelledEvents.removeAt(index)
                            pendingEvents.add(event)
                            feedbackMessage = "Event reactivated: ${LocalDateTime.now().format(dateTimeFormatter)}"
                            sendSms(context, phoneNumber, "Event Reactivated: ${event.title}")
                        }
                    },
                    context = context
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            ToggleSection(
                title = "Completed Events",
                showSection = showCompletedEvents,
                onToggle = { showCompletedEvents = !showCompletedEvents }
            )
            if (showCompletedEvents) {
                EventGrid(
                    events = completedEvents,
                    onEdit = { _, _ -> /* No editing for completed events */ },
                    onDelete = { event, index ->
                        completedEvents.removeAt(index)
                        feedbackMessage = "Event deleted: ${LocalDateTime.now().format(dateTimeFormatter)}"
                        sendSms(context, phoneNumber, "Event Deleted: ${event.title}")
                    },
                    onToggleDone = { event, index ->
                        event.isDone = !event.isDone
                        if (!event.isDone) {
                            completedEvents.removeAt(index)
                            pendingEvents.add(event)
                            feedbackMessage = "Event marked as pending: ${LocalDateTime.now().format(dateTimeFormatter)}"
                            sendSms(context, phoneNumber, "Event Pending: ${event.title}")
                        }
                    },
                    onToggleCancelled = { _, _ -> /* No cancelling for completed events */ },
                    context = context
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            ToggleSection(
                title = "Cancelled Events",
                showSection = showCancelledEvents,
                onToggle = { showCancelledEvents = !showCancelledEvents }
            )
            if (showCancelledEvents) {
                EventGrid(
                    events = cancelledEvents,
                    onEdit = { _, _ -> /* No editing for cancelled events */ },
                    onDelete = { event, index ->
                        cancelledEvents.removeAt(index)
                        feedbackMessage = "Event deleted: ${LocalDateTime.now().format(dateTimeFormatter)}"
                        sendSms(context, phoneNumber, "Event Deleted: ${event.title}")
                    },
                    onToggleDone = { _, _ -> /* No toggling done for cancelled events */ },
                    onToggleCancelled = { event, index ->
                        event.isCancelled = !event.isCancelled
                        if (!event.isCancelled) {
                            cancelledEvents.removeAt(index)
                            pendingEvents.add(event)
                            feedbackMessage = "Event reactivated: ${LocalDateTime.now().format(dateTimeFormatter)}"
                            sendSms(context, phoneNumber, "Event Reactivated: ${event.title}")
                        }
                    },
                    context = context
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                userViewModel.logout()
                navController.navigate("login") {
                    popUpTo(navController.graph.startDestinationId) {
                        inclusive = true
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Logout")
        }
    }
}

@Composable
fun EventGrid(
    events: List<Event>,
    onEdit: (Event, Int) -> Unit,
    onDelete: (Event, Int) -> Unit,
    onToggleDone: (Event, Int) -> Unit,
    onToggleCancelled: (Event, Int) -> Unit,
    context: Context
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 128.dp),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(events.size) { index ->
            val event = events[index]
            EventGridItem(
                event,
                onEdit = { onEdit(event, index) },
                onDelete = { onDelete(event, index) },
                onToggleDone = { onToggleDone(event, index) },
                onToggleCancelled = { onToggleCancelled(event, index) },
                context = context
            )
        }
    }
}

@Composable
fun EventGridItem(
    event: Event,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onToggleDone: () -> Unit,
    onToggleCancelled: () -> Unit,
    context: Context
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable { expanded = !expanded },  // Toggle expanded state on click
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp)  // Consistent padding for the entire card
        ) {
            // Always show event title
            Text(text = event.title, style = MaterialTheme.typography.titleLarge)

            // Show additional details and options if expanded
            if (expanded) {
                Spacer(modifier = Modifier.height(8.dp))

                // Show event details
                Text(text = "Date: ${event.date}", style = MaterialTheme.typography.bodySmall)
                Text(text = "Time: ${event.time}", style = MaterialTheme.typography.bodySmall)
                Text(text = "Note: ${event.note}", style = MaterialTheme.typography.bodySmall)

                Spacer(modifier = Modifier.height(8.dp))

                // Show action buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (!event.isDone && !event.isCancelled) {
                        IconButton(
                            onClick = onEdit,
                            modifier = Modifier.size(20.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit"
                            )
                        }
                    }
                    IconButton(
                        onClick = onDelete,
                        modifier = Modifier.size(20.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete"
                        )
                    }
                    IconButton(
                        onClick = onToggleDone,
                        modifier = Modifier.size(20.dp)
                    ) {
                        Icon(
                            imageVector = if (event.isDone) Icons.Default.CheckCircle else Icons.Outlined.CheckCircle,
                            contentDescription = if (event.isDone) "Mark as not done" else "Mark as done",
                            tint = if (event.isDone) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                        )
                    }
                    if (!event.isDone) {
                        IconButton(
                            onClick = onToggleCancelled,
                            modifier = Modifier.size(20.dp)
                        ) {
                            Icon(
                                imageVector = if (event.isCancelled) Icons.Default.Cancel else Icons.Outlined.Cancel,
                                contentDescription = if (event.isCancelled) "Mark as not cancelled" else "Mark as cancelled",
                                tint = if (event.isCancelled) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                    IconButton(
                        onClick = { shareEvent(event, context) },
                        modifier = Modifier.size(20.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share"
                        )
                    }
                }
            }
        }
    }
}





@Composable
fun ToggleSection(
    title: String,
    showSection: Boolean,
    onToggle: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onToggle() },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = title, style = MaterialTheme.typography.titleLarge)
        Icon(
            imageVector = if (showSection) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
            contentDescription = if (showSection) "Hide $title" else "Show $title"
        )
    }
}

fun shareEvent(event: Event, context: Context) {
    val shareIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, "Event: ${event.title}\nNote: ${event.note}\nDate: ${event.date}\nTime: ${event.time}")
        type = "text/plain"
    }
    context.startActivity(Intent.createChooser(shareIntent, null))
    Toast.makeText(context, "Sharing event...", Toast.LENGTH_SHORT).show()
}
