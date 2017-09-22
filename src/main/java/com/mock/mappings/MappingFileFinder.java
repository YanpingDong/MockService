package com.mock.mappings;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.mock.util.DirAndFileUtils;

public class MappingFileFinder {
	
	private final String MAPPINGS_DIR_NAME = "mappings";
	private final String RESPONSE_DIR_NAME = "__files";
	private String curruntDirectory;
	
	public MappingFileFinder()
	{
		File directory = new File("");//�趨Ϊ��ǰ�ļ��� 
		
		try{ 
		    System.out.println(directory.getCanonicalPath());//��ȡ��׼��·�� 
		    System.out.println(directory.getAbsolutePath());//��ȡ����·�� 
		}catch(Exception e){} 
		
		File currentLocationDirectory = new File(directory.getAbsolutePath());
		curruntDirectory = currentLocationDirectory.getAbsolutePath();
	}
	
	public List<String> getFileFromMappingsDir()
	{
		List<String> mappingsFiles = new ArrayList<String>();
		
		File currentLocationDirectory = new File(curruntDirectory);
		System.out.println(currentLocationDirectory.getParent());//��ȡ�ϼ�Ŀ¼
        //get mappings dir
		String mappingsDir = DirAndFileUtils.getDirByName(curruntDirectory, MAPPINGS_DIR_NAME);
        
		mappingsFiles = DirAndFileUtils.getFilesFromSpecifiedDir(mappingsDir);
        
        return mappingsFiles;
	}
	
	public InputStream getInputStreamFromFileName(String fileName)
	{
		File currentLocationDirectory = new File(curruntDirectory);
		System.out.println(currentLocationDirectory.getParent());//��ȡ�ϼ�Ŀ¼
		String fileDir = DirAndFileUtils.getDirByName(curruntDirectory, RESPONSE_DIR_NAME);
		String fileWithAbsPath = DirAndFileUtils.getFileWithAbsPathFromSpecifiedDir(fileDir, fileName);
		if(null == fileWithAbsPath)
		{
			return null;
		}
		else
		{
			File resource = new File(fileWithAbsPath);
            try {
				return new FileInputStream(resource);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		
		return null;
	}
	
}
