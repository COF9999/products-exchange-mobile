package com.example.mobile_cue_things.screens

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.mobile_cue_things.viewModel.product.CreateProductViewModel
import com.example.mobile_cue_things.viewModel.product.UploadState
import androidx.compose.runtime.livedata.observeAsState
import com.example.mobile_cue_things.enums.
import java.net.URI

@Composable
fun CreatePublicationScreen(email:String,
                            viewModel: CreateProductViewModel = viewModel(),
                            navigateToHome:()-> Unit
){

    val context = LocalContext.current
    var nameUX by remember { mutableStateOf("") }
    var descriptionUX by remember { mutableStateOf("") }
    var priceUX by remember { mutableStateOf("") }
    var conditionUX by remember { mutableStateOf("") }
    var categoryUX by remember { mutableStateOf("") }
    var brandUX by remember { mutableStateOf("") }


    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var options = listOf("","A","B","C","D")
    // Observa estado para mostrar progreso/resultado
    val state by viewModel.state.observeAsState(UploadState.Idle)

    FormProductWithStickyButton(
        nameUX = nameUX,
        nameUXSet = { nameUX = it },
        descriptionUX = descriptionUX,
        descriptionUXSet = { descriptionUX = it },
        priceUX = priceUX,
        priceUXSet = { priceUX = it },
        conditionUX = conditionUX,
        conditionUXSet = {conditionUX=it},
        categoryUX= categoryUX,
        categoryUXSet = {categoryUX = it},
        selectedImageUri = { value -> selectedImageUri = value },
        options = options,
        onSubmit = {
            viewModel.uploadProduct(
                name = nameUX,
                description = descriptionUX,
                price = priceUX.toFloat(),
                selectedImageUri = selectedImageUri,
                email = email,
                context = context.applicationContext
            )
        }

    )


    when (state) {
        is UploadState.Uploading ->  CircularProgressIndicator()
        is UploadState.Success -> {navigateToHome()}
        is UploadState.Failed -> { }
        else -> {}
    }

}

/*
@Composable
fun formCreateProduct(
    nameUX:String,
    nameUXSet: (String)-> Unit,
    descriptionUX: String,
    descriptionUXSet: (String)-> Unit,
    priceUX: String,
    priceUXSet: (String)-> Unit,
    conditionUX: String,
    categoryUX: String,
    categoryUXSet : (String)-> Unit,
    conditionUXSet: (String)-> Unit,
    selectedImageUri: (Uri?)-> Unit,
    onSubmit: ()-> Unit
) {
    val opciones = listOf("Opción A", "Opción B", "Opción C", "Opción D")
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(scrollState),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.80f)
                .height(800.dp)
                .background(
                    color =  Color.Transparent,
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(10.dp),

            verticalArrangement = Arrangement.spacedBy(16.dp)
        ){
            Box(modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ){
                Text("Crear Producto")
            }
            Column (modifier = Modifier
                .weight(9f)
                .fillMaxWidth()
                .padding(vertical = 5.dp, horizontal = 10.dp)
            ){

                TextUpField(label = "Nombre Producto") { (nameUX!="") }
                Box(modifier = Modifier.fillMaxWidth()){
                    TextUpField(label = "Nombre Producto") { (nameUX=="") }

                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(1f)
                            .padding(0.dp),
                        value = nameUX,

                        onValueChange = { input ->
                            // Validar si la entrada completa coincide con los caracteres permitidos
                            if (input.matches(ConstantsValidateInputs.ALLOWCHARREGEXNAMEPRODUCT.regex)) {
                                nameUXSet(input)
                            }
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        placeholder = {"Nombre de usuario"},
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                TextUpField(label = "Descripción") { (descriptionUX!="") }
                Box(){
                    TextUpField(label = "Descripción") { (descriptionUX=="") }
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = {"Descripción de usuario"},
                        value = descriptionUX,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        onValueChange = { input ->
                            // Validar si la entrada completa coincide con los caracteres permitidos
                            if (input.matches(ConstantsValidateInputs.ALLOWONLYPRINCIPALCHARACTERS.regex)) {
                                descriptionUXSet(input)
                            }
                        }
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                TextUpField(label = "Precio") { (priceUX!="") }
                Box(){
                    TextUpField(label = "Precio") { (priceUX=="") }
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = {"Descripción de usuario"},
                        value = priceUX,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        onValueChange = { input ->
                            val isValidInput = input.all { it.isDigit() } && input.length <= 8 &&
                                    // Nueva validación: Si la cadena no está vacía, no debe empezar por "0"
                                    // a menos que sea el único carácter permitido por el momento (ej: si se permite solo "0" como valor válido)
                                    (input.isEmpty() || !input.startsWith('0'))

                            if (isValidInput) {
                                priceUXSet(input)
                            }
                        }
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                TextUpField(label = "Condición") { (conditionUX!="") }
                Box(){
                    TextUpField(label = "Condición") { (conditionUX=="") }
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = conditionUX,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        onValueChange = { input ->
                            val isValidInput = input.all { it.isDigit() } && input.length <= 1 &&
                                    // Nueva validación: Si la cadena no está vacía, no debe empezar por "0"
                                    // a menos que sea el único carácter permitido por el momento (ej: si se permite solo "0" como valor válido)
                                    (input.isEmpty() || !input.startsWith('0'))

                            if (isValidInput) {
                                conditionUXSet(input)
                            }
                        }
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                DropDownMenu(opciones,categoryUX,categoryUXSet)
                Spacer(modifier = Modifier.height(10.dp))
                SingleImagePicker(onImageSelected = selectedImageUri)
                Spacer(modifier = Modifier.height(5.dp))
                Button(onClick = { onSubmit()}, modifier = Modifier.fillMaxWidth()) {
                    Text("Subir")
                }
            }
        }
    }
}

 */



/**
 * Reusable labeled outlined field that shows optional error/help text below the field.
 */
@Composable
fun LabeledOutlinedField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String = "",
    singleLine: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    isError: Boolean = false,
    helperText: String? = null,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            singleLine = singleLine,
            keyboardOptions = keyboardOptions,
            placeholder = { if (placeholder.isNotBlank()) Text(placeholder) },
            label = { Text(label) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
                focusedLeadingIconColor = MaterialTheme.colorScheme.primary,
                cursorColor = MaterialTheme.colorScheme.primary,
                focusedLabelColor = MaterialTheme.colorScheme.primary,
                unfocusedLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                focusedTrailingIconColor = MaterialTheme.colorScheme.primary,
                errorBorderColor = MaterialTheme.colorScheme.error,
                errorLabelColor = MaterialTheme.colorScheme.error
            ),
            isError = isError
        )

        if (!helperText.isNullOrBlank()) {
            Text(
                text = helperText,
                style = MaterialTheme.typography.bodySmall,
                color = if (isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                modifier = Modifier
                    .padding(top = 6.dp, start = 8.dp)
            )
        }
    }
}

/**
 * Field for numeric input with simple validation rules.
 */
@Composable
fun NumberField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    maxLength: Int = 8,
    allowLeadingZero: Boolean = false,
    helperText: String? = null,
    modifier: Modifier = Modifier
) {
    val isError = remember(value) {
        // optionally mark error when input starts with 0 and not allowed
        (!allowLeadingZero && value.length > 1 && value.startsWith('0'))
    }

    LabeledOutlinedField(
        label = label,
        value = value,
        onValueChange = { input ->
            val isDigitsOnly = input.all { it.isDigit() }
            val lengthOk = input.length <= maxLength
            if (isDigitsOnly && lengthOk) onValueChange(input)
        },
        placeholder = "",
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        isError = isError,
        helperText = helperText,
        modifier = modifier
    )
}

/**
 * Multiline text area for description
 */
@Composable
fun MultiLineField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    maxLines: Int = 4,
    helperText: String? = null,
    modifier: Modifier = Modifier
) {
    LabeledOutlinedField(
        label = label,
        value = value,
        onValueChange = onValueChange,
        placeholder = "",
        singleLine = false,
        keyboardOptions = KeyboardOptions.Default,
        isError = false,
        helperText = helperText,
        modifier = modifier
    )
}

/**
 * FormProduct: formulario completo que usa los campos refactorizados.
 *
 * Parámetros:
 * - name, description, price, condition, category: valores y setters
 * - opciones: lista de opciones para DropDownMenu (la función DropDownMenu la dejas como la tienes)
 * - selectedImageUri: lambda que recibe la uri del selector de imagen (mantener SingleImagePicker)
 * - onSubmit: callback cuando se pulsa 'Subir'
 *
 * Recuerda pasar las regex/constantes de validación (ej: ConstantsValidateInputs) desde tu proyecto.
 */



@Composable
fun FormProductWithStickyButton(
    // mismos params...
    nameUX: String,
    nameUXSet: (String) -> Unit,
    descriptionUX: String,
    descriptionUXSet: (String) -> Unit,
    priceUX: String,
    priceUXSet: (String) -> Unit,
    conditionUX: String,
    conditionUXSet: (String) -> Unit,
    categoryUX: String,
    categoryUXSet: (String) -> Unit,
    options: List<String>,
    selectedImageUri: (Uri?) -> Unit,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        // La parte scrollable ocupa el espacio restante (weight = 1)
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 12.dp, vertical = 12.dp)
        ) {
            FormProduct(
                nameUX = nameUX,
                nameUXSet = nameUXSet,
                descriptionUX = descriptionUX,
                descriptionUXSet = descriptionUXSet,
                priceUX = priceUX,
                priceUXSet = priceUXSet,
                conditionUX = conditionUX,
                conditionUXSet = conditionUXSet,
                categoryUX = categoryUX,
                categoryUXSet = categoryUXSet,
                options = options,
                selectedImageUri = selectedImageUri,
                onSubmit = { /* no-op: botón abajo llamará a onSubmit */ }
            )

            Spacer(modifier = Modifier.height(12.dp))
        }

        // Botón fijo en la parte inferior — respetar keyboard con imePadding
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .imePadding()
                .padding(12.dp)
        ) {
            Button(
                onClick = onSubmit,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                enabled = false
            ) {
                Text(text = "Subir")
            }
        }
    }
}


@Composable
fun FormProduct(
    nameUX: String,
    nameUXSet: (String) -> Unit,
    descriptionUX: String,
    descriptionUXSet: (String) -> Unit,
    priceUX: String,
    priceUXSet: (String) -> Unit,
    conditionUX: String,
    conditionUXSet: (String) -> Unit,
    categoryUX: String,
    categoryUXSet: (String) -> Unit,
    options: List<String>,
    selectedImageUri: (Uri?) -> Unit,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier
) {


    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 12.dp)
    ) {
        // Nombre producto (validación con regex que ya estabas usando)
        LabeledOutlinedField(
            label = "Nombre Producto",
            value = nameUX,
            onValueChange = { input ->
                if (input.matches(ConstantsValidateInputs.ALLOWCHARREGEXNAMEPRODUCT.regex)) {
                    nameUXSet(input)
                }
            },
            helperText = if (nameUX.isBlank()) "Campo requerido" else null
        )

        Spacer(modifier = Modifier.height(10.dp))

        MultiLineField(
            label = "Descripción",
            value = descriptionUX,
            onValueChange = { input ->
                if (input.matches(ConstantsValidateInputs.ALLOWONLYPRINCIPALCHARACTERS.regex)) {
                    descriptionUXSet(input)
                }
            },
            helperText = if (descriptionUX.isBlank()) "Campo requerido" else null
        )

        Spacer(modifier = Modifier.height(10.dp))

        NumberField(
            label = "Precio",
            value = priceUX,
            onValueChange = { input ->
                // validación: solo dígitos, max 8, no leading zero
                val isValidInput = input.all { it.isDigit() } && input.length <= 8 && (input.isEmpty() || !input.startsWith('0'))
                if (isValidInput) priceUXSet(input)
            },
            maxLength = 8,
            allowLeadingZero = false,
            helperText = if (priceUX.isBlank()) "Campo requerido" else null
        )

        Spacer(modifier = Modifier.height(10.dp))

        NumberField(
            label = "Condición (1-9)",
            value = conditionUX,
            onValueChange = { input ->
                val isValidInput = input.all { it.isDigit() } && input.length <= 1 && (input.isEmpty() || !input.startsWith('0'))
                if (isValidInput) conditionUXSet(input)
            },
            maxLength = 1,
            allowLeadingZero = false,
            helperText = if (conditionUX.isBlank()) "Campo requerido" else null
        )

        Spacer(modifier = Modifier.height(10.dp))

        // DropDownMenu: usalo tal como lo tienes (opciones, categoryUX, categoryUXSet)
        DropDownMenu(options = options, categoryUX = categoryUX, categoryUXSet = categoryUXSet)

        Spacer(modifier = Modifier.height(10.dp))

        // Selector de imagen: mantiene SingleImagePicker
        SingleImagePicker(onImageSelected = selectedImageUri)

    }
}

@Composable
fun SingleImagePicker(
    onImageSelected: (Uri?) -> Unit = {}
) {
    val context = LocalContext.current
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    // Permiso: en Android 13+ es READ_MEDIA_IMAGES; antes READ_EXTERNAL_STORAGE
    val neededPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        Manifest.permission.READ_MEDIA_IMAGES
    } else {
        Manifest.permission.READ_EXTERNAL_STORAGE
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        // no hacemos nada aquí; el launcher de contenido se encarga si hay permiso
    }

    // Selector de contenido para una sola imagen
    val pickImageLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
        onImageSelected(uri)
    }

    Column(modifier = Modifier
        .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(5.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            enabled = true,
            onClick = {
            // pedir permiso si no está concedido (opcional, GetContent muchas veces abre sin permiso)
            val hasPermission = ContextCompat.checkSelfPermission(context, neededPermission) ==
                    PackageManager.PERMISSION_GRANTED
            if (!hasPermission) {
                permissionLauncher.launch(neededPermission)
            }
            pickImageLauncher.launch("image/*")
        }) {
            Text("Seleccionar imagen")
        }

        imageUri?.let { uri ->
            // Coil AsyncImage muestra la imagen desde la Uri
            AsyncImage(
                model = uri,
                contentDescription = "Imagen seleccionada",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
            )
        } ?: Text("No hay imagen seleccionada")
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropDownMenu(options:List<String>, categoryUX: String, categoryUXSet: (String) -> Unit){

    var expanded by remember { mutableStateOf(false) }


    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded // Alterna el estado de expansión al hacer clic
        },
        modifier = Modifier.fillMaxWidth()
    ) {
        // El campo de texto (TextField) que siempre está visible
        TextField(
            // El valor del TextField es la opción seleccionada
            value = categoryUX,
            onValueChange = {}, // No permitimos que el usuario escriba directamente
            readOnly = true,    // Hacemos que sea de solo lectura para forzar la selección del menú
            label = { Text("Categoría") },
            trailingIcon = {
                // Icono que cambia dependiendo de si el menú está abierto o cerrado
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                // Importante: Usar este modificador de Compose Material para que funcione correctamente el anclaje del menú
                .menuAnchor()
        )

        // El menú desplegable que aparece cuando 'expanded' es verdadero
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                // Se cierra el menú si el usuario hace clic fuera de él
                expanded = false
            }
        ) {
            // Iteramos sobre nuestra lista de datos quemados para mostrar cada opción
            options.forEach { opcion ->
                DropdownMenuItem(
                    text = { Text(text = opcion) },
                    onClick = {
                        // Cuando se hace clic en un ítem:
                        categoryUXSet(opcion) // 1. Actualizamos la opción seleccionada
                        expanded = false               // 2. Cerramos el menú
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                )
            }
        }
    }
}




@Composable
fun TextUpField(padding: Dp = 16.dp,label:String,predicate: ()-> Boolean ){
    Text(modifier = Modifier
        .padding(start = padding),
        text =  if (predicate()) label else ""
    )
}