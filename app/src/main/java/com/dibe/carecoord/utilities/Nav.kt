package com.dibe.carecoord.utilities

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dibe.carecoord.auth.view.AuthOrMainScreen
import com.dibe.carecoord.auth.view.LoginScreen
import com.dibe.carecoord.auth.view.RegistrationScreen
import com.dibe.carecoord.auth.viewmodel.AuthEvent
import com.dibe.carecoord.folders.view.BasicDetails
import com.dibe.carecoord.folders.view.BodyParameters
import com.dibe.carecoord.folders.view.DiagnosisAndTreatmentScreen
import com.dibe.carecoord.folders.view.DoctorsRemark
import com.dibe.carecoord.folders.view.DocumentUploadScreen
import com.dibe.carecoord.folders.view.EditScreen
import com.dibe.carecoord.folders.view.FemaleDetails
import com.dibe.carecoord.folders.view.HomeScreen
import com.dibe.carecoord.folders.view.LaboratoryTests
import com.dibe.carecoord.folders.view.SearchScreen
import com.dibe.carecoord.folders.viewmodel.FolderEvent
import com.dibe.carecoord.folders.viewmodel.FolderState
import com.dibe.carecoord.old.Presentation.Screens.Screens
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import com.dibe.carecoord.chat.view.ChatScreen
import com.dibe.carecoord.chat.viewmodel.ChatEvent
import com.dibe.carecoord.chat.viewmodel.ChatState

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun Nav(
        authOnEvent: (AuthEvent) -> Unit ,
        folderOnEvent: (FolderEvent) -> Unit ,
        chatOnEvent: (ChatEvent) -> Unit ,
        chatState: ChatState ,
        folderState: FolderState ,
        context: Context
       ) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screens.DECISION.name
                   ) {
        composable(Screens.DECISION.name) {
            AuthOrMainScreen(navController = navController , authOnEvent = authOnEvent , folderOnEvent = folderOnEvent , folderState = folderState , context = context)
        }
        // Slide up animation from Login to Signup or Home
        composable(Screens.LOGIN.name,
                   enterTransition = { slideInVertically(animationSpec = tween(300), initialOffsetY = { it }) },
                   exitTransition = { slideOutVertically(animationSpec = tween(300), targetOffsetY = { -it }) }
                  ) {
            LoginScreen(onEvent = authOnEvent, context = context, navController = navController)
        }

        composable(Screens.REGISTRATION.name,
                   enterTransition = { slideInVertically(animationSpec = tween(300), initialOffsetY = { it }) },
                   exitTransition = { slideOutVertically(animationSpec = tween(300), targetOffsetY = { -it }) }
                  ) {
            RegistrationScreen(onEvent = authOnEvent, navController = navController)
        }

        // Home screen with fade transition
        composable(Screens.HOME.name,
                   enterTransition = { fadeIn(animationSpec = tween(500)) },
                   exitTransition = { fadeOut(animationSpec = tween(500)) }
                  ) {
            HomeScreen(onEvent = folderOnEvent, state = folderState, navController = navController, context = context)
        }

        // Home screen with fade transition
        composable(Screens.CHAT.name,
                   enterTransition = { fadeIn(animationSpec = tween(500)) },
                   exitTransition = { fadeOut(animationSpec = tween(500)) }
                  ) {
            ChatScreen(onEvent = chatOnEvent, state = chatState, navController = navController, context = context)
        }

        // Slide in from the right for Search, Edit, and Settings screens
        composable(Screens.SEARCH.name,
                   enterTransition = { slideInHorizontally(animationSpec = tween(300), initialOffsetX = { it }) },
                   exitTransition = { slideOutHorizontally(animationSpec = tween(300), targetOffsetX = { -it }) }
                  ) {
            SearchScreen(onEvent = folderOnEvent, state = folderState, navController = navController)
        }

        composable(Screens.EDIT.name,
                   enterTransition = { slideInHorizontally(animationSpec = tween(300), initialOffsetX = { it }) },
                   exitTransition = { slideOutHorizontally(animationSpec = tween(300), targetOffsetX = { -it }) }
                  ) {
            EditScreen(onEvent = folderOnEvent, state = folderState, navController = navController, context = context)
        }

        // Basic Details -> Body Parameters -> Female Details flow with upward sliding animation
        composable(Screens.BASIC_DETAILS.name,
                   enterTransition = { slideInVertically(animationSpec = tween(300), initialOffsetY = { it }) },
                   exitTransition = { slideOutVertically(animationSpec = tween(300), targetOffsetY = { -it }) }
                  ) {
            BasicDetails(onEvent = folderOnEvent, state = folderState, navController = navController)
        }

        composable(Screens.BODY_PARAMETERS.name,
                   enterTransition = { slideInVertically(animationSpec = tween(300), initialOffsetY = { it }) },
                   exitTransition = { slideOutVertically(animationSpec = tween(300), targetOffsetY = { -it }) }
                  ) {
            BodyParameters(onEvent = folderOnEvent, state = folderState, navController = navController)
        }

        composable(Screens.FEMALE_DETAILS.name,
                   enterTransition = { slideInVertically(animationSpec = tween(300), initialOffsetY = { it }) },
                   exitTransition = { slideOutVertically(animationSpec = tween(300), targetOffsetY = { -it }) }
                  ) {
            FemaleDetails(onEvent = folderOnEvent, state = folderState, navController = navController, context = context)
        }

        // Doctor’s Remark -> Test Screen -> Result Screen flow with left-to-right slide animations
        composable(Screens.DOCTORS_REMARK.name,
                   enterTransition = { slideInHorizontally(animationSpec = tween(300), initialOffsetX = { it }) },
                   exitTransition = { slideOutHorizontally(animationSpec = tween(300), targetOffsetX = { -it }) }
                  ) {
            DoctorsRemark(onEvent = folderOnEvent, state = folderState, navController = navController)
        }

        composable(Screens.DIAGNOSIS_AND_TREATMENT_SCREEN.name,
                   enterTransition = { slideInHorizontally(animationSpec = tween(300), initialOffsetX = { it }) },
                   exitTransition = { slideOutHorizontally(animationSpec = tween(300), targetOffsetX = { -it }) }
                  ) {
            DiagnosisAndTreatmentScreen(onEvent = folderOnEvent, state = folderState, navController = navController)
        }

        composable(Screens.LABORATORY_TESTS.name,
                   enterTransition = { slideInHorizontally(animationSpec = tween(300), initialOffsetX = { it }) },
                   exitTransition = { slideOutHorizontally(animationSpec = tween(300), targetOffsetX = { -it }) }
                  ) {
            LaboratoryTests(onEvent = folderOnEvent, state = folderState, navController = navController)
        }

        composable(Screens.RESULTS_SCREEN.name,
                   enterTransition = { slideInHorizontally(animationSpec = tween(300), initialOffsetX = { it }) },
                   exitTransition = { slideOutHorizontally(animationSpec = tween(300), targetOffsetX = { -it }) }
                  ) {
            DocumentUploadScreen(onEvent = folderOnEvent, state = folderState, navController = navController)
        }

//        composable(Screens.CHAT.name) {
//            ChatScreen(context = context, onEvent = onEvent , state = state, navController = navController)
//        }
//        composable(Screens.SETTINGS.name) {
//            SettingsScreen(context = context, navController = navController, state)
//        }
    }
}
