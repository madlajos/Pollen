import java.util.ArrayList;
import org.opencv.core.Mat;

public class Analize 
{
	
	/**
	 * LoadImg v�ltoz�i 
	 */
	
	/*originalImage: bet�lt�tt k�p*/
	protected static Mat originalImage = new Mat();
	
	/*resizedImage: m�retcs�kkentett k�p*/
	protected static Mat resizedImage = new Mat();
	
	/*greyImage: greyscale k�p a m�retcs�kkentettb�l*/
	protected static Mat greyImage = new Mat();
	
	/**
	 * PreFilther v�ltoz�i
	 */
	
	/*preFilteredImageForPollen: pollenek kiv�logatva*/
	protected static Mat preFilteredImageForPollen = new Mat();
	
	/*thresholdedImageForPollen: prefilterelt k�pet thresholdolja*/
	protected static Mat thresholdedImageForPollen = new Mat();
	
	/**
	 * PollenCounter v�ltoz�i
	 */
	
	/*Houghcircles ebbe a v�ltoz�ba adja vissza a tal�lt k�rt*/
	protected static Mat circles = new Mat();
	
	/*K�r�k v�ltoz�i elmentve. 0. elem a k�z�ppont x koordin�t�ja, 1.az y, 2.elem a sug�r.*/
	protected static ArrayList<double[]> circlesList = new ArrayList<double[]>();
	
	/*Megtal�lt k�r�k sz�ma*/
	protected static int circleNumber = 0;
	
	/**
	 * HosePreFilter v�ltoz�i
	 */
	
	/*Morfol�gi�zott k�p, hogy el�k�sz�ts�k a t�ml� keres�st*/
	protected static Mat preFilteredImageForHose = new Mat();
	
	/*Az preFilteredImageForHose v�ltoz� thresholdozva*/
	protected static Mat thresholdedImageForHose = new Mat();
	
	/*A thresholdedImageForHose -r�l el vannak t�ntetvea zajok*/
	protected static Mat removedNoiseFromHoseImage = new Mat();
	
	/**
	 * HoseCounter v�ltoz�i
	 */
	
	/*A removedNoiseFromHoseImage-r�l elt�ntetj�k a polleneket, de marad ut�nuk zaj. */
	protected static Mat removedPollens = new Mat();
	
	/*A removedPollens k�pr�l elt�vol�tjuk azajokat, �gy csak a t�ml�k maradnak a k�pen*/
	protected static Mat hoses = new Mat();
	
	/**
	 * Skeleton v�ltoz�i
	 */
	
	/*A hoses k�pb�l elk�sz�tj�k a t�ml�k skeletonj�t, ez van a k�pen*/
	protected static Mat skeletonImage = new Mat();
	
	/*A skeletonImage k�pen a skeletonok v�g�t feh�rre sz�nezz�k*/
	protected static Mat clearedSkeletonImage = new Mat();	
	
	/**
	 * AssignhoseEndsToPollens v�ltoz�i
	 */
	
	/*T�ml�k sz�ma*/
	protected static int hoseNumber = 0;
	
	/* A clearedSkeletonImage v�gei meghosszabb�tva*/
	protected static Mat grownhoseEnds = new Mat();
	
	/*A t�ml� azon v�gpontjai feh�ren jelezve a k�pen, amelyeken pollenhez csatlakozik*/
	protected static Mat hoseEnds = new Mat();
	
	/**
	 * HoseLengthMeasure v�ltoz�i
	 */
	
	/*Azon a t�ml� v�gek, melyekn�l 1 pollenhez 1 t�ml� tartozik*/
	protected static ArrayList<Double> onePollenHoseLengths = new ArrayList<Double>();

   
}
