package org.devgateway.toolkit.persistence.dao.feedback;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.devgateway.toolkit.persistence.dao.categories.Department;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

/**
 * @author mpostelnicu
 */
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity
@Audited
@Table(indexes = {@Index(columnList = "url")})
public class ReplyableFeedbackMessage extends FeedbackMessage {

    private String url;

    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @ManyToOne
    @JsonIgnore
    private Department department;

    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "parent_id")
    @OrderColumn(name = "index")
    private List<FeedbackMessage> replies = new ArrayList<>();

    public List<FeedbackMessage> getReplies() {
        return replies;
    }

    public void setReplies(List<FeedbackMessage> replies) {
        this.replies = replies;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }
}
