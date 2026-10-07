package com.example.mobile_cue_things.screens

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mobile_cue_things.viewModel.global.GlobalVariables
import com.example.mobile_cue_things.enums.ConstantsRoutes
import com.example.mobile_cue_things.ui.theme.MobilecuethingsTheme
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await



/*

/*
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun LoginScreen(goToHome:(String)-> Unit){
    val userName = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val passwordVisibility = remember { mutableStateOf(false) }
    val context = LocalContext.current
    val onLogingIn = remember { mutableStateOf(false) }
    val dbFirebase = Firebase.firestore

        Column(modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {

            Text(
                text = "Bienvenido",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(0.8f),
                placeholder = {Text(text = "Nombre de usuario")},
                value = userName.value,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                onValueChange = {
                    if (it.length <= 25){
                        userName.value = it
                    }
                }
            )

            Spacer(modifier = Modifier.height(10.dp))


            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(0.8f),
                placeholder = { Text(text = "Contraseña") },
                value = password.value,
                onValueChange = {
                    if (it.length<=15){
                        password.value = it }
                    },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),

                visualTransformation = if (passwordVisibility.value) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(
                        onClick = {
                            passwordVisibility.value = !passwordVisibility.value
                        }
                    ) {
                        Icon(

                            imageVector = if (passwordVisibility.value) {
                                Icons.Default.Check
                            } else {
                                Icons.Default.Close
                            },
                            contentDescription = null
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(20.dp))
            OutlinedButton (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp, vertical = 3.dp)
                    .height(70.dp),
                onClick = {
                    if (userName.value.isBlank() || password.value.isBlank()){
                        Toast.makeText(context, "Ingrese la contraseña y usuario", Toast.LENGTH_SHORT).show()
                    }else{
                        onLogingIn.value = true
                    }
                }
            ){
                Text("Iniciar Sesión")
            }

        }

        if (onLogingIn.value == true){

            val emailToFind = userName.value
            val passwordToFind = password.value
            val gv: GlobalVariables = viewModel(
                viewModelStoreOwner = LocalContext.current as ViewModelStoreOwner
            )

            dbFirebase.collection("users")
                .whereEqualTo("email", emailToFind)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    if (!querySnapshot.isEmpty) {
                        // Tomamos el primer documento que coincida (normalmente sólo debe haber uno)
                        val doc = querySnapshot.documents[0]
                        val pwdStored = doc.getString("password") // null-safe

                        if (pwdStored != null && pwdStored == passwordToFind) {
                            Toast.makeText(context, "OK", Toast.LENGTH_SHORT).show()
                            gv.updatePositionRoute(ConstantsRoutes.DASHBOARD.value)
                            goToHome(emailToFind)
                        } else {
                            Toast.makeText(context, "Usuario no encontrado", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(context, "Usuario no encontrado", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(context, "Error al buscar usuario: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
            onLogingIn.value = false
        }
}




 */

// imports (añade/asegura que estén presentes)
import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mobile_cue_things.viewModel.global.GlobalVariables
import com.example.mobile_cue_things.enums.ConstantsRoutes
import com.example.mobile_cue_things.ui.theme.MobilecuethingsTheme
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun LoginScreen(goToHome: (String) -> Unit) {
    // Estados de UI
    val userName = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val passwordVisibility = remember { mutableStateOf(false) }
    val isLoading = remember { mutableStateOf(false) }

    val context = LocalContext.current
    val gv: GlobalVariables = viewModel(viewModelStoreOwner = LocalContext.current as ViewModelStoreOwner)
    val dbFirebase = Firebase.firestore

    // Trigger para lanzar la búsqueda (evitamos ejecutar queries en recomposiciones)
    var triggerLogin by remember { mutableStateOf(false) }

    // Efecto que se ejecuta una sola vez cuando triggerLogin se vuelve true
    LaunchedEffect(key1 = triggerLogin) {
        if (!triggerLogin) return@LaunchedEffect
        isLoading.value = true
        try {
            val emailToFind = userName.value.trim()
            val passwordToFind = password.value

            val snapshot = dbFirebase.collection("users")
                .whereEqualTo("email", emailToFind)
                .get()
                .await()

            if (!snapshot.isEmpty) {
                val doc = snapshot.documents[0]
                val pwdStored = doc.getString("password")
                if (pwdStored != null && pwdStored == passwordToFind) {
                    Toast.makeText(context, "OK", Toast.LENGTH_SHORT).show()
                    gv.updatePositionRoute(ConstantsRoutes.DASHBOARD.value)
                    goToHome(emailToFind)
                } else {
                    Toast.makeText(context, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "Usuario no encontrado", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Toast.makeText(context, "Error al buscar usuario: ${e.message}", Toast.LENGTH_LONG).show()
        } finally {
            isLoading.value = false
            triggerLogin = false
        }
    }

    // UI
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Título
            Text(
                text = "Bienvenido",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Subtítulo
            Text(
                text = "Inicia sesión para continuar",
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.75f),
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Username
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(0.9f),
                value = userName.value,
                onValueChange = { if (it.length <= 50) userName.value = it },
                placeholder = { Text("Nombre de usuario", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)) },
                label = { Text("Usuario") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                colors = TextFieldDefaults.colors(
                    // indicator = border/underline colors for Material3
                    focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                    unfocusedIndicatorColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
                    cursorColor = MaterialTheme.colorScheme.primary,
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    unfocusedLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                    disabledTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                    // Containers: leave surface so fields look on-theme (MD3 uses container colors)
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Password
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(0.9f),
                value = password.value,
                onValueChange = { if (it.length <= 30) password.value = it },
                placeholder = { Text("Contraseña", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)) },
                label = { Text("Contraseña") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = if (passwordVisibility.value) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { passwordVisibility.value = !passwordVisibility.value }) {
                        Icon(
                            imageVector = if (passwordVisibility.value) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = if (passwordVisibility.value) "Ocultar contraseña" else "Mostrar contraseña",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                    unfocusedIndicatorColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
                    cursorColor = MaterialTheme.colorScheme.primary,
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    unfocusedLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Botón principal
            Button(
                onClick = {
                    if (userName.value.isBlank() || password.value.isBlank()) {
                        Toast.makeText(context, "Ingrese la contraseña y usuario", Toast.LENGTH_SHORT).show()
                    } else {
                        triggerLogin = true
                    }
                },
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                if (isLoading.value) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Ingresando...", color = MaterialTheme.colorScheme.onPrimary)
                } else {
                    Text("Iniciar Sesión", color = MaterialTheme.colorScheme.onPrimary)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLoginScreen() {
    MobilecuethingsTheme {
        LoginScreen(goToHome = { })
    }
}



 */



@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun LoginScreen(goToHome: (String) -> Unit) {
    // Estados de UI
    val userName = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val passwordVisibility = remember { mutableStateOf(false) }
    val isLoading = remember { mutableStateOf(false) }

    val context = LocalContext.current
    val gv: GlobalVariables = viewModel(viewModelStoreOwner = LocalContext.current as ViewModelStoreOwner)
    val dbFirebase = Firebase.firestore

    // Trigger para lanzar la búsqueda (evitamos ejecutar queries en recomposiciones)
    var triggerLogin by remember { mutableStateOf(false) }

    // Efecto que se ejecuta una sola vez cuando triggerLogin se vuelve true
    LaunchedEffect(key1 = triggerLogin) {
        if (!triggerLogin) return@LaunchedEffect
        isLoading.value = true
        try {
            val emailToFind = userName.value.trim()
            val passwordToFind = password.value

            val snapshot = dbFirebase.collection("users")
                .whereEqualTo("email", emailToFind)
                .get()
                .await()

            if (!snapshot.isEmpty) {
                val doc = snapshot.documents[0]
                val pwdStored = doc.getString("password")
                if (pwdStored != null && pwdStored == passwordToFind) {
                    Toast.makeText(context, "OK", Toast.LENGTH_SHORT).show()
                    gv.updatePositionRoute(ConstantsRoutes.DASHBOARD.value)
                    goToHome(emailToFind)
                } else {
                    Toast.makeText(context, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "Usuario no encontrado", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Toast.makeText(context, "Error al buscar usuario: ${e.message}", Toast.LENGTH_LONG).show()
        } finally {
            isLoading.value = false
            triggerLogin = false
        }
    }

    // UI
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Título
            Text(
                text = "Bienvenido",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Subtítulo
            Text(
                text = "Inicia sesión para continuar",
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.75f),
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Username
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(0.9f),
                value = userName.value,
                onValueChange = { if (it.length <= 50) userName.value = it },
                placeholder = { Text("Nombre de usuario", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)) },
                label = { Text("Usuario") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                colors = TextFieldDefaults.colors(

                    focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                    unfocusedIndicatorColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
                    cursorColor = MaterialTheme.colorScheme.primary,
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    unfocusedLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                    disabledTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Password
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(0.9f),
                value = password.value,
                onValueChange = { if (it.length <= 30) password.value = it },
                placeholder = { Text("Contraseña", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)) },
                label = { Text("Contraseña") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = if (passwordVisibility.value) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { passwordVisibility.value = !passwordVisibility.value }) {
                        Icon(
                            imageVector = if (passwordVisibility.value) Icons.Default.Check else Icons.Default.Close,
                            contentDescription = if (passwordVisibility.value) "Ocultar contraseña" else "Mostrar contraseña",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                    unfocusedIndicatorColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
                    cursorColor = MaterialTheme.colorScheme.primary,
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    unfocusedLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Botón principal
            Button(
                onClick = {
                    if (userName.value.isBlank() || password.value.isBlank()) {
                        Toast.makeText(context, "Ingrese la contraseña y usuario", Toast.LENGTH_SHORT).show()
                    } else {
                        triggerLogin = true
                    }
                },
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                if (isLoading.value) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Ingresando...", color = MaterialTheme.colorScheme.onPrimary)
                } else {
                    Text("Iniciar Sesión", color = MaterialTheme.colorScheme.onPrimary)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLoginScreen() {
    MobilecuethingsTheme {
        LoginScreen(goToHome = { })
    }
}