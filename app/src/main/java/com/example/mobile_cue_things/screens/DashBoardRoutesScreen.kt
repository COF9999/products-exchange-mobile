package com.example.mobile_cue_things.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mobile_cue_things.ui.theme.AccentCyan
import com.example.mobile_cue_things.ui.theme.AccentMagenta
import com.example.mobile_cue_things.ui.theme.MobilecuethingsTheme
import com.example.mobile_cue_things.ui.theme.SurfaceBlue


/*
@Composable
@Preview
fun preViewDashBoardRoutesScren(){
    DashBoardRoutesScreen(goToHome ={}, goToCreateProduct = {})
}


@Composable
fun DashBoardRoutesScreen(
    goToHome: () -> Unit,
    goToCreateProduct: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(50.dp),
        verticalArrangement = Arrangement.spacedBy(
            85.dp,
            Alignment.CenterVertically
        )
    ) {
        BoxButtonGoRoute(goToHome,"Home")

        BoxButtonGoRoute(goToCreateProduct,"Crear Producto")
    }
}


@Composable
fun BoxButtonGoRoute(definedRoute:()-> Unit, infoRoute:String){

       Box(){
           Button(
               onClick={definedRoute()},
               modifier = Modifier
                   .fillMaxWidth()
                   .height(54.dp),
               shape = RoundedCornerShape(16.dp),
               elevation = ButtonDefaults.buttonElevation(8.dp),
               colors = ButtonDefaults.buttonColors(
                   containerColor = Color(0xFF6200EE),
                   contentColor = Color.White
               ),
               contentPadding = PaddingValues(horizontal = 20.dp)
           ) {
               Row(
                   modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
               ) {
                   Text(infoRoute, fontSize = 18.sp)
                   Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null)
               }
           }
       }
}

 */

@Preview(showBackground = true)
@Composable
fun preViewDashBoardRoutesScren() {
    MobilecuethingsTheme(darkTheme = true) {
        DashBoardRoutesScreen(goToHome = {}, goToCreateProduct = {})
    }
}

@Composable
fun DashBoardRoutesScreen(
    goToHome: () -> Unit,
    goToCreateProduct: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = "Panel",
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(28.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth(0.92f),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            BoxButtonGoRoute(onClick = goToHome, infoRoute = "Publicaciones")
            BoxButtonGoRoute(onClick = goToCreateProduct, infoRoute = "Crear Producto")
        }
    }
}

/** Botón con gradiente sutil y sombra discreta */
@Composable
fun BoxButtonGoRoute(onClick: () -> Unit, infoRoute: String) {
    val corner = 14.dp
    // Gradiente sutil: mezcla acentos pero con baja opacidad
    val gentleGradient = Brush.horizontalGradient(
        colors = listOf(
            SurfaceBlue.copy(alpha = 0.88f),
            SurfaceBlue.copy(alpha = 1f)
        )
    )

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp),
        shape = RoundedCornerShape(corner),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 4.dp
    ) {
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(6.dp)
            .clickable{onClick()}) {

            // Fondo degradado muy sutil dentro del botón
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(corner))
                    .background(brush = gentleGradient)
                    .shadow(
                        elevation = 6.dp,
                        shape = RoundedCornerShape(corner),
                        ambientColor = AccentCyan.copy(alpha = 0.06f),
                        spotColor = AccentMagenta.copy(alpha = 0.04f)
                    )
            )

            // Contenido: texto + icon
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 18.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = infoRoute,
                    color = MaterialTheme.colorScheme.onSurface, // contraste fuerte
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium
                )

                // Icono con un pequeño fondo circular sutil
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.18f),
                    modifier = Modifier.size(36.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}


