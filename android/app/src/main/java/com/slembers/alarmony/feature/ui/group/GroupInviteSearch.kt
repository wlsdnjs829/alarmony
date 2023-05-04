package com.slembers.alarmony.feature.ui.group

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Checkbox
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.slembers.alarmony.R
import com.slembers.alarmony.feature.common.CardBox
import com.slembers.alarmony.feature.common.CardTitle
import com.slembers.alarmony.model.db.dto.MemberDto
import com.slembers.alarmony.network.service.GroupService


@OptIn(ExperimentalComposeUiApi::class)
@Composable
@ExperimentalMaterial3Api
@ExperimentalGlideComposeApi
fun SearchInviteMember() {

    var text by remember { mutableStateOf("") }
    var members : List<MemberDto> = remember { mutableStateListOf() }

    CardBox(
        title = { CardTitle(title = "검색") },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 20.dp,
                        top = 0.dp,
                        bottom = 10.dp,
                        end = 20.dp
                    )
            ) {
                BasicTextField(
                    value = text,
                    onValueChange = {
                        text = it
                        GroupService.searchMember(
                            keyword = it,
                            memberList = {members = it.memberList}
                        )
                    },
                    singleLine = true,
                    textStyle = TextStyle(
                        color = Color.Black,
                        fontSize = 20.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Normal,
                    ),
                    modifier = Modifier
                        .height(50.dp)
                        .fillMaxWidth(),
                    decorationBox = { innerTextField ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    Color(0xffD9D9D9),
                                    shape = MaterialTheme.shapes.extraLarge
                                )
                                .padding(horizontal = 16.dp), // inner padding
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Favorite icon",
                                tint = Color.DarkGray
                            )
                            Spacer(modifier = Modifier.width(width = 8.dp))
                            if (text.isEmpty()) {
                                Text(
                                    text = "닉네임을 입력새해주세요.",
                                    modifier = Modifier.fillMaxWidth(1f),
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Normal,
                                    color = Color.LightGray
                                )
                            }
                            innerTextField()
                        }
                    }
                )

                LazyColumn() {
                    items(members) { member ->
                        SearchMember(
                            member.nickname,
                            member.profileImg
                        )
                    }
                }
            }
        }
    )
}

@Composable
fun SearchMember(
    nickname : String,
    profiles : String? = null
) {

    var isClicked by remember { mutableStateOf(false)  }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .padding(start = 2.dp, end = 20.dp, top = 3.dp, bottom = 1.dp)
            .fillMaxWidth()
            .clickable { isClicked = !isClicked }
    )
    {
        if(profiles != null ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(profiles)
                    .build(),
                contentDescription = "ImageRequest example",
                modifier = Modifier.size(65.dp)
            )
        } else {
            Image(
                painter = painterResource(id = R.drawable.baseline_account_circle_24),
                contentDescription = "ImageRequest example",
                modifier = Modifier.size(65.dp)
            )
        }
        Text(
            text = nickname,
            fontSize = 17.sp,
            modifier = Modifier.fillMaxWidth(1f)
        )
        Checkbox(
            checked = isClicked,
            onCheckedChange = { isClicked = it }
        )
    }
}