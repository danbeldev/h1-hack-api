package ru.danbeldev.h1hackapi.features.services.importData.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Prop {

    private String name;
    private ValueType type = ValueType.STRING;
}
