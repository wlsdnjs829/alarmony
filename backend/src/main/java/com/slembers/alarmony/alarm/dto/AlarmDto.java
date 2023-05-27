package com.slembers.alarmony.alarm.dto;

import com.slembers.alarmony.global.util.CommonMethods;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
/*
    TODO-review P1

    제가 자바가 잘 기억이 나지 않는데,
    AllArgsConstructor annotation 선언을 하시고 아래에 모든 필드에 대한 메서드를 정의하신 부분과
    NoArgsConstructor의 쓰임새를 잘 모르겠네요.
 */
@NoArgsConstructor
@AllArgsConstructor
public class AlarmDto {

    private Long alarmId;

    private String title;

    private String content;

    private int hour;

    private int minute;

    private List<Boolean> alarmDate;

    /**
     *
     * @param alarmId 알람 아이디
     * @param title 제목
     * @param content 내용
     * @param hour 시간
     * @param minute 분
     * @param alarmDate 알람 요일 정보
     */
    public AlarmDto(Long alarmId, String title, String content, int hour, int minute, String alarmDate) {
        this.alarmId = alarmId;
        this.title = title;
        this.content = content;
        this.hour = hour;
        this.minute = minute;
        this.alarmDate = CommonMethods.changeStringToBooleanList(alarmDate);
    }
}
