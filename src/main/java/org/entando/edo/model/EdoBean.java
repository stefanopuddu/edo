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
package org.entando.edo.model;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;


public class EdoBean {
	
	public boolean buildWidgets() {
		return true;
	}

	public boolean buildApi() {
		return true;
	}
	
	public String getImports(boolean extras) {
		Set<String> imports = new HashSet<String>();
		Iterator<EdoField> it = this.getFields().iterator();
		while (it.hasNext()) {
			EdoField f = it.next();
			String importString = f.getType().getImportString();
			if (null != importString) {
				imports.add(importString);
			}
			if (extras) {
				String extraImportString = f.getType().getExtraImportString();
				if (null != extraImportString) {
					imports.add(extraImportString);
				}
			}
		}
		StringBuffer sbBuffer = new StringBuffer();
		Iterator<String> it2 = imports.iterator();
		while (it2.hasNext()) {
			if (sbBuffer.length() > 0) sbBuffer.append(System.getProperty("line.separator"));
			sbBuffer.append(it2.next());
		}
		return sbBuffer.toString();
		
	}
	
	public boolean isPlugin() {
		return this.getPackageName().contains(".plugins.");
	}

	public String getPluginName() {
		String name = null;
		if (isPlugin()) {
			name = StringUtils.substringAfter(this.getPackageName(), ".plugins.");
		}
		return name;
	}

	public String getProjectName() {
		String name = StringUtils.substringAfterLast(this.getBaseDir(), File.separator);
		name = name.replaceAll("_", "");
		name = name.replaceAll("-", "");
		return StringUtils.uncapitalize(name);
	}
	
	public String getTableName() {
		String name = this.getName().toLowerCase();
		if (isPlugin()) {
			String pname = StringUtils.substringAfter(this.getPackageName(), ".plugins.");
			name = pname.split("\\.")[0] + "_" + name;
		}
		return name;
	}
	
	//XXX
	public String getSpringBeanPreposition() {
		String name = StringUtils.substringAfterLast(this.getBaseDir(), File.separator);
		name = name.replaceAll("_", "");
		name = name.replaceAll("-", "");
		if (isPlugin()) {
			String pname = StringUtils.substringAfter(this.getPackageName(), ".plugins.");
			name = pname.split("\\.")[0];
		}
		return name;
	}
	
	public boolean needsActionStrutsConversionProperties() {
		boolean x = false;
		Iterator<EdoField> it = this.getFields().iterator();
		while (it.hasNext()) {
			EdoField field = it.next();
			if (StringUtils.isNotEmpty(field.getType().getStrutsConversionProperties())) {
				x = true;
				break;
			}
		}
		return x;
	}

	public boolean needsFinderActionStrutsConversionProperties() {
		boolean x = false;
		Iterator<EdoField> it = this.getSearchFields().iterator();
		while (it.hasNext()) {
			EdoField field = it.next();
			if (StringUtils.isNotEmpty(field.getType().getStrutsConversionProperties())) {
				x = true;
				break;
			}
		}
		return x;
	}

	public boolean needsStrutsActionValidation() {
		boolean x = false;
		Iterator<EdoField> it = this.getFields().iterator();
		while (it.hasNext()) {
			EdoField field = it.next();
			if (!field.isPrimaryKey() && field.isRequired() ) {
				x = true;
				break;
			}
		}
		return x;
	}

	public boolean isEntandoSearcherAvailable() {
		boolean apply = false;
		if (null == this.getFields() || this.getFields().isEmpty()) return false;
		apply = this.getFields().get(0).getType().getJavaType().equals("String") ||
				this.getFields().get(0).getType().getJavaType().equals("BigDecimal") ||
				//add more fields...  ||
				this.getFields().get(0).getType().getJavaType().equals("int");
		return apply;
	}


	/**
	 * @return
	 */
	public String getPackageFolder() {
		return this.getPackageName().replaceAll("\\.", "/");
	}
	
	public List<EdoField> getSearchFields() {
		List<EdoField> searchFields = new ArrayList<EdoField>();
		Iterator<EdoField> it = this.getFields().iterator();
		while (it.hasNext()) {
			EdoField current = it.next();
			if (current.getType().getJavaTypeGeneric().equals("Date")) {
				EdoField start = current.clone();
				start.setName(start.getName() + "Start");
				
				EdoField end = current.clone();
				end.setName(end.getName() + "End");
				searchFields.add(start);
				searchFields.add(end);
			} else {
				searchFields.add(current);
			}
		}
		return searchFields;
	}
	
	public String getName() {
		return _name;
	}
	public void setName(String name) {
		this._name = name;
	}
	
	public List<EdoField> getFields() {
		return _fields;
	}
	public void setFields(List<EdoField> fields) {
		this._fields = fields;
	}
	
	public String getPackageName() {
		return _packageName;
	}
	public void setPackageName(String packageName) {
		this._packageName = packageName;
	}
	
	public String getBaseDir() {
		return _baseDir;
	}
	public void setBaseDir(String baseDir) {
		this._baseDir = baseDir;
	}

	public String getPermission() {
		return _permission;
	}
	public void setPermission(String permission) {
		this._permission = permission;
	}

	public String getOriginalArgs() {
		return _originalArgs;
	}
	public void setOriginalArgs(String originalArgs) {
		this._originalArgs = originalArgs;
	}

	private String _name;
	private List<EdoField> _fields = new ArrayList<EdoField>();
	private String _packageName;
	private String _baseDir = System.getProperty("user.dir");
	private String _permission = "superuser";
	private String _originalArgs = null;

}
