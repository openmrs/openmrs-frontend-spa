/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * 
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.spa;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.context.Context;
import org.openmrs.module.BaseModuleActivator;
import org.openmrs.module.ModuleException;
import org.openmrs.module.spa.utils.SpaModuleUtils;
import org.openmrs.util.OpenmrsUtil;

import java.io.File;

public class SpaActivator extends BaseModuleActivator {

	private final Log log = LogFactory.getLog(getClass());
	
	@Override
	public void started() {

		createFrontendDirectory();
		log.info("SPA module started");
	}

	@Override
	public void stopped() {
		log.info("SPA module stopped");
	}

	/**
	 * Creates a directory to hold static files for the frontend.
	 * The name/path of the directory is managed through a global property.
	 * Implementations can change the directory name/path
	 */
	public void createFrontendDirectory() {

		AdministrationService as = Context.getAdministrationService();
		String folderName = as.getGlobalProperty(SpaModuleUtils.GLOBAL_PROPERTY_SPA_STATIC_FILES_DIR,
				SpaModuleUtils.DEFAULT_FRONTEND_DIRECTORY);

		// try to load the repository folder straight away.
		File folder = new File(folderName);

		// if the property wasn't a full path already, assume it was intended to be a folder in the
		// application directory
		if (!folder.exists()) {
			folder = new File(OpenmrsUtil.getApplicationDataDirectory(), folderName);
		}

		// now create the modules folder if it doesn't exist
		if (!folder.exists()) {
			log.warn("Frontend directory " + folder.getAbsolutePath() + " doesn't exist.  Creating it now.");
			folder.mkdirs();
		}

		if (!folder.isDirectory()) {
			throw new ModuleException("SPA frontend repository is not a directory at: " + folder.getAbsolutePath());
		}
	}
}