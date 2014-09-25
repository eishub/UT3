package nl.tudelft.goal.unreal.util.vecmathcheck;

import java.net.URL;

import javax.vecmath.Matrix3d;

/**
 * Utility class to check if the correct version of Vecmath is loaded. This is
 * needed because OSX installs Java3D as a Java extension. This extension uses
 * version 1.3 of Vecmath.jar. Other projects use version 1.5.* Because java
 * extensions are always loaded before our own libraries we end up with the
 * wrong version loaded.
 * 
 * @author mpkorstanje
 * 
 */
public class VecmathCheck {

	public static final String minimumVersion = "1.5";

	public static boolean check() {
		
		if(getSpecificationVersion() == null){
			//
			return true;
		}
		
		return Matrix3d.class.getPackage().isCompatibleWith(minimumVersion);
	}

	public static String getMinimumVersion() {
		return minimumVersion;
	}

	public static String getSpecificationVersion() {
		return Matrix3d.class.getPackage().getSpecificationVersion();
	}

	public static URL getPackageDir() {
		return Matrix3d.class.getProtectionDomain().getCodeSource().getLocation();
	}

	public static String getErrorMessage() {
		return "Version " + VecmathCheck.getMinimumVersion() + " of Vecmath.jar is required. "
				+ "You have version " + VecmathCheck.getSpecificationVersion() + " on your class path at:\n" 
				+ VecmathCheck.getPackageDir() +".\n"
				+ "Please remove this version from the class path.";
	}
}
