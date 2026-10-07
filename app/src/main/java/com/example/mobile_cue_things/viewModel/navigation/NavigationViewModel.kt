package com.example.mobile_cue_things.viewModel.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile_cue_things.principal.Routes
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch



data class NavigationCommand(
    val route: Routes,          // La ruta a navegar
    val popUpToRoute: Routes? = null,  // Ruta de destino de popUpTo
    val inclusive: Boolean = false,    // Si popUpTo debe ser inclusivo
)

class NavigationViewModel : ViewModel() {

    private val _navigationEvent = MutableSharedFlow<NavigationCommand>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    fun navigateTo(
        route: Routes,
        popUpToRoute: Routes? = null,
        inclusive: Boolean = false
    ) {
        viewModelScope.launch {
            _navigationEvent.emit(NavigationCommand(route, popUpToRoute, inclusive))
        }
    }
}

