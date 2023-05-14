package com.slembers.alarmony.feature.user


import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Switch
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment

import androidx.navigation.NavController
import com.slembers.alarmony.feature.common.NavItem
import com.slembers.alarmony.feature.ui.profilesetting.ProfileView


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun AccountMtnc(navController: NavController) {

    val Notichecked = remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("설정") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Filled.ArrowBack, "뒤로가기")
                    }
                },
                backgroundColor = MaterialTheme.colors.primary
            )
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            ProfileView(navController = navController)

            Button(
                onClick = {/* 태마설정변경*/ },
                modifier = Modifier
                    .width(240.dp)
                    .padding(10.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.OtherHouses,
                        contentDescription = "테마 설정",
//            tint = LocalContentColor.current.copy(alpha = ContentAlpha.high)
                    )
                    Text(text = "테마 설정")
                }

            }
            Button(
                onClick = {/* 어플리케이션 정보 페이지*/ },
                modifier = Modifier
                    .width(240.dp)
                    .padding(10.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.Info,
                        contentDescription = "어플리케이션 정보",
//            tint = LocalContentColor.current.copy(alpha = ContentAlpha.high)
                    )
                    Text(text = "어플리케이션 정보")
                }

            }
            Button(
                onClick = {/* 푸쉬알림 설정 변경*/ },
                modifier = Modifier
                    .width(240.dp)
                    .padding(10.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.Notifications,
                        contentDescription = "푸쉬알림 설정",
//            tint = LocalContentColor.current.copy(alpha = ContentAlpha.high)
                    )
                    Spacer(modifier = Modifier.width(20.dp))
                    Text(
                        text = "푸쉬알림 설정",
//            textAlign = TextAlign.Center

                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Switch(
                        checked = Notichecked.value,
                        onCheckedChange = { /**/ },
                        modifier = Modifier.padding(end = 12.dp)
                    )
                }
            }

            Button(
                onClick = {navController.navigate(NavItem.ReportPage.route)},
                modifier = Modifier
                    .width(240.dp)
                    .padding(10.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.Report,
                        contentDescription = "신고 하기",
                    )
                    Text(text = "신고 하기")
                }
            }
        }

    }

}