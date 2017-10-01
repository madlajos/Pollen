import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class LoadImg extends Analize{

	/**
	 * A k�p beolvas�s�ra szolg�l. originalImage-be matk�nt, image-be k�pk�nt t�lti.
	 * Az el�bbi a m�veletekhez kell, az image az interfacere val� ki�r�shoz.
	 * Minden kiv�lasztott k�ppel ez lesz megh�vva
	 */
	public void Beolvas()
	{
		Analize.originalImage = Imgcodecs.imread("C:\\Users\\mukodjpls\\Documents\\GitHub\\Pollen\\VH489.jpg");
		//Image image = new Image("file:C:\\Users\\madla\\OneDrive\\Documents\\GitHub\\imgproc\\VH484.jpg");
		//return image;
	}	

	/**
	 * �tm�retez�s a gyorsabb elemz�shez
	 * originalImageb�l resizedImage
	 */
	public void Resize()
	{
		Imgproc.resize(Analize.originalImage, Analize.resizedImage, Analize.resizedImage.size(), 0.5, 0.5, Imgproc.INTER_LINEAR);
	}

	/**
	 * Greyscalere transzform�l�s
	 * resizedImageb�l greyImage
	 */
	public void Decolorize()
	{
		Imgproc.cvtColor(Analize.resizedImage, Analize.greyImage, Imgproc.COLOR_RGB2GRAY);
	}
}
