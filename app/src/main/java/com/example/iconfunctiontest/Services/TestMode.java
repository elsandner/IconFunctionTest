package com.example.iconfunctiontest.Services;

public enum TestMode {
    Welcome,
    VisualMode,
    BlindMode,
    End,
    Invalid;

    public static TestMode getTestMode(String trial){
        switch(trial){
            //Welcome Screen
            case "Welcome":
                return Welcome;
            //Visual Mode
            case "Test1":
            case "Test3":
            case "Test5":
            case "Test7":
                return VisualMode;

            // Blind Mode
            case "Test2":
            case "Test4":
            case "Test6":
            case "Test8":
                return BlindMode;

            case "End":
                return End;
        }

        System.out.println("Something went wring in getTestMode Method");
        return Invalid;
    }


}
