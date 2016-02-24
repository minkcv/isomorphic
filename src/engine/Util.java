package engine;
// Utilities class written by Michael Ripley (zkxs)

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.channels.FileChannel;
import java.nio.file.Files;

public class Util
{	
	private final static String folderName = "isomorphic";
	private static final boolean inJar = inJarTest();
	
	private Util()
	{}
	
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
		}
	}
	
	public static boolean assertionsEnabled()
	{
		boolean assertionsEnabled = false;
		assert assertionsEnabled = true; // Intentional side-effect
		// Now assertionsEnabled is set to the correct value
		return assertionsEnabled;
	}
	
	
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
