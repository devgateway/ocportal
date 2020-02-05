package org.devgateway.toolkit.forms.wicket.behaviors;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.model.IModel;
import org.devgateway.toolkit.forms.wicket.components.form.Select2MultiChoiceBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.providers.GenericChoiceProvider;
import org.devgateway.toolkit.persistence.dao.categories.Subcounty;
import org.devgateway.toolkit.persistence.dao.categories.Ward;
import org.devgateway.toolkit.persistence.dao.form.WardsSettable;
import org.devgateway.toolkit.persistence.service.category.WardService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class CountyAjaxFormComponentUpdatingBehavior extends AjaxFormComponentUpdatingBehavior {
    private final IModel<WardService> wardServiceModel;
    private final Select2MultiChoiceBootstrapFormComponent<Ward> wards;
    private final Select2MultiChoiceBootstrapFormComponent<Subcounty> subcounties;
    private final IModel<? extends WardsSettable> wardsSettable;

    public CountyAjaxFormComponentUpdatingBehavior(Select2MultiChoiceBootstrapFormComponent<Subcounty> subcounties,
                                                   Select2MultiChoiceBootstrapFormComponent<Ward> wards,
                                                   IModel<WardService> wardServiceModel,
                                                   IModel<? extends WardsSettable> wardsSettable,
                                                   final String event) {
        super(event);
        this.wardServiceModel = wardServiceModel;
        this.wards = wards;
        this.subcounties = subcounties;
        this.wardsSettable = wardsSettable;
    }

    @Override
    protected void onUpdate(final AjaxRequestTarget target) {
        final Collection<Subcounty> subcountyList = subcounties.getModelObject();

        if (subcountyList.isEmpty()) {
            wardsSettable.getObject().setWards(new ArrayList<>());
            wards.provider(new GenericChoiceProvider<>(new ArrayList<>(wardServiceModel.getObject().findAll())));
        } else {
            final List<Ward> wardList = wardServiceModel.getObject().findAll().stream()
                    .filter(ward -> subcountyList.contains(ward.getSubcounty()))
                    .collect(Collectors.toList());
            wards.provider(new GenericChoiceProvider<>(wardList));

            // keep only wards that can be selected as well.
            final List<Ward> newWards = wards.getModelObject().stream().filter(wardList::contains)
                    .collect(Collectors.toList());
            wardsSettable.getObject().setWards(newWards);
        }

        target.add(wards);
    }
}