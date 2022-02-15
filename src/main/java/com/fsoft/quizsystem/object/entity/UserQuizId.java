package com.fsoft.quizsystem.object.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class UserQuizId implements Serializable {

    private long userId;

    private long quizId;

}
