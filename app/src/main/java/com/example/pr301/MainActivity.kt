package com.example.pr301

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pr301.ui.theme.PR301Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PR301Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
            }
        }
    }
}

@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    // Columna que organiza los elementos de la interfaz de usuario verticalmente
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Estados para los campos de entrada y mensajes
        var litrosPerjudicado by remember { mutableStateOf("") }
        var diametroVaso by remember { mutableStateOf("") }
        var alturaVaso by remember { mutableStateOf("") }
        var vasosConsumidos by remember { mutableStateOf("") }
        var mensaje by remember { mutableStateOf<String?>(null) }

        // Icono de advertencia
        Icon(
            Icons.Default.Warning,
            contentDescription = null,
            modifier = Modifier
                .size(64.dp)
                .padding(top = 16.dp),
            tint = Color.Cyan
        )

        // Campos de texto para ingresar valores
        OutlinedTextField(
            value = litrosPerjudicado,
            onValueChange = { litrosPerjudicado = it },
            label = { Text("Litros para estar perjudicado") },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = diametroVaso,
            onValueChange = { diametroVaso = it },
            label = { Text("Diámetro del vaso (cm)") },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = alturaVaso,
            onValueChange = { alturaVaso = it },
            label = { Text("Altura del vaso (cm)") },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = vasosConsumidos,
            onValueChange = { vasosConsumidos = it },
            label = { Text("Vasos consumidos hasta ahora") },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        // Botón para calcular la situación
        Button(
            onClick = {
                mensaje = calcularSituacion(
                    litrosPerjudicado,
                    diametroVaso,
                    alturaVaso,
                    vasosConsumidos
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Calcular Situación")
        }

        // Espaciador vertical
        Spacer(modifier = Modifier.height(16.dp))

        // Mostrar el mensaje resultante
        mensaje?.let {
            Text(it)
        }
    }
}

// Función para calcular la situación con respecto al consumo de líquidosS
fun calcularSituacion(
    litrosPerjudicado: String,
    diametroVaso: String,
    alturaVaso: String,
    vasosConsumidos: String
): String {
    try {
        // Convertir las entradas a tipos numéricos
        val litrosPerjudicadoDouble = litrosPerjudicado.toDouble()
        val diametroVasoDouble = diametroVaso.toDouble()
        val alturaVasoDouble = alturaVaso.toDouble()
        val vasosConsumidosInt = vasosConsumidos.toInt()

        // Validar entradas negativas
        if (litrosPerjudicadoDouble < 0 || diametroVasoDouble < 0 || alturaVasoDouble < 0) {
            return "Por favor, ingrese valores no negativos para todos los campos."
        }

        // Calcular el volumen total consumido y los vasos restantes
        val volumenTotalConsumido = vasosConsumidosInt * calcularVolumenVaso(diametroVasoDouble, alturaVasoDouble)
        val vasosRestantes = calcularVasosRestantes(litrosPerjudicadoDouble, volumenTotalConsumido, diametroVasoDouble, alturaVasoDouble)

        // Devolver el mensaje según la situación
        return if ( vasosRestantes > 0) {
            "Puedes tomar $vasosRestantes vasos más antes de estar perjudicado."
        } else {
            "¡Has sobrepasado el nivel tolerable de consumo!"
        }
    } catch (e: NumberFormatException) {
        return "Por favor, ingrese números válidos para todos los campos."
    }
}

// Función para calcular el volumen de un vaso
fun calcularVolumenVaso(diametro: Double, altura: Double): Double {
    val radio = diametro / 2.0
    val volumen = Math.PI * radio * radio * altura / 1000  // Convertir centímetros cúbicos a litros
    return formatearNumero(volumen)
}

// Función para calcular la cantidad de vasos restantes
fun calcularVasosRestantes(
    litrosPerjudicado: Double,
    volumenTotalConsumido: Double,
    diametroVaso: Double,
    alturaVaso: Double
): Double {
    val volumenVaso = calcularVolumenVaso(diametroVaso, alturaVaso)
    val vasosRestantes = maxOf(0.0, (litrosPerjudicado - volumenTotalConsumido) / volumenVaso)
    return formatearNumero(vasosRestantes)
}

// Función para formatear un número con dos decimales
fun formatearNumero(numero: Double): Double {
    return String.format("%.2f", numero).toDouble()
}

// Vista previa de la interfaz de usuario
@Preview(showBackground = true, widthDp = 360)
@Composable
fun Preview() {
    PR301Theme {
        MainActivity()
    }
}