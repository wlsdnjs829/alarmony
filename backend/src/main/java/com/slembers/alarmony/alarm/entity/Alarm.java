package com.slembers.alarmony.alarm.entity;

import com.slembers.alarmony.alarm.dto.AlarmInfoDto;
import com.slembers.alarmony.global.util.CommonMethods;
import com.slembers.alarmony.member.entity.Member;
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
import javax.persistence.ManyToOne;
import java.time.LocalTime;

@Entity(name = "alarm")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
/*
    TODO-review P5

    annotation 활용이 정말 좋네요!
 */
@DynamicInsert
public class Alarm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "alarm_id")
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "time", nullable = false)
    private LocalTime time;

    /*
        TODO-review P5

        모든 엔티티가 hard-delete와 foreign key를 가져가는 거 같은데,
        soft-delete와 foreign key가 없는 것에 장단점을 고민해 보시면 좋을 거 같아요.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "host_id", nullable = false)
    private Member host;

    @Column(name = "alarm_date", columnDefinition = "VARCHAR(7)")
    @ColumnDefault("'0000000'")
    private String alarmDate;

    @Column(name = "sound_name", nullable = false)
    private String soundName;

    @Column(name = "sound_volume")
    @ColumnDefault("7")
    private int soundVolume;

    @Column(name = "vibrate")
    @ColumnDefault("false")
    private boolean vibrate;

    /*
        TODO-review P5

        setter 애너테이션을 활용하지 않고, 의미있는 메서드명으로 데이터를 수정하는 습관이 좋네요!
     */
    public void changeHost(Member member) {
        this.host = member;
    }

    public void changeInfo(AlarmInfoDto infoDto) {
        /*
            TODO-review P1

            java와 같이 type-safe 하지 않은 언어에서는 항상 npe에 대비하는 게 좋습니다.
            해당 infoDto도 controller에서부터 아무런 validator가 없기에 npe가 발생할 요소가 있네요.
         */
        this.title = infoDto.getTitle();
        this.content = infoDto.getContent();
        this.time = LocalTime.of(infoDto.getHour(), infoDto.getMinute());
        this.alarmDate = CommonMethods.changeBooleanListToString(infoDto.getAlarmDate());
        this.soundName = infoDto.getSoundName();
        this.soundVolume = infoDto.getSoundVolume();
        this.vibrate = infoDto.isVibrate();
    }
}
