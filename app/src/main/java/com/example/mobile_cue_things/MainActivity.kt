package com.example.mobile_cue_things

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.mobile_cue_things.enums.ConstantsRoutes
import com.example.mobile_cue_things.principal.CreatePublicationRoute
import com.example.mobile_cue_things.principal.LoginRoute
import com.example.mobile_cue_things.principal.MyOffersRoute
import com.example.mobile_cue_things.principal.MyPublicationRoute
import com.example.mobile_cue_things.principal.NavigationWrapper
import com.example.mobile_cue_things.principal.PublicationRoute
import com.example.mobile_cue_things.ui.theme.MobilecuethingsTheme

import com.example.mobile_cue_things.viewModel.global.GlobalVariables
import com.example.mobile_cue_things.viewModel.navigation.NavigationViewModel
import com.example.mobile_cue_things.viewModel.product.FirestoreProductViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

/*

class MainActivity : ComponentActivity() {


    private fun signInAnonymously(auth: FirebaseAuth) {
        auth.signInAnonymously()
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // La app se ha autenticado como un usuario anónimo válido
                    val user = auth.currentUser
                    Log.d("Auth", "signInAnonymously:success UID: ${user?.uid}")
                } else {
                    // Fallo de autenticación
                    Log.w("Auth", "signInAnonymously:failure", task.exception)
                }
            }
    }

    @Composable
    fun ClickableAnimatedTitle(onClick: () -> Unit) {
        val defaultColor = TopAppBarDefaults.topAppBarColors().titleContentColor
        val gv: GlobalVariables = viewModel()
        // Estado para mostrar la animación solo una vez
        var playAnimation by remember { mutableStateOf(true) }

        // Animación que va de 0 → 1 solo una vez
        val progress by animateFloatAsState(
            targetValue = if (playAnimation) 1f else 0f,
            animationSpec = tween(durationMillis = 1200, easing = LinearEasing),
            finishedListener = { playAnimation = false } // Apagar la animación al terminar
        )

        // Si ya terminó, usamos el color normal sin efectos
        if (!playAnimation) {
            Text(
                text = "CUE-THINGS",
                color = defaultColor,
                fontSize = 20.sp,
                modifier = Modifier.clickable { onClick() }
            )
            return
        }

        // Brillo "chrome" que se mueve una vez
        val shineBrush = Brush.linearGradient(
            colors = listOf(
                Color.Transparent,
                Color.White.copy(alpha = 0.8f),
                Color.Transparent
            ),
            start = Offset(-300f + (600f * progress), 0f),
            end = Offset(0f + (600f * progress), 300f)
        )

        // Combinar color normal + brillo
        val combined = Brush.composite(
            Brush.verticalGradient(
                listOf(defaultColor, defaultColor) // conserva su color original
            ),
            shineBrush,
            BlendMode.Lighten
        )
        Column() {
            Text(
                text = "CUE-THINGS",
                fontSize = 20.sp,
                lineHeight = 10.sp,
                modifier = Modifier
                    .padding(2.dp)
                    .clickable { onClick() },
                style = LocalTextStyle.current.copy(brush = combined)
            )

            Text(
                text = gv.positionRoute,
                fontSize = 12.sp,
                lineHeight = 8.sp,
                modifier = Modifier
                    .padding(2.dp)
                    .clickable { onClick() },
                style = LocalTextStyle.current.copy(brush = combined)
            )
        }

    }


    @Composable
    fun ComponentRoutesOverlayPrincipalMenu(){
        val navViewModel: NavigationViewModel = viewModel()
        val gv: GlobalVariables = viewModel()
        Column(modifier = Modifier.fillMaxWidth(0.8f)) {
            Row(verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 8.dp)
                    .fillMaxWidth(0.7f)
                    .clickable{
                            gv.changeStatePanel()
                            gv.updatePositionRoute(ConstantsRoutes.PUBLICATION.value)
                            navViewModel.navigateTo(PublicationRoute(""))
                    }) {
                Text(text = "Publicaciones", fontSize = 16.sp,fontStyle = FontStyle.Italic)
            }
            Spacer(Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically, modifier =
                Modifier.padding(vertical = 8.dp)
                    .fillMaxWidth(0.7f)
                    .clickable{
                        gv.changeStatePanel()
                        gv.updatePositionRoute(ConstantsRoutes.CREATEPUBLICATION.value)
                            navViewModel.navigateTo(CreatePublicationRoute)
                    }
            ) {
                Text(text = "Crear publicación", fontSize = 16.sp,fontStyle = FontStyle.Italic)
            }

            Spacer(Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier
                .padding(vertical = 8.dp)
                .fillMaxWidth(0.7f)
                .clickable{
                    gv.changeStatePanel()
                    gv.updatePositionRoute(ConstantsRoutes.MYPUBLICATION.value)
                    navViewModel.navigateTo(MyPublicationRoute)
            }
            ) {
                Text(text = "Mis publicaciones", fontSize = 16.sp, fontStyle = FontStyle.Italic)
            }
        }
    }

    @Composable
    fun ComponentOptionsOverlayPrincipalMenu(){
        val navViewModel: NavigationViewModel = viewModel()
        val gv: GlobalVariables = viewModel()

        Column(modifier = Modifier.fillMaxWidth(0.8f)) {
            Row(verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 8.dp).fillMaxWidth(0.7f)) {
                Icon(imageVector = Icons.Default.AccountCircle, contentDescription = "Inicio")
                Spacer(modifier = Modifier.width(12.dp))
                Text(text = "Perfil", fontSize = 16.sp)
            }
            Spacer(Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically, modifier =
                Modifier.padding(vertical = 8.dp)
                    .fillMaxWidth(0.7f)) {
                Icon(imageVector = Icons.Default.Build, contentDescription = "Inicio")
                Spacer(modifier = Modifier.width(12.dp))
                Text(text = "Configuración", fontSize = 16.sp)
            }

            Spacer(Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier
                .padding(vertical = 8.dp)
                .fillMaxWidth(0.7f)
                .clickable {

                    gv.changeStatePanel()
                    gv.clearUserName()
                    gv.updatePositionRoute("")

                    navViewModel.navigateTo(
                        route = LoginRoute,
                        popUpToRoute = LoginRoute,
                        inclusive = true   // elimina también DashboardRoute
                    )
                }

            ) {
                Icon(imageVector = Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Inicio")
                Spacer(modifier = Modifier.width(12.dp))
                Text(text = "Cerrar sesión", fontSize = 16.sp)
            }
        }
    }


    @Preview(showBackground = true)
    @Composable
    fun OverlayMenu() {

        var visibleOptions by remember { mutableStateOf(false) }

        var visibleRoutes by remember { mutableStateOf(false) }


        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {

            // Fondo semitransparente
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.4f))
                    .clickable {  } // click solo en el fondo
            ){

                Column(
                    modifier = Modifier
                        .fillMaxWidth(0.80f)
                        .padding(top = 20.dp, start = 10.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            MaterialTheme.colorScheme.surface,
                        )
                        .padding(24.dp)

                ) {

                    Column(modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .clickable{
                                visibleRoutes = !visibleRoutes
                            }) {
                            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Inicio")
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(text = "Rutas", fontSize = 16.sp)
                        }
                        Column (modifier = Modifier
                            .fillMaxWidth()
                            .clickable{
                                visibleRoutes = false
                            }
                            .animateContentSize(),
                            horizontalAlignment = Alignment.CenterHorizontally

                        ) {
                            if(visibleRoutes){
                                ComponentRoutesOverlayPrincipalMenu()
                            }
                        }
                    }
                    Spacer(Modifier.height(8.dp))

                    Column(modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally) {
                        Row(verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(vertical = 8.dp)
                                .fillMaxWidth()
                                .clickable{
                                    visibleOptions = !visibleOptions
                                }) {
                            Icon(imageVector = Icons.Default.Menu, contentDescription = "Inicio")
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(text = "Opciones", fontSize = 16.sp)
                        }
                        Column (modifier = Modifier
                            .fillMaxWidth()
                            .clickable{
                            visibleOptions = false
                        }.animateContentSize(),
                            horizontalAlignment = Alignment.CenterHorizontally

                        ) {
                            if(visibleOptions){
                                ComponentOptionsOverlayPrincipalMenu()
                            }
                        }

                    }

                }
            }


        }
    }




    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()



        var auth = FirebaseAuth.getInstance()
        signInAnonymously(auth)
        setContent {
           // val firestoreProductViewModel: FirestoreProductViewModel = viewModel()
           // firestoreProductViewModel.uploadJsonFromAssets(this, "products.json")
            var isDark by rememberSaveable { mutableStateOf(false) }


            val navController = rememberNavController()
            val navViewModel: NavigationViewModel = viewModel()
            val gv: GlobalVariables = viewModel()
            MobilecuethingsTheme(darkTheme = isDark)
            {



                Scaffold(
                    modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.primary),
                    topBar = {
                        TopAppBar(
                            title = {ClickableAnimatedTitle(onClick = {gv.changeStatePanel()}) },
                            actions = {
                                if (gv.userName.isNotBlank()){
                                    Text(
                                        text = gv.userName,
                                        fontSize = 10.sp
                                    )

                                    IconButton(onClick = {
                                        gv.clearUserName()
                                        gv.updatePositionRoute("")
                                        navViewModel.navigateTo(
                                            route = LoginRoute,
                                            popUpToRoute = LoginRoute, // ruta tipada a hacer popUp
                                            inclusive = true           // incluye la ruta en el pop
                                        )
                                    }) {
                                        Icon(imageVector = Icons.Default.ExitToApp, contentDescription = "Logout")
                                    }
                                }


                                IconButton(onClick = { isDark = !isDark }) {
                                    Icon(imageVector = Icons.Default.Edit, contentDescription = "Toggle theme")
                                }
                            }
                        )
                    }
                )
                    {paddingValues ->


                        Box(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
                            NavigationWrapper(navController=navController,
                                setUserName = {usr->
                                    gv.updateUserName(usr)
                                },
                                username = gv.userName
                            )

                            if (gv.statePanel==true){
                                OverlayMenu()
                            }

                        }
                        }

            }
        }
    }
}


 */







class MainActivity : ComponentActivity() {

    private fun signInAnonymously(auth: FirebaseAuth) {
        auth.signInAnonymously()
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // La app se ha autenticado como un usuario anónimo válido
                    val user = auth.currentUser
                    Log.d("Auth", "signInAnonymously:success UID: ${user?.uid}")
                } else {
                    // Fallo de autenticación
                    Log.w("Auth", "signInAnonymously:failure", task.exception)
                }
            }
    }

    @Composable
    fun ClickableAnimatedTitle(onClick: () -> Unit) {
        val gv: GlobalVariables = viewModel()
        var playAnimation by remember { mutableStateOf(true) }
        val progress by animateFloatAsState(
            targetValue = if (playAnimation) 1f else 0f,
            animationSpec = tween(durationMillis = 900, easing = LinearEasing),
            finishedListener = { playAnimation = false }
        )

        // Brush de acento: cyan -> magenta -> purple
        val accentBrush = Brush.linearGradient(
            colors = listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.tertiary),
            start = Offset(0f, 0f),
            end = Offset(200f, 200f)
        )

        Column {
            Text(
                text = "CUE-THINGS",
                fontSize = 20.sp,
                lineHeight = 5.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .padding(2.dp)
                    .clickable { onClick() }
            )

            // subtítulo con menor énfasis
            Text(
                text = gv.positionRoute,
                fontSize = 12.sp,
                lineHeight = 5.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                modifier = Modifier
                    .padding(2.dp)
                    .clickable { onClick() }
            )
        }
    }

    @Composable
    fun ComponentRoutesOverlayPrincipalMenu() {
        val navViewModel: NavigationViewModel = viewModel()
        val gv: GlobalVariables = viewModel()
        Column(modifier = Modifier.fillMaxWidth(0.8f)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .fillMaxWidth(0.7f)
                    .clickable {
                        gv.changeStatePanel()
                        gv.updatePositionRoute(ConstantsRoutes.PUBLICATION.value)
                        navViewModel.navigateTo(PublicationRoute(""))
                    }
            ) {
                Text(
                    text = "Publicaciones",
                    fontSize = 16.sp,
                    fontStyle = FontStyle.Italic,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Spacer(Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .fillMaxWidth(0.7f)
                    .clickable {
                        gv.changeStatePanel()
                        gv.updatePositionRoute(ConstantsRoutes.CREATEPUBLICATION.value)
                        navViewModel.navigateTo(CreatePublicationRoute)
                    }
            ) {
                Text(
                    text = "Crear publicación",
                    fontSize = 16.sp,
                    fontStyle = FontStyle.Italic,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Spacer(Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .fillMaxWidth(0.7f)
                    .clickable {
                        gv.changeStatePanel()
                        gv.updatePositionRoute(ConstantsRoutes.MYPUBLICATION.value)
                        navViewModel.navigateTo(MyPublicationRoute)
                    }
            ) {
                Text(
                    text = "Mis publicaciones",
                    fontSize = 16.sp,
                    fontStyle = FontStyle.Italic,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Spacer(Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .fillMaxWidth(0.7f)
                    .clickable {
                        gv.changeStatePanel()
                        gv.updatePositionRoute(ConstantsRoutes.MYOFFER.value)
                        navViewModel.navigateTo(MyOffersRoute)
                    }
            ) {
                Text(
                    text = "Mis Ofertas",
                    fontSize = 16.sp,
                    fontStyle = FontStyle.Italic,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }

    @Composable
    fun ComponentOptionsOverlayPrincipalMenu() {
        val navViewModel: NavigationViewModel = viewModel()
        val gv: GlobalVariables = viewModel()

        Column(modifier = Modifier.fillMaxWidth(0.8f)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .fillMaxWidth(0.7f)
            ) {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "Perfil",
                    tint = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(text = "Perfil", fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurface)
            }

            Spacer(Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .fillMaxWidth(0.7f)
            ) {
                Icon(imageVector = Icons.Default.Build, contentDescription = "Configuración", tint = MaterialTheme.colorScheme.onSurface)
                Spacer(modifier = Modifier.width(12.dp))
                Text(text = "Configuración", fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurface)
            }

            Spacer(Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .fillMaxWidth(0.7f)
                    .clickable {
                        gv.changeStatePanel()
                        gv.clearUserName()
                        gv.updatePositionRoute("")
                        navViewModel.navigateTo(
                            route = LoginRoute,
                            popUpToRoute = LoginRoute,
                            inclusive = true
                        )
                    }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                    contentDescription = "Cerrar sesión",
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(text = "Cerrar sesión", fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurface)
            }
        }
    }


    @Composable
    fun OverlayMenu(onClick: () -> Unit) {
        val gv: GlobalVariables = viewModel()
        // controla visibilidad dentro del preview
        var visibleOptions by remember { mutableStateOf(false) }
        var visibleRoutes by remember { mutableStateOf(false) }


        Box(modifier = Modifier.fillMaxSize()) {
            // Fondo semitransparente
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.4f)).
                clickable{onClick()}
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(0.80f)
                        .padding(top = 10.dp, start = 10.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(24.dp)
                ) {
                    if (gv.userName!=""){
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                                    .clickable {
                                        visibleRoutes = !visibleRoutes
                                    }
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                    contentDescription = "Rutas",
                                    tint = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(text = "Rutas", fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurface)
                            }
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { visibleRoutes = false }
                                    .animateContentSize(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                if (visibleRoutes) {
                                    ComponentRoutesOverlayPrincipalMenu()
                                }
                            }
                        }
                    }

                    Spacer(Modifier.height(8.dp))

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .padding(vertical = 8.dp)
                                .fillMaxWidth()
                                .clickable {
                                    visibleOptions = !visibleOptions
                                }
                        ) {
                            Icon(imageVector = Icons.Default.Menu, contentDescription = "Opciones", tint = MaterialTheme.colorScheme.onSurface)
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(text = "Opciones", fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurface)
                        }
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { visibleOptions = false }
                                .animateContentSize(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            if (visibleOptions) {
                                ComponentOptionsOverlayPrincipalMenu()
                            }
                        }
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        var auth = FirebaseAuth.getInstance()

        signInAnonymously(auth)


        setContent {
            //val firestoreProductViewModel: FirestoreProductViewModel = viewModel()
            var isDark by rememberSaveable { mutableStateOf(false) }
           // val context = LocalContext.current
          //  LaunchedEffect(Unit) {
           //     firestoreProductViewModel.exportProductsToJson(context)
           // }

            val navController = rememberNavController()
            val navViewModel: NavigationViewModel = viewModel()
            val gv: GlobalVariables = viewModel()
           // val firestoreProductViewModel: FirestoreProductViewModel = viewModel()
           // firestoreProductViewModel.uploadJsonFromAssets(this, "products.json")
            MobilecuethingsTheme(darkTheme = true, dynamicColor = true) {
                Scaffold(
                    containerColor = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .fillMaxSize(),
                    topBar = {
                        TopAppBar(
                            title = { ClickableAnimatedTitle(onClick = { gv.changeStatePanel() }) },

                            actions = {
                                if (gv.userName.isNotBlank()) {
                                    Text(
                                        text = gv.userName,
                                        fontSize = 10.sp,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )

                                    IconButton(onClick = {
                                        gv.clearUserName()
                                        gv.updatePositionRoute("")
                                        navViewModel.navigateTo(
                                            route = LoginRoute,
                                            popUpToRoute = LoginRoute,
                                            inclusive = true
                                        )
                                    }) {
                                        Icon(imageVector = Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Logout", tint = MaterialTheme.colorScheme.primary)
                                    }
                                }

                                IconButton(onClick = { isDark = !isDark }) {
                                    Icon(imageVector = Icons.Default.Edit, contentDescription = "Toggle theme", tint = MaterialTheme.colorScheme.onSurface)
                                }
                            }
                        )
                    }
                ) { paddingValues ->
                    Box(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colorScheme.background
                        ){
                            NavigationWrapper(
                                navController = navController,
                                setUserName = { usr -> gv.updateUserName(usr) },
                                username = gv.userName
                            )
                        }

                        if (gv.statePanel == true) {
                            OverlayMenu(onClick = {gv.changeStatePanel()})
                        }
                    }
                }
            }
        }
    }
}







