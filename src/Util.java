// Utilities class written by Michael Ripley (zkxs)

import java.awt.BufferCapabilities;
import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.channels.FileChannel;
import java.nio.file.CopyOption;
import java.nio.file.Files;

public class Util
{	
	private static final boolean inJar = inJarTest();
	
	private Util()
	{}
	
	public static void debugBufferCapabilities(BufferCapabilities bc, String title)
	{
		GraphicsDevice monitor = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		BufferCapabilities.FlipContents flipContents = bc.getFlipContents();
		
		System.out.println("Buffer Capabilities for \""+ title + "\"");
		System.out.println("Full Screen Mode:           " + (monitor.getFullScreenWindow() != null));
		System.out.println("Full Screen Required:       " + bc.isFullScreenRequired());
		System.out.println("Multi Buffer Available:     " + bc.isMultiBufferAvailable());
		System.out.println("Page Flipping Enabled:      " + bc.isPageFlipping());
		System.out.println("Flip Contents:              " + ((flipContents == null)?"null":flipContents));
		System.out.println("Front Buffer Accelerated:   " + bc.getFrontBufferCapabilities().isAccelerated());
		System.out.println("Front Buffer Volatile:      " + bc.getFrontBufferCapabilities().isTrueVolatile());
		System.out.println("Back Buffer Accelerated:    " + bc.getBackBufferCapabilities().isAccelerated());
		System.out.println("Back Buffer Volatile:       " + bc.getBackBufferCapabilities().isTrueVolatile());
		System.out.println();
	}
	
	public static void debugScreenDevice()
	{
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice monitor = ge.getDefaultScreenDevice();
		DisplayMode dm = monitor.getDisplayMode();
		System.out.println(ge.getScreenDevices().length + " monitors detected.");
		System.out.println("Full screen is " + (monitor.isFullScreenSupported()?"":"NOT ") + "supported!");
		System.out.println(monitor.getConfigurations().length + " graphics configurations detected");
		System.out.println("Display mode changing is " + (monitor.isDisplayChangeSupported()?"":"NOT ") + "supported!");
		System.out.println(monitor.getAvailableAcceleratedMemory() + " bytes of memory available");
		System.out.println("Refresh rate of " + dm.getRefreshRate() + "Hz");
		System.out.println(dm.getWidth() + "x" + dm.getHeight() + " pixels");
		System.out.println();
	}
	
	public static void debugThreads()
	{
		Thread[] threads = new Thread[Thread.activeCount()];
		Thread.enumerate(threads);
		for (Thread thread : threads)
		{
			System.out.println("> " + thread.getName());
		}
		System.out.println();
	}
	
	/**
	 * Compare two doubles to see if they're "close enough" using percent error.
	 * This means that for numbers very close to zero, a larger acceptable
	 * epsilon value will be required to match.
	 * @param d1 A floating point number
	 * @param d2 A floating point number
	 * @param epsilon The percent error (1.0 = 100%) that is acceptable
	 * @return <code>true</code> if the numbers are "close enough"
	 */
	public static boolean fuzzyCompare(double d1, double d2, double epsilon)
	{
		return Math.abs(d1 - d2) <= epsilon * Math.max(Math.abs(d1), Math.abs(d2));
	}
	
	public static boolean inJar()
	{
		return inJar;
	}
	
	private static boolean inJarTest()
	{
		String className = Util.class.getName().replace('.', '/');
		String classJar =
				Util.class.getResource("/" + className + ".class").toString();
		if ( classJar.startsWith("jar:") )
		{
			return true;
		}
		return false;
	}
	
	/**
	 * Gets the inputstream of a file given its relative path
	 * HOLY CRAP. Finding out how to do this was a tremendous pain in the ass
	 * @param relativePath
	 * @return
	 * @throws URISyntaxException
	 * @throws FileNotFoundException
	 */
	public static InputStream getFileInputStream(String relativePath) throws URISyntaxException, FileNotFoundException
	{
		if (inJar())
		{
			return Util.class.getResourceAsStream("/" + relativePath);
		}
		else
		{
			File root = new File(Util.class.getResource("/").toURI()).getParentFile();
			File target = new File(root, relativePath);
			return new FileInputStream(target);
			
//			File[]derps = root.listFiles();
//			for (File derp : derps)
//				System.out.println(derp.getName());
//			return null;
		}
	}
	
	public static boolean assertionsEnabled()
	{
		boolean assertionsEnabled = false;
		assert assertionsEnabled = true; // Intentional side-effect
		// Now assertionsEnabled is set to the correct value
		return assertionsEnabled;
	}
	
	private final static String folderName = "urbansnake";
	
	public static File getWorkingDirectory()
	{
		String userHome = System.getProperty("user.home", ".");
		File workingDirectory;
		switch (getPlatform())
		{
			case 1:
				// fall through
			case 2:
				workingDirectory = new File(userHome, "." + folderName +"/");
				break;
			case 3:
				String applicationData = System.getenv("APPDATA");
				if (applicationData != null)
				{
					workingDirectory = new File(applicationData, "." + folderName + "/");
				}
				else
				{
					workingDirectory = new File(userHome, "." + folderName + "/");
				}
				break;
			case 4:
				workingDirectory = new File(userHome, "Library/Application Support/" + folderName);
				break;
			default:
				workingDirectory = new File(userHome, "." + folderName + "/");
		}
		
		return workingDirectory;
	}
	
	private static int getPlatform()
	{
		String osName = System.getProperty("os.name").toLowerCase();
		if (osName.contains("linux"))	return 1;
		if (osName.contains("unix"))	return 1;
		if (osName.contains("solaris"))	return 2;
		if (osName.contains("sunos"))	return 2;
		if (osName.contains("win"))		return 3;
		if (osName.contains("mac"))		return 4;
		return 5;
	}
	
	
	public static void streamToFile(InputStream in, File outFile) throws IOException
	{
		//FileOutputStream out = new FileOutputStream(outFile);
		
		assert in != null;
		Files.copy(in, outFile.toPath());
	}
	
	public static void copyFile(File sourceFile, File destFile) throws IOException
	{
		if (!destFile.exists())
		{
			destFile.createNewFile();
		}
		
		FileChannel source = null;
		FileChannel destination = null;
		
		try
		{
			source = new FileInputStream(sourceFile).getChannel();
			destination = new FileOutputStream(destFile).getChannel();
			destination.transferFrom(source, 0, source.size());
		}
		finally
		{
			if (source != null)
			{
				source.close();
			}
			if (destination != null)
			{
				destination.close();
			}
		}
	}
}
