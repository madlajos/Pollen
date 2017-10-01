import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class LoadImg extends Analize{

	/**
	 * A kép beolvasására szolgál. originalImage-be matként, image-be képként tölti.
	 * Az elõbbi a mûveletekhez kell, az image az interfacere való kiíráshoz.
	 * Minden kiválasztott képpel ez lesz meghívva
	 */
	public void Beolvas()
	{
		Analize.originalImage = Imgcodecs.imread("C:\\Users\\mukodjpls\\Documents\\GitHub\\Pollen\\VH489.jpg");
		//Image image = new Image("file:C:\\Users\\madla\\OneDrive\\Documents\\GitHub\\imgproc\\VH484.jpg");
		//return image;
	}	

	/**
	 * Átméretezés a gyorsabb elemzéshez
	 * originalImagebõl resizedImage
	 */
	public void Resize()
	{
		Imgproc.resize(Analize.originalImage, Analize.resizedImage, Analize.resizedImage.size(), 0.5, 0.5, Imgproc.INTER_LINEAR);
	}

	/**
	 * Greyscalere transzformálás
	 * resizedImagebõl greyImage
	 */
	public void Decolorize()
	{
		Imgproc.cvtColor(Analize.resizedImage, Analize.greyImage, Imgproc.COLOR_RGB2GRAY);
	}
}
