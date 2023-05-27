package com.slembers.alarmony.global.security.util;

import com.slembers.alarmony.global.execption.CustomException;
import com.slembers.alarmony.member.exception.AuthErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

@Slf4j
/*
    TODO-review P3

    utility 클래스에 싱글톤을 유지하려고 하셔서 private 생성자를 만드신 것으로 보이는데,
    해당 방법보다는 public class final SecurityUtil로 선언하시거나,

    private SecurityUtil() {
        throw new AssertionError();
    }

    와 같은 방법으로 명시적으로 표현해 주시는 게 좋아보입니다.

    effective java를 참고해 보시면 좋을 거 같아요.
*/
public class SecurityUtil {
    private SecurityUtil() {
        throw new AssertionError();
    }

    /*
        TODO-review P5

        spring-security 기능을 사용하지만, authentication 정보는 sessionUtils 더 가까운 메서드가 아닐까 싶어요.
        한 번 고민해 보는 것만으로도 충분할 것 같아 P5로 두었습니다.
     */
    public static String getCurrentUsername() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            /*
                TODO-review P3

                authentication 관련 에러 로그를 따로 유틸 메서드에서 찍지 않고,
                GlobalExceptionHandler에 위임하는 게 어떨까 싶네요.

                그리고 CustomException이 맡고 있는 역할이 조금 커보입니다.
                AuthenticationException과 같이 명시적인 Exception을 만들어 사용하시는 건 어떠실까요?
             */
            log.error("[SecurityUtil] Authentication 정보가 없습니다.");

            /*
                TODO-review P5

                가끔 빈 공백이 하나씩 들어가 있는데, 클래스 정렬을 하는 습관을 들이면 좋을 거 같아요.

                window 기준 : ctrl + a -> ctrl + alt + l
             */
            throw new CustomException(AuthErrorCode.NO_AUTHENTICATION);
        }

        /*
            TODO-review P2

            가변 변수를 활용하는 건 지양하는 게 좋습니다.
            현재는 하나의 메서드에서만 사용이 되지만, 매개변수로 다른 메서드에 영향을 미친다면 해당 메서드만 보고 변수의 값을 예측하기가 힘들어집니다.

            final Object principal = authentication.getPrincipal();

            if (principal instanceof UserDetails) {
                UserDetails springSecurityUser = (UserDetails) principal;
                return springSecurityUser.getUsername();
            }

            if (principal instanceof String) {
                return (String) principal;
            }

            return null;

            다음과 같이 조건 별 로직을 알 수 있고, 포함되지 않는 조건에는 null이 반환되는 것을 확실히 알 수 있도록 하면 어떨까요?
         */

        String username = null;
        final Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            UserDetails springSecurityUser = (UserDetails) principal;
            username = springSecurityUser.getUsername();
        } else if (principal instanceof String) {
            username = (String) principal;
        }

        return username;
    }
}
