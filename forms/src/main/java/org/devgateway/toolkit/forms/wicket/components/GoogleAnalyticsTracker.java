package org.devgateway.toolkit.forms.wicket.components;

import org.apache.wicket.core.util.string.JavaScriptUtils;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnLoadHeaderItem;
import org.apache.wicket.markup.html.WebComponent;
import org.springframework.util.ObjectUtils;

public class GoogleAnalyticsTracker extends WebComponent {

    private final String trackingId;

    public GoogleAnalyticsTracker(String id, String trackingId) {
        super(id);
        this.trackingId = trackingId;
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        if (!ObjectUtils.isEmpty(trackingId)) {
            //<!-- Global site tag (gtag.js) - Google Analytics -->
            JavaScriptUtils.writeJavaScriptUrl(response.getResponse(),
                    "https://www.googletagmanager.com/gtag/js?id=" + trackingId, "googletagmanager",
                    false, null, true
            );

            response.render(OnLoadHeaderItem.forScript("window.dataLayer = window.dataLayer || [];\n"
                    + "  function gtag(){dataLayer.push(arguments);}\n"
                    + "  gtag('js', new Date());\n"
                    + "\n"
                    + "  gtag('config', '" + trackingId + "');")
            );
        }
    }
}
