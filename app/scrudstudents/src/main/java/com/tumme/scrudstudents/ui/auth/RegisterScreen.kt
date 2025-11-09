package com.tumme.scrudstudents.ui.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.tumme.scrudstudents.data.local.model.UserRole
import com.tumme.scrudstudents.data.local.model.Gender
import com.tumme.scrudstudents.data.local.model.LevelCourse
import java.util.Date

// Écran d'inscription pour créer un nouveau compte (étudiant ou enseignant)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    navController: NavController,
    viewModel: AuthViewModel = hiltViewModel()
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var selectedRole by remember { mutableStateOf(UserRole.STUDENT) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    
    // Informations étudiant
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var level by remember { mutableStateOf(LevelCourse.P1) }
    var gender by remember { mutableStateOf(Gender.MALE) }
    
    // Informations enseignant
    var teacherFirstName by remember { mutableStateOf("") }
    var teacherLastName by remember { mutableStateOf("") }
    var teacherEmail by remember { mutableStateOf("") }
    var department by remember { mutableStateOf("") }
    var teacherGender by remember { mutableStateOf(Gender.MALE) }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Inscription",
            style = MaterialTheme.typography.headlineMedium
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Sélection du rôle
        Text("Type de compte", style = MaterialTheme.typography.titleMedium)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Row(
                modifier = Modifier.selectable(
                    selected = selectedRole == UserRole.STUDENT,
                    onClick = { selectedRole = UserRole.STUDENT }
                )
            ) {
                RadioButton(
                    selected = selectedRole == UserRole.STUDENT,
                    onClick = { selectedRole = UserRole.STUDENT }
                )
                Text("Étudiant")
            }
            Row(
                modifier = Modifier.selectable(
                    selected = selectedRole == UserRole.TEACHER,
                    onClick = { selectedRole = UserRole.TEACHER }
                )
            ) {
                RadioButton(
                    selected = selectedRole == UserRole.TEACHER,
                    onClick = { selectedRole = UserRole.TEACHER }
                )
                Text("Enseignant")
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Informations de connexion
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Nom d'utilisateur") },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Mot de passe") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirmer le mot de passe") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Informations spécifiques selon le rôle
        if (selectedRole == UserRole.STUDENT) {
            OutlinedTextField(
                value = firstName,
                onValueChange = { firstName = it },
                label = { Text("Prénom") },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            OutlinedTextField(
                value = lastName,
                onValueChange = { lastName = it },
                label = { Text("Nom") },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading
            )
        } else {
            OutlinedTextField(
                value = teacherFirstName,
                onValueChange = { teacherFirstName = it },
                label = { Text("Prénom") },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            OutlinedTextField(
                value = teacherLastName,
                onValueChange = { teacherLastName = it },
                label = { Text("Nom") },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            OutlinedTextField(
                value = teacherEmail,
                onValueChange = { teacherEmail = it },
                label = { Text("Email") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            OutlinedTextField(
                value = department,
                onValueChange = { department = it },
                label = { Text("Département") },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        
        Button(
            onClick = {
                if (password != confirmPassword) {
                    errorMessage = "Les mots de passe ne correspondent pas"
                    return@Button
                }
                
                if (username.isBlank() || password.isBlank()) {
                    errorMessage = "Veuillez remplir tous les champs"
                    return@Button
                }
                
                isLoading = true
                errorMessage = ""
                
                if (selectedRole == UserRole.STUDENT) {
                    if (firstName.isBlank() || lastName.isBlank() || email.isBlank()) {
                        errorMessage = "Veuillez remplir tous les champs"
                        isLoading = false
                        return@Button
                    }
                    
                    viewModel.registerStudent(
                        username, password, firstName, lastName, email, level, gender
                    ) { success ->
                        isLoading = false
                        if (success) {
                            navController.navigate("login") {
                                popUpTo("register") { inclusive = true }
                            }
                        } else {
                            errorMessage = "Erreur lors de l'inscription"
                        }
                    }
                } else {
                    if (teacherFirstName.isBlank() || teacherLastName.isBlank() || teacherEmail.isBlank() || department.isBlank()) {
                        errorMessage = "Veuillez remplir tous les champs"
                        isLoading = false
                        return@Button
                    }
                    
                    viewModel.registerTeacher(
                        username, password, teacherFirstName, teacherLastName, teacherEmail, department, teacherGender
                    ) { success ->
                        isLoading = false
                        if (success) {
                            navController.navigate("login") {
                                popUpTo("register") { inclusive = true }
                            }
                        } else {
                            errorMessage = "Erreur lors de l'inscription"
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(16.dp))
            } else {
                Text("S'inscrire")
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        TextButton(
            onClick = { navController.navigate("login") }
        ) {
            Text("Déjà un compte ? Se connecter")
        }
    }
}
