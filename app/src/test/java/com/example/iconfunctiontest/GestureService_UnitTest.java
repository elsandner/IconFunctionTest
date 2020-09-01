package com.example.iconfunctiontest;

import android.content.Context;

import com.example.iconfunctiontest.Services.GestureService;

import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;

public class GestureService_UnitTest {

    @Mock
    Context mockContext;
    GestureService gs = new GestureService(mockContext);

    @Test
    public void TestCalcAngle(){

        /*
        assertEquals(80,gs.calcAngle(90,15),1);

        assertEquals(0, gs.calcAngle(1,0),1); //East
        assertEquals(90,  gs.calcAngle(0,-1),1);//North
        assertEquals(180,  gs.calcAngle(-1,0),1);//West
        assertEquals(270,  gs.calcAngle(0,1),1);//South


        assertEquals(45,gs.calcAngle(1,-1),1);//North-East
        assertEquals(135,gs.calcAngle(-1,-1),1);//North-West
        assertEquals(225,gs.calcAngle(-1,1),1);//South-West
        assertEquals(315,gs.calcAngle(1,1),1);//South-East
*/

    }


}
