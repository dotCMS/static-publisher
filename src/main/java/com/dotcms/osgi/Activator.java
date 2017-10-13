package com.dotcms.osgi;

import com.dotcms.repackage.org.apache.logging.log4j.LogManager;
import com.dotcms.repackage.org.apache.logging.log4j.core.LoggerContext;
import com.dotmarketing.loggers.Log4jUtil;
import com.dotmarketing.osgi.GenericBundleActivator;
import org.osgi.framework.BundleContext;

/**
 * This class will be used by OSGi as the entry point to your code during the bundle
 * activation.
 */
public class Activator extends GenericBundleActivator {

    private LoggerContext pluginLoggerContext;

    @Override
    public void start ( BundleContext bundleContext ) throws Exception {

        //Initializing log4j...
        LoggerContext dotcmsLoggerContext = Log4jUtil.getLoggerContext();
        //Initialing the log4j context of this plugin based on the dotCMS logger context
        pluginLoggerContext = (LoggerContext) LogManager
                .getContext(this.getClass().getClassLoader(),
                        false,
                        dotcmsLoggerContext,
                        dotcmsLoggerContext.getConfigLocation());

        //Initializing services...
        initializeServices( bundleContext );
        
        // You could...
		//registerActionlet(BundleContext, WorkFlowActionlet)
		//registerActionMapping(ActionMapping)
		//registerBundleResourceMessages(BundleContext)
		//registerCacheProvider(BundleContext, String, Class<CacheProvider>)
		//registerPortlets(BundleContext, String[])
		//registerRuleActionlet(BundleContext, RuleActionlet)
		//registerRuleConditionlet(BundleContext, Conditionlet)
		//registerViewToolService(BundleContext, ToolInfo)
    }

    @Override
    public void stop ( BundleContext bundleContext ) throws Exception {
		// remember to "unregister" anything you registered at the "start" method
		// this will ensure the working consistency of the system.

        //Unregister all the bundle services
        unregisterServices(bundleContext);

        //Shutting down log4j in order to avoid memory leaks
        Log4jUtil.shutdown(pluginLoggerContext);
    }
} // EOC Activator