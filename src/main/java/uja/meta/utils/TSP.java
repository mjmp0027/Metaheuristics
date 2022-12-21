package uja.meta.utils;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class TSP {
    private String name;
    private String type;
    private String comment;
    private Integer dimension;
    private String edgeWeightType;
    private double[][] matriz;
}
