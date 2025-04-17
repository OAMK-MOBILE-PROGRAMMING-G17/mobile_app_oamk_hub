package com.example.oamkhub.navigation

sealed class Routes(val route: String) {
    object Auth {
        object Front      : Routes("front")
        object Login      : Routes("login")
        object Signup     : Routes("signup")
    }
    object Reset {
        object Email      : Routes("resetEmail")
        object Otp        : Routes("otpScreen/{email}")
        object ChangePass : Routes("changePassword/{email}/{otp}")
    }
    object General {
        object Home       : Routes("home")
        object News       : Routes("news")
        object Events     : Routes("events")
        object Contact    : Routes("contact")
    }
    object LostFound {
        object Root       : Routes("lostfound")
        object Form       : Routes("lostfoundform")
        object Comments   : Routes("lostfoundcomments/{lostProductId}/{title}")
    }
    object Marketplace {
        object Root       : Routes("marketplace")
        object AddItem    : Routes("addItem")
        object Detail     : Routes("marketplaceDetail/{itemId}")
        object FullImage  : Routes("fullscreen_image/{initialImageUrl}/{encodedImages}")
    }
}
