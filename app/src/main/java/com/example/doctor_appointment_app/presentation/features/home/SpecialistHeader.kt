package com.arya.bookmydoc.presentation.features.home

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arya.bookmydoc.R
import com.arya.bookmydoc.domain.model.Category


@Composable
fun SpecialistHeader(
    categories: List<Category>,
    isLoading: Boolean,
    error: String?,
    onClick: () -> Unit
){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 18.dp, start = 8.dp, end = 8.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ){
            Text(
                text = "Specialist",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .align(Alignment.TopStart)
            )
            Text(
                text = "See all",
                fontSize = 14.sp,
                modifier = Modifier
                    .align(Alignment.TopEnd),
                color = colorResource(R.color.teal)
            )
        }
        Spacer(Modifier.height(12.dp))
        when{
            isLoading -> {
                CircularIndicator()
            }
            error != null -> {
                ErrorView(
                    message = error,
                    onRetry = {

                    }
                )
            }
            categories.isEmpty() -> EmptyState("No data available")
            else -> {
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ){
                    items(categories){ item->
                        SpecialistItem(
                            item = item,
                            onClick = {},
                        )
                    }
                }
            }
        }
    }
}


@SuppressLint("LocalContextResourcesRead")
@Composable
fun SpecialistItem(
    item: Category,
    onClick:()-> Unit
){
    Column(
        modifier = Modifier
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(65.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.White)
        ) {
            val context = LocalContext.current
            val resourceId = remember(item.picture) {
                context.resources.getIdentifier(item.picture, "drawable", context.packageName)
            }

            if(resourceId != 0){
                Image(
                    modifier = Modifier.padding(12.dp),
                    painter = painterResource(resourceId),
                    contentDescription = "category_icon"
                )
            }
        }
        Spacer(Modifier.height(8.dp))
        Text(
            text = item.name,
            color = Color.Gray
        )
    }
}

@Preview
@Composable
fun CategoryRowPreview(){
    CircularProgressIndicator(
        modifier = Modifier.size(12.dp),
        strokeWidth = 4.dp,
        color = colorResource(R.color.teal)
    )
}