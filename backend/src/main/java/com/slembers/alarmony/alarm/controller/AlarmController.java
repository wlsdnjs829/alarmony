package com.slembers.alarmony.alarm.controller;

import com.slembers.alarmony.alarm.dto.AlarmDto;
import com.slembers.alarmony.alarm.dto.AlarmEndRecordDto;
import com.slembers.alarmony.alarm.dto.AlarmInfoDto;
import com.slembers.alarmony.alarm.dto.request.PutAlarmMessageRequestDto;
import com.slembers.alarmony.alarm.dto.request.PutAlarmRecordTimeRequestDto;
import com.slembers.alarmony.alarm.dto.response.AlarmListResponseDto;
import com.slembers.alarmony.alarm.service.AlarmRecordService;
import com.slembers.alarmony.alarm.service.AlarmService;
import com.slembers.alarmony.alarm.service.AlertService;
import com.slembers.alarmony.global.security.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@RestController
/*
  TODO-review P2

  기본적으로 Restful API 하위 호환성을 보장할 수 없는 상황이 많이 나오기 때문에,
  RequestMapping 버저닝을 하는 게 좋습니다.

  ex. @RequestMapping(value = PATH_V1 + "/alarms")
 */
@RequestMapping("/alarms")
@RequiredArgsConstructor
public class AlarmController {

    private final AlarmService alarmService;

    private final AlarmRecordService alarmRecordService;

    private final AlertService alertService;

    /**
     * 현재 로그인 유저의 알람 리스트를 가져온다
     *
     * @return 알람 정보 리스트
     */
    @GetMapping
    /*
      TODO-review P2

      RestController annotation을 활용하고 계시고, 따로 상태값을 핸들링하고 계신 게 아닌 것으로 보입니다.
      단순 DTO 반환에서 ResponseEntity를 활용하시는 이유가 있을까요?
     */
    public ResponseEntity<AlarmListResponseDto> getAlarmList() {
        String username = SecurityUtil.getCurrentUsername();
        return ResponseEntity.ok(alarmService.getAlarmList(username));
    }

    /**
     * 신규 알람을 생성한다.
     *
     * @param alarmInfoDto 생성 알람 정보
     * @return 성공 메시지
     */
    @PostMapping
    /*
       TODO-review P1

       Response에 Map을 반환하는 건 상당히 좋지 않습니다.

       잘못된 타입이 들어가더라도 컴파일 에러로 잡아낼 수 없고,
       추가적인 타입캐스팅 비용 발생과 객체 내 불변성을 확보할 수 없습니다.

       가장 중요한 건 OOP에 위배됩니다.
     */
    public ResponseEntity<Map<String, Object>> createAlarm(
            @Valid @RequestBody AlarmInfoDto alarmInfoDto) {
        String username = SecurityUtil.getCurrentUsername();
        Long alarmId = alarmService.createAlarm(username, alarmInfoDto);

        Map<String, Object> map = new HashMap<>();
        map.put("groupId", alarmId);
        return ResponseEntity.ok(map);
    }

    /*
        TODO-review P5

        메서드에 주석을 다는 습관은 정말 좋네요!
     */
    /**
     * 특정 알람아이디를 주면, 알람 기록을 찾아서 메시지를 기록해둔다.
     *
     * @param alarmId                   알람 아이디
     * @param putAlarmMessageRequestDto 넣을 메시지
     * @return 확인 메시지
     */
    /*
        TODO-review P5

        Request Method를 분리해서 사용하시네요! 좋은 습관입니다.
     */
    @PutMapping("/{alarm-id}/message")
    public ResponseEntity<Void> putAlarmMessage(
            @PathVariable("alarm-id") Long alarmId,
            @Valid @RequestBody PutAlarmMessageRequestDto putAlarmMessageRequestDto) {

        String username = SecurityUtil.getCurrentUsername();

        alarmService.putAlarmMessage(username, alarmId, putAlarmMessageRequestDto.getMessage());

        /*
            TODO-review P5

            response status도 활용하시네요! 아주 좋은 습관입니다.
            하지만 이것도 단순히 상태값 변경이라면 ResponseEntity를 활용할 이유는 없어보여요.

            @ResponseStatus(value = HttpStatus.NO_CONTENT)
            와 같이 애너테이션을 활용하는 건 어떨까요? 뎁스를 더 들어가지 않고 반환하는 상태 코드도 바로 알 수 있습니다.
         */
        return ResponseEntity.noContent().build();
    }

    /**
     * 특정 알람 아이디로 알람 정보를 가져온다
     *
     * @param alarmId 알람 아이디
     * @return 알람 정보
     */
    @GetMapping("/{alarm-id}")
    public ResponseEntity<AlarmDto> getAlarmInfo(@PathVariable("alarm-id") Long alarmId) {

        return ResponseEntity.ok(alarmService.getAlarmInfo(alarmId));
    }

    /**
     * 알람 종료에 성공하면 기록을 저장한다.
     *
     * @param alarmId                      알람 아이디
     * @param putAlarmRecordTimeRequestDto 알람 성공 요청 객체
     * @return 성공 메시지
     */
    @PutMapping("/{alarm-id}/record")
    public ResponseEntity<Void> putAlarmRecord(@PathVariable("alarm-id") Long alarmId,
                                               @Valid @RequestBody PutAlarmRecordTimeRequestDto putAlarmRecordTimeRequestDto) {
        String username = SecurityUtil.getCurrentUsername();

        /*
            TODO-review P3

            빌더 패턴을 사용하는 건 좋지만,
            현재 소스에서 빌더 패턴을 사용하려는 의도가 나타난 로직은 없어보입니다.

            내부 객체에 직접 접근을 막기 위해 접근 제어자를 추가하고, 정적 메서드로 변경해 보는 건 어떨까요?
         */
        alarmRecordService.putAlarmRecord(AlarmEndRecordDto.builder()
                .alarmId(alarmId)
                .username(username)
                .datetime(
                        LocalDateTime.parse(putAlarmRecordTimeRequestDto.getDatetime(),
                                /*
                                    TODO-review P5

                                    정의되어 있는 constant 활용하는 건 좋은 습관입니다!
                                 */
                                DateTimeFormatter.ISO_DATE_TIME))
                /*
                    TODO-review P5

                    우리에겐 깃이 있으니까, 불필요한 주석은 과감히 삭제하는 게 좋습니다.
                 */
//                    putAlarmRecordTimeRequestDto.getDatetime()
                .success(true)
                .build());

        /*
            TODO-review P3

            생성 / 수정 API는 변경된 객체 정보를 반환해 주는 게 좋습니다.
            수정된 정보를 반환해 주지 않으면, 클라이언트 입장에서는 수정 이후 조회라는 불필요한 I/O가 생깁니다.
         */
        return ResponseEntity.noContent().build();
    }

    /**
     * 알람 종료에 실패시 기록을 저장한다.
     *
     * @param alarmId 알람 아이디
     * @return 실패 메시지
     */
    @PutMapping("/{alarm-id}/failed")
    public ResponseEntity<Void> putAlarmFailed(@PathVariable("alarm-id") Long alarmId) {
        String username = SecurityUtil.getCurrentUsername();
        alarmRecordService.putAlarmRecord(AlarmEndRecordDto.builder()
                .alarmId(alarmId)
                .username(username)
                .success(false)
                .build());
        return ResponseEntity.noContent().build();
    }

    /**
     * 알람 정보 변경
     * @param alarmId 변경할 알람 아이디
     * @param alarmInfoDto 변경 정보
     * @return 없음
     */
    @PutMapping("/{alarm-id}")
    public ResponseEntity<Void> modifyAlarmInfo(
            @PathVariable(name = "alarm-id") Long alarmId,
            @RequestBody AlarmInfoDto alarmInfoDto
    ) {
        String username = SecurityUtil.getCurrentUsername();
        String previousName = alarmService.modifyAlarmInfo(
                username,
                alarmId, alarmInfoDto);
        alertService.sendModifiedAlarm(username, alarmId, previousName);
        return ResponseEntity.noContent().build();
    }
}
