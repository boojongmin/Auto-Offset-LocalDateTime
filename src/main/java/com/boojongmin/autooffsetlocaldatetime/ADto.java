package com.boojongmin.autooffsetlocaldatetime;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class ADto {
    private LocalDateTime a = LocalDateTime.now();
}
