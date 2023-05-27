package com.slembers.alarmony.alarm.service;

import com.slembers.alarmony.alarm.dto.AlarmEndRecordDto;
import com.slembers.alarmony.alarm.dto.AlarmRecordDto;
import com.slembers.alarmony.alarm.dto.MemberRankingDto;

import java.time.LocalDateTime;
import java.util.List;

/*
    TODO-review P1

    개인적으로 service interface layer를 둔 의미를 잘 모르겠네요.
    controller - service layer간 의존성을 끊기 위한 역할도 아니고,
    interface가 controller에 있어 dip를 의도한 설계도 아닌 것으로 보입니다.

    이렇게 설계한 의도가 있을까요?
 */
public interface AlarmRecordService {

    /**
     * 오늘의 알람 기록을 얻어온다.
     *
     * @param groupId 그룹 id
     * @return 오늘 알람 기록
     */
    List<AlarmRecordDto> getTodayAlarmRecords(Long groupId, LocalDateTime todayTime);

    /**
     * 알람 랭킹 기록을 얻어온다.
     *
     * @param groupId 그룹 id
     * @return 알람 랭킹 기록
     */
    List<MemberRankingDto> getAlarmRanking(Long groupId);

    /**
     * 알람 종료 성공 시 기록한다.
     * @param alarmEndRecordDto 알람 성공 객체
     */
    void putAlarmRecord(AlarmEndRecordDto alarmEndRecordDto);
}
