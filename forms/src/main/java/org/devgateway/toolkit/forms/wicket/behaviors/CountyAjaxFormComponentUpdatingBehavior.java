package org.devgateway.toolkit.forms.wicket.behaviors;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.model.IModel;
import org.danekja.java.util.function.serializable.SerializableConsumer;
import org.devgateway.toolkit.forms.wicket.components.form.Select2MultiChoiceBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.providers.GenericChoiceProvider;
import org.devgateway.toolkit.persistence.dao.categories.LocationPointCategory;
import org.devgateway.toolkit.persistence.service.BaseJpaService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class CountyAjaxFormComponentUpdatingBehavior<C extends LocationPointCategory,
        P extends LocationPointCategory, CS extends BaseJpaService<C>>
        extends AjaxFormComponentUpdatingBehavior {
    private final IModel<CS> childServiceModel;
    private final Select2MultiChoiceBootstrapFormComponent<C> wards;
    private final Select2MultiChoiceBootstrapFormComponent<P> subcounties;
    private final SerializableConsumer<List<C>> cListSetter;

    public CountyAjaxFormComponentUpdatingBehavior(Select2MultiChoiceBootstrapFormComponent<P> subcounties,
                                                   Select2MultiChoiceBootstrapFormComponent<C> wards,
                                                   IModel<CS> childServiceModel,
                                                   SerializableConsumer<List<C>> cListSetter,
                                                   String event) {
        super(event);
        this.childServiceModel = childServiceModel;
        this.wards = wards;
        this.subcounties = subcounties;
        this.cListSetter = cListSetter;
    }

    @Override
    protected void onUpdate(final AjaxRequestTarget target) {
        final Collection<P> subcountyList = subcounties.getModelObject();

        if (subcountyList.isEmpty()) {
            cListSetter.accept(new ArrayList<>());
            wards.provider(new GenericChoiceProvider<>(new ArrayList<>(childServiceModel.getObject().findAll())));
        } else {
            final List<C> wardList = childServiceModel.getObject().findAll().stream()
                    .filter(ward -> subcountyList.contains(ward.getParent()))
                    .collect(Collectors.toList());
            wards.provider(new GenericChoiceProvider<>(wardList));

            // keep only wards that can be selected as well.
            List<C> collect = wards.getModelObject().stream().filter(wardList::contains)
                    .collect(Collectors.toList());
            cListSetter.accept(collect);
        }

        target.add(wards);
    }
}