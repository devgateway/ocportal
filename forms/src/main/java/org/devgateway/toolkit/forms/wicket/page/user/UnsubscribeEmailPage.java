package org.devgateway.toolkit.forms.wicket.page.user;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.apache.wicket.markup.html.TransparentWebMarkupContainer;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.wicket.page.BasePage;
import org.devgateway.toolkit.persistence.dao.alerts.Alert;
import org.devgateway.toolkit.persistence.service.alerts.AlertService;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wicketstuff.annotation.mount.MountPath;

/**
 * @author idobre
 * @since 29/08/2019
 * <p>
 * Unsubscribe an user from an {@link Alert}.
 */
@MountPath(value = "/unsubscribeEmail")
public class UnsubscribeEmailPage extends BasePage {
    private static final Logger logger = LoggerFactory.getLogger(UnsubscribeEmailPage.class);

    private final String secret;

    @SpringBean
    private AlertService alertService;

    public UnsubscribeEmailPage(final PageParameters parameters) {
        super(parameters);

        secret = parameters.get(0).toOptionalString();
        pageTitle.setVisibilityAllowed(false);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        // check if we have any errors
        final Boolean error;
        if (secret == null) {
            error = true;
        } else {
            final Alert alert = alertService.findBySecret(secret);

            if (alert == null) {
                error = true;
            } else {
                error = false;

                alertService.unsubscribeAlert(alert);

                // clear "servicesCache" cache;
                CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder()
                        .withCache("servicesCache", CacheConfigurationBuilder.
                                newCacheConfigurationBuilder(
                                        String.class, Object.class, ResourcePoolsBuilder.heap(100))
                                .build())
                        .build(true);

                Cache<String, Object> servicesCache = cacheManager.
                        getCache("servicesCache", String.class, Object.class);

                if (servicesCache != null) {
                    servicesCache.clear(); // Clear all entries from the cache
                }
            }
        }

        final TransparentWebMarkupContainer messageContainer =
                new TransparentWebMarkupContainer("messageContainer");
        add(messageContainer);
        final TransparentWebMarkupContainer errorContainer =
                new TransparentWebMarkupContainer("errorContainer");
        add(errorContainer);

        errorContainer.setVisibilityAllowed(error);
        messageContainer.setVisibilityAllowed(!error);
    }
}
