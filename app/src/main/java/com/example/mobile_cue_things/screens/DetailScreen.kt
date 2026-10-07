package com.example.mobile_cue_things.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mobile_cue_things.enums.ConstantsRoutes
import com.example.mobile_cue_things.model.Product
import com.example.mobile_cue_things.ux.overlay.OverlayCreateOffer
import com.example.mobile_cue_things.viewModel.global.GlobalVariables
import com.example.mobile_cue_things.viewModel.product.FirestoreProductViewModel

@Composable
fun KeyValueTable(
    data: List<Pair<String, String>>,
    modifier: Modifier = Modifier
) {
    // Colores derivados del Theme (se adaptan automáticamente a dark/light)
    val bg = MaterialTheme.colorScheme.surface               // fondo del contenedor
    val borderColor = MaterialTheme.colorScheme.outline     // borde suave
    val rowAlt = MaterialTheme.colorScheme.surfaceVariant   // color alterno de fila
    val textColor = MaterialTheme.colorScheme.onSurface

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(bg)
            .border(width = 1.dp, color = borderColor, shape = RoundedCornerShape(12.dp))
            .padding(8.dp),
        color = bg
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            data.forEachIndexed { index, (key, value) ->
                val rowColor = if (index % 2 == 0) bg else rowAlt

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(rowColor, shape = RoundedCornerShape(6.dp))
                        .padding(vertical = 10.dp, horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = key,
                        modifier = Modifier.weight(0.4f),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 15.sp,
                        color = textColor
                    )
                    Text(
                        text = value,
                        modifier = Modifier.weight(0.6f),
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Normal),
                        fontSize = 10.sp,
                        color = textColor
                    )
                }
            }
        }
    }
}

@Composable
fun DetailScreen(id: String, navigateToHome:()-> Unit, firestoreProductViewModel: FirestoreProductViewModel){
    var product: Product? by remember { mutableStateOf<Product?>(null) }
    var isExpanded by rememberSaveable { mutableStateOf(true) }
    var isOverlayOffer by remember { mutableStateOf(false) }
    val gv: GlobalVariables = viewModel(
        viewModelStoreOwner = LocalContext.current as ViewModelStoreOwner
    )

    gv.updatePositionRoute(ConstantsRoutes.DETAILPUBLICATION.value)
    LaunchedEffect(id) {
        product = firestoreProductViewModel.loadProductById(id)
    }

    product?.let { p ->
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
                ProductItemDetail(
                    product = p,
                    isExpanded = isExpanded,
                    setIsExpandend = { isExpanded = !isExpanded },
                    setActiveOverlayCreateOffer = {isOverlayOffer=true}
                )

                Spacer(modifier = Modifier.height(20.dp))


                if (isExpanded) {
                    KeyValueTable(
                        data = listOf(
                            "ID" to p.id,
                            "Nombre" to p.name,
                            "Marca" to p.brand,
                            "Categoria" to p.category,
                            "Descripción" to p.description,
                            "Precio" to "$${p.price}",
                            "Condición" to (p.condition.toString()),
                            "Publicado " to p.createdAt,
                            "Dueño" to p.emailUser

                        )
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))
        }

        if (isOverlayOffer){
            OverlayCreateOffer(ownerProduct =p.emailUser , idProduct = p.id, closePopUp = {isOverlayOffer=false})
        }
    }
}

@Composable
fun ProductItemDetail(product: Product, isExpanded: Boolean, setIsExpandend :()-> Unit,setActiveOverlayCreateOffer:()-> Unit) {
    var expanded by rememberSaveable { mutableStateOf(false) }


    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.Top) {
            SmartProductImageComposable(product.imgUrl, modifier = Modifier
                .size(120.dp)
                .clip(RoundedCornerShape(8.dp))
            )

            Spacer(modifier = Modifier.width(8.dp))

            Column(modifier = Modifier // ahora sí llenará la altura del Row
                .weight(1f) // opcional: ocupa todo el espacio horizontal disponible
                .padding(4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "$${product.price}",
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = if (expanded) "Ocultar Detalle" else "Ver detalle",
                    modifier = Modifier.clickable {
                        setIsExpandend()
                    },
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row {
                    Button(onClick = {setActiveOverlayCreateOffer()}) {
                        Text("Ofertar" ,modifier = Modifier.fillMaxWidth(0.5f), textAlign = TextAlign.Center)
                    }
                }
            }
        }
    }
}