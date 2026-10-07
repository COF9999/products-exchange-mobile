package com.example.mobile_cue_things.principal

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.mobile_cue_things.screens.CreatePublicationScreen
import com.example.mobile_cue_things.screens.DashBoardRoutesScreen
import com.example.mobile_cue_things.screens.DetailScreen
import com.example.mobile_cue_things.screens.HomeScreen
import com.example.mobile_cue_things.screens.LoginScreen
import com.example.mobile_cue_things.screens.MyOfferScreen
import com.example.mobile_cue_things.screens.MyPublicationScreen
import com.example.mobile_cue_things.screens.OfferDetailScreen
import com.example.mobile_cue_things.viewModel.product.CreateProductViewModel
import com.example.mobile_cue_things.viewModel.product.FirestoreProductViewModel
import com.example.mobile_cue_things.viewModel.navigation.NavigationViewModel
import com.example.mobile_cue_things.viewModel.offer.OfferViewModel


private fun NavGraph.findStartDestinationId(route: Routes): Int {
    return when(route) {
        is LoginRoute -> findNode(LoginRoute::class)!!.id
        is DashboardRoute -> findNode(DashboardRoute::class)!!.id
        is CreatePublicationRoute -> findNode(CreatePublicationRoute::class)!!.id
        is PublicationRoute -> findNode(PublicationRoute::class)!!.id
        else -> throw IllegalArgumentException("Ruta no registrada en NavGraph")
    }
}


@Composable
fun NavigationWrapper(navController: NavHostController,
                      setUserName:(String)->Unit,
                      username:String){

    val navViewModel: NavigationViewModel = viewModel()
    val firestoreProductViewModel: FirestoreProductViewModel = viewModel()
    val fireStoreOfferViewModel: OfferViewModel = viewModel()


    LaunchedEffect(navViewModel) {
        navViewModel.navigationEvent.collect { command ->
            navController.navigate(command.route) {
                // Solo aplica popUpTo si está definido
                command.popUpToRoute?.let { route ->
                    popUpTo(navController.graph.findStartDestinationId(route)) {
                        inclusive = command.inclusive
                    }
                }
            }
        }
    }


    NavHost(navController = navController, startDestination = LoginRoute) {
        composable<LoginRoute> {
            LoginScreen(goToHome = { email ->
                setUserName(email)
                navViewModel.navigateTo(route = DashboardRoute,
                    popUpToRoute = LoginRoute,
                    inclusive = true
                )
            })
        }

        composable<DashboardRoute> {
            DashBoardRoutesScreen(
                goToHome = { navViewModel.navigateTo(PublicationRoute(username)) },
                goToCreateProduct = { navViewModel.navigateTo(CreatePublicationRoute) }
            )
        }

        composable<PublicationRoute> { backStackEntry ->
            val homeRoute = backStackEntry.toRoute<PublicationRoute>()
            HomeScreen(navViewModel=navViewModel)
        }

        composable<DetailProductRoute>{backStackEntry->
            val detailProductRoute = backStackEntry.toRoute<DetailProductRoute>()
            val id = detailProductRoute.id
            DetailScreen(id=id, navigateToHome = { navController.popBackStack()},firestoreProductViewModel=firestoreProductViewModel)
        }

        composable<CreatePublicationRoute> {
            val vm: CreateProductViewModel = viewModel()
            CreatePublicationScreen(email = username, viewModel = vm, navigateToHome = {navViewModel.navigateTo(
                PublicationRoute(username))})
        }

        composable<MyPublicationRoute> {
            val vm: CreateProductViewModel = viewModel()
            MyPublicationScreen(navViewModel=navViewModel)
        }

        composable<OfferDetailRoute>{backStackEntry->
            val offerDetailProductRoute = backStackEntry.toRoute<OfferDetailRoute>()
            val idProduct:String = offerDetailProductRoute.idProduct
            OfferDetailScreen(
                navViewModel=navViewModel,
                idProduct=idProduct,
                firestoreProductViewModel = firestoreProductViewModel,
                fireStoreOfferViewModel = fireStoreOfferViewModel,
                goToDetailProduct = {idProduct-> navViewModel.navigateTo(DetailProductRoute(id=idProduct))}
            )
        }

        composable<MyOffersRoute>{
            MyOfferScreen(navViewModel=navViewModel)
        }
    }

}

