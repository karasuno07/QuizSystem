package com.fsoft.quizsystem.object.entity;

import lombok.*;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "users_quizs")
@Audited
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserQuiz {

    @EmbeddedId
    private UserQuizId id;

    @ManyToOne
    @JoinColumn(name = "userId", insertable = false, updatable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "quizId", insertable = false, updatable = false)
    private Quiz quiz;

    private Integer maxScore;

    private Integer remainingTime;

    @Column(name = "recent_active_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date recentActiveDate;
}