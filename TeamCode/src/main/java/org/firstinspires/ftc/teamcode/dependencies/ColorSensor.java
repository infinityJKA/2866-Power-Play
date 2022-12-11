package org.firstinspires.ftc.teamcode.dependencies;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.dependencies.CameraColorSensor.Color_Enum;
import org.opencv.core.Point;

public class ColorSensor {
    CameraColorSensor camSensor;
    LinearOpMode linearOpMode;
    final Point[] DEFAULT_POINTS = {new Point(1, 30), new Point(110, 127), new Point(175, 30)};
    final int DEFAULT_WIDTH = 40;
    final int DEFAULT_HEIGHT = 60;
    public ColorSensor(String cameraName, HardwareMap hardwareMap, LinearOpMode linearOpMode){
        camSensor = new CameraColorSensor(cameraName, hardwareMap, linearOpMode);
        this.linearOpMode = linearOpMode;
        while (!camSensor.isCameraInitialized()) {
            linearOpMode.sleep(100);
        }
        camSensor.UsingWebcam = true;
        camSensor.RegionTopLeft[0] = DEFAULT_POINTS[0];
        camSensor.RegionTopLeft[1] = DEFAULT_POINTS[1];
        camSensor.RegionTopLeft[2] = DEFAULT_POINTS[2];
        camSensor.RegionWidth = DEFAULT_WIDTH;
        camSensor.RegionHeight = DEFAULT_HEIGHT;
    }
    public ColorSensor(String cameraName, HardwareMap hardwareMap, LinearOpMode linearOpMode, int width, int height, Point region1, Point region2, Point region3){
        camSensor = new CameraColorSensor(cameraName, hardwareMap, linearOpMode);
        this.linearOpMode = linearOpMode;
        while (!camSensor.isCameraInitialized()) {
            linearOpMode.sleep(100);
        }
        camSensor.UsingWebcam = true;
        camSensor.RegionTopLeft[0] = region1;
        camSensor.RegionTopLeft[1] = region2;
        camSensor.RegionTopLeft[2] = region3;
        camSensor.RegionWidth = width;
        camSensor.RegionHeight = height;
    }
    public ColorSensor(String cameraName, HardwareMap hardwareMap, LinearOpMode linearOpMode, Point region1, Point region2, Point region3){
        camSensor = new CameraColorSensor(cameraName, hardwareMap, linearOpMode);
        this.linearOpMode = linearOpMode;
        while (!camSensor.isCameraInitialized()) {
            linearOpMode.sleep(100);
        }
        camSensor.UsingWebcam = true;
        camSensor.RegionTopLeft[0] = region1;
        camSensor.RegionTopLeft[1] = region2;
        camSensor.RegionTopLeft[2] = region3;
        camSensor.RegionWidth = DEFAULT_WIDTH;
        camSensor.RegionHeight = DEFAULT_HEIGHT;
    }
    public ColorSensor(String cameraName, HardwareMap hardwareMap, LinearOpMode linearOpMode, int width, int height){
        camSensor = new CameraColorSensor(cameraName, hardwareMap, linearOpMode);
        this.linearOpMode = linearOpMode;
        while (!camSensor.isCameraInitialized()) {
            linearOpMode.sleep(100);
        }
        camSensor.UsingWebcam = true;
        camSensor.RegionTopLeft[0] = DEFAULT_POINTS[0];
        camSensor.RegionTopLeft[1] = DEFAULT_POINTS[1];
        camSensor.RegionTopLeft[2] = DEFAULT_POINTS[2];
        camSensor.RegionWidth = width;
        camSensor.RegionHeight = height;
    }
    public Color_Enum getColorAtRegion(int regionNumber){
        while (!camSensor.isCameraInitialized()) {
            linearOpMode.sleep(100);
        }
        Color_Enum color;
        if (camSensor.isRegionGreen(regionNumber)) color = Color_Enum.Color_Green;
        else if (camSensor.isRegionYellow(regionNumber)) color = Color_Enum.Color_Yellow;
        else if (camSensor.isRegionRed(regionNumber)) color = Color_Enum.Color_Red;
        else if (camSensor.isRegionBlue(regionNumber)) color = Color_Enum.Color_Blue;
        else color = Color_Enum.Color_None;
        return color;
    }
    public boolean isRegionRed(int regionNumber){
        return camSensor.isRegionRed(regionNumber);
    }
    public boolean isRegionBlue(int regionNumber){
        return camSensor.isRegionBlue(regionNumber);
    }
    public boolean isRegionGreen(int regionNumber){
        return camSensor.isRegionGreen(regionNumber);
    }
    public boolean isRegionYellow(int regionNumber){
        return camSensor.isRegionYellow(regionNumber);
    }

}
