package com.slembers.alarmony.alarm.entity;

import com.slembers.alarmony.alarm.exception.AlertErrorCode;
import com.slembers.alarmony.global.execption.CustomException;

public enum AlertTypeEnum {
    INVITE {
        @Override
        public String createAlertContent(String title) {
            return String.format("'%s' 그룹 초대입니다.", title);
        }
    },
    BANN {
        @Override
        public String createAlertContent(String title) {
            return String.format("'%s' 그룹에서 퇴출되었습니다.", title);
        }
    },
    REPLY {
        @Override
        public String createAlertContent(String title) {
            throw new CustomException(AlertErrorCode.ENUM_NOT_ALLOW);
        }
    },
    BASIC {
        @Override
        public String createAlertContent(String title) {
            throw new CustomException(AlertErrorCode.ENUM_NOT_ALLOW);
        }
    },
    DELETE {
        @Override
        public String createAlertContent(String title) {
            return String.format("'%s' 그룹이 삭제되었습니다.", title);
        }
    },
    ALARM {
        @Override
        public String createAlertContent(String title) {
            throw new CustomException(AlertErrorCode.ENUM_NOT_ALLOW);
        }
    },
    AUTO_LOGOUT {
        @Override
        public String createAlertContent(String title) {
            throw new CustomException(AlertErrorCode.ENUM_NOT_ALLOW);
        }
    },
    CHANGE_HOST {
        @Override
        public String createAlertContent(String title) {
            throw new CustomException(AlertErrorCode.ENUM_NOT_ALLOW);
        }
    },
    MODIFY_ALARM {
        @Override
        public String createAlertContent(String title) {
            throw new CustomException(AlertErrorCode.ENUM_NOT_ALLOW);
        }
    },
    ;

    public abstract String createAlertContent(String title);
}
