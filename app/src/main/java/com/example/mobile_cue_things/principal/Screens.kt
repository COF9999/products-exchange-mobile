package com.example.mobile_cue_things.principal

import android.net.Uri
import kotlinx.serialization.Serializable




/*

@Serializable
data class Home(val email:String)

object routes{
    const val LOGIN = "login"
    const val HOME = "home/{email}"

    const val DASHBOARDROUTES = "dashboardroutes"

    const val CREATEPUBLICATION = "createpublication"
    fun homeRoute(email: String) = "home/${Uri.encode(email)}"
}

 */


@Serializable
sealed interface Routes

@Serializable
object LoginRoute : Routes

@Serializable
data class PublicationRoute(val email: String) : Routes

@Serializable
data class DetailProductRoute(val id: String): Routes
@Serializable
object DashboardRoute : Routes

@Serializable
object CreatePublicationRoute : Routes

@Serializable
object MyPublicationRoute:Routes

@Serializable
data class OfferDetailRoute(val idProduct:String):Routes

@Serializable
object MyOffersRoute: Routes
