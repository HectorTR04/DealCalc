package com.example.dealcalc

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import com.example.dealcalc.ui.theme.DealCalcTheme
import com.example.dealcalc.ui.theme.customBlue
import com.example.dealcalc.ui.theme.customRed

const val titleSpacing = 200
const val buttonSpacing = 25

val titleGradient = listOf(customRed, customBlue)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DealCalcTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    var dealList by remember { mutableStateOf(listOf<DrinkDeal>()) }
                    var inputVisible by remember { mutableStateOf(true) }
                    TitleText()
                    if (inputVisible) {
                        UserInput(
                            onAddDeal = { deal ->
                                dealList = dealList + deal
                            },
                            onCalculateBestDeal = {
                                for(deal: DrinkDeal in dealList){
                                    deal.calculateCostPerML()
                                }
                                dealList = dealList.sortedBy { it.costPerML }
                                inputVisible = false
                            }
                        )
                    } else {
                        Result(dealList)
                    }
            }
        }
    }
}

@Composable
fun TitleText() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ){
        Spacer(modifier = Modifier.height(titleSpacing.dp))

        Text(
            text = "DealCalc \n V1",
            fontSize = 40.sp,
            fontWeight = FontWeight.Normal,
            fontFamily = FontFamily.Serif,
            textAlign = TextAlign.Center,
            style = TextStyle(
                brush = Brush.linearGradient(
                    colors = titleGradient
                )
            )
        )
    }
}

@Composable
fun UserInput( onAddDeal: (DrinkDeal) -> Unit, onCalculateBestDeal: () -> Unit) {
    var name by remember { mutableStateOf("")}
    var price by remember {mutableStateOf("")}
    var amount by remember {mutableStateOf("")}
    var millilitres by remember {mutableStateOf("")}
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ){
        Spacer(modifier = Modifier.height((titleSpacing + buttonSpacing * 4).dp))
        InputField(
            value = name,
            onValueChange = { name = it },
            label = "Name"
        )

        Spacer(modifier = Modifier.height(buttonSpacing.dp))

        InputField(
            value = price,
            onValueChange = { price = it },
            label = "Price"
        )

        Spacer(modifier = Modifier.height(buttonSpacing.dp))

        InputField(
            value = amount,
            onValueChange = { amount = it },
            label = "Cans"
        )

        Spacer(modifier = Modifier.height(buttonSpacing.dp))

        InputField(
            value = millilitres,
            onValueChange = { millilitres = it },
            label = "Millilitres"
        )

        Spacer(modifier = Modifier.height(buttonSpacing.dp))
        Button(onClick = {
            var success = addToDealList(name, price, amount, millilitres)
            if(success){
                var dealToAdd = DrinkDeal(name, price.toFloat(), amount.toFloat(), millilitres.toFloat())
                onAddDeal(dealToAdd)
                Toast.makeText(context, "Deal added successfully!", Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(context, "Unsuccessful add, enter valid input!", Toast.LENGTH_SHORT).show()
            }
            name = ""
            price = ""
            amount = ""
            millilitres = ""
        },
            modifier = Modifier.size(width = 280.dp, height = 40.dp),
            colors = ButtonDefaults.buttonColors(containerColor = customBlue)
        ){
            Text("Add")
        }

        Spacer(modifier = Modifier.height(buttonSpacing.dp))
        Button(onClick = {
            onCalculateBestDeal()
        },
            modifier = Modifier.size(width = 280.dp, height = 40.dp),
            colors = ButtonDefaults.buttonColors(containerColor = customRed)
        ){
            Text("Calculate Best Deal")
        }
    }
}

@Composable
fun InputField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(
                text = label,
                style = TextStyle(color = customBlue)
            )
        },
        maxLines = 1,
        modifier = modifier
    )
}

fun addToDealList(drinkName: String, drinkPrice: String, amount: String, millilitres: String): Boolean {
    val priceValue = drinkPrice.toFloatOrNull()
    val amountValue = amount.toFloatOrNull()
    val millilitresValue = millilitres.toFloatOrNull()

    return drinkName.isNotBlank() &&
            priceValue != null && priceValue > 0 &&
            amountValue != null && amountValue > 0 &&
            millilitresValue != null && millilitresValue > 0
}

@Composable
fun Result(dealList: List<DrinkDeal>){
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Spacer(modifier = Modifier.height((titleSpacing + buttonSpacing * 4).dp))
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(16.dp)
        ) {
            itemsIndexed(dealList) { index, drink ->
                Row(
                    modifier = Modifier.fillMaxWidth()
                        .wrapContentHeight()
                        .padding(vertical = buttonSpacing.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${index + 1}. " + drink.toString(),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Normal,
                        fontFamily = FontFamily.Serif,
                        fontStyle = FontStyle.Italic,
                        textAlign = TextAlign.Center,
                        style = TextStyle(
                            brush = Brush.linearGradient(
                                colors = titleGradient
                            )
                        )
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TitleTextPreview() {
    DealCalcTheme {
            TitleText()
            UserInput(
                onAddDeal = TODO(),
                onCalculateBestDeal = TODO()
            )
            Result(
                dealList = TODO()
            )
        }
    }
}