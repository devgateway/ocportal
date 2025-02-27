package org.devgateway.toolkit.forms.wicket.styles;

import de.agilecoders.wicket.extensions.javascript.YuiCssCompressor;

/**
 * This is a workaround for https://github.com/yui/yuicompressor/issues/59
 *
 * @author Nadejda Mandrescu
 */
public class CustomCssCompressor extends YuiCssCompressor {

    @Override
    public String compress(String original) {
        // Note: com.yahoo.platform.yui.compressor.CssCompressor does many replaceAll calls, so a few more don't add.
        // The issue is about spaces around + sign in functions like calc().
        original = original.replaceAll(" \\+ ", "__space__+__space__");
        // e.g. calc(1.5em +(.75rem)) is valid
        original = original.replaceAll(" \\+", "__space__+");
        // e.g. calc((1.5em)+ .75rem) is invalid, still processing in case it will be supported and some css will use it
        original = original.replaceAll("\\+ ", "+__space__");

        String css = super.compress(original);

        css = css.replaceAll("__space__", " ");

        return css;
    }
}
