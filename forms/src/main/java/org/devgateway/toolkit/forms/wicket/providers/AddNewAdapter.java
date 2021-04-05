package org.devgateway.toolkit.forms.wicket.providers;

import org.devgateway.toolkit.persistence.dao.GenericPersistable;
import org.wicketstuff.select2.ChoiceProvider;
import org.wicketstuff.select2.Response;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * An adapter that allows creating and adding new {@link GenericPersistable} items to the choice list.
 *
 * @author Octavian Ciubotaru
 */
public abstract class AddNewAdapter<T extends GenericPersistable> extends ChoiceProvider<T> {

    private static final String NEW = "newItem";

    private final ChoiceProvider<T> target;

    public AddNewAdapter(ChoiceProvider<T> target) {
        this.target = target;
    }

    @Override
    public String getDisplayValue(T object) {
        String displayValue = target.getDisplayValue(object);
        if (object.isNew()) {
            return getAddNewDisplayValue(displayValue);
        } else {
            return displayValue;
        }
    }

    public abstract String getAddNewDisplayValue(String displayValue);

    public abstract T instantiate(String value);

    @Override
    public String getIdValue(T object) {
        if (object.isNew()) {
            return NEW + target.getDisplayValue(object);
        } else {
            return target.getIdValue(object);
        }
    }

    @Override
    public void query(String term, int page, Response<T> response) {
        target.query(term, page, response);
        if (!(term == null || term.isEmpty()) && page == 0) {
            if (response.getResults().stream().noneMatch(r -> term.equalsIgnoreCase(getDisplayValue(r)))) {
                response.getResults().add(0, instantiate(term));
            }
        }
    }

    @Override
    public Collection<T> toChoices(Collection<String> ids) {
        List<String> idList = new ArrayList<>(ids);
        int indexOfNew = -1;
        T newChoice = null;
        for (int i = 0; i < idList.size(); i++) {
            String id = idList.get(i);
            if (id.startsWith(NEW)) {
                idList.remove(i);
                indexOfNew = i;
                newChoice = instantiate(id.substring(NEW.length()));
                break;
            }
        }

        List<T> choices = new ArrayList<>(target.toChoices(idList));
        if (indexOfNew >= 0) {
            choices.add(indexOfNew, newChoice);
        }
        return choices;
    }
}
