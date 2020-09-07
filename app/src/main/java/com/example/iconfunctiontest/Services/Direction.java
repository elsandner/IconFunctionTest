package com.example.iconfunctiontest.Services;

//THIS CLASS IS NOT NEEDED ANYMORE !!!


public enum Direction {
    East,
    NorthEast,
    North,
    NorthWest,
    West,
    SouthWest,
    South,
    SouthEast,
    Invalid;


    @Override
    public String toString() {
        switch (this) {
            case East:
                return "East";
            case NorthEast:
                return "North-East";
            case North:
                return "North";
            case NorthWest:
                return "North-West";
            case West:
                return "West";
            case SouthWest:
                return "South-West";
            case South:
                return "South";
            case SouthEast:
                return "South-East";
            case Invalid:
                return "Something went wrong";
            default:
                throw new IllegalArgumentException();
        }
    }


}