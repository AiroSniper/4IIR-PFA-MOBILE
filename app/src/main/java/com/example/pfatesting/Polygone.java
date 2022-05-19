package com.example.pfatesting;

import java.util.ArrayList;
import java.util.List;

public class Polygone {

    private List<Point> points;
    private List<Line> lines;
    private String annotation;

    public Polygone(List<Point> points, List<Line> lines, String annotation) {
        this.points = new ArrayList<>(points);
        this.lines = new ArrayList<>(lines);
        this.annotation = annotation;
    }


    public List<Point> getPoints() {
        return points;
    }

    public void setPoints(List<Point> points) {
        this.points = points;
    }

    public List<Line> getLines() {
        return lines;
    }

    public void setLines(List<Line> lines) {
        this.lines = lines;
    }

    public String getAnnotation() {
        return annotation;
    }

    public void setAnnotation(String annotation) {
        this.annotation = annotation;
    }

    @Override
    public String toString() {
        return "Polygone{" +
                "points=" + points.size() +
                ", lines=" + lines.size() +
                ", annotation='" + annotation + '\'' +
                '}';
    }
}
