package com.example.mobile_cue_things.ux.overlay

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mobile_cue_things.enums.ConstantsValidateInputs
import com.example.mobile_cue_things.viewModel.global.GlobalVariables
import com.example.mobile_cue_things.viewModel.offer.OfferViewModel



fun CreateOfferCaller(
    offerViewModel: OfferViewModel,
    ownerProduct: String,
    emailOffer: String,
    idProduct: String,
    priceToOffer: Float,
    productToOffer: String,
    onResult: (Boolean) -> Unit
) {

        offerViewModel.createOfferCaller(
            ownerProduct = ownerProduct,
            emailOffer = emailOffer,
            idProduct = idProduct,
            priceToOffer = priceToOffer,
            productToOffer = productToOffer,
            onResult = onResult
        )

}

@Composable
fun OverlayCreateOffer(
    ownerProduct: String,
    idProduct: String,
    closePopUp: () -> Unit
) {

    var producto by rememberSaveable { mutableStateOf("") }
    var precio by rememberSaveable { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }
    var isEnabledButton by remember { mutableStateOf(true) }
    val gv: GlobalVariables = viewModel(
        viewModelStoreOwner = LocalContext.current as ViewModelStoreOwner
    )
    val context = LocalContext.current
    val offerViewModel: OfferViewModel = viewModel()



    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Fondo oscurecido
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))
                .clickable { closePopUp() }
        )

        Column(
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .align(Alignment.Center)
                .background(
                    MaterialTheme.colorScheme.surface,
                    RoundedCornerShape(16.dp)
                )
                .padding(24.dp)
        ) {

            Text(
                text = "Crear Oferta",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = producto,
                onValueChange = { input ->
                    // Validar si la entrada completa coincide con los caracteres permitidos
                    if (input.matches(ConstantsValidateInputs.ALLOWCHARREGEXNAMEPRODUCT.regex)) {
                        producto = input
                    }
                },
                label = { Text("Producto a ofertar") },
                // Opcional: puedes dejar el keyboardType como Default o Text
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = precio,
                onValueChange = { input ->
                    val isValidInput = input.all { it.isDigit() } && input.length <= 8 &&
                            // Nueva validación: Si la cadena no está vacía, no debe empezar por "0"
                            // a menos que sea el único carácter permitido por el momento (ej: si se permite solo "0" como valor válido)
                            (input.isEmpty() || !input.startsWith('0'))

                    if (isValidInput) {
                        precio = input
                    }
                },
                label = { Text("Precio a ofertar") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            error?.let {
                Text(text = it, color = Color.Red)
                Spacer(modifier = Modifier.height(8.dp))
            }

            Button(
                onClick = {
                    var priceOffer: Float? = 0f
                    var productOffer:String = ""
                    isEnabledButton=false
                    try {
                        priceOffer = precio.toFloatOrNull()
                        productOffer = producto
                    }catch (e: Exception){
                        error = "Ingrese valores correctos"
                        isEnabledButton=true
                        return@Button
                    }


                    if (priceOffer == null && productOffer=="") {
                        error = "Por favor ingrese almenos un campo"
                        isEnabledButton=true
                        return@Button
                    }

                    error = null

                    // LLAMADA REAL
                    CreateOfferCaller(
                        offerViewModel = offerViewModel,
                        ownerProduct = ownerProduct,
                        emailOffer = gv.userName,        // <- tu email correcto
                        idProduct = idProduct,
                        priceToOffer = priceOffer ?: 0f,
                        productToOffer = productOffer
                    ) { created ->

                        if (created) {
                            Toast.makeText(
                                context,
                                "Oferta creada con éxito",
                                Toast.LENGTH_SHORT
                            ).show()
                            isEnabledButton=true
                            closePopUp()
                        }else if (ownerProduct == gv.userName) {
                            isEnabledButton=true
                            Toast.makeText(context, "No puedes ofertar tus propios productos", Toast.LENGTH_SHORT).show()
                            closePopUp()
                        } else {
                            isEnabledButton=true
                            Toast.makeText(
                                context,
                                "Ya existe una oferta realizada por este usuario",
                                Toast.LENGTH_SHORT
                            ).show()
                            closePopUp()
                        }
                    }
                },
                enabled = isEnabledButton,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Ofertar")
            }
        }
    }
}

