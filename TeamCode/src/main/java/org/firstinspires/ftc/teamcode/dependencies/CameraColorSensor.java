package org.firstinspires.ftc.teamcode.dependencies;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDouble;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;
import org.openftc.easyopencv.OpenCvPipeline;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Objects;

//////////////////////////////////////////////////////////
//
// This class searches for colors within specified regions.
// It returns data for each color, which allows the user to
// prioritize colors if multiple colors are found in a region.
//
/////////////////////////////////////////////////////////

public class CameraColorSensor {

    public enum Color_Enum {
        Color_None,
        Color_Green,
        Color_Red,
        Color_Blue,
        Color_Yellow,
        // Following made by Justin, WIP
        Color_Purple
    }

    // this is used to return color information
    public static class ColorData {
        public Color_Enum color = Color_Enum.Color_None;
        public double score;
        public int x_location;
        public int y_location;
    }

    OpenCvCamera camera;
    DeterminationPipeline pipeline;
    LinearOpMode linearOpMode;
    HardwareMap hardwareMap;
    Telemetry telemetry;


    // these can be configured at compile time
    final static int NumRegions = 3;

    // these can be configured at run time
    public boolean UsingWebcam = true;  // default to phone internal camera
    public static int SquareSize = 5;           // p
    // ixel size of edge of squares for checking
    //public static Point[] RegionTopLeft = new Point[NumRegions];
    public int RegionWidth = 20;
    public int RegionHeight = 60;
    //public Region[] regions;
    public Point[] RegionTopLeft = {new Point(0, 98),new Point(181, 98), new Point(253, 98)};
    public static int MinSaturation = 100;
    public static int MinBrightness = 75;
    public static double MaxStdDev = 10;

    // constructor
    public CameraColorSensor(String webcamName, HardwareMap hardwareMap, LinearOpMode linearOpMode) {

        this.hardwareMap = hardwareMap;
        this.linearOpMode = linearOpMode;
        this.telemetry = linearOpMode.telemetry;

        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());

        if (UsingWebcam) {
            camera = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, webcamName), cameraMonitorViewId);
        } else  // phone camera
        {
            camera = OpenCvCameraFactory.getInstance().createInternalCamera(OpenCvInternalCamera.CameraDirection.BACK, cameraMonitorViewId);
            camera.setViewportRenderingPolicy(OpenCvCamera.ViewportRenderingPolicy.OPTIMIZE_VIEW);
        }

        pipeline = new DeterminationPipeline(this);
        camera.setPipeline(pipeline);

        camera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
            @Override
            public void onOpened() {
                if (UsingWebcam) {
                    camera.startStreaming(320, 240, OpenCvCameraRotation.UPRIGHT);
                } else {
                    camera.startStreaming(320, 240, OpenCvCameraRotation.SIDEWAYS_LEFT);
                }

                telemetry.addData("State", "Camera opened");
                telemetry.update();
            }

            @Override
            public void onError(int errorCode) {
                /*
                 * This will be called if the camera could not be opened
                 */

                telemetry.addData("State", "Error opening camera");
                telemetry.update();
            }
        });
    }
    public void setLeftRegion(Point region){
        RegionTopLeft[0] = region;
    }
    public void setMiddleRegion(Point region){
        RegionTopLeft[1] = region;
    }
    public void setRightRegion(Point region){
        RegionTopLeft[2] = region;
    }
    public boolean isCameraInitialized() {
        boolean returnValue = false;
        List<EnumMap<Color_Enum, ColorData>> colorData = getColorData();

        // after the first frame is processed, each region will have data
        if (colorData.size() >= NumRegions) {
            returnValue = true;
        }

        return (returnValue);
    }

    public boolean isRegionBlue(int region) {
        List<EnumMap<Color_Enum, ColorData>> colorData = getColorData();
        return (colorData.size() > 0 && Objects.requireNonNull(colorData.get(region).get(Color_Enum.Color_Blue)).color == Color_Enum.Color_Blue);
    }

    public boolean isRegionRed(int region) {
        List<EnumMap<Color_Enum, ColorData>> colorData = getColorData();
        return (colorData.size() > 0 && Objects.requireNonNull(colorData.get(region).get(Color_Enum.Color_Red)).color == Color_Enum.Color_Red);
    }

    public boolean isRegionYellow(int region) {
        List<EnumMap<Color_Enum, ColorData>> colorData = getColorData();
        return (colorData.size() > 0 && Objects.requireNonNull(colorData.get(region).get(Color_Enum.Color_Yellow)).color == Color_Enum.Color_Yellow);
    }

    public boolean isRegionGreen(int region) {
        List<EnumMap<Color_Enum, ColorData>> colorData = getColorData();
        if (region < colorData.size ()) {
            return (colorData.size() > 0 && Objects.requireNonNull(colorData.get(region).get(Color_Enum.Color_Green)).color == Color_Enum.Color_Green);
        }
        return false;
    }

    public boolean isRegionPurple(int region) {
        List<EnumMap<Color_Enum, ColorData>> colorData = getColorData();
        return (colorData.size() > 0 && Objects.requireNonNull(colorData.get(region).get(Color_Enum.Color_Purple)).color == Color_Enum.Color_Purple);
    }

    // this class is used for drawing boxes on the screen
    public static class DebugData {
        static EnumMap<Color_Enum, Scalar> colors = new EnumMap<Color_Enum, Scalar>(Color_Enum.class);

        public Scalar color = new Scalar(0, 0, 0);
        public Point upperLeft;
        public Point lowerRight;
        public int width;

        // constructor
        public DebugData() {
            colors.put(Color_Enum.Color_None, new Scalar(0, 0, 0));
            colors.put(Color_Enum.Color_Blue, new Scalar(0, 0, 255));
            colors.put(Color_Enum.Color_Green, new Scalar(0, 255, 0));
            colors.put(Color_Enum.Color_Red, new Scalar(255, 0, 0));
            colors.put(Color_Enum.Color_Yellow, new Scalar(255, 255, 0));
            colors.put(Color_Enum.Color_Purple, new Scalar(255, 0, 255));
        }

        public DebugData(Scalar color, Point upperLeft, Point lowerRight, int width) {
            // call common constructor
            this();

            this.color = color;
            this.upperLeft = upperLeft;
            this.lowerRight = lowerRight;
            this.width = width;
        }
    }

    // this class is to store telemetry data from camera thread
    public static class TelemetryData {
        public String caption;
        public Object object;

        // constructor
        public TelemetryData(String caption, Object object) {
            this.caption = caption;
            this.object = object;
        }
    }

    // this class is used to pass color data from camera thread to opMode thread
    public static class SynchronizedColorData {
        List<EnumMap<Color_Enum, ColorData>> colorData = new ArrayList<EnumMap<Color_Enum, ColorData>>();

        public synchronized void setColorData(List<EnumMap<Color_Enum, ColorData>> colorData) {
            // copy the list without being interrupted
            this.colorData = new ArrayList<EnumMap<Color_Enum, ColorData>>(colorData);
        }

        public synchronized List<EnumMap<Color_Enum, ColorData>> getColorData() {
            // get the list without being interrupted
            return this.colorData;
        }
    }

    // this class is used to pass telemetry data from camera thread to opMode thread
    public static class SynchronizedTelemetryList {
        private List<TelemetryData> list = new ArrayList<TelemetryData>();

        public synchronized void setList(List<TelemetryData> list) {
            // copy the list without being interrupted
            this.list = new ArrayList<TelemetryData>(list);
        }

        public synchronized List<TelemetryData> getList() {
            // get the list without being interrupted
            return this.list;
        }
    }

    // this class is the main openCV camera class
    public static class DeterminationPipeline extends OpenCvPipeline {
        EnumMap<Color_Enum, Scalar> colorScalars = new EnumMap<Color_Enum, Scalar>(Color_Enum.class);
        /*
         * Points which actually define the sample region rectangles, derived from above values
         *
         * Example of how points work to define a rectangle
         *
         *   ------------------------------------
         *   | (0,0) TopLeft                    |
         *   |                                  |
         *   |                                  |
         *   |                                  |
         *   |                                  |
         *   |                                  |
         *   |                                  |
         *   |              BottomRight (70,50) |
         *   ------------------------------------
         *
         */

        // Working variables

        List<EnumMap<Color_Enum, ColorData>> region_colorData = new ArrayList<EnumMap<Color_Enum, ColorData>>();
        private SynchronizedColorData synchronizedColorData = new SynchronizedColorData();

        List<DebugData> debugList = new ArrayList<DebugData>();

        private List<TelemetryData> telemetryData = new ArrayList<TelemetryData>();
        private SynchronizedTelemetryList synchronizedTelemetryData = new SynchronizedTelemetryList();
        public CameraColorSensor parent;

        public DeterminationPipeline(CameraColorSensor parent) {
            colorScalars.put(Color_Enum.Color_None, new Scalar(0, 0, 0));
            colorScalars.put(Color_Enum.Color_Blue, new Scalar(0, 0, 255));
            colorScalars.put(Color_Enum.Color_Green, new Scalar(0, 255, 0));
            colorScalars.put(Color_Enum.Color_Red, new Scalar(255, 0, 0));
            colorScalars.put(Color_Enum.Color_Yellow, new Scalar(255, 255, 0));
            colorScalars.put(Color_Enum.Color_Purple, new Scalar(255, 0, 255));
            this.parent=parent;
        }

        /*
         * This function takes the RGB frame and converts to HSV
         */
        Mat inputToHSV(Mat input) {
            Mat hsv = new Mat();

            Imgproc.cvtColor(input, hsv, Imgproc.COLOR_RGB2HSV);

            return hsv;
        }

        //
        // finds the average hue and saturation of the input, and maps it to a color
        // with an associated score. Location is not set.
        //
        ColorData getColorData(Mat input) {
            ColorData colorData = new ColorData();

            int deltaHue = 999;
            int deltaSat = 999;

            colorData.color = Color_Enum.Color_None;
            colorData.score = 0.0;

            // channel 0 is hue

            // get average and standard deviation
            MatOfDouble mean = new MatOfDouble();
            MatOfDouble stdDev = new MatOfDouble();
            Core.meanStdDev(input, mean, stdDev);

            int average_hue = (int) mean.get(0, 0)[0];
            int stdDev_hue = (int) stdDev.get(0, 0)[0];

            // channel 1 is saturation
            int average_sat = (int) Core.mean(input).val[1];
            // channel 2 is brightness
            int average_brightness = (int) Core.mean(input).val[2];

            // since red is at 0/180, need to adjust to 90-270 with red at 180
            Mat redMask = new Mat();
            Mat redHSV = input.clone();

            // find all values between 0-89
            Core.inRange(input, new Scalar(0, 0, 0), new Scalar(89, 255, 255), redMask);
            // convert them to 180-270 (by adding 180)
            Core.add(input, new Scalar(180, 0, 0), redHSV, redMask);

            // channel 0 is hue, with red at 180

            // get average and standard deviation
            mean = new MatOfDouble();
            stdDev = new MatOfDouble();
            Core.meanStdDev(redHSV, mean, stdDev);

            int average_hue_red = (int) mean.get(0, 0)[0];
            int stdDev_hue_red = (int) stdDev.get(0, 0)[0];

            // channel 1 is saturation
            int average_sat_red = (int) Core.mean(redHSV).val[1];

            if (average_hue > 90 && average_hue < 120 && average_sat > MinSaturation && average_brightness > MinBrightness && stdDev_hue <= MaxStdDev) {
                colorData.color = Color_Enum.Color_Blue;
                deltaHue = 105 - average_hue;
                deltaSat = 150 - average_sat;
            }
            if (average_hue > 45 && average_hue < 85 && average_sat > MinSaturation && average_brightness > MinBrightness && stdDev_hue <= MaxStdDev) {
                colorData.color = Color_Enum.Color_Green;
                deltaHue = 75 - average_hue;
                deltaSat = 150 - average_sat;
            }
            if (average_hue > 20 && average_hue < 40 && average_sat > MinSaturation && average_brightness > MinBrightness && stdDev_hue <= MaxStdDev) {
                colorData.color = Color_Enum.Color_Yellow;
                deltaHue = 30 - average_hue;
                deltaSat = 150 - average_sat;
            }
            if (average_hue_red > 170 && average_hue_red < 190 && average_sat_red > MinSaturation && average_brightness > MinBrightness && stdDev_hue_red <= MaxStdDev) {
                colorData.color = Color_Enum.Color_Red;
                deltaHue = 180 - average_hue_red;
                deltaSat = 150 - average_sat_red;
            }

            if (average_hue > 90 && average_hue < 120 && average_sat > MinSaturation && average_brightness > MinBrightness && stdDev_hue <= MaxStdDev) {
                colorData.color = Color_Enum.Color_Blue;
                deltaHue = 105 - average_hue;
                deltaSat = 150 - average_sat;
            }

            // I HAVE NO IDEA HOW TO DO THIS HELP, HOW DO I ADD PURPLE?????
            // -Justin

            // compute score
            if (deltaSat < 0) {
                deltaSat = 0;
            }

            colorData.score = 100 - Math.sqrt(deltaHue * deltaHue + deltaSat * deltaSat);

            return colorData;
        }

        ColorData searchForColor(Mat input, Color_Enum color, int region) {

            ColorData bestColorData = new ColorData();
            ColorData workingColorData = new ColorData();
            DebugData bestDebug;
            Mat regionMat;

            if (region < NumRegions) {
                Point regionTopLeft = new Point(
                        parent.RegionTopLeft[region].x,
                        parent.RegionTopLeft[region].y);
                Point regionBottomRight = new Point(
                        parent.RegionTopLeft[region].x + parent.RegionWidth,
                        parent.RegionTopLeft[region].y + parent.RegionHeight);
                Rect rectangle = new Rect(regionTopLeft, regionBottomRight);
                Log.d("rectangleValues", rectangle.toString());

                regionMat = input.submat(rectangle);

                // search in squares for highest scoring one
                for (int row = 0; row < regionMat.rows() - SquareSize; row += SquareSize) {
                    for (int column = 0; column < regionMat.cols() - SquareSize; column += SquareSize) {
                        Mat square = regionMat.submat(row, row + SquareSize, column, column + SquareSize);

                        int row2 = row + (int) parent.RegionTopLeft[region].y;
                        int col2 = column + (int) parent.RegionTopLeft[region].x;
                        Point upperLeft = new Point(col2, row2);
                        Point lowerRight = new Point(col2 + SquareSize, row2 + SquareSize);

                        workingColorData = getColorData(square);

                        int width = 1;
                        if (workingColorData.score >= 98) {
                            width = 2;
                        }

                        if (workingColorData.color == color) {

                            DebugData data = new DebugData(DebugData.colors.get(color), upperLeft, lowerRight, width);
                            debugList.add(data);

                            if (workingColorData.score > bestColorData.score) {
                                bestColorData.color = workingColorData.color;
                                bestColorData.score = workingColorData.score;
                                bestColorData.x_location = col2;
                                bestColorData.y_location = row2;
                                bestDebug = data;
                            }
                        }
                    }
                }
            }

            return bestColorData;
        }

        @Override
        public Mat processFrame(Mat input) {
            Mat hsvMat = new Mat();
            MatOfDouble mean = new MatOfDouble();
            MatOfDouble stdDev = new MatOfDouble();
            ColorData colorData = new ColorData();

            // clear out previous data
            telemetryData.clear();
            debugList.clear();
            for (int region = 0; region < NumRegions; ++region) {
                region_colorData.add(region, new EnumMap<Color_Enum, ColorData>(Color_Enum.class));
            }

            // convert to HSV format
            hsvMat = inputToHSV(input);

            for (int region = 0; region < NumRegions; ++region) {
                // search for the color
                colorData = searchForColor(hsvMat, Color_Enum.Color_Red, region);
                telemetryData.add(new TelemetryData("Red score", colorData.score));
                // save off the data
                region_colorData.get(region).put(Color_Enum.Color_Red, colorData);

                colorData = searchForColor(hsvMat, Color_Enum.Color_Blue, region);
                telemetryData.add(new TelemetryData("Blue score", colorData.score));
                region_colorData.get(region).put(Color_Enum.Color_Blue, colorData);

                colorData = searchForColor(hsvMat, Color_Enum.Color_Green, region);
                region_colorData.get(region).put(Color_Enum.Color_Green, colorData);

                colorData = searchForColor(hsvMat, Color_Enum.Color_Yellow, region);
                telemetryData.add(new TelemetryData("Yellow score", colorData.score));
                region_colorData.get(region).put(Color_Enum.Color_Yellow, colorData);

                colorData = searchForColor(hsvMat, Color_Enum.Color_Purple, region);
                telemetryData.add(new TelemetryData("Yellow score", colorData.score));
                region_colorData.get(region).put(Color_Enum.Color_Purple, colorData);
            }
//            findBestColor(hsv, new Point(0,0), colorData);
//getColorData(region1_hsv, colorData);

            /*
             * Draw a rectangle showing each region on the screen.
             * Simply a visual aid. Serves no functional purpose.
             */
            Scalar WHITE = new Scalar(255, 255, 255);
            DebugData debugData = new DebugData();

            for (int region = 0; region < NumRegions; ++region) {
                Scalar color = WHITE;

                if (region_colorData.get(region).get(Color_Enum.Color_Green).score > 0) {
                    telemetryData.add(new TelemetryData("Green score", region_colorData.get(region).get(Color_Enum.Color_Green).score));
                    color = colorScalars.get(Color_Enum.Color_Green);
                } else if (region_colorData.get(region).get(Color_Enum.Color_Yellow).color == Color_Enum.Color_Yellow) {
                    telemetryData.add(new TelemetryData("Yellow score", region_colorData.get(region).get(Color_Enum.Color_Yellow).score));
                    color = colorScalars.get(Color_Enum.Color_Yellow);
                } else if (region_colorData.get(region).get(Color_Enum.Color_Purple).color == Color_Enum.Color_Purple) {
                    telemetryData.add(new TelemetryData("Purple score", region_colorData.get(region).get(Color_Enum.Color_Purple).score));
                    color = colorScalars.get(Color_Enum.Color_Yellow);
                } else if (region_colorData.get(region).get(Color_Enum.Color_Red).score > 0) {
                    color = colorScalars.get(Color_Enum.Color_Red);
                } else if (region_colorData.get(region).get(Color_Enum.Color_Blue).score > 0) {
                    color = colorScalars.get(Color_Enum.Color_Blue);
                }

                Point regionBottomRight = new Point(parent.RegionTopLeft[region].x + parent.RegionWidth,
                        parent.RegionTopLeft[region].y + parent.RegionHeight);

                debugList.add(new DebugData(color, parent.RegionTopLeft[region], regionBottomRight, 2));
            }

            synchronizedColorData.setColorData(region_colorData);
            synchronizedTelemetryData.setList(telemetryData);

            // draw debug data on the screen
            Mat output = input;
            for (DebugData data : debugList) {
                /*
                if(data!=null && data.color!=null) {
                    Imgproc.rectangle(output, data.upperLeft, data.lowerRight, data.color, data.width);
                }
                */
                //Imgproc.rectangle(output, data.upperLeft, data.lowerRight, data.color, data.width);
                try {
                    Imgproc.rectangle(output, data.upperLeft, data.lowerRight, data.color, data.width);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }

            }
            return output;
        }

        public List<EnumMap<Color_Enum, ColorData>> getColorData() {
            return synchronizedColorData.getColorData();
        }

        public List<TelemetryData> getTelemetryData() {
            return synchronizedTelemetryData.getList();
        }

    }

    public List<EnumMap<Color_Enum, ColorData>> getColorData() {
        return pipeline.getColorData();
    }

    public List<TelemetryData> getTelemetryData() {
        return pipeline.getTelemetryData();
    }

}
