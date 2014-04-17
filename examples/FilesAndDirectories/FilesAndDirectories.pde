import ocrUtils.*;

void setup() {
  // smaple folder
  String targetDirectory = sketchPath("") + "../../";

  // start the OCRUtils
  OCRUtils.begin(this);
  
  // getDirectoryNames(String targetDirectory, boolean whether or not to stay at top level or go deep);
  // getFileNames(String targetDirectory, boolean whether or not to stay at top level or go deep);
  
  // get all folders in a directory only at the top level
  String[] folders = OCRUtils.getDirectoryNames(targetDirectory, false);
  println("\nfolders at: " + targetDirectory);
  printArray(folders);
  
  // get all folders in a directory including all end points 
  String[] endFolders = OCRUtils.getDirectoryNames(targetDirectory, true);
  println("\nall folders including end points at: " + targetDirectory);
  printArray(endFolders);
  
  // get all files in a directory only at the top level 
  String[] files = OCRUtils.getFileNames(targetDirectory, false);
  println("\nall files at: " + targetDirectory);
  printArray(files);
  
  // get all files in a directory and in all subdirectories
  String[] allFiles = OCRUtils.getFileNames(targetDirectory, true);
  println("\nall files including everything in subfolders at: " + targetDirectory);
  printArray(allFiles);
  
  exit();
} // end setup

//

