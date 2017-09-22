package com.mock.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DirAndFileUtils {
	
	public static String getFileWithAbsPathFromSpecifiedDir( String fileDir, String fileName) {
		File[] files;
		File mappringsDir = new File(fileDir);
        files = mappringsDir.listFiles();
        for (File file2 : files) {
            if (!file2.isDirectory() && file2.getAbsolutePath().contains(fileName)) {     
                System.out.println("文件:" + file2.getAbsolutePath());
                return file2.getAbsolutePath();
            } 
        }
        
        return null;
	}
	
	public static List<String> getFilesFromSpecifiedDir( String mappingsDir) {
		List<String> mappingsFiles = new ArrayList<String>();
		File[] files;
		File mappringsDir = new File(mappingsDir);
        files = mappringsDir.listFiles();
        for (File file2 : files) {
            if (!file2.isDirectory()) {     
                System.out.println("文件:" + file2.getAbsolutePath());
                mappingsFiles.add(file2.getAbsolutePath());
            } 
        }
        
        return mappingsFiles;
	}
	
	public static String getDirByName(String absolutDirPath, String dirName) {
		File currentLocationDirectory = new File(absolutDirPath);
		System.out.println(currentLocationDirectory.getParent());//获取上级目录
		File[] files = currentLocationDirectory.listFiles();
		 String mappingsDir = null;
		for (File file2 : files) {
            if (file2.isDirectory()) {
                if(file2.getAbsolutePath().contains(dirName))
                {
                	System.out.println("文件夹:" + file2.getAbsolutePath());
                	mappingsDir = file2.getAbsolutePath();
                	break;
                }
            } 
        }
		return mappingsDir;
	}
}
