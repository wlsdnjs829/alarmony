package com.slembers.alarmony.alarm.exception;

import com.slembers.alarmony.global.util.ErrorCode;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public enum AlarmErrorCode implements ErrorCode {

    ALARM_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 알람 정보를 찾을 수 없습니다."),
    MEMBER_NOT_IN_GROUP(HttpStatus.NOT_FOUND, "그룹에 존재하지 않는 멤버입니다."),
    ALARM_GET_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "알람 정보를 가져오는 중 에러가 발생했습니다."),
    ALARM_CREATE_ERROR(HttpStatus.BAD_REQUEST, "알람을 생성하는 중 에러가 발생했습니다."),
    MEMBER_IN_GROUP(HttpStatus.BAD_REQUEST, "그룹장은 그룹에 멤버가 없을 때만 탈퇴 가능합니다."),
    CANNOT_REMOVE_HOST(HttpStatus.BAD_REQUEST, "그룹장은 퇴출할 수 없습니다."),
    CANNOT_SEND_TO_HOST(HttpStatus.BAD_REQUEST, "그룹장에게 알람을 보낼 수 없습니다."),
    MEMBER_NOT_HOST(HttpStatus.FORBIDDEN, "호스트 권한이 없습니다."),
    ALARM_DATE_INFO_WRONG(HttpStatus.BAD_REQUEST, "요일 정보가 잘못되었습니다."),

    /*
        TODO-review P5

        메시지를 enum으로 관리하는 습관이 정말 좋네요!
        추후 글로벌로 확장할 때도 해당 클래스만 신경쓰면 되겠네요.

        사소한 것이지만 enum class에서는 trailing comma를 유지하는 습관도 있으면 좋을 거 같아요.
     */
    ALARM_ALREADY_INCLUDED(HttpStatus.MULTI_STATUS,"이미 알람 멤버에 포함되었습니다.")

    ;

    private final HttpStatus httpStatus;
    private final String detail;

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public String getDetail() {
        return detail;
    }

    @Override
    public String getName() {
        return name();
    }
}
