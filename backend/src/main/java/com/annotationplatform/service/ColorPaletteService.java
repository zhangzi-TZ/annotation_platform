package com.annotationplatform.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class ColorPaletteService {

    private static final List<String> COLORS = List.of(
            "#FF6B6B",
            "#4ECDC4",
            "#556270",
            "#C7F464",
            "#FF9F1C",
            "#2EC4B6",
            "#5E60CE",
            "#FF5D8F",
            "#06D6A0",
            "#118AB2"
    );

    private final AtomicInteger counter = new AtomicInteger();

    public String nextColor() {
        int index = Math.abs(counter.getAndIncrement()) % COLORS.size();
        return COLORS.get(index);
    }
}
