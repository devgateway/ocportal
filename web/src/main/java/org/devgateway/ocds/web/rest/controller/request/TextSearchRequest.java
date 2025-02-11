/**
 *
 */
package org.devgateway.ocds.web.rest.controller.request;

import jakarta.validation.constraints.Size;

/**
 * @author mpostelnicu
 *
 */
public class TextSearchRequest extends GenericPagingRequest {

    @Size(min = 3, max = 255)
    private String text;

    public TextSearchRequest() {
        super();
    }

    public String getText() {
        return text;
    }

    public void setText(final String text) {
        this.text = (text == null || text.isEmpty()) ? null : text;
    }

}
