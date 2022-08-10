package com.example.dogedex.dogdetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.annotation.ExperimentalCoilApi
import coil.compose.AsyncImage
import coil.compose.rememberImagePainter
import com.example.dogedex.R
import com.example.dogedex.api.ApiResponseStatus
import com.example.dogedex.models.Dog

@ExperimentalCoilApi
@Composable
fun DogDetailScreen(
    dog: Dog,
    status: ApiResponseStatus<Any>? = null,
    onButtonClicked: () -> Unit,
    onErrorDialogDissmis: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.secondary_background))
            .padding(start = 8.dp, end = 8.dp, bottom = 16.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        DogInformation(dog)
        /* Image(
             modifier = Modifier
                 .width(270.dp)
                 .padding(top = 80.dp),
             painter = rememberImagePainter(dog.imageUrl), contentDescription = dog.name
         ) */
        AsyncImage(
            modifier = Modifier
                .width(270.dp)
                .padding(top = 80.dp),
            model = dog.imageUrl,
            contentDescription = dog.name
        )
        FloatingActionButton(
            modifier = Modifier.align(alignment = Alignment.BottomCenter),
            onClick = {
                onButtonClicked()
            }) {
            Icon(imageVector = Icons.Filled.Check, contentDescription = "")

        }
        if (status is ApiResponseStatus.Loading) {
            LoadingWeel()
        } else if (status is ApiResponseStatus.Error) {
            ErrorDialoig(onErrorDialogDissmis, status)
        }
    }
}

@Composable
fun DogInformation(dog: Dog) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 180.dp)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(4.dp),
            color = colorResource(id = android.R.color.white)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally

            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = stringResource(id = R.string.dog_index_format, dog.index),
                    fontSize = 32.sp, color = colorResource(id = R.color.text_black),
                    textAlign = TextAlign.End
                )
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 32.dp, bottom = 8.dp, start = 8.dp, end = 8.dp),
                    text = dog.name,
                    fontSize = 32.sp, color = colorResource(id = R.color.text_black),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Medium
                )
                LiveIcon()

                Text(
                    text = stringResource(
                        id = R.string.dog_life_expectancy_format,
                        dog.lifeExpectancy
                    ),
                    fontSize = 16.sp,
                    color = colorResource(id = R.color.text_black),
                    textAlign = TextAlign.Center,

                    )
                Text(
                    text = dog.temperament,
                    fontSize = 16.sp,
                    color = colorResource(id = R.color.text_black),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(top = 8.dp)

                )
                Divider(
                    modifier = Modifier.padding(top = 8.dp, start = 8.dp, end = 8.dp),
                    color = colorResource(
                        id = R.color.divider
                    ), thickness = 1.dp
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    DogDataColumn(
                        modifier = Modifier.weight(1f),
                        stringResource(id = R.string.female),
                        dog.weightFemale,
                        dog.heightFemale
                    )
                    VerticalDivider()
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = dog.type,
                            textAlign = TextAlign.Center,
                            fontSize = 16.sp,
                            color = colorResource(id = R.color.text_black),
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                        Text(
                            text = stringResource(id = R.string.group),
                            textAlign = TextAlign.Center,
                            fontSize = 16.sp,
                            color = colorResource(id = R.color.dark_gray),
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                    VerticalDivider()
                    DogDataColumn(
                        modifier = Modifier.weight(1f),
                        stringResource(id = R.string.male),
                        dog.weighMale,
                        dog.heightMale
                    )

                }


            }

        }
    }
}


@ExperimentalCoilApi
@Preview(showBackground = true)
@Composable
fun DogPreview() {
    val dog = Dog(
        1L,
        78,
        "Pug",
        "Herding",
        "70",
        "70",
        "https://e7.pngegg.com/pngimages/552/1/png-clipart-dogs-dogs-thumbnail.png",
        "10 - 12 years",
        "Friendly, playful",
        "70",
        "70",
        true
    )
    DogDetailScreen(dog, onButtonClicked = {}, onErrorDialogDissmis = {})
}

@Composable
fun VerticalDivider() {
    Divider(
        modifier = Modifier
            .height(42.dp)
            .width(1.dp),
        color = colorResource(id = R.color.divider)
    )
}

@Composable
fun DogDataColumn(
    modifier: Modifier = Modifier,
    genere: String, weight: String, heigh: String
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier) {
        Text(
            text = genere,
            textAlign = TextAlign.Center,
            color = colorResource(id = R.color.text_black),
            modifier = Modifier.padding(top = 8.dp)
        )
        Text(
            text = weight,
            textAlign = TextAlign.Center,
            fontSize = 16.sp,
            color = colorResource(id = R.color.text_black),
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(top = 8.dp)
        )
        Text(
            text = stringResource(id = R.string.weight),
            textAlign = TextAlign.Center,
            fontSize = 16.sp,
            color = colorResource(id = R.color.dark_gray),
            modifier = Modifier.padding(top = 8.dp)
        )
        Text(
            text = heigh,
            textAlign = TextAlign.Center,
            fontSize = 16.sp,
            color = colorResource(id = R.color.text_black),
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(top = 8.dp)
        )
        Text(
            text = stringResource(id = R.string.height),
            textAlign = TextAlign.Center,
            fontSize = 16.sp,
            color = colorResource(id = R.color.dark_gray),
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

@Composable
private fun LoadingWeel(

) {

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }

}


@Composable
private fun LiveIcon() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 80.dp, end = 80.dp)
    ) {
        Surface(shape = CircleShape, color = colorResource(id = R.color.color_primary)) {
            Icon(
                painter = painterResource(id = R.drawable.ic_hearth_white),
                tint = Color.White,
                contentDescription = null, modifier = Modifier
                    .width(24.dp)
                    .height(24.dp)
                    .padding(24.dp)
            )
        }
        Surface(
            shape = RoundedCornerShape(bottomEnd = 2.dp, topEnd = 2.dp),
            modifier = Modifier
                .width(200.dp)
                .height(6.dp), color = colorResource(id = R.color.color_primary)
        ) {

        }
    }

}

@Composable
fun ErrorDialoig(
    onDialogDissmis: () -> Unit,
    status: ApiResponseStatus.Error<Any>
) {
    AlertDialog(
        onDismissRequest = { },
        title = { Text("Oops, something happned") },
        text = { Text(text = stringResource(id = status.message)) },
        confirmButton = {
            Button(onClick = { onDialogDissmis() }) {
                Text("Try again")

            }
        }
    )


}
