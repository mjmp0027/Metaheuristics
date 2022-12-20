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
    private List<Double> x;
    private List<Double> y;
    private double[][] matriz;

    public void addX(String x) {
        this.x.add(Double.valueOf(x));
    }

    public void addY(String y) {
        this.y.add(Double.valueOf(y));
    }
}
