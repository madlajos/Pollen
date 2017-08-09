import java.util.ArrayList;
import org.opencv.core.Mat;

public class Analize 
{
	
	/**
	 * originalImage: betöltött kép / LoadImg.Beolvas()
	 * resizedImage: méretcsökkentett kép / LoadImg.Resize()
	 * greyImage: greyscale kép a méretcsökkentettbõl / LoadImg.Decolorize() 
	 */
	protected static Mat originalImage = new Mat();
	protected static Mat resizedImage = new Mat();
	protected static Mat greyImage = new Mat();
	
	/**
	 * preFilteredImageForPollen: pollenek kiválogatva / Prefilter.preFilter()
	 * thresholdedImageForPollen: prefilterelt képet thresholdolja / PreFilter.setThreshol()
	 */
	protected static Mat preFilteredImageForPollen = new Mat();
	protected static Mat thresholdedImageForPollen = new Mat();
	protected static Mat circles = new Mat();
	protected static ArrayList<double[]> circlesList = new ArrayList<double[]>();
	protected static int circleNumber = 0;
	
	protected static Mat preFilteredImageForHose = new Mat();
	protected static Mat thresholdedImageForHose = new Mat();
	protected static Mat removedNoiseFromHoseImage = new Mat();
	protected static Mat removedPollens = new Mat();
	protected static int hoseNumber = 0;
	protected static Mat hoses = new Mat();
	
	protected static Mat skeletonImage = new Mat();
	protected static Mat clearedSkeletonImage = new Mat();	
	
	protected static Mat grownHoseEnds = new Mat();
	protected static Mat HoseEnds = new Mat();
   
}
