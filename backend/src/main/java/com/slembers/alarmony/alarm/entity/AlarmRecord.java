package com.slembers.alarmony.alarm.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity(name = "alarm_record")
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
public class AlarmRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "alarm_record_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_alarm_id", nullable = false)
    private MemberAlarm memberAlarm;

    @Column(name = "success_count")
    @ColumnDefault("0")
    private int successCount;

    @Column(name = "total_count")
    @ColumnDefault("0")
    private int totalCount;

    @Column(name = "total_wake_up_time")
    @ColumnDefault("0")
    private long totalWakeUpTime;

    @Column(name = "today_alarm_record")
    private LocalDateTime todayAlarmRecord;

    @Column(name = "message")
    private String message;

    /**
     * 전달된 메시지로 기록한다.
     *
     * @param message 메시지
     */
    public void changeMessage(String message) {
        this.message = message;
    }

    /**
     * 알람 종료 성공으로 기록한다.
     * @param alarmTime 알람 시간
     * @param recordTime 기록 시간
     */
    public void recordSuccess(LocalTime alarmTime, LocalDateTime recordTime) {
        this.message = "";
        this.totalCount++;
        this.successCount++;
        long seconds = Duration.between(alarmTime,recordTime.toLocalTime()).toSeconds();
        /*
            TODO-review P1

            메서드 내 리터럴 상수로 고정되어 있는 건 TC 작성에서도 도움이 되지 않습니다.
            제어가 가능한 메서드로 변경해 보시는 건 어떨까요?

            https://jojoldu.tistory.com/676 블로그 참고해 보시면 좋을 거 같아요.
         */
        totalWakeUpTime += seconds < 0 ? 86400 + seconds : seconds;
        // TODO : 서버 시간이 9시간이 다르기 때문에 저장하기 전에는 9시간을 빼주어야 한다. (추후 수정 필요)
        this.todayAlarmRecord = recordTime.minusHours(9);

    }

    /**
     * 알람 종료 실패로 기록한다.
     * @param alarmTime 알람 시간
     */
    public void recordFailed(LocalTime alarmTime, LocalDateTime recordTime) {
        this.message = "";
        // 최대 스누즈 시간 일단 30분으로 설정
        long maxSnooze = 3600;
        this.totalCount++;
        this.todayAlarmRecord = LocalDateTime.of(
                LocalDate.of(recordTime.getYear(),recordTime.getMonth(),recordTime.getDayOfMonth()),
                alarmTime.plusSeconds(maxSnooze)
        );
        totalWakeUpTime += maxSnooze;

    }
}
