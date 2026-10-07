package com.example.mobile_cue_things.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mobile_cue_things.enums.ConstantsRoutes
import com.example.mobile_cue_things.model.Product
import com.example.mobile_cue_things.principal.DetailProductRoute
import com.example.mobile_cue_things.principal.OfferDetailRoute
import com.example.mobile_cue_things.ux.overlay.OverlayCreateOffer
import com.example.mobile_cue_things.viewModel.global.GlobalVariables
import com.example.mobile_cue_things.viewModel.navigation.NavigationViewModel
import com.example.mobile_cue_things.viewModel.publication.PublicationViewModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlin.collections.forEach

@Composable
fun MyPublicationScreen(navViewModel: NavigationViewModel){
    var activeOverlayCreateOffer by remember { mutableStateOf(false) }
    var publicationViewModel: PublicationViewModel = viewModel()

    Column (modifier = Modifier.fillMaxSize()
    ){
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            loadProduct(navViewModel = navViewModel)
        }

        if (activeOverlayCreateOffer){
            OverlayCreateOffer(ownerProduct = publicationViewModel.ownerProduct, idProduct = publicationViewModel.idProduct, closePopUp = {activeOverlayCreateOffer=false})
        }
    }
}



@Composable
fun loadProduct(navViewModel: NavigationViewModel) {
    val db = Firebase.firestore
    val context = LocalContext.current

    var products by remember { mutableStateOf<List<Product>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    var activeOverlayCreateOffer by remember { mutableStateOf(false) }
    val gv: GlobalVariables = viewModel(
        viewModelStoreOwner = LocalContext.current as ViewModelStoreOwner
    )

    gv.updatePositionRoute(ConstantsRoutes.MYPUBLICATION.value)

    LaunchedEffect(Unit) {
        loading = true

        db.collection("products")
            .whereEqualTo("emailUser",gv.userName )   // ← FILTRO POR EMAIL
            .get()
            .addOnSuccessListener { snap ->
                products = snap.documents.mapNotNull { doc ->
                    println("Document ID=${doc.id}, data=${doc.data}")
                    try {
                        val p = doc.toObject(Product::class.java)
                        if (p == null) println("Documento ${doc.id} no pudo convertirse a Product")
                        p?.copy(id = doc.id)
                    } catch (e: Exception) {
                        println("Error convirtiendo documento ${doc.id}: $e")
                        null
                    }
                }

                loading = false
            }
            .addOnFailureListener { exc ->
                error = exc.localizedMessage ?: "Error al cargar los productos"
                loading = false
            }
    }


    Box(modifier = Modifier.fillMaxSize()){
        when{
            loading-> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            error !=null-> Text(text= error!!,modifier=Modifier.align(Alignment.Center))
            else-> {

                products.forEach { product ->
                    println("[PRODUCT] $product")
                }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)

                ) {


                    items(products, key = { it.id }) { p ->
                        ProductItem(product = p,
                            goToProductDetail = {navViewModel.navigateTo(DetailProductRoute(p.id))},
                            goToOfferDetail = {navViewModel.navigateTo(OfferDetailRoute(idProduct = p.id))}
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ProductItem(
    product: Product,
    goToProductDetail: () -> Unit,
    goToOfferDetail: ()-> Unit
) {
    var expanded by rememberSaveable { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded }
            .animateContentSize(),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        // Row principal: izquierda imagen+texto, derecha el icono (respetando su ancho)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .height(IntrinsicSize.Min),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            // Left: imagen + contenido textual
            Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.Top) {
                SmartProductImageComposable(
                    imgRelative = product.imgUrl,
                    modifier = Modifier
                        .size(88.dp)
                        .clip(RoundedCornerShape(8.dp))
                )

                Spacer(modifier = Modifier.width(8.dp))

                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f)
                        .padding(end = 8.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = product.name,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "$${product.price}",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    // descripción solo cuando está expandido
                    if (expanded) {
                        Text(
                            text = product.description,
                            maxLines = 3,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f),
                            modifier = Modifier.padding(top = 4.dp, bottom = 8.dp)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(1f),
                        horizontalArrangement = Arrangement.Center) {
                        Button(
                            onClick = { goToOfferDetail() },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.height(40.dp)
                        ) {
                            Text("Ver ofertas", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }

            // Right: icono respetando espacio (alineado al top dentro del Row)
            Column(
                modifier = Modifier
                    .wrapContentWidth()
                    .padding(start = 4.dp),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Top
            ) {
                IconButton(
                    onClick = { goToProductDetail() },
                    modifier = Modifier
                        .size(36.dp)
                ) {

                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Ver detalles del producto",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}