package ru.danbeldev.h1hackapi.features.services.importData.models;

import lombok.Data;

import java.io.Serializable;

@Data
public class ValueData implements Serializable {

    private String value;
    private ValueType valueType;
}
