package com.slembers.alarmony.alarm.service;

import com.slembers.alarmony.alarm.dto.AlarmDto;
import com.slembers.alarmony.alarm.dto.AlarmInfoDto;
import com.slembers.alarmony.alarm.dto.AlarmListDetailDto;
import com.slembers.alarmony.alarm.dto.response.AlarmListResponseDto;
import com.slembers.alarmony.alarm.entity.Alarm;
import com.slembers.alarmony.alarm.entity.AlarmRecord;
import com.slembers.alarmony.alarm.entity.MemberAlarm;
import com.slembers.alarmony.alarm.exception.AlarmErrorCode;
import com.slembers.alarmony.alarm.exception.AlarmRecordErrorCode;
import com.slembers.alarmony.alarm.exception.MemberAlarmErrorCode;
import com.slembers.alarmony.alarm.repository.AlarmRecordRepository;
import com.slembers.alarmony.alarm.repository.AlarmRepository;
import com.slembers.alarmony.alarm.repository.MemberAlarmRepository;
import com.slembers.alarmony.global.execption.CustomException;
import com.slembers.alarmony.global.util.CommonMethods;
import com.slembers.alarmony.member.entity.Member;
import com.slembers.alarmony.member.exception.MemberErrorCode;
import com.slembers.alarmony.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlarmServiceImpl implements AlarmService {

    private final AlarmRepository alarmRepository;

    private final MemberAlarmRepository memberAlarmRepository;

    private final MemberRepository memberRepository;

    private final AlarmRecordRepository alarmRecordRepository;

    /**
     * 유저네임을 기준으로 멤버알람 리스트를 가져오고, 이를 responseDTO에 담는다.
     *
     * @param username 아이디
     * @return 알람 리스트
     */
    @Override
    public AlarmListResponseDto getAlarmList(String username) {

        Member member = memberRepository.findByUsername(username)
            .orElseThrow(() -> new CustomException(MemberErrorCode.MEMBER_NOT_FOUND));

        try {
            //멤버의 멤버알람 목록을 가져온다
            List<AlarmListDetailDto> alarms = memberAlarmRepository.getAlarmDtosByMember(member.getId());
            // 리스트를 객체에 담아서 전송한다.
            return AlarmListResponseDto.builder().alarms(alarms).build();
        } catch (Exception e) {
            /*
                TODO-review P1

                에러메시지의 내용만 가져오는 건 빠른 트래킹에 한계가 있습니다.
                Exceptionutils getstacktrace 메서드를 활용해 보는 건 어떨까요?
             */
            log.error(e.getMessage());
            throw new CustomException(AlarmErrorCode.ALARM_GET_ERROR);
        }
    }

    /**
     * 신규 알람을 생성한다.
     *
     * @param username       현재 로그인 아이디
     * @param alarmInfoDto 알람 생성 정보
     * @return 알람 아이디
     */
    @Override
    public Long createAlarm(String username, AlarmInfoDto alarmInfoDto) {

        Member groupLeader = memberRepository.findByUsername(username)
            .orElseThrow(() -> new CustomException(MemberErrorCode.MEMBER_NOT_FOUND));

        /*
            TODO-review P3

            개인적인 취향이지만, 다음과 같이 try-catch에서 변수를 초기화하는 방식은 메서드의 길이도 길어질 뿐 아니라
            가독성도 좋지 않습니다.

            메인 메서드에서는 한 눈에 흐름을 알 수 있도록 메서드로 변경해 보시는 건 어떨까요? (예제 참고)
         */
        final Alarm alarm = createAlarm(alarmInfoDto, groupLeader);

        MemberAlarm memberAlarm;
        // 그룹장을 알람-멤버에 추가한다.
        try {
            memberAlarm = MemberAlarm.builder()
                .member(groupLeader)
                .alarm(alarm)
                .build();
            memberAlarmRepository.save(memberAlarm);
        } catch (Exception e) {
            log.error(e.getMessage());
            // 알람-멤버에 추가하는 도중 에러가 생긴다면 알람도 지워야 한다.
            alarmRepository.delete(alarm);
            throw new CustomException(MemberAlarmErrorCode.MEMBER_ALARM_INPUT_ERROR);
        }

        // 그룹장의 알람을 알림-기록에 추가한다.
        AlarmRecord alarmRecord;
        try {
            alarmRecord = AlarmRecord.builder()
                .memberAlarm(memberAlarm)
                .successCount(0)
                .totalCount(0)
                .message("")
                .build();
            alarmRecordRepository.save(alarmRecord);
        } catch (Exception e) {
            log.error(e.getMessage());
            // 알림-기록 추가에 실패하면 알람과 알람-멤버도 지워야 한다.
            memberAlarmRepository.delete(memberAlarm);
            alarmRepository.delete(alarm);
            throw new CustomException(AlarmRecordErrorCode.ALARM_RECORD_INPUT_ERRER);
        }

        return alarm.getId();
    }

    @NotNull
    private Alarm createAlarm(AlarmInfoDto alarmInfoDto, Member groupLeader) {
        // 알람을 생성한다
        try {
            // 알람을 생성한다.
            Alarm alarm = Alarm.builder()
                .title(alarmInfoDto.getTitle())
                .content(alarmInfoDto.getContent())
                .time(LocalTime.of(alarmInfoDto.getHour(), alarmInfoDto.getMinute()))
                .host(groupLeader)
                .alarmDate(CommonMethods.changeBooleanListToString(alarmInfoDto.getAlarmDate()))
                .soundName(alarmInfoDto.getSoundName())
                .soundVolume(alarmInfoDto.getSoundVolume())
                .vibrate(alarmInfoDto.isVibrate())
                .build();

            return alarmRepository.save(alarm);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new CustomException(AlarmErrorCode.ALARM_CREATE_ERROR);
        }
    }

    /**
     * 특정 알람아이디를 주면, 알람 기록을 찾아서 메시지를 기록해둔다.
     *
     * @param alarmId 알람 아이디
     * @param message 메시지
     */
    @Override
    public void putAlarmMessage(String username, Long alarmId, String message) {

        Member member = memberRepository.findByUsername(username)
            .orElseThrow(() -> new CustomException(MemberErrorCode.MEMBER_NOT_FOUND));

        // 멤버 정보와 알람 아이디를 바탕으로 알람 레코드를 가져온다.
        AlarmRecord alarmRecord = alarmRecordRepository.findByMemberAndAlarm(member.getId(),
                alarmId)
            .orElseThrow(() -> new CustomException(AlarmRecordErrorCode.ALARM_RECORD_NOT_EXIST));

        try {
            alarmRecord.changeMessage(message);
            alarmRecordRepository.save(alarmRecord);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new CustomException(MemberAlarmErrorCode.MEMBER_ALARM_INPUT_ERROR);
        }
    }

    /**
     * 특정 알람 아이디의 정보를 반환한다.
     *
     * @param alarmId 알람 아이디
     * @return 알람 정보
     */
    @Override
    public AlarmDto getAlarmInfo(Long alarmId) {
        try {
            // 알람 정보를 가져온다.
            Alarm alarm = findAlarmByAlarmId(alarmId);
            // 중복 계산을 피하기 위해 시간 정보를 가져온다.
            LocalTime localTime = alarm.getTime();

            // 알람 객체를 바로 리턴한다.
            return new AlarmDto(alarm.getId(), alarm.getTitle(), alarm.getContent(),
                localTime.getHour(), localTime.getMinute(), alarm.getAlarmDate());
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new CustomException(AlarmErrorCode.ALARM_GET_ERROR);
        }
    }

    /**
     * 알람 아이디로 알람 객체를 찾아온다.
     *
     * @param alarmID 알람 아이디
     * @return 알람 객체
     */
    @Override
    public Alarm findAlarmByAlarmId(Long alarmID) {
        return alarmRepository.findById(alarmID)
            .orElseThrow(() -> new CustomException(AlarmErrorCode.ALARM_NOT_FOUND));
    }

    /**
     * 알람 정보 변경
     * @param username 현재 로그인 유저
     * @param alarmId 알람 아이디
     * @param alarmInfoDto 알람 변경 정보
     * @return 알람 이전 이름
     */
    @Transactional
    @Override
    public String modifyAlarmInfo(String username, Long alarmId, AlarmInfoDto alarmInfoDto) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(MemberErrorCode.MEMBER_NOT_FOUND));
        Alarm alarm = alarmRepository.findById(alarmId)
                .orElseThrow(() -> new CustomException(AlarmErrorCode.ALARM_NOT_FOUND));

        String previousName = alarm.getTitle();

        /*
            TODO-review P3

            host도 non-null로 지정되어 있지만, 애너테이션으로 지정되어 있고 내부 메서드로 충분히 nullable한 객체로 될 수 있어보입니다.
            확실한 member를 앞으로 변경하시는 건 어떨까요?

            ex) !member.equals~

            그리고, 코드 컨벤션을 위해 조건식 내 한 줄이라도 중괄호로 묶어주는 습관을 들이는 게 좋을 거 같습니다.
         */
        if(!alarm.getHost().equals(member))
            throw new CustomException(AlarmErrorCode.MEMBER_NOT_HOST);

        alarm.changeInfo(alarmInfoDto);
        alarmRepository.save(alarm);

        return previousName;
    }
}
