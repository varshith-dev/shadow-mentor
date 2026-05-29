package com.example.ui

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ChatMessage
import com.example.data.GeneratedProject
import com.example.data.LearningNode
import com.example.data.AppDatabase
import com.example.ui.theme.*
import com.example.viewmodel.ShadowViewModel
import com.example.viewmodel.ApiState
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// --- Shared Core Top App Bar ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShadowTopBar(
    title: String = "Shadow Mentor",
    onMenuClick: () -> Unit = {},
    onNotificationsClick: () -> Unit = {}
) {
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.primary
        ),
        title = {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                letterSpacing = (-0.5).sp
            )
        },
        navigationIcon = {
            IconButton(onClick = onMenuClick) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Menu",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        actions = {
            IconButton(onClick = onNotificationsClick) {
                BadgedBox(
                    badge = {
                        Badge(containerColor = MaterialTheme.colorScheme.error)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Notifications",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    )
}

// --- Screen 1: Welcome & Landing Page (Tab: Learn) ---
@Composable
fun LearnScreen(
    viewModel: ShadowViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Welcome Header & Accent Gradients
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .drawBehind {
                    drawCircle(
                        color = Color(0x0A005BBF),
                        radius = 400.dp.toPx(),
                        center = Offset(-50.dp.toPx(), -50.dp.toPx())
                    )
                }
                .padding(horizontal = 20.dp, vertical = 24.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Meet Co-Pilot badge
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(100.dp))
                        .background(MaterialTheme.colorScheme.surface)
                        .border(
                            1.dp,
                            MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f),
                            RoundedCornerShape(100.dp)
                        )
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Meet your new co-pilot",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Heading
                Text(
                    text = "Your Personal",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    lineHeight = 38.sp,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "AI Mentor",
                    fontSize = 36.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary,
                    lineHeight = 42.sp,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "for Infinite Growth",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    lineHeight = 38.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Elevate your skills with intelligent, tailored guidance. Shadow Mentor adapts to your cognitive style.",
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    lineHeight = 22.sp,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )

                Spacer(modifier = Modifier.height(28.dp))

                // Start Learning Button (Navigates to Mentor tab)
                Button(
                    onClick = {
                        viewModel.selectedTab = "Mentor"
                        viewModel.mentorActiveSubTab = "Chat"
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .height(52.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "Start Learning Free",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Section 2: Interactive Chat Preview Snippet
        Card(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
                .clickable {
                    viewModel.selectedTab = "Mentor"
                    viewModel.mentorActiveSubTab = "Chat"
                },
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                // Header of preview
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Face,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "GEMMA-4 E2B",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF4CAF50))
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Active now",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Conversation Block
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    // User query
                    Row(
                        modifier = Modifier.align(Alignment.End),
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp, 16.dp, 2.dp, 16.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                .padding(horizontal = 14.dp, vertical = 10.dp)
                        ) {
                            Text(
                                text = "I'm struggling to grasp concept closures.",
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }

                    // Shadow AI Response with Psychology accent border
                    Row(
                        verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primary),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(12.dp)
                            )
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp, 16.dp, 16.dp, 2.dp))
                                .background(MaterialTheme.colorScheme.surface)
                                .border(
                                    1.dp,
                                    MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f),
                                    RoundedCornerShape(16.dp, 16.dp, 16.dp, 2.dp)
                                )
                                .drawBehind {
                                    // Primary Color left border strap
                                    drawLine(
                                        color = ShadowPrimary,
                                        start = Offset(0f, 0f),
                                        end = Offset(0f, size.height),
                                        strokeWidth = 3.dp.toPx()
                                    )
                                }
                                .padding(horizontal = 14.dp, vertical = 12.dp)
                        ) {
                            Text(
                                text = "Think of a closure like a backpack. A function gets to keep a 'backpack' of variables from its outer scope, even after that outer scope is done running. Shall we write a quick snippet?",
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurface,
                                lineHeight = 18.sp
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // --- GAME-INFUSED INTERACTIVE COURSE HUB ---
        Text(
            text = "Active Course Hub",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(10.dp))

        // satisfying real-time gaming statistics
        Card(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.outlineVariant)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Streak Card
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "🔥",
                        fontSize = 24.sp,
                        modifier = Modifier.clickable {
                            Toast.makeText(context, "Dopamine streak multiplier: ${viewModel.streakCount}x!", Toast.LENGTH_SHORT).show()
                        }
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Column {
                        Text(
                            text = "${viewModel.streakCount} Days",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "ACTIVE STREAK",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                }

                // Vertical Divider
                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .height(32.dp)
                        .background(MaterialTheme.colorScheme.outlineVariant)
                )

                // EXP Score Card
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "✨",
                        fontSize = 24.sp
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Column {
                        Text(
                            text = "${viewModel.userExperienceScore}",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFFC79200)
                        )
                        Text(
                            text = "TOTAL EXP (PTS)",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Horizontal Chips for Selecting Courses
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            viewModel.coursesList.forEach { course ->
                val isSelected = viewModel.selectedCourseId == course.id
                val containerColor = if (isSelected) {
                    MaterialTheme.colorScheme.primaryContainer
                } else {
                    MaterialTheme.colorScheme.surface
                }
                val contentColor = if (isSelected) {
                    MaterialTheme.colorScheme.onPrimaryContainer
                } else {
                    MaterialTheme.colorScheme.onSurface
                }
                val borderColor = if (isSelected) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.outlineVariant
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(100.dp))
                        .background(containerColor)
                        .border(1.dp, borderColor, RoundedCornerShape(100.dp))
                        .clickable { viewModel.selectedCourseId = course.id }
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = course.title,
                        fontSize = 13.sp,
                        fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Medium,
                        color = contentColor
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Selected Course Overview Box
        val activeCourse = viewModel.coursesList.find { it.id == viewModel.selectedCourseId }
        if (activeCourse != null) {
            val accentBorderColor = when (activeCourse.accentColor) {
                "orange" -> Color(0xFFF2994A)
                "teal" -> Color(0xFF03DAC6)
                else -> MaterialTheme.colorScheme.primary
            }

            Card(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.outlineVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = activeCourse.category.uppercase(),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = accentBorderColor,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "${activeCourse.chapters.count { it.isCompleted }}/${activeCourse.chapters.size} Chapters Complete",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = activeCourse.title,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = activeCourse.description,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 18.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

                    Spacer(modifier = Modifier.height(12.dp))

                    // Chapter List within course
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        activeCourse.chapters.forEach { chapter ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (chapter.isCompleted) {
                                        MaterialTheme.colorScheme.primary.copy(alpha = 0.04f)
                                    } else {
                                        MaterialTheme.colorScheme.background
                                    }
                                ),
                                border = BorderStroke(
                                    1.dp,
                                    if (chapter.isCompleted) {
                                        accentBorderColor.copy(alpha = 0.4f)
                                    } else {
                                        MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.7f)
                                    }
                                )
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                if (chapter.isCompleted) {
                                                    Icon(
                                                        imageVector = Icons.Default.Check,
                                                        contentDescription = "Completed indicator",
                                                        tint = accentBorderColor,
                                                        modifier = Modifier.size(16.dp)
                                                    )
                                                    Spacer(modifier = Modifier.width(6.dp))
                                                }
                                                Text(
                                                    text = chapter.title,
                                                    fontSize = 14.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = if (chapter.isCompleted) {
                                                        MaterialTheme.colorScheme.primary
                                                    } else {
                                                        MaterialTheme.colorScheme.onSurface
                                                    }
                                                )
                                            }
                                            Spacer(modifier = Modifier.height(2.dp))
                                            Text(
                                                text = chapter.subtitle,
                                                fontSize = 12.sp,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }

                                        Text(
                                            text = chapter.duration,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.outline
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(12.dp))

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        if (!chapter.isCompleted) {
                                            Button(
                                                onClick = {
                                                    viewModel.completeCourseChapter(activeCourse.id, chapter.id)
                                                },
                                                modifier = Modifier.weight(1f),
                                                colors = ButtonDefaults.buttonColors(containerColor = accentBorderColor),
                                                shape = RoundedCornerShape(8.dp),
                                                contentPadding = PaddingValues(vertical = 4.dp, horizontal = 8.dp)
                                            ) {
                                                Text("Mark Completed +150 XP", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                            }
                                        } else {
                                            Box(
                                                modifier = Modifier
                                                    .weight(1f)
                                                    .height(36.dp)
                                                    .clip(RoundedCornerShape(8.dp))
                                                    .background(accentBorderColor.copy(alpha = 0.15f)),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text(
                                                    text = "Completed! ✨ +150 EXP gained",
                                                    fontSize = 11.sp,
                                                    fontWeight = FontWeight.ExtraBold,
                                                    color = accentBorderColor
                                                )
                                            }
                                        }

                                        OutlinedButton(
                                            onClick = {
                                                viewModel.generateGemmaSummary(activeCourse.title, chapter.title)
                                            },
                                            shape = RoundedCornerShape(8.dp),
                                            modifier = Modifier.weight(1f),
                                            contentPadding = PaddingValues(vertical = 4.dp, horizontal = 8.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Lightbulb,
                                                contentDescription = "AI insights",
                                                modifier = Modifier.size(12.dp)
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text("Summarize (Gemma)", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // GEMMA QUICK BYTES INTERACTIVE SHELF
        Card(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)),
            border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.outlineVariant)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Lightbulb,
                        contentDescription = "Gemma model summary",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Gemma-4 Quick Bytes Summary Console",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                if (viewModel.isGeneratingQuickBytes) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        LinearProgressIndicator(
                            modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)),
                            color = MaterialTheme.colorScheme.primary,
                            trackColor = MaterialTheme.colorScheme.outlineVariant
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Gemma AI is processing chapter concepts...",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                } else {
                    Text(
                        text = viewModel.activeQuickBytes,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        lineHeight = 18.sp,
                        modifier = Modifier.padding(vertical = 6.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // Section 3: Engineered for Mastery Bento Grid Header
        Text(
            text = "Engineered for Mastery",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Bento grid cards
        Column(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Feature 1: Personalized Learning
            BentoFeatureCard(
                icon = Icons.Default.Tune,
                iconTint = MaterialTheme.colorScheme.primary,
                iconBgColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                title = "Personalized Learning",
                description = "Algorithms that adapt to your pace, identifying knowledge gaps dynamically."
            )

            // Feature 2: Skill Building
            BentoFeatureCard(
                icon = Icons.Default.Build,
                iconTint = MaterialTheme.colorScheme.secondary,
                iconBgColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f),
                title = "Skill Building",
                description = "Hands-on micro-assessments designed to solidify understanding via active recall."
            )

            // Feature 3: Career Growth
            BentoFeatureCard(
                icon = Icons.AutoMirrored.Filled.TrendingUp,
                iconTint = Color(0xFF795900),
                iconBgColor = Color(0xFFFFDFA0).copy(alpha = 0.4f),
                title = "Career Growth",
                description = "Align your trajectory with real-world industry demands to accelerate advancement."
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Section 4: Success metrics banner
        Card(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp, horizontal = 12.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Metric 1
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "50k+",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        letterSpacing = (-1).sp
                    )
                    Text(
                        text = "LEARNERS",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Vertical separator
                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .height(48.dp)
                        .background(MaterialTheme.colorScheme.outlineVariant)
                )

                // Metric 2
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "98%",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        letterSpacing = (-1).sp
                    )
                    Text(
                        text = "SKILL BOOST",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        // Footer Section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(top = 28.dp, bottom = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Shadow Mentor",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Social/Interactions Icon row (Mock clicks displaying Toast alert)
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { Toast.makeText(context, "Link shared!", Toast.LENGTH_SHORT).show() },
                    modifier = Modifier
                        .size(40.dp)
                        .background(MaterialTheme.colorScheme.background, CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Share",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(20.dp)
                    )
                }
                IconButton(
                    onClick = { Toast.makeText(context, "Feedback mail initiated!", Toast.LENGTH_SHORT).show() },
                    modifier = Modifier
                        .size(40.dp)
                        .background(MaterialTheme.colorScheme.background, CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Mail,
                        contentDescription = "Mail",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(20.dp)
                    )
                }
                IconButton(
                    onClick = { Toast.makeText(context, "Support page is launching!", Toast.LENGTH_SHORT).show() },
                    modifier = Modifier
                        .size(40.dp)
                        .background(MaterialTheme.colorScheme.background, CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Support Information",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Privacy and Terms
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Privacy",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.outline
                )
                Text(
                    text = "•",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.outline
                )
                Text(
                    text = "Terms",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.outline
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "© 2024 Shadow Mentor Inc.",
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
fun BentoFeatureCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconTint: Color,
    iconBgColor: Color,
    title: String,
    description: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.2f))
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(iconBgColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = description,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 18.sp
            )
        }
    }
}

// --- Screen 2 & 4 Core Container: Mentor active panel ---
@Composable
fun MentorTabScreen(
    viewModel: ShadowViewModel,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Tab Segment Selector at the top (Switch seamlessly between Chat & Generate Ideas)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 16.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                .padding(4.dp)
        ) {
            // Chat option
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(8.dp))
                    .background(if (viewModel.mentorActiveSubTab == "Chat") MaterialTheme.colorScheme.surface else Color.Transparent)
                    .clickable { viewModel.mentorActiveSubTab = "Chat" }
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "AI Mentor Chat",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = if (viewModel.mentorActiveSubTab == "Chat") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Generate Idea option
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(8.dp))
                    .background(if (viewModel.mentorActiveSubTab == "Generate") MaterialTheme.colorScheme.surface else Color.Transparent)
                    .clickable { viewModel.mentorActiveSubTab = "Generate" }
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Generate Ideas",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = if (viewModel.mentorActiveSubTab == "Generate") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Display active sub-screen
        Box(modifier = Modifier.weight(1f)) {
            if (viewModel.mentorActiveSubTab == "Chat") {
                MentorChatView(viewModel)
            } else {
                MentorGenerateView(viewModel)
            }
        }
    }
}

// Sub View: Chats Screen (Screen 2)
@Composable
fun MentorChatView(viewModel: ShadowViewModel) {
    val messages by viewModel.chatMessages.collectAsState()
    val scrollState = rememberScrollState()
    val listState = rememberScrollState()

    // Scroll to bottom when a new message arrives
    LaunchedEffect(messages.size) {
        // Scroll list State automatically
        if (messages.isNotEmpty()) {
            listState.animateScrollTo(listState.maxValue)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Chat Conversation list
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(listState)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Instream Header (avatar + online status)
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier.size(68.dp),
                        contentAlignment = Alignment.BottomEnd
                    ) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primaryContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Face,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(34.dp)
                            )
                        }
                        // Live green dot
                        Box(
                            modifier = Modifier
                                .size(16.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF4CAF50))
                                .border(2.dp, MaterialTheme.colorScheme.background, CircleShape)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "GEMMA-4 E2B",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "AI CO-PILOT • ONLINE",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 1.sp,
                        color = MaterialTheme.colorScheme.outline
                    )
                }

                // Date separator matching HTML view "Today, 9:41 AM"
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(1.dp)
                            .background(MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                    )
                    Text(
                        text = " Today, 9:41 AM ",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.outline,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(1.dp)
                            .background(MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                    )
                }

                // Inline API state cards mapping retry button and offline status feedback
                if (viewModel.apiState == ApiState.OFFLINE) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.45f)
                        ),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.3f))
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = "Offline indicator icon",
                                tint = MaterialTheme.colorScheme.error,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "Working Offline: Please configure GEMINI_API_KEY inside the Secrets panel to activate live Gemma-4 queries.",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onErrorContainer,
                                modifier = Modifier.weight(1f),
                                lineHeight = 16.sp
                            )
                        }
                    }
                } else if (viewModel.apiState == ApiState.TIMEOUT) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                        ),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Timeout retry icon",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "Connection timed out during peak queries. Tap to retry safely.",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.weight(1f),
                                lineHeight = 16.sp
                            )
                            Button(
                                onClick = {
                                    val lastUserMsg = messages.lastOrNull { it.sender == "user" }
                                    if (lastUserMsg != null) {
                                        viewModel.sendChatMessage(lastUserMsg.text)
                                    } else {
                                        viewModel.sendChatMessage("Let's resume learning python pipeline bottleneck issues")
                                    }
                                },
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                                modifier = Modifier.height(32.dp),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("Retry", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                // Loop messages
                messages.forEach { msg ->
                    ChatBubbleItem(message = msg)
                }

                // Shimmer if model responds online
                if (viewModel.isSendingChat) {
                    Row(
                        verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primary),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(13.dp)
                            )
                        }
                        Card(
                            shape = RoundedCornerShape(16.dp, 16.dp, 16.dp, 2.dp),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = "GEMMA-4 E2B is typing...",
                                    fontSize = 13.sp,
                                    color = MaterialTheme.colorScheme.outline
                                )
                            }
                        }
                    }
                }
            }

            // Input fields & Suggestions Area fixed at bottom
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .border(
                        BorderStroke(
                            1.dp,
                            MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.2f)
                        ), RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                    )
                    .padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Prompt smart suggestion chips
                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val context = LocalContext.current
                    SuggestionChip(text = "Review my last project") {
                        viewModel.sendChatMessage("Review my last project")
                    }
                    SuggestionChip(text = "What's next for Python?") {
                        viewModel.sendChatMessage("What's next for Python?")
                    }
                    SuggestionChip(text = "Explain closures") {
                        viewModel.sendChatMessage("Explain closures")
                    }
                }

                // Text field inputs & Buttons
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val context = LocalContext.current
                    IconButton(
                        onClick = { Toast.makeText(context, "Attachment triggered!", Toast.LENGTH_SHORT).show() }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Attach File",
                            tint = MaterialTheme.colorScheme.outline
                        )
                    }

                    // Rounded Input Bar
                    TextField(
                        value = viewModel.chatInput,
                        onValueChange = { viewModel.chatInput = it },
                        placeholder = { Text("Message GEMMA-4 E2B...", fontSize = 14.sp) },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(20.dp)),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                        keyboardActions = KeyboardActions(onSend = { viewModel.sendChatMessage() }),
                        trailingIcon = {
                            IconButton(onClick = { Toast.makeText(context, "Voice memo triggered!", Toast.LENGTH_SHORT).show() }) {
                                Icon(
                                    imageVector = Icons.Default.Lock,
                                    contentDescription = "Voice recorder input",
                                    tint = MaterialTheme.colorScheme.outline,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    )

                    // Sending CTA (with physical elevation scale spring effect)
                    IconButton(
                        onClick = { viewModel.sendChatMessage() },
                        enabled = !viewModel.isSendingChat,
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                if (viewModel.isSendingChat) MaterialTheme.colorScheme.outlineVariant
                                else MaterialTheme.colorScheme.primary
                            )
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Send,
                            contentDescription = "Send Message",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ChatBubbleItem(message: ChatMessage) {
    val isUser = message.sender == "user"
    val context = LocalContext.current
    val clipboard = LocalClipboardManager.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                clipboard.setText(AnnotatedString(message.text))
                Toast
                    .makeText(context, "Copied content to clipboard", Toast.LENGTH_SHORT)
                    .show()
            },
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
    ) {
        if (!isUser) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Face,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(14.dp)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
        }

        Column(horizontalAlignment = if (isUser) Alignment.End else Alignment.Start) {
            Text(
                text = if (isUser) "You" else "GEMMA-4 E2B",
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.outline,
                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
            )

            Box(
                modifier = Modifier
                    .widthIn(max = 280.dp)
                    .clip(
                        if (isUser) RoundedCornerShape(16.dp, 16.dp, 2.dp, 16.dp)
                        else RoundedCornerShape(16.dp, 16.dp, 16.dp, 2.dp)
                    )
                    .background(
                        if (isUser) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.surface
                    )
                    .border(
                        if (isUser) 0.dp else 1.dp,
                        if (isUser) Color.Transparent else MaterialTheme.colorScheme.outlineVariant.copy(
                            alpha = 0.3f
                        ),
                        if (isUser) RoundedCornerShape(0.dp) else RoundedCornerShape(
                            16.dp,
                            16.dp,
                            16.dp,
                            2.dp
                        )
                    )
                    .drawBehind {
                        // Shadow Left brand-stripe accent on AI reply
                        if (!isUser) {
                            drawLine(
                                color = ShadowPrimary,
                                start = Offset(0f, 0f),
                                end = Offset(0f, size.height),
                                strokeWidth = 3.dp.toPx()
                            )
                        }
                    }
                    .padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
                Text(
                    text = message.text,
                    fontSize = 14.sp,
                    color = if (isUser) Color.White else MaterialTheme.colorScheme.onSurface,
                    lineHeight = 20.sp
                )
            }
        }
    }
}

@Composable
fun SuggestionChip(text: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(100.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            .border(
                1.dp,
                MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f),
                RoundedCornerShape(100.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 8.dp)
    ) {
        Text(
            text = text,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

// Sub View: Generate Projects Screen (Screen 4)
@Composable
fun MentorGenerateView(viewModel: ShadowViewModel) {
    val projects by viewModel.generatedProjects.collectAsState()
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Topic Title description matching designs
        Text(
            text = "Generate Your Next Big Project",
            fontSize = 24.sp,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.onSurface,
            lineHeight = 31.sp
        )
        Text(
            text = "Describe your goals, and let's structure a roadmap.",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        // Connection & API state cards for Ideas tab
        if (viewModel.apiState == ApiState.OFFLINE) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.35f)
                ),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.2f))
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = "Offline Mode",
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Working Offline: GEMINI_API_KEY is missing. Smart Local Synthesis module is active to generate premium, instant blueprints automatically!",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        lineHeight = 16.sp
                    )
                }
            }
        } else if (viewModel.apiState == ApiState.TIMEOUT || viewModel.apiState == ApiState.ERROR) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                ),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "API Alert",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Active Server load or API rate limits experienced. Tap Generate to use our hyper-fast Local Synthesis fallback instantly!",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 16.sp
                    )
                }
            }
        }

        // Custom Rich Textarea for entering idea
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                // The main multiline text area
                TextField(
                    value = viewModel.projectInput,
                    onValueChange = { viewModel.projectInput = it },
                    placeholder = {
                        Text(
                            text = "Tell GEMMA-4 E2B what you want to build...",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.outline
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(110.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    maxLines = 5
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Attachment option
                    IconButton(
                        onClick = { Toast.makeText(context, "Attachment requested!", Toast.LENGTH_SHORT).show() }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add attachment",
                            tint = MaterialTheme.colorScheme.outline
                        )
                    }

                    // Generate Button with shimmering star
                    Button(
                        onClick = { viewModel.generateProjectIdea() },
                        enabled = !viewModel.isGeneratingProject && viewModel.projectInput.trim().isNotEmpty(),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = if (viewModel.isGeneratingProject) "Generating..." else "Generate",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }
        }

        // Smart suggestions pre-populators headers
        Text(
            text = "SMART SUGGESTIONS",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.outline,
            modifier = Modifier.padding(start = 2.dp)
        )

        // Pill scrolling suggestions
        Row(
            modifier = Modifier.horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SmartSuggestionChip(text = "Full-Stack Web App", icon = Icons.Default.Search) {
                viewModel.projectInput = "Full-Stack E-Commerce Web App with realtime inventory"
            }
            SmartSuggestionChip(text = "UX Case Study", icon = Icons.Default.Star) {
                viewModel.projectInput = "UX redesign of a popular banking application"
            }
            SmartSuggestionChip(text = "Python Script", icon = Icons.Default.Build) {
                viewModel.projectInput = "Python directory cleaner and file categorization helper"
            }
        }

        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f), thickness = 1.dp)

        // Recent Ideas Header list
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                text = "Recent Ideas",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "View All",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable {
                    Toast.makeText(context, "Full history layout upcoming!", Toast.LENGTH_SHORT).show()
                }
            )
        }

        // Project Items List
        if (projects.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Describe your goals above to generate ideas instantly!",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.outline
                )
            }
        } else {
            projects.forEach { proj ->
                IdeaCardItem(
                    project = proj,
                    onSaveToggle = { viewModel.toggleProjectSave(proj) },
                    onDelete = { viewModel.deleteProject(proj.id) }
                )
            }
        }
    }
}

@Composable
fun SmartSuggestionChip(
    text: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(100.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            .border(
                1.dp,
                MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f),
                RoundedCornerShape(100.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(13.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = text,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun IdeaCardItem(
    project: GeneratedProject,
    onSaveToggle: () -> Unit,
    onDelete: () -> Unit
) {
    val context = LocalContext.current
    val shareText = "Hey! Check out this awesome project idea generated by my Shadow Mentor: ${project.title} - ${project.description}"

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Gradient strip at the top
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .background(
                        Brush.horizontalGradient(
                            listOf(
                                MaterialTheme.colorScheme.primary,
                                MaterialTheme.colorScheme.secondaryContainer
                            )
                        )
                    )
            )

            Column(modifier = Modifier.padding(16.dp)) {
                // Title and Delete
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = project.title,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = project.category,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    IconButton(onClick = onDelete) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete project",
                            tint = MaterialTheme.colorScheme.outline.copy(alpha = 0.6f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = project.description,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Technologies pills
                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    project.technologies.split(",").forEach { tech ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = tech.trim(),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.2f), thickness = 0.5.dp)

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Lock, // Replaced placeholder schedule
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.outline,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = project.duration,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.outline,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    // Share and Save bookmarks actions
                    Row {
                        val shareManager = LocalClipboardManager.current
                        IconButton(onClick = {
                            shareManager.setText(AnnotatedString(shareText))
                            Toast.makeText(context, "Copied share text!", Toast.LENGTH_SHORT).show()
                        }) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = "Share",
                                tint = MaterialTheme.colorScheme.outline,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        IconButton(onClick = onSaveToggle) {
                            Icon(
                                imageVector = if (project.isSaved) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                contentDescription = "Save",
                                tint = if (project.isSaved) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

// --- Screen 3 & 5 Container: Roadmap Progress timeline & Career Dashboard (Tab: Progress) ---
@Composable
fun ProgressTabScreen(
    viewModel: ShadowViewModel,
    modifier: Modifier = Modifier
) {
    var progressActiveSubTab by remember { mutableStateOf("Timeline") } // Timeline, Career

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Tab Segment Selector at the top
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 16.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                .padding(4.dp)
        ) {
            // Timeline option
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(8.dp))
                    .background(if (progressActiveSubTab == "Timeline") MaterialTheme.colorScheme.surface else Color.Transparent)
                    .clickable { progressActiveSubTab = "Timeline" }
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Learning Path",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = if (progressActiveSubTab == "Timeline") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Career option
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(8.dp))
                    .background(if (progressActiveSubTab == "Career") MaterialTheme.colorScheme.surface else Color.Transparent)
                    .clickable { progressActiveSubTab = "Career" }
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Career Intelligence",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = if (progressActiveSubTab == "Career") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Display active sub-screen
        Box(modifier = Modifier.weight(1f)) {
            if (progressActiveSubTab == "Timeline") {
                RoadmapTimelineView(viewModel)
            } else {
                CareerDashboardView(viewModel)
            }
        }
    }
}

// Sub View: Learning Path Timeline list (Screen 3)
@Composable
fun RoadmapTimelineView(viewModel: ShadowViewModel) {
    val nodes by viewModel.learningNodes.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(100.dp))
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                .padding(horizontal = 14.dp, vertical = 6.dp)
        ) {
            Text(
                text = "LEARNING PATH",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                letterSpacing = 1.sp
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Data Science Mastery",
            fontSize = 24.sp,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Your journey from fundamentals to advanced machine learning.",
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        Spacer(modifier = Modifier.height(28.dp))

        // Vertical connecting list roadmap container
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp),
            contentAlignment = Alignment.Center
        ) {
            // Background line across connecting nodes path
            Canvas(
                modifier = Modifier
                    .matchParentSize()
            ) {
                val middleX = size.width / 2f
                // Draw dotted roadmap line matching screen designs
                drawLine(
                    color = ShadowOutlineVariant,
                    start = Offset(middleX, 40.dp.toPx()),
                    end = Offset(middleX, size.height - 40.dp.toPx()),
                    strokeWidth = 3.dp.toPx(),
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                )

                // Colored line matching completed sections
                drawLine(
                    color = ShadowPrimary,
                    start = Offset(middleX, 40.dp.toPx()),
                    end = Offset(middleX, size.height / 2f),
                    strokeWidth = 3f
                )
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(40.dp)
            ) {
                nodes.forEach { node ->
                    RoadmapNodeItem(
                        node = node,
                        onContinueClick = { viewModel.advanceLearningNode(node) }
                    )
                }
            }
        }
    }
}

@Composable
fun RoadmapNodeItem(node: LearningNode, onContinueClick: () -> Unit) {
    val isCompleted = node.status == "completed"
    val isInProgress = node.status == "in_progress"
    val isLocked = node.status == "locked"

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Node Details Left Pane (for balanced layout symmetry matching mock)
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(end = 12.dp),
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = node.title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = if (isLocked) MaterialTheme.colorScheme.outline else MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.End
            )
            Text(
                text = node.subtitle,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.End
            )

            // Left certificate/extra info card matching Completed Row
            if (node.cardText != null && isCompleted && !node.isCertificate) {
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.surface)
                        .border(
                            1.dp,
                            MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f),
                            RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = node.cardText,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

        // Center Node Icon status
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(
                    when {
                        isCompleted -> MaterialTheme.colorScheme.primary
                        isInProgress -> MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                        else -> MaterialTheme.colorScheme.surfaceVariant
                    }
                )
                .border(
                    4.dp,
                    MaterialTheme.colorScheme.surface,
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            when {
                isCompleted -> {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Completed module marker",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
                isInProgress -> {
                    // Pulsing Ring inner play arrow
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(4.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Active progress marker",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
                else -> {
                    Icon(
                        imageVector = if (node.id == "capstone") Icons.Default.Star else Icons.Default.Lock,
                        contentDescription = "Module locked marker",
                        tint = MaterialTheme.colorScheme.outline,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }

        // Action CTA Node Details Right Pane
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 12.dp)
        ) {
            if (isCompleted && node.cardText != null && node.isCertificate) {
                // Certified check
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.surface)
                        .border(
                            1.dp,
                            MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f),
                            RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Star, // Certificate mock
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Certificate Earned",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            } else if (isInProgress) {
                // Continue active CTA button
                Button(
                    onClick = onContinueClick,
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 4.dp),
                    modifier = Modifier.height(34.dp)
                ) {
                    Text(
                        text = "Continue",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            } else {
                // Locked status placeholders
                Text(
                    text = if (isLocked) "Locked" else "",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.outline,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

// Sub View: Career Intelligence dashboard metrics and learning charts (Screen 5)
@Composable
fun CareerDashboardView(viewModel: ShadowViewModel) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Upper Intro
        Column {
            Text(
                text = "Career Intelligence",
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Your real-time market alignment.",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Upper metrics cards row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Readiness Card
            Card(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.outlineVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "Readiness",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "${viewModel.readinessScore}%",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.TrendingUp,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "+2% this week",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Demand Card
            Card(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.outlineVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "Demand",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "High",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Product Design",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Custom drawn bar chart Learning Velocity
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.outlineVariant)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Learning Velocity",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.TrendingUp,
                        contentDescription = "Chart trend indicator",
                        tint = MaterialTheme.colorScheme.outline
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Custom Graphic Bars row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.Bottom
                ) {
                    // Weekly Bars (W1 -> W4 progress representing learning index)
                    WeeklyChartBar(percentage = 30f, week = "W1", color = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f))
                    WeeklyChartBar(percentage = 45f, week = "W2", color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f))
                    WeeklyChartBar(percentage = 60f, week = "W3", color = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f))
                    WeeklyChartBar(percentage = 85f, week = "W4", color = MaterialTheme.colorScheme.primary)
                }
            }
        }

        // Section 3: Missing Career Skills Gaps List
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(
                text = "Missing Skills",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            // Skill Gap Item 1 (Figma Advanced)
            MissingSkillItem(
                title = "Figma Advanced",
                priority = "High Priority",
                priorityColor = MaterialTheme.colorScheme.error,
                actionLabel = "Learn",
                onAction = {
                    viewModel.selectedTab = "Mentor"
                    viewModel.mentorActiveSubTab = "Chat"
                    viewModel.sendChatMessage("Create a crash course overview roadmap for Figma Advanced please!")
                }
            )

            // Skill Gap Item 2 (React Query)
            MissingSkillItem(
                title = "React Query",
                priority = "Medium Priority",
                priorityColor = MaterialTheme.colorScheme.onSurfaceVariant,
                actionLabel = "Explore",
                onAction = {
                    viewModel.selectedTab = "Mentor"
                    viewModel.mentorActiveSubTab = "Chat"
                    viewModel.sendChatMessage("Explain React Query and how it benefits server-state sync architecture?")
                }
            )
        }

        // Glassmorphic AI Recommendation Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.05f)
            ),
            border = BorderStroke(
                1.5.dp,
                MaterialTheme.colorScheme.primary
            )
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "AI Recommendation",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Recommended path: Strategic Management",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Based on your portfolio strength and market demand, pivoting slightly towards strategic roles will increase your readiness score to 92%.",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier
                        .clickable {
                            Toast
                                .makeText(
                                    context,
                                    "Redirecting to Mentor Chat recommendation path details...",
                                    Toast.LENGTH_SHORT
                                )
                                .show()
                            viewModel.selectedTab = "Mentor"
                            viewModel.mentorActiveSubTab = "Chat"
                            viewModel.sendChatMessage("Provide details on the Recommended path: Strategic Management")
                        }
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "View Path Details",
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun WeeklyChartBar(percentage: Float, week: String, color: Color) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(48.dp)
    ) {
        Box(
            modifier = Modifier
                .height(86.dp)
                .width(28.dp)
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f), RoundedCornerShape(4.dp)),
            contentAlignment = Alignment.BottomCenter
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(percentage / 100f)
                    .background(color, RoundedCornerShape(4.dp))
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = week,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun MissingSkillItem(
    title: String,
    priority: String,
    priorityColor: Color,
    actionLabel: String,
    onAction: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Circular dummy skill badge
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.School,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.outline,
                        modifier = Modifier.size(16.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = title,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = priority,
                        fontSize = 11.sp,
                        color = priorityColor,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Button(
                onClick = onAction,
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (actionLabel == "Learn") MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.surface,
                    contentColor = if (actionLabel == "Learn") Color.White
                    else MaterialTheme.colorScheme.primary
                ),
                border = if (actionLabel == "Learn") null
                else BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                modifier = Modifier.height(34.dp)
            ) {
                Text(
                    text = actionLabel,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

// --- Screen 4: Profile Configuration & Offline Cache Clearing Panel ---
@Composable
fun ProfileScreen(
    viewModel: ShadowViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context)

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Simple avatar image block matching accessibility guidelines
        Box(
            modifier = Modifier
                .size(90.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "User Profile Picture",
                tint = Color.White,
                modifier = Modifier.size(54.dp)
            )
        }

        // Details
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Beta Learner",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "varshith.code@gmail.com",
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))

        // Cognitive Customizer slider values
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.outlineVariant)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "Cognitive Options",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Shadow adjusts explaining parameters such as tone, vocabulary complexity, and depth based on selections.",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 16.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Custom settings list
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Explanations Tone",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Analogies & Backpacks",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Difficulty level",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Incremental growth",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Database Caching Admin Control Operations
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.outlineVariant)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "Storage & Administration",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Shadow Mentor stores your generated project roadmaps, saved bookmarks, and custom chat histories safely inside modern SQLite tables.",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 16.sp
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Action Buttons for database operations
                Button(
                    onClick = {
                        viewModel.selectedTab = "Learn"
                        Toast.makeText(context, "Storage and models synced securely!", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Sync Live Database", fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Clear Offline Storage
                OutlinedButton(
                    onClick = {
                        viewModel.resetDatabaseCache()
                        Toast.makeText(context, "Cleared Database. Reloading defaults!", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.error)
                ) {
                    Text("Reset Database Cache", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

// --- Dopamine Visual Celebrator & Firework Particle System ---
@Composable
fun DopamineCelebrator(
    viewModel: ShadowViewModel
) {
    if (viewModel.dopamineTitle != null) {
        AlertDialog(
            onDismissRequest = {
                viewModel.dopamineTitle = null
                viewModel.dopamineSubtitle = null
                viewModel.dopamineRewardText = null
            },
            icon = {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Success Star Badge",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(36.dp)
                    )
                }
            },
            title = {
                Text(
                    text = viewModel.dopamineTitle ?: "SYNAPSE IGNITED!",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.primary,
                    letterSpacing = (-0.5).sp
                )
            },
            text = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = viewModel.dopamineSubtitle ?: "",
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 18.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(100.dp))
                            .background(Color(0xFFFFDFA0))
                            .border(1.5.dp, Color(0xFFC79200), RoundedCornerShape(100.dp))
                            .padding(horizontal = 20.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Reward Icon",
                            tint = Color(0xFFC79200),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = viewModel.dopamineRewardText ?: "+150 EXP",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 16.sp,
                            color = Color(0xFF795900)
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "🔥 Streak Multiplier Active • Keep the fire burning!",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.dopamineTitle = null
                        viewModel.dopamineSubtitle = null
                        viewModel.dopamineRewardText = null
                    },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
                ) {
                    Text("Dismiss & Keep Learning ✨", fontWeight = FontWeight.Bold)
                }
            },
            shape = RoundedCornerShape(24.dp),
            containerColor = MaterialTheme.colorScheme.surface
        )
    }
}

@Composable
fun DopamineParticleEffect(dopamineCount: Int) {
    if (dopamineCount == 0) return
    
    val infiniteTransition = rememberInfiniteTransition(label = "particles")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "particleAlpha"
    )
    val floatOffset by infiniteTransition.animateFloat(
        initialValue = 100f,
        targetValue = -300f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "particleFloat"
    )

    var triggerKey by remember { mutableStateOf(0) }
    LaunchedEffect(dopamineCount) {
        triggerKey = dopamineCount
    }

    if (triggerKey > 0 && alpha > 0.05f) {
        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {
            val randomPoints = listOf(
                Offset(size.width * 0.2f, size.height * 0.7f),
                Offset(size.width * 0.4f, size.height * 0.8f),
                Offset(size.width * 0.6f, size.height * 0.75f),
                Offset(size.width * 0.8f, size.height * 0.65f),
                Offset(size.width * 0.15f, size.height * 0.5f),
                Offset(size.width * 0.85f, size.height * 0.45f)
            )
            
            randomPoints.forEachIndexed { idx, base ->
                val particleColor = when (idx % 3) {
                    0 -> Color(0xFF6750A4)
                    1 -> Color(0xFFFFDFA0)
                    else -> Color(0xFF03DAC6)
                }
                val driftX = (idx - 3) * 15f
                drawCircle(
                    color = particleColor.copy(alpha = alpha),
                    radius = (10 + (idx * 3)).dp.toPx(),
                    center = Offset(base.x + driftX, base.y + floatOffset)
                )
            }
        }
    }
}

