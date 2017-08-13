import java.util.ArrayList;
import org.opencv.core.Mat;

public class Analize 
{
	
	/**
	 * LoadImg változói 
	 */
	
	/*originalImage: betöltött kép*/
	protected static Mat originalImage = new Mat();
	
	/*resizedImage: méretcsökkentett kép*/
	protected static Mat resizedImage = new Mat();
	
	/*greyImage: greyscale kép a méretcsökkentettbõl*/
	protected static Mat greyImage = new Mat();
	
	/**
	 * PreFilther változói
	 */
	
	/*preFilteredImageForPollen: pollenek kiválogatva*/
	protected static Mat preFilteredImageForPollen = new Mat();
	
	/*thresholdedImageForPollen: prefilterelt képet thresholdolja*/
	protected static Mat thresholdedImageForPollen = new Mat();
	
	/**
	 * PollenCounter változói
	 */
	
	/*Houghcircles ebbe a változóba adja vissza a talált kört*/
	protected static Mat circles = new Mat();
	
	/*Körök változói elmentve. 0. elem a középpont x koordinátája, 1.az y, 2.elem a sugár.*/
	protected static ArrayList<double[]> circlesList = new ArrayList<double[]>();
	
	/*Megtalált körök száma*/
	protected static int circleNumber = 0;
	
	/**
	 * HosePreFilter változói
	 */
	
	/*Morfológiázott kép, hogy elõkészítsük a tömlõ keresést*/
	protected static Mat preFilteredImageForHose = new Mat();
	
	/*Az preFilteredImageForHose változó thresholdozva*/
	protected static Mat thresholdedImageForHose = new Mat();
	
	/*A thresholdedImageForHose -ról el vannak tüntetvea zajok*/
	protected static Mat removedNoiseFromHoseImage = new Mat();
	
	/**
	 * HoseCounter változói
	 */
	
	/*A removedNoiseFromHoseImage-rõl eltüntetjük a polleneket, de marad utánuk zaj. */
	protected static Mat removedPollens = new Mat();
	
	/*A removedPollens képrõl eltávolítjuk azajokat, így csak a tömlõk maradnak a képen*/
	protected static Mat hoses = new Mat();
	
	/**
	 * Skeleton változói
	 */
	
	/*A hoses képbõl elkészítjük a tömlõk skeletonját, ez van a képen*/
	protected static Mat skeletonImage = new Mat();
	
	/*A skeletonImage képen a skeletonok végét fehérre színezzük*/
	protected static Mat clearedSkeletonImage = new Mat();	
	
	/**
	 * AssignhoseEndsToPollens változói
	 */
	
	/*Tömlõk száma*/
	protected static int hoseNumber = 0;
	
	/* A clearedSkeletonImage végei meghosszabbítva*/
	protected static Mat grownhoseEnds = new Mat();
	
	/*A tömlõ azon végpontjai fehéren jelezve a képen, amelyeken pollenhez csatlakozik*/
	protected static Mat hoseEnds = new Mat();
	
	/**
	 * HoseLengthMeasure változói
	 */
	
	/*Azon a tömlõ végek, melyeknél 1 pollenhez 1 tömlõ tartozik*/
	protected static ArrayList<Double> onePollenHoseLengths = new ArrayList<Double>();

   
}
