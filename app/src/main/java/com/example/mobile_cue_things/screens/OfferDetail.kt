package com.example.mobile_cue_things.screens

import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
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
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mobile_cue_things.enums.ConstantsManageOffer
import com.example.mobile_cue_things.enums.ConstantsRoutes
import com.example.mobile_cue_things.model.Offer
import com.example.mobile_cue_things.model.Product
import com.example.mobile_cue_things.viewModel.global.GlobalVariables

import com.example.mobile_cue_things.viewModel.navigation.NavigationViewModel
import com.example.mobile_cue_things.viewModel.offer.OfferViewModel
import com.example.mobile_cue_things.viewModel.product.FirestoreProductViewModel

@Composable
fun OfferDetailScreen(
    navViewModel: NavigationViewModel,
    idProduct: String,
    firestoreProductViewModel: FirestoreProductViewModel,
    fireStoreOfferViewModel: OfferViewModel,
    goToDetailProduct: (String) -> Unit
) {

    val gv: GlobalVariables = viewModel(
        viewModelStoreOwner = LocalContext.current as ViewModelStoreOwner
    )
    val context = LocalContext.current
    val offersList = remember { mutableStateListOf<Offer>() }

    var product by remember { mutableStateOf<Product?>(null) }
    //val offersList = remember { mutableStateOf<Offer> }}




    gv.updatePositionRoute(ConstantsRoutes.OFFER.value)

    LaunchedEffect(idProduct) {

        product = firestoreProductViewModel.loadProductById(idProduct)

        offersList.clear()
        offersList.addAll(fireStoreOfferViewModel.getOffersByProduct(idProduct))
    }

    product?.let { p ->

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            ProductOfferDetail(
                navViewModel = navViewModel,
                product = p,
                goToDetailProduct = goToDetailProduct
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (offersList.isEmpty()) {
                // Mensaje centrado cuando no hay ofertas
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.6f) // ocupa parte central, ajustable
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "No hay ofertas hechas",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.9f)
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                ) {
                    items(offersList, key = { it.id }) { offer ->
                        OfferExpandableCard(
                            offer = offer,
                            updateStateOffer = { idOffer, newState ->
                                fireStoreOfferViewModel.updateOfferStatus(
                                    offerId = idOffer,
                                    newStatus = newState
                                ) { success ->
                                    if (success) {
                                        offersList.removeAll { it.id == idOffer }
                                        Toast.makeText(context, "Operación realizada con éxito", Toast.LENGTH_SHORT).show()
                                    } else {
                                        Toast.makeText(context, "Error al hacer la operación", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}



@Composable
fun ProductOfferDetail(
    navViewModel: NavigationViewModel,
    product: Product,
    goToDetailProduct: (String) -> Unit
) {
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

            Column(modifier = Modifier
                .weight(1f)
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

                Button(
                    onClick = { goToDetailProduct(product.id)},
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.height(40.dp)
                ) {
                    Text(
                        text = "Ver detalle",
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Normal),
                    )
                }


            }
        }
    }
}


@Composable
fun OfferExpandableCard(offer: Offer,updateStateOffer:(String,Int)->Unit) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize()
            .clickable { expanded = !expanded },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            // TÍTULO
            Text(
                text = "Ofertador: ${offer.emailOfferer}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Tap para ver detalles",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )

            if (expanded) {
                Spacer(modifier = Modifier.height(12.dp))

                OfferKeyValue("ID", offer.id)
                OfferKeyValue("Producto ofertado", offer.productToOffer)
                OfferKeyValue("Precio ofertado", "${offer.priceToOffer}")
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                horizontalArrangement = Arrangement.Center
            ) {

                Button(
                    onClick = { updateStateOffer(offer.id, ConstantsManageOffer.CLOSE.value)},
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = MaterialTheme.colorScheme.onError
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Cancelar")
                }

                Spacer(modifier = Modifier.width(12.dp))

                // BOTÓN ACEPTAR (VERDE)
                Button(
                    onClick = { /* TODO aceptar */ },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.onSecondary
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Aceptar")
                }
            }
        }
    }
}

@Composable
fun OfferKeyValue(key: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = key, fontWeight = FontWeight.SemiBold)
        Text(text = value)
    }
    Spacer(modifier = Modifier.height(6.dp))
}

