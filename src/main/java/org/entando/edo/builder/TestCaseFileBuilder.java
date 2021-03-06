/*
*
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
* This file is part of Entando Enterprise Edition software.
* You can redistribute it and/or modify it
* under the terms of the Entando's EULA
* 
* See the file License for the specific language governing permissions   
* and limitations under the License
* 
* 
* 
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/
package org.entando.edo.builder;

import java.io.File;
import java.util.regex.Matcher;

import org.apache.commons.lang.StringUtils;
import org.entando.edo.model.EdoBean;


public class TestCaseFileBuilder {

	private static String getJavaTestDir(EdoBean bean) {
		//String workingDir = System.getProperty("user.dir");
		String pojoPath = bean.getBaseDir() + FolderConstants.getJavaTestFolder() + bean.getPackageName().replaceAll("\\.", Matcher.quoteReplacement(File.separator)) + File.separator;
		return pojoPath;
	}

	private static String getApsadminTestDir(EdoBean bean) {
		//String workingDir = System.getProperty("user.dir");
		String pojoPath = bean.getBaseDir() + FolderConstants.getJavaTestFolder() + bean.getPackageName().replaceAll("\\.", Matcher.quoteReplacement(File.separator)) + File.separator + FolderConstants.getApsadminFolder() + File.separator;
		return pojoPath;
	}

	private static String getApsTestDir(EdoBean bean) {
		//String workingDir = System.getProperty("user.dir");
		String pojoPath = bean.getBaseDir() + FolderConstants.getJavaTestFolder() + bean.getPackageName().replaceAll("\\.", Matcher.quoteReplacement(File.separator)) + File.separator + FolderConstants.getApsFolder() + File.separator;
		return pojoPath;
	}

	private static String getApsServicesTestDir(EdoBean bean) {
		//String workingDir = System.getProperty("user.dir");
		String pojoPath = bean.getBaseDir() + FolderConstants.getJavaTestFolder() + bean.getPackageName().replaceAll("\\.", Matcher.quoteReplacement(File.separator)) + File.separator + FolderConstants.getServicesFolder() + File.separator;
		return pojoPath;
	}

	private static String getActionTestDir(EdoBean bean) {
		String pojoPath = TestCaseFileBuilder.getApsadminTestDir(bean) + bean.getName().toLowerCase() + File.separator;
		return pojoPath;
	}

	public static String getTestConfigTestUtilFilePath(EdoBean bean) {
		String filename = StringUtils.capitalize(bean.getSpringBeanPreposition()) + "ConfigTestUtils.java";
		String finalfile = TestCaseFileBuilder.getJavaTestDir(bean) + filename;
		return finalfile;
	}
	
	public static String getTestApsAdminBaseActionFilePath(EdoBean bean) {
		String filename = StringUtils.capitalize(bean.getSpringBeanPreposition()) + "ApsAdminBaseTestCase.java";
		String finalfile = TestCaseFileBuilder.getApsadminTestDir(bean) + filename;
		return finalfile;
	}
	
	public static String getTestActionFilePath(EdoBean bean) {
		String filename = "Test" + bean.getName() + "Action.java";
		String finalfile = TestCaseFileBuilder.getActionTestDir(bean) + filename;
		return finalfile;
	}
	public static String getTestFinderActionFilePath(EdoBean bean) {
		String filename = "Test" + bean.getName() + "FinderAction.java";
		String finalfile = TestCaseFileBuilder.getActionTestDir(bean) + filename;
		return finalfile;
	}

	
	public static String getTestApsBaseTestCaseFilePath(EdoBean bean) {
		String filename = StringUtils.capitalize(bean.getSpringBeanPreposition()) + "BaseTestCase.java";
		String finalfile = TestCaseFileBuilder.getApsTestDir(bean) + filename;
		return finalfile;
	}

	public static String getTestManagerFilePath(EdoBean bean) {
		String filename = "Test" + bean.getName() + "Manager.java";
		String finalfile = TestCaseFileBuilder.getApsServicesTestDir(bean) + filename;
		return finalfile;
	}


}
