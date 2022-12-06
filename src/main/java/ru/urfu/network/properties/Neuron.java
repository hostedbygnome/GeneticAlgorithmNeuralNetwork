package ru.urfu.network.properties;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
public class Neuron {
    Double totalizerValue = 0.0;
    Double outputValue = 0.0;
}
