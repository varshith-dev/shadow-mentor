package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.outlined.Assessment
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.School
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalConfiguration
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.*
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.ShadowViewModel

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MyApplicationTheme {
        val viewModel: ShadowViewModel = viewModel()
        MainAppShell(viewModel = viewModel)
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppShell(viewModel: ShadowViewModel) {
  val context = LocalContext.current
  val configuration = LocalConfiguration.current
  val isWideScreen = configuration.screenWidthDp >= 600

  Row(modifier = Modifier.fillMaxSize()) {
    if (isWideScreen) {
      BentoNavRail(viewModel = viewModel)
    }

    Scaffold(
      modifier = Modifier.weight(1f),
      containerColor = MaterialTheme.colorScheme.background,
      topBar = {
        val title = when (viewModel.selectedTab) {
          "Learn" -> "Shadow Mentor"
          "Mentor" -> "GEMMA-4 E2B AI Mentor"
          "Progress" -> "Progress & Career"
          "Profile" -> "Beta Settings"
          else -> "Shadow Mentor"
        }
        ShadowTopBar(
          title = title,
          onNotificationsClick = {
            Toast.makeText(context, "All milestones and statistics are in real-time sync!", Toast.LENGTH_SHORT).show()
          }
        )
      },
      bottomBar = {
        if (!isWideScreen) {
          BentoBottomNavBar(viewModel = viewModel)
        }
      }
    ) { innerPadding ->
      Box(
        modifier = Modifier
          .fillMaxSize()
          .padding(innerPadding)
      ) {
        when (viewModel.selectedTab) {
          "Learn" -> LearnScreen(viewModel = viewModel)
          "Mentor" -> MentorTabScreen(viewModel = viewModel)
          "Progress" -> ProgressTabScreen(viewModel = viewModel)
          "Profile" -> ProfileScreen(viewModel = viewModel)
        }

        // Globally overlay the satisfying dopamine celebration systems
        DopamineParticleEffect(dopamineCount = viewModel.dopamineCount)
        DopamineCelebrator(viewModel = viewModel)
      }
    }
  }
}

@Composable
fun BentoNavRail(viewModel: ShadowViewModel) {
  NavigationRail(
    containerColor = MaterialTheme.colorScheme.surface,
    header = {
      Spacer(modifier = Modifier.height(24.dp))
      Box(
        modifier = Modifier
          .size(48.dp)
          .clip(RoundedCornerShape(12.dp))
          .background(MaterialTheme.colorScheme.primary),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = Icons.Default.School,
          contentDescription = "Logo",
          tint = Color.White
        )
      }
    },
    modifier = Modifier
      .fillMaxHeight()
      .width(80.dp)
      .border(BorderStroke(1.5.dp, MaterialTheme.colorScheme.outlineVariant)),
  ) {
    Spacer(modifier = Modifier.weight(1f))
    BentoNavRailItem(
      label = "Learn",
      isSelected = viewModel.selectedTab == "Learn",
      activeIcon = Icons.Default.School,
      inactiveIcon = Icons.Outlined.School,
      onClick = { viewModel.selectedTab = "Learn" }
    )
    Spacer(modifier = Modifier.height(20.dp))
    BentoNavRailItem(
      label = "Mentor",
      isSelected = viewModel.selectedTab == "Mentor",
      activeIcon = Icons.Default.ChatBubble,
      inactiveIcon = Icons.Outlined.ChatBubbleOutline,
      onClick = { viewModel.selectedTab = "Mentor" }
    )
    Spacer(modifier = Modifier.height(20.dp))
    BentoNavRailItem(
      label = "Progress",
      isSelected = viewModel.selectedTab == "Progress",
      activeIcon = Icons.Default.Assessment,
      inactiveIcon = Icons.Outlined.Assessment,
      onClick = { viewModel.selectedTab = "Progress" }
    )
    Spacer(modifier = Modifier.height(20.dp))
    BentoNavRailItem(
      label = "Profile",
      isSelected = viewModel.selectedTab == "Profile",
      activeIcon = Icons.Default.Person,
      inactiveIcon = Icons.Outlined.Person,
      onClick = { viewModel.selectedTab = "Profile" }
    )
    Spacer(modifier = Modifier.weight(1f))
  }
}

@Composable
fun BentoNavRailItem(
  label: String,
  isSelected: Boolean,
  activeIcon: ImageVector,
  inactiveIcon: ImageVector,
  onClick: () -> Unit
) {
  val containerColor = if (isSelected) {
    MaterialTheme.colorScheme.primaryContainer
  } else {
    Color.Transparent
  }
  val contentColor = if (isSelected) {
    MaterialTheme.colorScheme.onPrimaryContainer
  } else {
    MaterialTheme.colorScheme.onSurfaceVariant
  }

  Box(
    modifier = Modifier
      .size(64.dp)
      .clip(RoundedCornerShape(16.dp))
      .background(containerColor)
      .clickable(onClick = onClick)
      .padding(4.dp),
    contentAlignment = Alignment.Center
  ) {
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center
    ) {
      Icon(
        imageVector = if (isSelected) activeIcon else inactiveIcon,
        contentDescription = label,
        tint = contentColor,
        modifier = Modifier.size(24.dp)
      )
      Spacer(modifier = Modifier.height(4.dp))
      Text(
        text = label,
        fontSize = 11.sp,
        fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Medium,
        color = contentColor
      )
    }
  }
}

@Composable
fun BentoBottomNavBar(viewModel: ShadowViewModel) {
  // Float bar using standard Material 3 custom layouts for a polished Bento block appearance
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp, vertical = 12.dp)
      .navigationBarsPadding(),
    shape = RoundedCornerShape(24.dp),
    colors = CardDefaults.cardColors(
      containerColor = MaterialTheme.colorScheme.surface
    ),
    border = BorderStroke(2.dp, MaterialTheme.colorScheme.outlineVariant)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 12.dp, vertical = 10.dp),
      horizontalArrangement = Arrangement.SpaceEvenly,
      verticalAlignment = Alignment.CenterVertically
    ) {
      BentoNavItem(
        label = "Learn",
        isSelected = viewModel.selectedTab == "Learn",
        activeIcon = Icons.Default.School,
        inactiveIcon = Icons.Outlined.School,
        onClick = { viewModel.selectedTab = "Learn" }
      )
      BentoNavItem(
        label = "Mentor",
        isSelected = viewModel.selectedTab == "Mentor",
        activeIcon = Icons.Default.ChatBubble,
        inactiveIcon = Icons.Outlined.ChatBubbleOutline,
        onClick = { viewModel.selectedTab = "Mentor" }
      )
      BentoNavItem(
        label = "Progress",
        isSelected = viewModel.selectedTab == "Progress",
        activeIcon = Icons.Default.Assessment,
        inactiveIcon = Icons.Outlined.Assessment,
        onClick = { viewModel.selectedTab = "Progress" }
      )
      BentoNavItem(
        label = "Profile",
        isSelected = viewModel.selectedTab == "Profile",
        activeIcon = Icons.Default.Person,
        inactiveIcon = Icons.Outlined.Person,
        onClick = { viewModel.selectedTab = "Profile" }
      )
    }
  }
}

@Composable
fun RowScope.BentoNavItem(
  label: String,
  isSelected: Boolean,
  activeIcon: ImageVector,
  inactiveIcon: ImageVector,
  onClick: () -> Unit
) {
  val containerColor = if (isSelected) {
    MaterialTheme.colorScheme.primaryContainer
  } else {
    Color.Transparent
  }
  val contentColor = if (isSelected) {
    MaterialTheme.colorScheme.onPrimaryContainer
  } else {
    MaterialTheme.colorScheme.onSurfaceVariant
  }

  Box(
    modifier = Modifier
      .weight(1f)
      .height(48.dp)
      .clip(RoundedCornerShape(16.dp))
      .background(containerColor)
      .clickable(onClick = onClick)
      .padding(horizontal = 4.dp),
    contentAlignment = Alignment.Center
  ) {
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center
    ) {
      Icon(
        imageVector = if (isSelected) activeIcon else inactiveIcon,
        contentDescription = label,
        tint = contentColor,
        modifier = Modifier.size(20.dp)
      )
      Text(
        text = label,
        fontSize = 10.sp,
        fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Medium,
        color = contentColor
      )
    }
  }
}
