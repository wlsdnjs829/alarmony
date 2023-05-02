package com.slembers.alarmony.alarm.controller;

import com.slembers.alarmony.alarm.dto.request.ResponseInviteRequestDto;
import com.slembers.alarmony.alarm.dto.response.AlertListResponseDto;
import com.slembers.alarmony.alarm.exception.AlertErrorCode;
import com.slembers.alarmony.alarm.service.AlertService;
import com.slembers.alarmony.global.execption.CustomException;
import com.slembers.alarmony.global.jwt.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/alert")
@RequiredArgsConstructor
public class AlertController {

    private final AlertService alertService;

    /**
     * 내 알림 목록 가져오기
     *
     * @return 알림 목록
     */
    @GetMapping
    public ResponseEntity<AlertListResponseDto> getAlertList() {
        String username = SecurityUtil.getCurrentUsername().get();
        return new ResponseEntity<>(alertService.getAlertList(username), HttpStatus.OK);
    }

    /**
     * 알림 화면에서 특정 알림을 선택하여 개별로 지울 수 있다.
     *
     * @param alertId 알림 아이디
     * @return 성공 메시지
     */
    @DeleteMapping("/{alert-id}")
    public ResponseEntity<String> deleteAlert(@PathVariable("alert-id") Long alertId) {
        alertService.deleteAlert(alertId);
        return new ResponseEntity<>("알림을 삭제했습니다.", HttpStatus.OK);
    }

    /**
     * 그룹 초대에 대해 수락/거절 응답을 한다.
     * @param alertId 알림 아이디
     * @param responseInviteRequestDto 응답 객체 true : 수락, false : 거절
     * @return 응답 메시지
     */
    @PostMapping("/{alert-id}/response")
    public ResponseEntity<String> responseInvite(@PathVariable("alert-id") Long alertId, @RequestBody ResponseInviteRequestDto responseInviteRequestDto) {

        if (responseInviteRequestDto == null || responseInviteRequestDto.getAccept() == null)
            throw new CustomException(AlertErrorCode.ALERT_BAD_REQUEST);
        else if (responseInviteRequestDto.getAccept()) {
            alertService.acceptInvite(alertId);
            return new ResponseEntity<>("초대를 수락했습니다.", HttpStatus.OK);
        } else {
            alertService.refuseInvite(alertId);
            return new ResponseEntity<>("초대를 거절했습니다.", HttpStatus.OK);
        }
    }

    /**
     * 메시지 전송 테스트 메소드
     *
     * @return 성공 메시지
     */
    @PostMapping("/test")
    public ResponseEntity<String> testPushAlert() {
        String username = SecurityUtil.getCurrentUsername().get();
        alertService.testPushAlert(username);
        return new ResponseEntity<>("메시지 전송 성공", HttpStatus.OK);
    }
}