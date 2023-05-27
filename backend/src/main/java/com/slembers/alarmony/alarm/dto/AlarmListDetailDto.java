package com.slembers.alarmony.alarm.dto;

import com.slembers.alarmony.global.util.CommonMethods;
import lombok.Getter;

import java.util.List;

@Getter
public class AlarmListDetailDto {

    /*
        TODO-review P5

        변경될 요소가 없는 프로퍼티는 명시적으로 final 추가해 주시는 게 어떨까요?

        추가로 Dto로 통일하고 계시지만, 대부분 VO의 성격을 띄고 있는 객체로 보입니다.
        객체지향에 대해 다시 한 번 생각해 보면 더 도움이 될 거 같아요.
     */
    private boolean host;

    private Long alarmId;

    private String title;

    private String content;

    private int hour;

    private int minute;

    private List<Boolean> alarmDate;

    private String soundName;

    private int soundVolume;

    private boolean vibrate;

    public AlarmListDetailDto(boolean host ,Long alarmId, String title, String content, int hour, int minute, String alarmDate, String soundName, int soundVolume, boolean vibrate) {
        this.host = host;
        this.alarmId = alarmId;
        this.title = title;
        this.content = content;
        this.hour = hour;
        this.minute = minute;
        this.alarmDate = CommonMethods.changeStringToBooleanList(alarmDate);
        this.soundName = soundName;
        this.soundVolume = soundVolume;
        this.vibrate = vibrate;
    }
}
