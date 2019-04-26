package org.devgateway.toolkit.forms.wicket.components.table.filter.form;

import org.apache.commons.lang3.StringUtils;
import org.devgateway.toolkit.persistence.dao.form.CabinetPaper;
import org.devgateway.toolkit.persistence.dao.form.CabinetPaper_;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author gmutuhu
 *
 */
public class CabinetPaperFilterState extends AbstractMakueniFormFilterState<CabinetPaper> {
    private String name;

    private String number;

    @Override
    public Specification<CabinetPaper> getSpecification() {
        return (root, query, cb) -> {
            final List<Predicate> predicates = new ArrayList<>();

            if (StringUtils.isNotBlank(name)) {
                predicates.add(cb.like(cb.lower(root.get(CabinetPaper_.name)), "%" + name.toLowerCase() + "%"));
            }
            
            if (StringUtils.isNotBlank(number)) {
                predicates.add(cb.like(cb.lower(root.get(CabinetPaper_.number)), "%" + number.toLowerCase() + "%"));
            }
            
            query.orderBy(cb.asc(root.get(CabinetPaper_.createdDate)));
            predicates.add(super.getSpecification().toPredicate(root, query, cb));
            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public final String getNumber() {
        return number;
    }

    public final void setNumber(final String number) {
        this.number = number;
    }   
}
