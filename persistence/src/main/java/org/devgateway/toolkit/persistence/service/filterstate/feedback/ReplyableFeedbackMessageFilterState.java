package org.devgateway.toolkit.persistence.service.filterstate.feedback;

import org.devgateway.toolkit.persistence.dao.categories.Department;
import org.devgateway.toolkit.persistence.dao.feedback.ReplyableFeedbackMessage;
import org.devgateway.toolkit.persistence.dao.feedback.ReplyableFeedbackMessage_;
import org.devgateway.toolkit.persistence.service.filterstate.JpaFilterState;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author
 */
public class ReplyableFeedbackMessageFilterState extends JpaFilterState<ReplyableFeedbackMessage> {

    private Department department;

    @Override
    public Specification<ReplyableFeedbackMessage> getSpecification() {
        return (root, query, cb) -> {
            final List<Predicate> predicates = new ArrayList<>();

            if (department != null) {
                predicates.add(cb.equal(root.get(ReplyableFeedbackMessage_.department), department));
            }

            predicates.add(super.getSpecification().toPredicate(root, query, cb));
            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }
}
