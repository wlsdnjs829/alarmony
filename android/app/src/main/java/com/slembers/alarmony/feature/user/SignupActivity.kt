package com.slembers.alarmony.feature.user


import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.slembers.alarmony.feature.common.NavItem
import com.slembers.alarmony.model.db.SignupRequest
import com.slembers.alarmony.network.repository.MemberService.checkEmail
import com.slembers.alarmony.network.repository.MemberService.checkId
import com.slembers.alarmony.network.repository.MemberService.checkNickname
import com.slembers.alarmony.network.repository.MemberService.signup


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
@ExperimentalMaterial3Api
@ExperimentalGlideComposeApi
fun SignupScreen(navController: NavController) {
    Log.d("email", "SignupScreen called")
    val context = LocalContext.current
    /** 아이디 **/
    var username by rememberSaveable { mutableStateOf("") }
    var idMessage = remember { mutableStateOf("") }
    var isIdCanUse = remember { mutableStateOf(true) }
    var idMessageColor = remember { mutableStateOf(Color.Black) }
    var isIdError = remember { mutableStateOf(false) }

    /** 비번 **/
    var password by remember { mutableStateOf("") }
    var isPasswordError = remember { mutableStateOf(false) }
    val passwordVisibility = remember { mutableStateOf(false) }

    /** 비번 확인 **/
    var passwordConfirm by remember { mutableStateOf("") }
    var isPasswordConfirmError = remember { mutableStateOf(false) }
    val passwordConfirmVisibility = remember { mutableStateOf(false) }

    /** 이메일 **/
    var email by rememberSaveable { mutableStateOf("") }
    var emailMessage = remember { mutableStateOf("") }

    var isEmailError = remember { mutableStateOf(false) }
    var isEmailCanUse = remember { mutableStateOf(true) }
    var emailMessageColor = remember { mutableStateOf(Color.Black) }

    /** 닉네임 **/
    var nickname by rememberSaveable { mutableStateOf("") }
    var nicknameMessage = remember { mutableStateOf("") }
    var isNicknameError = remember { mutableStateOf(false) }
    var isNicknameCanUse = remember { mutableStateOf(true) }
    var nicknameMessageColor = remember { mutableStateOf(Color.Black) }



    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("회원가입") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Filled.ArrowBack, "뒤로가기")
                    }
                },
                backgroundColor = MaterialTheme.colors.background
            )
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
            ) {
                /** 아이디 **/
                IdTextField(
                    username = username,
                    onIdChange = { username = it },
                    isIdError = isIdError,
                    message = idMessage,
                    color = idMessageColor,
                    isIdCanUse = isIdCanUse,
                )
                /** 비밀번호 **/
                PasswordText(
                    password = password,
                    onPasswordChange = { password = it },
                    isPasswordError = isPasswordError,
                    passwordVisibility = passwordVisibility

                )
                /** 비밀번호 확인 **/
                PasswordConfirmText(
                    password = password,
                    passwordConfirm = passwordConfirm,
                    onPasswordConfirmChange = { passwordConfirm = it },
                    isPasswordConfirmError = isPasswordConfirmError,
                    passwordConfirmVisibility = passwordConfirmVisibility
                )
                /** 이메일 **/
                EmailTextField(
                    email = email, // pass the initial email value here
                    onEmailChange = { email = it },
                    isEmailError = isEmailError,
                    message = emailMessage,
                    color = emailMessageColor,
                    isEmailCanUse = isEmailCanUse,
                )
                /** 닉네임 **/
                NicknameText(
                    nickname = nickname, // pass the initial email value here
                    onNicknameChange = { nickname = it },
                    isNicknameError = isNicknameError,
                    message = nicknameMessage,
                    color = nicknameMessageColor,
                    isNicknameCanUse = isNicknameCanUse
                )

            } //Column
        },

        bottomBar = {
            Box(
                contentAlignment = Alignment.BottomEnd,
                modifier = Modifier.fillMaxWidth()
            ) {
                /** 회원 가입 버튼 **/
                Button(
                    onClick = {
                        // 회원가입이 완료되면 Snackbar 띄우기
                        Log.d("회원", "회원가입버튼누름")
                        signup(
                            SignupRequest(
                                username = username,
                                password = password,
                                nickname = nickname,
                                email = email
                            )
                        ) { isSuccess ->
                            if (isSuccess) {
                                //회원 가입 성공
                                Toast.makeText(
                                    context,
                                    "회원가입 완료! 이메일을 확인해주세요.",
                                    Toast.LENGTH_LONG
                                ).show()
                                navController.navigate(NavItem.LoginScreen.route)
                            } else {
                                //회원 가입 실패
                                Toast.makeText(
                                    context,
                                    "회원가입 실패...",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }

                    },
                    enabled = ((isIdCanUse.value && username.isNotBlank()) && (!isPasswordError.value && !isPasswordConfirmError.value)
                    && (isEmailCanUse.value && email.isNotBlank()) && (isNicknameCanUse.value && nickname.isNotBlank())),
                    shape = RoundedCornerShape(0.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Black,
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text(text = "알라모니 가입하기")
                }
            }
        }
    )
} //signup screen



/**
 * 아이디
 */

@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Composable
fun IdTextField(
    username: String,
    onIdChange: (String) -> Unit,
    isIdError: MutableState<Boolean>,
    message: MutableState<String>,
    color: MutableState<Color>,
    isIdCanUse: MutableState<Boolean>,
) {
    val usernameRegex = "^[a-z0-9]{5,11}$".toRegex()
    TitleText("아이디 *")
    TextField(
        value = username,
        onValueChange = {
            onIdChange(it)
            if (!usernameRegex.matches(it)) {
                Log.d("email", "새로만든 아이디 창 정규식")
                isIdError.value = true
            } else {
                isIdError.value = false
            }
        },
        placeholder = { Text("영문, 숫자 5-11자") },
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.White,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            errorCursorColor = Color.Transparent,
            errorIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            disabledTextColor = Color.Transparent,
            cursorColor = Color.Transparent
        ),
        singleLine = true,
        maxLines = 1,
        isError = isIdError.value,
        modifier = Modifier
            .fillMaxWidth()
            .border(
                1.dp,
                if (isIdError.value || !isIdCanUse.value) Red else Gray,
                shape = RoundedCornerShape(8.dp)
            ),
        keyboardActions = KeyboardActions { },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next
        ),
    )

    if (username.isNotBlank()) {

        if (isIdError.value) {
            Log.d("회원", "아이디 정규식이 틀릴때 ")
            ErrorMessageText(
                message = "아이디는 영문, 숫자를 조합하여 4-20자로 입력해주세요.",
                color = Red
            )
        } else {

            checkId(username) { isDuplicated ->
                if (isDuplicated) {
                    Log.d("회원", "중복된 아이디")
                    message.value = "이미 사용중인 아이디 입니다."
                    color.value = Red
                    isIdError.value = false
                    isIdCanUse.value = false
                } else {
                    color.value = Black
                    Log.d("회원", "사용가능  아이디")
                    // message = "사용 가능한 아이디 입니다."
                    message.value = "사용가능한  아이디 입니다."
                    isIdError.value = false
                    isIdCanUse.value = true
                }
            }
            ErrorMessageText(message = message.value, color = color.value)
        }
    }
}


/**
 * 비밀번호
 */

@Composable
fun PasswordText(
    password: String,
    onPasswordChange: (String) -> Unit,
    isPasswordError: MutableState<Boolean>,
    passwordVisibility :MutableState<Boolean>
) {
    val passwordRegex = "^(?=.*[0-9])(?=.*[a-zA-Z])[a-zA-Z\\d]{8,16}\$".toRegex()
    TitleText("비밀번호 *")
    TextField(
        value = password,
        onValueChange = {
            onPasswordChange(it)

            if (!passwordRegex.matches(it)) {
                Log.d("email", "새로만든 비밀번호 창 정규식")
                isPasswordError.value = true
            } else {
                isPasswordError.value = false
            }
        },
        placeholder = { Text("영문, 숫자 조합 최소 8자") },
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.White,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            errorCursorColor = Color.Transparent,
            errorIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            disabledTextColor = Color.Transparent,
            cursorColor = Color.Transparent
        ),
        singleLine = true,
        maxLines = 1,
        isError = isPasswordError.value,


        visualTransformation = if (passwordVisibility.value) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            val visibilityIcon = if (passwordVisibility.value) Icons.Filled.VisibilityOff else Icons.Filled.Visibility
            IconButton(onClick = { passwordVisibility.value = !passwordVisibility.value }) {
                Icon(visibilityIcon, "Toggle password visibility")
            }
        },

        modifier = Modifier
            .fillMaxWidth()
            .border(
                1.dp,
                if (isPasswordError.value) Red else Gray,
                shape = RoundedCornerShape(8.dp)
            ),
        keyboardActions = KeyboardActions { },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Next
        ),
    )
    if (isPasswordError.value) {
        ErrorMessageText(message = "영문, 숫자를 조합하여 8-20자 입력해주세요.", color = Red)
    }

}


/**
 * 비밀번호 확인
 */

@Composable
fun PasswordConfirmText(
    password: String,
    passwordConfirm: String,
    onPasswordConfirmChange: (String) -> Unit,
    isPasswordConfirmError: MutableState<Boolean>,
    passwordConfirmVisibility :  MutableState<Boolean>
) {
    TextField(
        value = passwordConfirm,
        onValueChange = {
            onPasswordConfirmChange(it)
            isPasswordConfirmError.value = password != it
        },
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.White,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            errorCursorColor = Color.Transparent,
            errorIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            disabledTextColor = Color.Transparent,
            cursorColor = Color.Transparent
        ),
        placeholder = { Text("비밀번호 재입력") },
        singleLine = true,
        maxLines = 1,
        isError = isPasswordConfirmError.value,
        textStyle = TextStyle(textDecoration = TextDecoration.None),
        visualTransformation = if (passwordConfirmVisibility.value) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            val visibilityIcon = if (passwordConfirmVisibility.value) Icons.Filled.VisibilityOff else Icons.Filled.Visibility
            IconButton(onClick = { passwordConfirmVisibility.value = !passwordConfirmVisibility.value }) {
                Icon(visibilityIcon, "Toggle password visibility")
            }
        },

        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp)
            .border(
                1.dp,
                if (isPasswordConfirmError.value) Red else Gray,
                shape = RoundedCornerShape(8.dp)
            ),

        keyboardActions = KeyboardActions { },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Next
        ),
    )
    if (isPasswordConfirmError.value) {
        ErrorMessageText(message = "비밀번호가 일치하지 않습니다.", color = Red)
    }
}


/**
 * 이메일
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Composable
fun EmailTextField(
    email: String,
    onEmailChange: (String) -> Unit,
    isEmailError: MutableState<Boolean>,
    message: MutableState<String>,
    color: MutableState<Color>,
    isEmailCanUse: MutableState<Boolean>
) {
    val emailRegex = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{2,})".toRegex()
    TitleText("이메일 *")
    TextField(
        value = email,
        onValueChange = {
            onEmailChange(it)
            Log.d("회원", "이메일 입력")
            if (!emailRegex.matches(it)) {
                isEmailError.value = true
            } else {
                isEmailError.value = false
            }
        },
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.White,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            errorCursorColor = Color.Transparent,
            errorIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            disabledTextColor = Color.Transparent,
            cursorColor = Color.Transparent
        ),
        singleLine = true,
        maxLines = 1,
        isError = isEmailError.value,
        modifier = Modifier
            .fillMaxWidth()
            .border(
                1.dp,
                if (isEmailError.value || !isEmailCanUse.value) Red else Gray,
                shape = RoundedCornerShape(8.dp)
            ),
        keyboardActions = KeyboardActions { },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Next
        ),
    )
    Log.d("eeeeeeeee", "${isEmailError.value}")
    if (email.isNotBlank()) {

        if (isEmailError.value) {
            Log.d("회원", "이메일 정규식 통과못함")
            ErrorMessageText(
                message = "이메일 형식에 맞게 입력해 주세요",
                color = Red
            )
        } else {
            Log.d("회원", "이메일 정규식 통과")
            checkEmail(email) { isDuplicated ->
                if (isDuplicated) {
                    Log.d("email", "사용중인 이메일 입니다.")
                    message.value = "이미 사용중인 이메일 입니다."
                    color.value = Red
                    isEmailError.value = false
                    isEmailCanUse.value = false
                } else {
                    color.value = Black
                    Log.d("email", "사용가능한 이메일 입니다.")
                    // message = "사용 가능한 아이디 입니다."
                    message.value = "사용가능한 이메일 입니다."
                    isEmailError.value = false
                    isEmailCanUse.value = true
                }
            }
            ErrorMessageText(message = message.value, color = color.value)
        }
    }
}


/**
 * 닉네임
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Composable
fun NicknameText(

    nickname: String,
    onNicknameChange: (String) -> Unit,
    isNicknameError: MutableState<Boolean>,
    message: MutableState<String>,
    color: MutableState<Color>,
    isNicknameCanUse: MutableState<Boolean>
) {

    val nicknameRegex = "^[가-힣a-zA-Z0-9]{2,10}\$".toRegex()
    TitleText("닉네임 *")
    TextField(
        value = nickname,
        onValueChange = {
            onNicknameChange(it)
            if (!nicknameRegex.matches(it)) {
                Log.d("email", "새로만든 이메일 창 정규식")
                //  onEmailFocusChange(true)
                isNicknameError.value = true
            } else {
                //onEmailFocusChange(false)
                isNicknameError.value = false
            }
        },
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.White,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            errorCursorColor = Color.Transparent,
            errorIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            disabledTextColor = Color.Transparent,
            cursorColor = Color.Transparent
        ),
        singleLine = true,
        maxLines = 1,
        isError = isNicknameError.value,
        modifier = Modifier
            .fillMaxWidth()
            .border(
                1.dp,
                if (isNicknameError.value || !isNicknameCanUse.value) Red else Gray,
                shape = RoundedCornerShape(8.dp)
            ),
        keyboardActions = KeyboardActions { },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Done
        ),
    )
    if (nickname.isNotBlank()) {

        if (isNicknameError.value) {

            ErrorMessageText(
                message = "닉네임은 특수문자를 제외하고 2자-10자 로 입력해주세요.",
                color = MaterialTheme.colors.error
            )
        } else {
            Log.d("eeeeeeeee", "닉네임 정규식 통과")
            checkNickname(nickname) { isDuplicated ->
                if (isDuplicated) {
                    Log.d("email", "사용중인 닉네임 입니다.")
                    message.value = "이미 사용중인 닉네임 입니다."
                    color.value = Red
                    isNicknameError.value = false
                    isNicknameCanUse.value = false
                } else {
                    color.value = Black
                    Log.d("email", "사용가능한 닉네임 입니다.")
                    message.value = "사용가능한 닉네임 입니다."
                    isNicknameError.value = false
                    isNicknameCanUse.value = true
                }
            }
            ErrorMessageText(message = message.value, color = color.value)
        }
    }
}

/**
 * 설명 Text
 */
@Composable
fun TitleText(
    message: String
) {
    Text(message, modifier = Modifier.padding(bottom = 6.dp, top = 14.dp), fontWeight = FontWeight.Bold)
}
/**
 * 에러 발생 메세지
 */
@Composable
fun ErrorMessageText(
    message: String,
    color: Color
) {
    Text(
        text = message,
        color = color,
        style = MaterialTheme.typography.caption,
        modifier = Modifier.padding(start = 11.dp)
    )
}