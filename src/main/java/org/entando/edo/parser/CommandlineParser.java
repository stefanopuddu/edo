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
package org.entando.edo.parser;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.entando.edo.model.EdoBean;


public class CommandlineParser {

	private static Logger _logger = LogManager.getLogger(CommandlineParser.class);

	public EdoBean generate(String[] args) throws Throwable {
		EdoBean edoBean = null;
		try {
			if (null != args) {
				ArrayList<String> rawArgs = new ArrayList<String>();
				for(String s : args) {
					if(StringUtils.isNotBlank(s)) {
						rawArgs.add(s);
					}
				}
				args = rawArgs.toArray(new String[rawArgs.size()]);
			}
			String originalArgs = Arrays.toString(args);

			/////////
			CommandLineParser commandlineparser = new GnuParser();
			Options options = createCommandLineOptions();
			CommandLine cl = null;
			cl = commandlineparser.parse(options, args);
			if ((null != cl) && cl.hasOption("help") || null == args || args.length < 1) {
				outputCommandLineHelp(options);
				return null;
			}
			edoBean = new EdoBean();
			edoBean.setOriginalArgs(originalArgs);
			if (null != cl) {
				args = processCommandline(cl, edoBean, args);
			}
			/////////

			_logger.trace("check for a pom.xml");
			boolean pomExists = new File(edoBean.getBaseDir(), "pom.xml").exists();
			if (!pomExists) {
				_logger.error("pom.xml not found in dir:'{}'", edoBean.getBaseDir());
				throw new Exception("no pom.xml found in " + edoBean.getBaseDir());
			}
			_logger.trace("found {}.pom.xml", edoBean.getBaseDir());



			IAgrumentParser parser = new NameParser();
			String a[] = parser.parse(edoBean, args);

			parser = new FieldsParser();
			a = parser.parse(edoBean, a);
			_logger.warn("These parameters were skipped: " + Arrays.toString(a));

		} catch (ParseException exp) {
			_logger.error("Error parsing command line", exp);
		} catch (Throwable t) {
			_logger.error("Error parsing command line", t);
			throw t;
		}
		return edoBean;
	}




	private static Options createCommandLineOptions() {
		final Options options = new Options();
		options.addOption(OPTION_BASE_DIR, OPTION_BASE_DIR, true, OPTION_BASE_DIR);
		options.addOption(OPTION_PERMISSION, OPTION_PERMISSION, true, OPTION_PERMISSION);
		options.addOption(OPTION_PACKAGE, OPTION_PACKAGE, true, OPTION_PACKAGE);
		return options;
	}

	private static void outputCommandLineHelp(final Options options) {
		final HelpFormatter formater = new HelpFormatter();
		formater.printHelp("Edo [options] [ARGS] ", options);
	}


	/**
	 * Setta le opzioni e pulisce args!
	 * @param cl
	 * @param edoBean
	 * @param args
	 * @return
	 * @throws IllegalArgumentException
	 */
	private String[] processCommandline(CommandLine cl, EdoBean edoBean, String[] args) throws IllegalArgumentException {
		if (cl.hasOption(OPTION_BASE_DIR)) {
			String baseDir = cl.getOptionValue(OPTION_BASE_DIR);
			if (StringUtils.isNotBlank(baseDir)) {
				if (baseDir.endsWith(File.separator)) {
					baseDir = StringUtils.removeEnd(baseDir, File.separator);
				}
				edoBean.setBaseDir(baseDir);
			}
		}
		_logger.info("baseDir is: '{}'", edoBean.getBaseDir());

		if (cl.hasOption(OPTION_PERMISSION)) {
			String perm = cl.getOptionValue(OPTION_PERMISSION);
			if (StringUtils.isNotBlank(perm)) {
				edoBean.setPermission(perm);
			}
		}
		_logger.info("permission is: '{}'", edoBean.getPermission());

		String packageName = null;
		if (cl.hasOption(OPTION_PACKAGE)) {
			String packageNameParam = cl.getOptionValue(OPTION_PACKAGE);
			if (StringUtils.isNotBlank(packageNameParam)) {
				if (PackageValidator.isValidPackageName(packageNameParam)) {					
					packageName = packageNameParam;
				} else {
					_logger.error("invalid package name specified: {}", packageName);
					throw new IllegalArgumentException("invalid package name specified: " + packageName);
				}
			}
		} else {
			_logger.trace("auoto generate packagename");
			packageName = "org.entando.entando.plugins.jp" + cl.getArgs()[0].toLowerCase();
		}
		edoBean.setPackageName(packageName);
		_logger.info("packagename: is '{}'", edoBean.getPackageName());


		args = Arrays.copyOfRange(args, cl.getOptions().length, args.length);
		return args;
	}

	public static final String OPTION_BASE_DIR = "baseDir";
	public static final String OPTION_PERMISSION = "permission";
	public static final String OPTION_PACKAGE = "package";
}
