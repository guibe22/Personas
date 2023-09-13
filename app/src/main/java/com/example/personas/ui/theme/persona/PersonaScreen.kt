package com.example.personas.ui.theme.persona

import android.app.DatePickerDialog
import android.content.Context
import android.widget.DatePicker
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.personas.R
import com.example.personas.data.local.entity.Persona
import kotlinx.coroutines.flow.collectLatest
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import android.icu.util.Calendar
import androidx.compose.foundation.clickable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext
import java.util.Date


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonasScreen(
    viewModel: PersonaViewModel = hiltViewModel()
){
    val personas by viewModel.Personas.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.isMessageShownFlow.collectLatest {
            if (it) {
                snackbarHostState.showSnackbar(
                    message = "Persona guardada",
                    duration = SnackbarDuration.Short
                )
            }
        }
    }
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = "Personas") }
               )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(8.dp)
        ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    item {
                        // Vista de AddPersonas
                        AddPersonas(viewModel)
                    }
                    items(personas) { persona ->
                        PersonaCard(
                            Modifier.padding(dimensionResource(R.dimen.padding_small)),
                            persona,
                            viewModel

                        )


                    }
                }
            }


    }






}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AddPersonas(
    viewModel: PersonaViewModel
){
    var expanded by remember { mutableStateOf(false) }
    Card {

        Column(
            modifier = Modifier
                .animateContentSize(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioNoBouncy,
                        stiffness = Spring.StiffnessMedium
                    )
                )
        ) {
            Row(

            ){
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { expanded = !expanded }
                ) {
                    if(expanded) Text(text = "Ocultar") else Text(text = "Agregar")
                }
            }

            if(expanded) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    ExpandedContent(viewModel = viewModel)
                }
            }


        }
    }
}
@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ExpandedContent(viewModel: PersonaViewModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        val keyboardController = LocalSoftwareKeyboardController.current
        val focusManager = LocalFocusManager.current
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            label = { Text(text = "Nombre") },
            singleLine = true,
            maxLines=1,
            value = viewModel.nombre,
            onValueChange = viewModel::onNombreChanged,
            leadingIcon ={
                Icon(imageVector = Icons.Filled.Person, contentDescription = "person icon")
            },
            isError = viewModel.nombreError,
            trailingIcon = {
                AnimatedVisibility(
                    visible = viewModel.nombreError,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Icon(imageVector = Icons.Default.Warning, contentDescription = "")
                }
            } ,
            keyboardOptions = KeyboardOptions(
                    keyboardType =  KeyboardType.Text,
                imeAction = ImeAction.Next,
                capitalization = KeyboardCapitalization.Words
            ),
            keyboardActions = KeyboardActions{
                focusManager.moveFocus(FocusDirection.Next)
            }
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            label = { Text(text = "Telefono") },
            singleLine = true,
            maxLines=1,
            value = viewModel.telefono,
            onValueChange = viewModel::onTelefonoChanged,
            leadingIcon ={
                Icon(imageVector = Icons.Filled.Call, contentDescription = "phone icon")
            },
            isError = viewModel.telefonoError,
            trailingIcon = {
                AnimatedVisibility(
                    visible = viewModel.telefonoError,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Icon(imageVector = Icons.Default.Warning, contentDescription = "")
                }
            } ,
            keyboardOptions = KeyboardOptions(
                keyboardType =  KeyboardType.Phone,
                imeAction = ImeAction.Next,
                capitalization = KeyboardCapitalization.Words
            ),
            keyboardActions = KeyboardActions{
                focusManager.moveFocus(FocusDirection.Next)
            }
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            label = { Text(text = "Celular") },
            singleLine = true,
            maxLines=1,
            value = viewModel.celular,
            onValueChange = viewModel::oncelularChanged,
            leadingIcon ={
                Icon(imageVector = Icons.Filled.Call, contentDescription = "phone icon")
            },
            isError = viewModel.celularError,
            trailingIcon = {
                AnimatedVisibility(
                    visible = viewModel.celularError,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Icon(imageVector = Icons.Default.Warning, contentDescription = "")
                }
            } ,
            keyboardOptions = KeyboardOptions(
                keyboardType =  KeyboardType.Phone,
                imeAction = ImeAction.Next,
                capitalization = KeyboardCapitalization.Words
            ),
            keyboardActions = KeyboardActions{
                focusManager.moveFocus(FocusDirection.Next)
            }
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            label = { Text(text = "Email") },
            singleLine = true,
            maxLines=1,
            value = viewModel.email,
            onValueChange = viewModel::onEmailChanged,
            leadingIcon ={
                Icon(imageVector = Icons.Filled.Email, contentDescription = "email icon")
            },
            isError = viewModel.emailError,
            trailingIcon = {
                AnimatedVisibility(
                    visible = viewModel.emailError,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Icon(imageVector = Icons.Default.Warning, contentDescription = "")
                }
            } ,
            keyboardOptions = KeyboardOptions(
                keyboardType =  KeyboardType.Email,
                imeAction = ImeAction.Next,
                capitalization = KeyboardCapitalization.Words
            ),
            keyboardActions = KeyboardActions{
                focusManager.moveFocus(FocusDirection.Next)
            }
        )
        val context = LocalContext.current
        DateTextField(viewModel = viewModel, context =context )

        DropdownMenuBox(viewModel)



        OutlinedButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                keyboardController?.hide()
                viewModel.savePersona()
                viewModel.setMessageShown()
            })
        {
            Icon(imageVector = Icons.Default.CheckCircle, contentDescription = "guardar")
            Text(text = "Guardar")
        }


    }
}
@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun DateTextField(
    viewModel: PersonaViewModel,
    context: Context
) {
    val year: Int
    val month: Int
    val day: Int
    val calendar = Calendar.getInstance()
    year = calendar.get(Calendar.YEAR)
    month = calendar.get(Calendar.MONTH)
    day = calendar.get(Calendar.DAY_OF_MONTH)
    calendar.time = Date()

    var isDatePickerVisible by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current

    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        label = { Text(text = "Fecha Nacimiento") },
        singleLine = true,
        maxLines=1,
        value = viewModel.fechaNac,
        onValueChange = viewModel::onfechaNacChanged,
        leadingIcon ={
            IconButton(
                onClick = {
                    keyboardController?.hide()
                    isDatePickerVisible = true
                }
            ) {
                Icon(imageVector = Icons.Filled.DateRange, contentDescription = "email icon")
            }
        },
        isError = viewModel.fechaNacError,
        trailingIcon = {
            AnimatedVisibility(
                visible = viewModel.fechaNacError,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Icon(imageVector = Icons.Default.Warning, contentDescription = "")
            }
        },
        readOnly = true
    )
    if (isDatePickerVisible) {
        val datePickerDialog = DatePickerDialog(
            context,
            { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
                viewModel.fechaNac = "$dayOfMonth/$month/$year"
                isDatePickerVisible = false
            },
            year, month, day
        )
        DisposableEffect(Unit) {
            datePickerDialog.show()
            onDispose {
                datePickerDialog.dismiss()

            }
        }
        datePickerDialog.setOnCancelListener {isDatePickerVisible = false }
        datePickerDialog.setOnDismissListener { isDatePickerVisible = false }

    }



}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownMenuBox(
    viewModel: PersonaViewModel
) {

    val Opciones = arrayOf("ingeniero", "Contable", "Medico", "Abogado", "Civil")
    var expanded by remember { mutableStateOf(false) }


    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(3.dp)

    ) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                expanded = !expanded
            }
        ) {
            OutlinedTextField(

                value = viewModel.ocupacion,
                label = { Text(text = "Ocupacion") },
                onValueChange = viewModel::onOcupacionChanged,
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                leadingIcon ={
                    Icon(imageVector = Icons.Filled.Build, contentDescription = "email icon")
                }
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Opciones.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(text = item) },
                        onClick = {
                            viewModel.ocupacion = item
                            expanded = false

                        }
                    )
                }
            }
        }
    }
}




@Composable
fun PersonaCard(
    modifier: Modifier,
    persona: Persona,
    viewModel: PersonaViewModel
) {
    var expanded by remember { mutableStateOf(false) }
    Card(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .animateContentSize(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioNoBouncy,
                        stiffness = Spring.StiffnessMedium
                    )
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(R.dimen.padding_small))
            ) {
                Text(
                    text = persona.Nombre,
                    style = MaterialTheme.typography.displaySmall,
                    modifier = Modifier.padding(top = dimensionResource(R.dimen.padding_small))
                )
                Spacer(Modifier.weight(1f))
                ItemButton(
                    expanded = expanded,
                    onClick = { expanded = !expanded },
                )
            }
            if (expanded) {
                PersonaInfo(
                    persona, modifier = Modifier.padding(
                        start = dimensionResource(R.dimen.padding_medium),
                        top = dimensionResource(R.dimen.padding_small),
                        bottom = dimensionResource(R.dimen.padding_medium),
                        end = dimensionResource(R.dimen.padding_medium)
                    ),
                    viewModel
                )
            }

        }
    }




}
@Composable
fun PersonaInfo(
    persona: Persona,
    modifier: Modifier = Modifier,
    viewModel: PersonaViewModel
) {
    Column(
        modifier = modifier
    ) {
        Row() {
            Text(
                text = stringResource(R.string.Telefono),
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(Modifier.weight(1f))
            Text(
                text = persona.Telefono,
                style = MaterialTheme.typography.bodyLarge
            )
        }
        Spacer(Modifier.height(2.dp))
        Row() {
            Text(
                text = stringResource(R.string.Celular),
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(Modifier.weight(1f))
            Text(
                text = persona.Celular,
                style = MaterialTheme.typography.bodyLarge
            )
        }

        Spacer(Modifier.height(2.dp))

        Row() {
            Text(
                text = stringResource(R.string.Email),
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(Modifier.weight(1f))
            Text(
                text = persona.Email,
                style = MaterialTheme.typography.bodyLarge
            )
        }
        Spacer(Modifier.height(2.dp))

        Row() {
            Text(
                text = stringResource(R.string.FechaNacimiento),
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(Modifier.weight(1f))
            Text(
                text = persona.FechaNacimiento,
                style = MaterialTheme.typography.bodyLarge
            )
        }
        Row() {
            Text(
                text = stringResource(R.string.Ocupacion),
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(Modifier.weight(1f))
            Text(
                text = persona.Ocupacion,
                style = MaterialTheme.typography.bodyLarge
            )
        }
        Spacer(Modifier.weight(2f))
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(onClick = {
                viewModel.deletePersona(persona)

            }) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "guardar")
                Text(text = "Eliminar")

            }

        }

    }
}
@Composable
private fun ItemButton(
    expanded: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(
            imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
            contentDescription = "",
            tint = MaterialTheme.colorScheme.secondary
        )
    }
}
