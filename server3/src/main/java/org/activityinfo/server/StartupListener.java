package org.activityinfo.server;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import com.google.common.collect.Lists;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.servlet.GuiceServletContextListener;
import org.activityinfo.server.authentication.AuthenticationModule;
import org.activityinfo.server.branding.BrandingModule;
import org.activityinfo.server.database.hibernate.HibernateModule;
import org.activityinfo.server.endpoint.odk.OdkModule;
import org.activityinfo.server.endpoint.test.TestModule;
import org.activityinfo.server.login.LoginModule;
import org.activityinfo.server.mail.MailModule;
import org.activityinfo.server.util.TemplateModule;
import org.activityinfo.server.util.config.ConfigModule;
import org.activityinfo.server.util.jaxrs.JaxRsModule;
import org.activityinfo.server.util.locale.LocaleModule;
import org.activityinfo.server.util.logging.LoggingModule;
import org.activityinfo.store.blob.GcsUserBlobServiceModule;
import org.activityinfo.store.hrd.HrdResourceStoreModule;

import javax.servlet.ServletContextEvent;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

/**
 * A Servlet context listener that initializes the Dependency Injection
 * Framework (Guice) upon startup.
 */
public class StartupListener extends GuiceServletContextListener {

    private static Logger logger = Logger.getLogger(StartupListener.class.getName());

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        logger.info("ActivityInfo servlet context is initializing");
        super.contextInitialized(servletContextEvent);

    }

    @Override
    protected Injector getInjector() {

        List<Module> modules = Lists.newArrayList();
        modules.addAll(Arrays.<Module>asList(
                new HrdResourceStoreModule(),
                new HibernateModule(),
                new ConfigModule(),
                new LoggingModule(),
                new TemplateModule(),
                new MailModule(),
                new AuthenticationModule(),
                new LoginModule(),
                new LocaleModule(),
                new BrandingModule(),
                new JaxRsModule(),
                new OdkModule(),
                new GcsUserBlobServiceModule(),
                new ServiceModule(),
                new TestModule()));

        return Guice.createInjector(modules);
    }
}