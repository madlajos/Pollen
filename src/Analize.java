import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Mat;

public class Analize 
{
	protected static Mat originalImage = new Mat();
	protected static Mat resizedImage = new Mat();
	protected static Mat greyImage = new Mat();
	protected static Mat preFilteredImageForPollen = new Mat();
	protected static Mat thresholdedImageForPollen = new Mat();
	protected static Mat circles = new Mat();
	protected static ArrayList<double[]> circlesList = new ArrayList<double[]>();
	protected static int circleNumber = 0;
	protected static Mat preFilteredImageForHose = new Mat();
	protected static Mat thresholdedImageForHose = new Mat();
	protected static Mat removedNoiseFromHoseImage = new Mat();
   
}
