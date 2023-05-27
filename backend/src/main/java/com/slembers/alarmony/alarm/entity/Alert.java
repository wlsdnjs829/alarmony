package com.slembers.alarmony.alarm.entity;

import com.slembers.alarmony.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "alert")
public class Alert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "alert_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    private Member sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id", nullable = false)
    private Member receiver;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    /*
        TODO-review P5

        ColumnDefault annotation 활용해 보지 않아 순수 궁금증인데,
        private AlertTypeEnum type = AlertTypeEnum.BASIC; 으로 선언하는 것과 무슨 차이가 있을까요?
        enum으로 선언되어 있는 필드를 리터럴 문자로 선언하며 위험성을 가져갈 필요는 없어보입니다.

        BASIC의 값이 변경되었을 때도 놓칠 위험성이 있어보이구요.
     */
    @ColumnDefault("'BASIC'")
    private AlertTypeEnum type = AlertTypeEnum.BASIC;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "alarm_id")
    private Alarm alarm;

    public void setAlarm(Alarm alarm) {
        this.alarm = alarm;
    }
}
