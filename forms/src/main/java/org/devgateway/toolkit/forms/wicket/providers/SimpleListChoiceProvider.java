package org.devgateway.toolkit.forms.wicket.providers;

import org.apache.wicket.model.IModel;
import org.wicketstuff.select2.ChoiceProvider;
import org.wicketstuff.select2.Response;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Octavian Ciubotaru
 */
public class SimpleListChoiceProvider<T> extends ChoiceProvider<T> {

    private final IModel<List<T>> model;

    public SimpleListChoiceProvider(IModel<List<T>> model) {
        this.model = model;
    }

    @Override
    public String getDisplayValue(T object) {
        return object.toString();
    }

    @Override
    public String getIdValue(T object) {
        return object.toString();
    }

    @Override
    public void query(String term, int page, Response<T> response) {
        String lowerTerm = term != null ? term.toLowerCase() : null;
        model.getObject().stream()
                .filter(obj -> lowerTerm == null || getDisplayValue(obj).toLowerCase().contains(lowerTerm))
                .forEach(response::add);
        response.setHasMore(false);
    }

    @Override
    public Collection<T> toChoices(Collection<String> ids) {
        Map<String, T> groupedById = model.getObject().stream()
                .collect(Collectors.toMap(this::getIdValue, Function.identity()));
        return ids.stream().map(groupedById::get)
                .collect(Collectors.toList());
    }
}
