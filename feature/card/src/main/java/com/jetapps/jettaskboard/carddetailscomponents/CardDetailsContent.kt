package com.example.trelloclonelist.carddetailscomponents

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.outlined.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jetapps.jettaskboard.feature.card.R
import com.squaredem.composecalendar.ComposeCalendar
import java.time.LocalDate

@Composable
fun CardDetailsContent(scrollState: ScrollState) {

    val configuration = LocalConfiguration.current

    Column(modifier = Modifier.verticalScroll(scrollState)) {
        Text(
            modifier = Modifier.padding(16.dp),
            text = "BackLog",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = "Praxis in list Backlog",
            fontSize = 16.sp,
        )
        Spacer(modifier = Modifier.height(16.dp))

        Divider()

        QuickActionsCard()

        Divider()

        EditTextCard()

        LabelRow()

        ItemRow(leadingIcon = Icons.Outlined.Person, text = "Members...")


        val showCalendar = rememberSaveable { mutableStateOf(false) }
        val isTopText = rememberSaveable { mutableStateOf(false) }
        val isBottomText = rememberSaveable { mutableStateOf(false) }

        val topText = rememberSaveable {
            mutableStateOf("Start Date...")
        }

        val bottomText = rememberSaveable {
            mutableStateOf("Due Date...")
        }

        TimeItemRow(
            modifier = Modifier,
            icon = R.drawable.ic_time,
            topText = topText.value,
            bottomText = bottomText.value,
            onTopTextClick = {
                showCalendar.value = !showCalendar.value
                isTopText.value = true
                isBottomText.value = false
            },
            onBottomTextClick = {
                showCalendar.value = !showCalendar.value
                isBottomText.value = true
                isTopText.value = false
            }
        ) {
            if (showCalendar.value) {
                ComposeCalendar(
                    onDone = { it: LocalDate ->
                        // Hide dialog
                        showCalendar.value = false
                        // Do something with the date
                        if (isTopText.value) topText.value = "Starts on ${it.dayOfMonth} ${
                            (it.month).toString().lowercase()
                        }, ${it.year}"
                        if (isBottomText.value) bottomText.value = "Due on ${it.dayOfMonth} ${
                            (it.month).toString().lowercase()
                        }, ${it.year}"
                    },
                    onDismiss = {
                        // Hide dialog
                        showCalendar.value = false
                    }
                )
            }
        }

        ItemRow(
            leadingIcon = R.drawable.ic_attachment,
            text = "ATTACHMENTS",
            trailingIcon = Icons.Default.Add
        )

        Divider()

        when (configuration.orientation) {
            Configuration.ORIENTATION_PORTRAIT -> {
                Spacer(modifier = Modifier.height(600.dp))
            }
            else -> {

            }
        }

    }
}

@Composable
fun LabelRow() {
    var isClicked by remember { mutableStateOf(false) }
    val labels = remember {
        mutableStateListOf(
            LabelColor(Color(0x89ABE3FF)),
            LabelColor(Color(0xADEFD1FF)),
            LabelColor(Color(0x80203FFF)),
            LabelColor(Color(0xFFD662FF)),
            LabelColor(Color(0xE69A8DFF)),
        )
    }

    val selectedColors = remember {
        mutableStateListOf<Color>()
    }

    val rotate = animateFloatAsState(targetValue = if (isClicked) 180f else 0f, tween(500))

    Column {
        Divider()

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { isClicked = !isClicked },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.padding(16.dp),
                painter = painterResource(id = R.drawable.ic_label),
                contentDescription = "Leading Icon"
            )
            if (selectedColors.isEmpty()) {
                Text(text = "Labels...", modifier = Modifier.weight(5f))
            } else {
                Row(modifier = Modifier.weight(5f)) {
                    selectedColors.forEach {
                        Card(
                            modifier = Modifier
                                .height(36.dp)
                                .width(48.dp)
                                .padding(4.dp),
                            backgroundColor = it,
                            shape = RoundedCornerShape(8.dp)
                        ) {}
                    }

                }
            }
            Icon(
                modifier = Modifier
                    .padding(16.dp)
                    .weight(1f)
                    .rotate(rotate.value),
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = "Trailing Icon"
            )
        }

        AnimatedVisibility(visible = isClicked) {
            Column {
                labels.forEach { labelColor ->
                    LabelCheckBox(
                        color = labelColor.color,
                        isSelected = labelColor.isSelected,
                        onClick = {
                            labelColor.isSelected = !labelColor.isSelected
                            if (labelColor.isSelected && !selectedColors.contains(labelColor.color)) {
                                selectedColors.add(labelColor.color)
                            } else if (!labelColor.isSelected) {
                                selectedColors.remove(labelColor.color)
                            }
                        }
                    )
                }
            }
        }
    }
}

