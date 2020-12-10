package org.devgateway.toolkit.forms.wicket.page.lists.fm;

import com.google.common.collect.ImmutableMap;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.devgateway.toolkit.persistence.fm.entity.DgFeature;
import org.devgateway.toolkit.persistence.fm.service.DgFmService;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @author Octavian Ciubotaru
 */
public class FeaturesDataProvider extends SortableDataProvider<DgFeature, String> {

    private static final Map<String, Function<DgFeature, Comparable>> KEY_EXTRACTORS =
            ImmutableMap.<String, Function<DgFeature, Comparable>>builder()
                    .put("name", DgFeature::getName)
                    .put("visible", DgFeature::getVisible)
                    .put("enabled", DgFeature::getEnabled)
                    .put("mandatory", DgFeature::getMandatory)
                    .build();

    private final DgFmService dgFmService;

    private final IModel<Filter> filterModel;

    private List<DgFeature> page;

    private Long totalCount;

    public FeaturesDataProvider(DgFmService dgFmService, IModel<Filter> filterModel) {
        this.dgFmService = dgFmService;
        this.filterModel = filterModel;
        setSort("name", SortOrder.ASCENDING);
    }

    @Override
    public Iterator<? extends DgFeature> iterator(long first, long count) {
        if (page == null) {
            page = dgFmService.getFeatures()
                    .stream()
                    .filter(getFilterPredicate())
                    .sorted(getComparator())
                    .skip(first)
                    .limit(count)
                    .collect(Collectors.toList());
        }
        return page.iterator();
    }

    private Comparator<DgFeature> getComparator() {
        Comparator<DgFeature> comp = Comparator.comparing(KEY_EXTRACTORS.get(getSort().getProperty()));
        if (!getSort().isAscending()) {
            comp = comp.reversed();
        }
        return comp;
    }

    @Override
    public long size() {
        if (totalCount == null) {
            totalCount = dgFmService.getFeatures().stream().filter(getFilterPredicate()).count();
        }
        return totalCount;
    }

    private Predicate<DgFeature> getFilterPredicate() {
        Filter filter = filterModel.getObject();
        return f -> nameFilter(f, filter.getFeature())
                && boolFilter(f::getVisible, filter.getVisible())
                && boolFilter(f::getEnabled, filter.getEnabled())
                && boolFilter(f::getMandatory, filter.getMandatory())
                && depsFilter(f::getChainedVisibleDeps, filter::getVisibleDep)
                && depsFilter(f::getChainedEnabledDeps, filter::getEnabledDep)
                && depsFilter(f::getChainedMandatoryDeps, filter::getMandatoryDep)
                && depsFilter(f::getChainedMixins, filter::getMixin);
    }

    private boolean nameFilter(DgFeature f, String name) {
        return name == null || f.getName().contains(name);
    }

    private boolean boolFilter(Supplier<Boolean> boolSupplier, String bool) {
        return bool.equals(Filter.ALL)
                || (bool.equals(Filter.YES) && boolSupplier.get())
                || (bool.equals(Filter.NO) && !boolSupplier.get());
    }

    private boolean depsFilter(Supplier<Set<DgFeature>> actualDeps, Supplier<String> filteredDepFn) {
        String filteredDep = filteredDepFn.get();
        return filteredDep == null
                || actualDeps.get().stream().anyMatch(cf -> cf.getName().equals(filteredDep));
    }

    @Override
    public IModel<DgFeature> model(DgFeature object) {
        return Model.of(object);
    }

    @Override
    public void detach() {
        page = null;
        totalCount = null;
    }
}
