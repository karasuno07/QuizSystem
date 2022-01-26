package com.fsoft.quizsystem.object.constant.converter;

import com.fsoft.quizsystem.object.constant.DifficultyPoint;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

@Converter(autoApply = true)
public class DifficultyPointConverter implements AttributeConverter<DifficultyPoint, Integer> {
    @Override
    public Integer convertToDatabaseColumn(DifficultyPoint attribute) {
        if (attribute == null) return null;

        return attribute.getPoint();
    }

    @Override
    public DifficultyPoint convertToEntityAttribute(Integer value) {
        if (value == null) return null;

        return Stream.of(DifficultyPoint.values())
                     .filter(e -> e.getPoint() == value)
                     .findFirst()
                     .orElseThrow(IllegalArgumentException::new);
    }
}
