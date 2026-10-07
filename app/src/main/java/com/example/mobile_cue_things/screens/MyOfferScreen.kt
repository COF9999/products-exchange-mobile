package com.example.mobile_cue_things.screens

import androidx.compose.animation.animateContentSize
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mobile_cue_things.enums.ConstantsRoutes
import com.example.mobile_cue_things.objects.OfferWithProduct
import com.example.mobile_cue_things.principal.DetailProductRoute
import com.example.mobile_cue_things.viewModel.global.GlobalVariables
import com.example.mobile_cue_things.viewModel.navigation.NavigationViewModel
import com.example.mobile_cue_things.viewModel.offer.OfferViewModel
import com.example.mobile_cue_things.viewModel.publication.PublicationViewModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore


// Colores directos para la barra final (puedes sustituir por theme colors si prefieres)
private val PositiveGreen = Color(0xFF2ECC71)
private val NegativeRed = Color(0xFFE74C3C)
@Composable
fun MyOfferScreen(navViewModel: NavigationViewModel){
    var activeOverlayCreateOffer by remember { mutableStateOf(false) }
    var publicationViewModel: PublicationViewModel = viewModel()

    Column (modifier = Modifier.fillMaxSize()
    ){
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            loadMyOffers(navViewModel = navViewModel)
        }
    }
}

@Composable
fun loadMyOffers(navViewModel: NavigationViewModel) {
    val db = Firebase.firestore
    val context = LocalContext.current

    var offersWithProduct by remember { mutableStateOf<List<OfferWithProduct>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    var activeOverlayCreateOffer by remember { mutableStateOf(false) }
    val gv: GlobalVariables = viewModel(
        viewModelStoreOwner = LocalContext.current as ViewModelStoreOwner
    )

    val  offerViewModel:OfferViewModel = viewModel()
    gv.updatePositionRoute(ConstantsRoutes.MYOFFER.value)

    LaunchedEffect(Unit) {
        loading = true
        offerViewModel.getOffersAndProductName(
            emailOfferer = gv.userName,
            onResult = { list ->
                offersWithProduct = list
                loading = false
            },
            onFailure = { ex ->
                error = ex.message ?: "Error desconocido"
                loading = false
            }
        )
    }


    Box(modifier = Modifier.fillMaxSize()){
        when{
            loading-> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            error !=null-> Text(text= error!!,modifier=Modifier.align(Alignment.Center))
            else-> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)

                ) {


                    items(offersWithProduct, key = { it.offer.id }) { it ->
                        OfferItem(item = it,
                            goToProductDetail = { it.product?.id?.let { id -> navViewModel.navigateTo(DetailProductRoute(id)) } }
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun OfferItem(
    item: OfferWithProduct,
    goToProductDetail: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 6.dp)
            .animateContentSize(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),

    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Header: ID + Product name + Detalle clickable
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Oferta: ${item.offer.id}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    item.product?.name?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp
                        )
                    }
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "Detalle producto",
                        modifier = Modifier
                            .clickable { goToProductDetail() }
                            .padding(6.dp),
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    // Toggle expand/collapse
                    Text(
                        text = if (expanded) "Ocultar" else "Ver detalles",
                        modifier = Modifier
                            .clickable { expanded = !expanded }
                            .padding(6.dp),
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            // Expandable area tabla clave-valor con la información de la oferta
            if (expanded) {
                Divider(color = MaterialTheme.colorScheme.outline, thickness = 1.dp)
                Spacer(modifier = Modifier.height(8.dp))

                val kvData = listOf(
                    "ID Oferta" to item.offer.id,
                    "ID Producto" to item.offer.idProduct,
                    "Email Offerer" to item.offer.emailOfferer,
                    "Producto ofrecido" to item.offer.productToOffer,
                    "Precio ofrecido" to item.offer.priceToOffer.toString(),
                    "Estado" to item.offer.statusOffer.toString()
                )

                // Usa tu KeyValueTable si está disponible, si no, usa el fallback simple
                KeyValueTable(
                    data = kvData,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 10.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))
            }

            // Barra final que ocupa 100% y cambia de color según statusOffer (1 => green, else => red)
            val status = item.offer.statusOffer
            val stripColor = if (status == 1) PositiveGreen else NegativeRed
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .background(color = stripColor)
            )
        }
    }
}