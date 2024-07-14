package com.ufpb.meuguia.view

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FmdGood
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import coil.size.Scale
import coil.transform.CircleCropTransformation
import com.ufpb.meuguia.R
import com.ufpb.meuguia.ui.theme.components.Destinations
import com.ufpb.meuguia.viewmodel.AttractionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListAttractiveView(navController: NavController, viewModel: AttractionViewModel = viewModel()) {

    val isLoading = viewModel.isLoading.collectAsState()

    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    ),
                    title = {
                        Text(
                            text = stringResource(R.string.app_name),
                            fontWeight = FontWeight.Normal,
                            modifier = Modifier.fillMaxWidth(),
                            style = MaterialTheme.typography.titleLarge
                        )
                    },
                    actions = {
                        IconButton(onClick = {
                            viewModel.getAttractionList()
                        }) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                modifier = Modifier.size(24.dp),
                                contentDescription = stringResource(R.string.refresh),
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                        IconButton(onClick = {
                            navController.navigate(Destinations.FINDOPTIONS.name)
                        }) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                modifier = Modifier.size(24.dp),
                                contentDescription = stringResource(R.string.find_options_view),
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                )
            },
            content = {
                Box(modifier = Modifier
                    .padding(it)
                    .fillMaxSize()) {
                    ListAttraction(
                        modifier = Modifier.fillMaxSize(),
                        viewModel = viewModel,
                        navController = navController
                    )

                    if (isLoading.value) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color(0x99000000)),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }
            }
        )
    }
}

@Composable
fun ListAttraction(
    modifier: Modifier,
    viewModel: AttractionViewModel,
    navController: NavController,
) {
    LaunchedEffect(viewModel) {
        viewModel.getAttractionList()
    }
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFEAEEED)),
        verticalArrangement = Arrangement.Top
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(id = R.string.listAttractiveView),
                style = MaterialTheme.typography.titleLarge,
            )
        }
        LazyColumn {
            items(viewModel.attractionListResponse) { attractive ->
                Card(
                    modifier = Modifier
                        .padding(8.dp, 4.dp)
                        .fillMaxWidth()
                        .height(150.dp)
                        .clickable {
                            navController.navigate("attractiveView/${attractive.id}")
                        },
                    shape = RoundedCornerShape(8.dp),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 4.dp
                    )
                ) {
                    Surface {
                        Row(
                            Modifier
                                .padding(4.dp)
                                .fillMaxSize()
                        ) {
                            Image(
                                painter = rememberImagePainter(
                                    data = attractive.image_link,
                                    builder = {
                                        scale(Scale.FILL)
                                        transformations(CircleCropTransformation())
                                    }),
                                contentDescription = attractive.state,
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .weight(0.2f)
                            )
                            Column(
                                verticalArrangement = Arrangement.Center,
                                modifier = Modifier
                                    .padding(4.dp)
                                    .fillMaxHeight()
                                    .weight(0.8f)
                            ) {
                                Text(
                                    text = attractive.name,
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = attractive.city,
                                    style = MaterialTheme.typography.labelSmall,
                                    modifier = Modifier
                                        .background(Color.LightGray)
                                        .padding(4.dp)
                                )
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.FmdGood,
                                        contentDescription = "Attraction Location",
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier
                                            .size(24.dp)
                                            .padding(bottom = 4.dp)
                                            .clickable {
                                                val intent = Intent(
                                                    Intent.ACTION_VIEW,
                                                    Uri.parse(attractive.map_link)
                                                ).apply {
                                                    setPackage("com.google.android.apps.maps")
                                                }
                                                context.startActivity(intent)
                                            }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
